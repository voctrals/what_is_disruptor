package voctrals.study.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author lei.liu
 * @since 19-8-2
 */
public class MyEventProducer {
    private final RingBuffer<MyEvent> ringBuffer;

    public MyEventProducer(RingBuffer<MyEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try {
            MyEvent myEvent = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            myEvent.setValue(bb.getLong(0));  // Fill with data
            System.out.println("Thread : " + Thread.currentThread().getName() + " Producing event " + myEvent.getValue());
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}

