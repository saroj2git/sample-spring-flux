package com.learning.springboot.samplespringflux.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Location {

    private String name;
    private String region;
    private String country;
    private Float lat;
    private Float lon;
    private String tz_id;
    private Long localtime_epoch;
    private String localtime; 
}
