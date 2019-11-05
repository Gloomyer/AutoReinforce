package com.gloomyer.auto.runner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Scheduler implements Runnable {
    private static Scheduler ins = new Scheduler();

    public static Scheduler get() {
        return ins;
    }

    private int count;
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 10, TimeUnit.MINUTES,
            new LinkedBlockingDeque<>());

    synchronized int count() {
        return count;
    }

    private synchronized void add() {
        count++;
    }

    synchronized void del() {
        count--;
    }

    private List<ReinForceRunner> runns = new ArrayList<>();

    public void addRunner(ReinForceRunner runner) {
        //runns.add(runner);
        add();
        executor.execute(runner);
        //new Thread(runner).start();
    }

    @Override
    public void run() {

    }
}
