package com.learning.springboot.samplespringflux.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Current {

    private Long last_updated_epoch;
    private String last_updated;
    private Float temp_c;
    private Float temp_f;
    private Integer is_day;
    private Condition condition;
    private Float wind_mph;
    private Float wind_kph;

    private Integer wind_degree;
    private String wind_dir;
    private Float pressure_mb;
    private Float pressure_in;
    private Float precip_mm;
    private Float precip_in;

    private Float humidity;
    private Integer cloud;

    
}
