package org.noear.socketd.transport.smartsocket;

import org.noear.socketd.transport.core.BufferWriter;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;

/**
 * @author noear
 * @since 2.0
 */
public class TcpAioBufferWriter implements BufferWriter {
    private WriteBuffer target;

    public TcpAioBufferWriter(WriteBuffer target) {
        this.target = target;
    }

    @Override
    public void putBytes(byte[] bytes) throws IOException {
        target.write(bytes);
    }

    @Override
    public void putBytes(byte[] src, int offset, int length) throws IOException {
        target.write(src, offset, length);
    }

    @Override
    public void putInt(int val) throws IOException {
        target.writeInt(val);
    }

    @Override
    public void putChar(int val) throws IOException {
        target.writeShort((short) val);
    }

    @Override
    public void flush() throws IOException {
        target.flush();
    }
}
