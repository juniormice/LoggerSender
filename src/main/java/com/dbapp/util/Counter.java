package com.dbapp.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {

    private String name;

    private final AtomicInteger sent;

    private final AtomicLong sentTotal;

    public Counter(String name) {
        this.name = name.length() > 30 ? name.substring(0, 30) : name;
        this.sent = new AtomicInteger(0);
        this.sentTotal = new AtomicLong(0);
    }

    public void countSent() {
        this.sent.incrementAndGet();
    }

    public void countSent(int c) {
        this.sent.addAndGet(c);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSent() {
        return this.sent.intValue();
    }

    public long getSentTotal() {
        return this.sentTotal.longValue();
    }

    public int resetSent() {
        int c = this.sent.intValue();
        if (c > 0) {
            this.sent.set(0);
            this.sentTotal.addAndGet(c);
            return c;
        }
        return 0;
    }
}
