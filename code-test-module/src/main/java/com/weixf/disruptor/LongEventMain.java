package com.weixf.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain {
    public static void main(String[] args) throws Exception {
        int bufferSize = 1024;

        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();


        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            ringBuffer.publishEvent(new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
                @Override
                public void translateTo(LongEvent event, long sequence, ByteBuffer buffer) {
                    event.set(buffer.getLong(0));
                }
            }, bb);
            Thread.sleep(1000);
        }
    }
}
