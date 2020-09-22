package com.example.chaoticsubway;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class Spot {
    private String station;
    private LocalDateTime time;

    public Spot(String station, LocalDateTime localTime) {
        this.station = station;
        this.time = localTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append(station).append('\'');
        sb.append(time.toLocalTime()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
