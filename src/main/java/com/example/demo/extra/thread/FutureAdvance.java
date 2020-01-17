package com.example.demo.extra.thread;

import com.google.common.util.concurrent.*;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Guava 对 future 进行了增强，核心接口就是 ListenableFuture
 * <p>
 * 装饰线程，生成监听器
 * ListeningExecutorService listeningExecutor = MoreExecutors.listeningDecorator(executor);
 * 将回调绑定到监听器上
 * Futures.addCallback(task, callback);
 * <p>
 * 异步回调  asynCallback()
 */
public class FutureAdvance {

    /**
     * 异步线程回调
     */
    @Test
    public void asynCallback() {
        ExecutorService executor = null;
        ListeningExecutorService listeningExecutor = null;
        try {
            final CountDownLatch count = new CountDownLatch(3);

            long startTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            System.out.println("start");
            executor = Executors.newFixedThreadPool(2);
            // 监听器
            listeningExecutor = MoreExecutors.listeningDecorator(executor);
            // 提交任务
            List<ListenableFuture> batchTask = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ListenableFuture<String> task = listeningExecutor.submit(() -> {
                    Thread.sleep(2000);
//            String s = null;
//            s.length();
                    return "OK";
                });
                batchTask.add(task);
            }
            System.out.println("task");

            final FutureCallback<String> callback = new FutureCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    //成功回调
                    System.out.println(s);
                    count.countDown();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    // 捕获异常
                    throwable.printStackTrace();
                    count.countDown();
                }
            };
            System.out.println("callback");

            for (int i = 0; i < 3; i++) {
                // MoreExecutors.directExecutor()源码，execute方法就是直接运行，没有新开线程；MoreExecutors.newDirectExecutorService() 新建相同线程
                Futures.addCallback(batchTask.get(i), callback, MoreExecutors.newDirectExecutorService());
            }
            System.out.println("end");

            try {
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long endTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            System.out.println("program end with " + (endTime - startTime));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (listeningExecutor != null) {
                listeningExecutor.shutdown();
            }
            if (executor != null) {
                executor.shutdown();
            }
        }
    }


    /**
     * guava 异步链式执行
     */
    @Test
    public void testThreadChain() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            int t = i;
            executorService.execute(() -> threadChain(t));
        }
        System.out.println("task is all pulled to the queue");
        while (!listeningExecutor.isTerminated()) {
            Thread.sleep(1000);
        }

    }

    // 创建线程池
    private static final ExecutorService executor = Executors.newFixedThreadPool(3);
    // 封装监听线程池
    private static final ListeningExecutorService listeningExecutor = MoreExecutors.listeningDecorator(executor);

    private void threadChain(int i) {

        // 任务1
        ListenableFuture<String> listenableFuture = listeningExecutor.submit(
                () -> {
                    String threadName = Thread.currentThread().getName();
                    if (threadName.indexOf("2") > -1) Thread.sleep(3000);
                    else Thread.sleep(500);
                    return StringUtils.join("[ ", i, " ] task [ ", threadName, " ]");
                });

        try {
            listenableFuture.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            listenableFuture.cancel(true);
            System.out.println(StringUtils.join("No.", i, " Task is timeout"));
        }

        // 任务2。任务1完成处理任务2
        ListenableFuture<String> listenableFuture2 = Futures.transform(
                listenableFuture,
                t -> StringUtils.join(t, " -> task2 [", Thread.currentThread().getName(), " ]"),
                MoreExecutors.directExecutor());

        // 任务3。处理完任务2 后，回调任务3
        Futures.addCallback(listenableFuture2, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String data) {
                System.out.println(data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, MoreExecutors.directExecutor());

    }

}
