package com.example.demo.extra.thread;

import com.google.common.util.concurrent.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
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
 * 异步回调(多个任务一次性执行)         asynCallback()
 * 异步链式执行(多线程处理任务链)       mutilthreadedChain()
 */
public class FutureAdvance {

    // 任务队列
    private static LinkedBlockingQueue queue;
    // 封装监听线程池
    private static ListeningExecutorService firstTaskExecutor;
    private static Executor secondTaskExecutor;
    private static Executor thirdTaskExecutor;
    // 记录任务数
    private static int queueCount = 0;


    /**
     *  异步链式执行，封装成批量多个任务执行
     */
    @Test
    public void mutilthreadedChain() throws Exception {
        // init thread
        initTask(100, 5, 5, 5);

        // push data to queue
        LinkedList<Object> preData = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            preData.add(i);
        }

        while (!firstTaskExecutor.isTerminated()) {
            Thread.sleep(1000);
            int queueSize = queue.size();
            System.out.println("还剩任务数：" + queueSize);

            // 模拟动态添加任务
            if (queueSize < 1) {
                System.out.println("\r\n\r\n========== 第 " + ++queueCount + " 批任务开始 ========");
                pushToQueue(ObjectUtils.clone(preData));
            }
        }

    }

    public void initTask(Integer queueSize, Integer firstThread, Integer secondThread, Integer thirdThread) throws Exception {
        // 初始化队列大小
        if (queueSize == null) {
            queue = new LinkedBlockingQueue(Integer.MAX_VALUE);
        }
        if (queueSize <= 0) {
            throw new Exception("非法队列长度");
        }
        if (queueSize >= Integer.MAX_VALUE) {
            queue = new LinkedBlockingQueue(Integer.MAX_VALUE);
        } else {
            queue = new LinkedBlockingQueue(queueSize);
        }

        // 初始化任务线程
        int firstThreadCount;
        if (firstThread == null) {
            firstThreadCount = 5;
        } else {
            firstThreadCount = firstThread.intValue();
        }
        firstTaskExecutor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(firstThreadCount));

        int secondThreadCount;
        if (secondThread == null) {
            secondThreadCount = 5;
        } else {
            secondThreadCount = secondThread.intValue();
        }
        secondTaskExecutor = Executors.newFixedThreadPool(secondThreadCount);

        int thirdThreadCount;
        if (thirdThread == null) {
            thirdThreadCount = 5;
        } else {
            thirdThreadCount = thirdThread.intValue();
        }
        thirdTaskExecutor = Executors.newFixedThreadPool(thirdThreadCount);

        //启动任务
        ExecutorService start = Executors.newFixedThreadPool(firstThreadCount);
        start.execute(() -> {
            while (true) {
                ListenableFuture<Object> listenableFuture = firstTask();
                if (listenableFuture == null) continue;
                ListenableFuture<Object> listenableFuture2 = secondTask(listenableFuture);
                if (listenableFuture2 == null) continue;
                callbackTask(listenableFuture2);
            }
        });
        System.out.println("task队列初始化完毕");
    }

    /**
     * 添加任务
     *
     * @param list
     */
    private void pushToQueue(LinkedList<Object> list) {
        int size = list.size();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            while (true) {
                try {
                    Object pop = list.poll();
                    if (pop == null && list.isEmpty()) {
                        System.out.println("第 " + queueCount + " 批任务，共添加 " + size + " 个任务");
                        break;
                    }
                    queue.put(pop);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        System.out.println("队列已满，重试中...");
                        Thread.sleep(100);
                    } catch (InterruptedException ee) {
                        ee.printStackTrace();
                    }
                }
            }
            executorService.shutdown();
        });
    }

    /**
     * task1
     *
     * @return
     */
    private ListenableFuture<Object> firstTask() {
        Object data;
        try {
            data = queue.take();
        } catch (InterruptedException e) {
            System.out.println("任务拉去失败");
            e.printStackTrace();
            return null;
        }

        // 任务1
        ListenableFuture<Object> listenableFuture = firstTaskExecutor.submit(() -> {
            if (data.toString().indexOf("6") > -1) {
                System.out.println("--- task1 任务 " + data + " 模拟超时 ---");
                Thread.sleep(2000);
            }
            return StringUtils.join(data, " -> task1 [ ", Thread.currentThread().getName(), " ]");
        });

        try {
            listenableFuture.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            listenableFuture.cancel(true);
            System.out.println("task1 -> 该任务已取消：" + data);
            return null;
        }

        return listenableFuture;
    }

    /**
     * task2
     *
     * @return
     */
    private ListenableFuture<Object> secondTask(ListenableFuture<Object> input) {

        // 任务2。任务1完成处理任务2
        ListenableFuture<Object> listenableFuture = Futures.transform(
                input,
                t -> {
                    if (t.toString().indexOf("8") > -1) {
                        try {
                            System.out.println("--- task2 模拟超时 ---");
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return StringUtils.join(t, " -> task2 [ ", Thread.currentThread().getName(), " ]");
                },
                secondTaskExecutor);

        try {
            listenableFuture.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            listenableFuture.cancel(true);
            try {
                System.out.println("task2 -> 该任务已取消：" + input.get());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        return listenableFuture;

    }

    /**
     * task3
     *
     * @return
     */
    private void callbackTask(ListenableFuture<Object> listenableFuture) {

        // 任务3。处理完任务2 后，回调任务3
        Futures.addCallback(listenableFuture, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object data) {
                System.out.println(data);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, thirdTaskExecutor);

    }

    /*************************************************************************/
    /****************************** 分割线 ************************************/
    /*************************************************************************/

    /**
     * 异步线程回调，一次性处理批量任务
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

}
