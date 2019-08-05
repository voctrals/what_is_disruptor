package voctrals.study.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author lei.liu
 * @since 19-8-2
 */
public class MyEventHandler implements EventHandler<MyEvent> {
    @Override
    public void onEvent(MyEvent myEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("Thread : " + Thread.currentThread().getName() + " Handling event: " + myEvent.getValue());
        System.out.println("myEvent: " + Math.pow(myEvent.getValue(), 2) + " sequence: " + sequence + " endOfBatch: " + endOfBatch);
    }
}
