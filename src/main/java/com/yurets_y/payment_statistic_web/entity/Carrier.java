package com.yurets_y.payment_statistic_web.entity;

public class Carrier {
    private Station from;
    private Station to;

    public Carrier(Station from, Station to) {
        this.from = from;
        this.to = to;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }
}
