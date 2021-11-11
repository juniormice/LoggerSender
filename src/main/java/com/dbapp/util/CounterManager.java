package com.dbapp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CounterManager {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    /**
     * count统计任务
     */
    private CounterTask task;

    private Counter counter;

    private int period;

    private CounterManager() {
        this.period = 2;
    }

    public static CounterManager getInstance() {
        return CounterManagerHolder.instance;
    }

    public static Counter createCounter(String name) {
        Counter counter = new Counter(name);
        getInstance().addCounter(counter);
        return counter;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    private void addCounter(Counter counter) {
        if (this.counter != null) {
            stop();
        }
        this.counter = counter;
    }

    private void removeAll() {
        this.counter = null;
    }

    public void start() {
        this.task = new CounterTask(CounterTask.class.getName());
        this.task.running = true;
        this.task.start();
    }

    public void stop() {
        if (this.task != null) {
            this.task.running = false;
        }
        getInstance().removeAll();
    }

    private static class CounterManagerHolder {
        private final static CounterManager instance = new CounterManager();
    }

    private class CounterTask extends Thread {
        private boolean running = false;

        CounterTask(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (running) {
                int sent = counter.resetSent();
                int sentEps = (int) Math.ceil((double) sent / period);
                long sendTotal = counter.getSentTotal();
                System.out.println(String.format("%s - %s  Total Send: %s  Speed: %s EPS ", formatter.format(LocalDateTime.now()), counter.getName(), sendTotal, sentEps));

                try {
                    Thread.sleep(period * 1000);
                } catch (InterruptedException e) {
                    CounterManager.this.stop();
                }
            }
        }
    }

}
