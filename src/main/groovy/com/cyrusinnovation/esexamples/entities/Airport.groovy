package com.cyrusinnovation.esexamples.entities

import groovy.transform.ToString

@ToString
public class Airport {
    final String locationID
    final Coordinates coordinates

    Airport(String locationID, String latitude, String longitude) {
        this.locationID = locationID
        this.coordinates = new Coordinates(latitude, longitude)
    }
}
