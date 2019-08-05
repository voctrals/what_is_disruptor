package voctrals.study.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author lei.liu
 * @since 19-8-2
 */
public class MyEventFactory implements EventFactory<MyEvent> {
    public static Integer count = 0;

    @Override
    public MyEvent newInstance() {
        System.out.println("Thread : " + Thread.currentThread().getName() + " Create MyEvent " + ++count);
        return new MyEvent();
    }
}
