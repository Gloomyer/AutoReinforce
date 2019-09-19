package com.gloomyer.auto.runner;

import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
    private static Scheduler ins = new Scheduler();

    public static Scheduler get() {
        return ins;
    }

    private int count;

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
        new Thread(runner).start();
    }

    @Override
    public void run() {

    }
}
