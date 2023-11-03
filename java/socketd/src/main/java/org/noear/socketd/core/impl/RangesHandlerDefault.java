package org.noear.socketd.core.impl;

import org.noear.socketd.core.Config;
import org.noear.socketd.core.Constants;
import org.noear.socketd.core.Entity;
import org.noear.socketd.core.RangesHandler;
import org.noear.socketd.core.entity.EntityDefault;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 数据分片默认实现
 *
 * @author noear
 * @since 2.0
 */
public class RangesHandlerDefault implements RangesHandler {
    @Override
    public Entity nextRange(Config config, AtomicReference<Integer> rangeIndex, Entity entity) throws IOException {
        rangeIndex.set(rangeIndex.get() + 1);

        byte[] rangeBytes = readRangeBytes(config, entity);
        if (rangeBytes.length == 0) {
            return null;
        }
        EntityDefault rangeEntity = new EntityDefault(null, rangeBytes);
        rangeEntity.setMetaMap(entity.getMetaMap());
        rangeEntity.putMeta(Constants.META_DATA_RANGE_IDX, String.valueOf(rangeIndex));
        return rangeEntity;
    }

    /**
     * 获取分片数据
     */
    private static byte[] readRangeBytes(Config config, Entity entity) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int tmp;
        while ((tmp = entity.getData().read()) != -1) {
            buf.write(tmp);
            if (buf.size() == config.getRangeSize()) {
                break;
            }
        }

        return buf.toByteArray();
    }
}