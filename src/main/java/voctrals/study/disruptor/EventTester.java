package voctrals.study.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lei.liu
 * @since 19-8-2
 */
public class EventTester {
    public static void main(String[] args) {
        // Executor that will be used to construct new threads for consumers
        // 用于创建消费者线程
        Executor executor = Executors.newCachedThreadPool();

        // Called by the RingBuffer to pre-populate all the events to fill the RingBuffer.
        // 是被RingBuffer被动调用的，用来提前创建bufferSize个event填充RingBuffer。
        MyEventFactory factory = new MyEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        // 指定RingBuffer的容量，必须是2的幂次方
        int bufferSize = 4;

        // Construct the Disruptor
        Disruptor<MyEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(new MyEventHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<MyEvent> ringBuffer = disruptor.getRingBuffer();

        final AtomicInteger nextId = new AtomicInteger();
        Executors.newFixedThreadPool(5, r -> new Thread(r, "Producer-Thread-" + nextId.incrementAndGet())).submit(() -> {
            // 生产者，将event放入ringBuffer
            MyEventProducer producer = new MyEventProducer(ringBuffer);

            ByteBuffer bb = ByteBuffer.allocate(8);
            for (long l = 0; true; l++) {
                bb.putLong(0, l);
                producer.onData(bb);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
