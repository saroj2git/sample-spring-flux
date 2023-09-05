package com.learning.springboot.samplespringflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springboot.samplespringflux.services.WeatherApiService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    @Autowired
    private WeatherApiService weatherApiService;

    @GetMapping("/")
    public String home(){
        return "Welcome to weather app - Powered Spring WebFlux and Powered by <a href=\"https://www.weatherapi.com/\" title=\"Free Weather API\">WeatherAPI.com</a>"; 
    }

    @GetMapping("/current")
    public Mono<byte[]> getWeatherByCity(@RequestParam String location){
        return weatherApiService.getWeatherByCity(location);
    }

    @GetMapping("/forecast")
    public Mono<byte[]> getWeatherForecastByCity(@RequestParam String location, @RequestParam Integer days){
        return weatherApiService.getWeatherForecastByCity(location, days);
    }

    @GetMapping("/astronomy")
    public Mono<byte[]> getAstronomyByCity(@RequestParam String location){
        return weatherApiService.getAstronomyByCity(location);
    }
    
    @GetMapping("/all")
    public Flux<Mono<byte[]>> getAllWeatherDataByCity(@RequestParam String location, @RequestParam Integer days){
        return weatherApiService.getAllWeatherDataByCity(location, days);
    }
}
