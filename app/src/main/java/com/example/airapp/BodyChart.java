package com.example.airapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BodyChart {

    private long fromTimestamp;


    private long toTimestamp;


    private String fromTime;


    private String toTime;

    private String type;

    public long getFromTimestamp() {
        return fromTimestamp;
    }

    public long getToTimestamp() {
        return toTimestamp;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public String getType() {
        return type;
    }



    public BodyChart(long fromTimestamp, long toTimestamp, String fromTime, String toTime, String type) {
        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.type = type;
    }


    public BodyChart(String tpyeTime)
    {
        this.fromTimestamp = 0;
        this.toTimestamp = 0;
        this.type = "string";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đặt múi giờ UTC
        this.toTime =  dateFormat.format(calendar.getTime());
        if ("week".equals(tpyeTime)) {
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            this.fromTime = dateFormat.format(calendar.getTime());
        } else if ("month".equals(tpyeTime)) {
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            this.fromTime = dateFormat.format(calendar.getTime());
        } else if ("year".equals(tpyeTime)) {
            calendar.add(Calendar.DAY_OF_YEAR, -365);
            this.fromTime = dateFormat.format(calendar.getTime());
        }
    }

    public BodyChart(String tpyeTime, String toTime)
    {
        this.fromTimestamp = 0;
        this.toTimestamp = 0;
        this.type = "string";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đặt múi giờ UTC
        this.toTime =  toTime;
        Date toDate = null;
        try {
            toDate = dateFormat.parse(this.toTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ("week".equals(tpyeTime)) {
            calendar.setTime(toDate);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            this.fromTime = dateFormat.format(calendar.getTime());
        } else if ("month".equals(tpyeTime)) {
            calendar.setTime(toDate);
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            this.fromTime = dateFormat.format(calendar.getTime());
        } else if ("year".equals(tpyeTime)) {
            calendar.setTime(toDate);
            calendar.add(Calendar.DAY_OF_YEAR, -365);
            this.fromTime = dateFormat.format(calendar.getTime());
        }
    }
}
