package com.example.chaoticsubway;

import java.time.LocalTime;

public class Node {
    private Station station;
    private LocalTime localTime;

    public Node(Station station, LocalTime localTime) {
        this.station = station;
        this.localTime = localTime;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }
}
