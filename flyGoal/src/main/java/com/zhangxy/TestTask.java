package com.zhangxy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestTask {
    @Test
    public void TestWorker() {
        // 一个有7个作业线程的线程池，老大的老大找到一个管7个人的小团队的老大
        final ExecutorService laodaA = Executors.newFixedThreadPool(7);

        try {
        	  laodaA.submit(() -> {
                Thread.sleep(3 * 1000);
                return "I am a task, which submited by the so called laoda, and run by those anonymous workers";
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
