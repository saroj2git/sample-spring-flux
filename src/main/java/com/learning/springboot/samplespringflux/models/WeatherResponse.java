package com.learning.springboot.samplespringflux.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherResponse {
    
    private Location location;
    private Current current;
}
