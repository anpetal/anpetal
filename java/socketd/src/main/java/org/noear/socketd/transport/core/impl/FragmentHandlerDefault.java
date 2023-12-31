package org.noear.socketd.transport.core.impl;

import org.noear.socketd.transport.core.*;
import org.noear.socketd.transport.core.entity.EntityDefault;
import org.noear.socketd.utils.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 数据分片默认实现（可以重写，把大流先缓存到磁盘以节省内存）
 *
 * @author noear
 * @since 2.0
 */
public class FragmentHandlerDefault implements FragmentHandler {
    /**
     * 获取一个分片
     */
    @Override
    public Entity nextFragment(Config config, AtomicReference<Integer> fragmentIndex, Entity entity) throws IOException {
        fragmentIndex.set(fragmentIndex.get() + 1);

        ByteArrayOutputStream fragmentBuf = new ByteArrayOutputStream();
        IoUtils.transferTo(entity.getData(), fragmentBuf, 0, Config.MAX_SIZE_FRAGMENT);
        byte[] fragmentBytes = fragmentBuf.toByteArray();
        if (fragmentBytes.length == 0) {
            return null;
        }
        EntityDefault fragmentEntity = new EntityDefault().data(fragmentBytes);
        if (fragmentIndex.get() == 1) {
            fragmentEntity.metaMap(entity.getMetaMap());
        }
        fragmentEntity.putMeta(EntityMetas.META_DATA_FRAGMENT_IDX, String.valueOf(fragmentIndex));
        return fragmentEntity;
    }

    /**
     * 聚合分片（可以重写，把大流先缓存到磁盘以节省内存）
     */
    @Override
    public Frame aggrFragment(Channel channel, int index, Frame frame) throws IOException {
        FragmentAggregator aggregator = channel.getAttachment(frame.getMessage().getSid());
        if (aggregator == null) {
            aggregator = new FragmentAggregator(frame);
            channel.setAttachment(aggregator.getSid(), aggregator);
        }

        aggregator.add(index, frame);

        if (aggregator.getDataLength() > aggregator.getDataStreamSize()) {
            //长度不够，等下一个分片包
            return null;
        } else {
            //重置为聚合帖
            return aggregator.get();
        }
    }
}