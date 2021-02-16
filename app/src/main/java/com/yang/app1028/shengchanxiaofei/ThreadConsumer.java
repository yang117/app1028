package com.yang.app1028.shengchanxiaofei;

public class ThreadConsumer extends Thread {
    private Consumer consumer;

    public ThreadConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        while (true) {
            consumer.getValue(); //死循环，不断消费
        }
    }
}
