package com.lamdah.medicinereminder.models;

import java.io.Serializable;
import java.time.LocalDateTime;
public class Pill implements Serializable {
    private int id;
    private String name;
    private long datetime;
    private boolean[] days;

    public Pill(int id, String name, long datetime, boolean[] days) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.days = days;
    }

    public Pill() {
    }

    public boolean[] getDays() {
        return days;
    }

    public Pill(int id, String name, long datetime) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

}