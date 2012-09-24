package me.hatter.tests.disruptor;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorTest {

    public static class ValueEvent {

        public static final EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {

                                                                       public ValueEvent newInstance() {
                                                                           return null;
                                                                       }

                                                                   };
    }

    public static void main(String[] args) {
        Executor executor = Executors.newFixedThreadPool(10);
        EventHandler<ValueEvent> eventHandler = new EventHandler<DisruptorTest.ValueEvent>() {

            public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) throws Exception {
                System.out.println(new Date());
            }
        };
        Disruptor<ValueEvent> disruptor = new Disruptor<DisruptorTest.ValueEvent>(ValueEvent.EVENT_FACTORY, 8,
                                                                                  executor);
        disruptor.handleEventsWith(eventHandler);
        RingBuffer<ValueEvent> ringBuffer = disruptor.start();
        disruptor.publishEvent(new EventTranslator<DisruptorTest.ValueEvent>() {
            
            public void translateTo(ValueEvent event, long sequence) {
                
            }
        });
        disruptor.publishEvent(new EventTranslator<DisruptorTest.ValueEvent>() {
            
            public void translateTo(ValueEvent event, long sequence) {
                
            }
        });
    }
}
