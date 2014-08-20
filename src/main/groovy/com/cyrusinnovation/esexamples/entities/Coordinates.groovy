package com.cyrusinnovation.esexamples.entities

import groovy.transform.ToString

@ToString
public class Coordinates {
    final String lat
    final String lon

    Coordinates(String lat, String lon) {
        this.lat = lat
        this.lon = lon
    }
}
