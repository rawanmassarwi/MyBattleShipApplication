package com.massarwi.rawan.mybattleshipapplication.util;

import java.util.concurrent.atomic.AtomicInteger;

/*Ship Class*/
public class Ship {

    //Setting Variables
    private int id;
    private int size;
    private boolean sink;
    private int numOfHits;
    private boolean place;
    private static final AtomicInteger counter = new AtomicInteger();

    /*Constractor*/
    public Ship(int size) {
        this.size = size;
        this.sink = false;
        this.numOfHits = 0;
        this.place = false;
        this.id = counter.getAndIncrement();

    }

    /*Clear Ship Method*/
    public void clearShip() {
        this.sink = false;
        this.numOfHits = 0;
        this.place = false;
    }

    /*Is Sink Method*/
    public boolean isSink() {
        return sink;
    }

    /*Is Placed Method*/
    public boolean isPlace() {
        return place;
    }

    /*Get Size Method*/
    public int getSize() {
        return size;
    }

    /*Get ID Method*/
    public int getId() {
        return id;
    }

    /*Set Sink Method*/
    public void setSink(boolean sink) {
        this.sink = sink;
    }

    /*Set Placed Method*/
    public void setPlace(boolean place) {
        this.place = place;
    }

    /*Set Number Of Hits Method*/
    public void setNumOfHits() {
        this.numOfHits++;
        if (numOfHits == size) {
            setSink(true);
        }
    }

}

