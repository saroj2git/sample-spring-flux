package com.learning.springboot.samplespringflux.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.learning.springboot.samplespringflux.models.WeatherResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

@Service
public class WeatherApiService {
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${weatherapi.baseurl.realtime}")
    private  String realtimeBaseUrl;

    @Value("${weatherapi.baseurl.forecast}")
    private  String forecastBaseUrl;

    @Value("${weatherapi.baseurl.astronomy}")
    private  String astronomyBaseUrl;

    @Value("${weatherapi.apikey}")
    private  String apiKey;

    @Value("${target.output.location}")
    private String outPath;

    private String filePath_cityWeather;
    private String filePath_cityWeatherForecast;
    private String filePath_cityAstronomy;
    private String filePath_cityAllWeatherData;
    
    private Path file_cityWeather;
    private Path file_cityWeatherForecast;
    private Path file_cityAstronomy;
    private Path file_cityAllWeatherData;

    public Mono<byte[]> getWeatherByCity(String location){
        filePath_cityWeather = outPath + "/" + location + "_current_weather.json";
        file_cityWeather = Path.of(filePath_cityWeather);

        String apiUrl = realtimeBaseUrl + "?key=" + apiKey + "&q=" + location;
        Mono<byte[]> weatherResponse = webClientBuilder.baseUrl(apiUrl)
            .build()
            .get()
            .retrieve()
            .bodyToMono(byte[].class);

        weatherResponse.subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseBytes -> {
                    try {
                        Files.write(file_cityWeather, responseBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println("JSON response saved to " + filePath_cityWeather);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });        
                        
        return weatherResponse;
    }

    public Mono<byte[]> getWeatherForecastByCity(String location, Integer days) {
        filePath_cityWeatherForecast = outPath + "/" + location + "_" + days + "_days_forecast.json";
        file_cityWeatherForecast = Path.of(filePath_cityWeatherForecast);

        String apiUrl = forecastBaseUrl + "?key=" + apiKey + "&q=" + location + "&days=" + days;
        Mono<byte[]> forecastWeatherResponse = webClientBuilder.baseUrl(apiUrl)
            .build()
            .get()
            .retrieve()
            .bodyToMono(byte[].class);

        forecastWeatherResponse.subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseBytes -> {
                    try {
                        Files.write(file_cityWeatherForecast, responseBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        System.out.println("JSON response saved to " + filePath_cityWeatherForecast);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }); 

        return forecastWeatherResponse;
    }

    public Mono<byte[]> getAstronomyByCity(String location) {
        filePath_cityAstronomy = outPath + "/" + location + "_astronomy.json";
        file_cityAstronomy = Path.of(filePath_cityAstronomy);
        
        String apiUrl = astronomyBaseUrl + "?key=" + apiKey + "&q=" + location;
        Mono<byte[]> astronomyResponse = webClientBuilder.baseUrl(apiUrl)
            .build()
            .get()
            .retrieve()
            .bodyToMono(byte[].class);
        
        astronomyResponse.subscribeOn(Schedulers.boundedElastic())
        .subscribe(responseBytes -> {
            try {
                Files.write(file_cityAstronomy, responseBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("JSON response saved to " + filePath_cityAstronomy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });     
        
        
        return astronomyResponse;
    }

    /*public Mono<byte[]> getAllWeatherDataByCity(String location, Integer days) {
        filePath_cityAllWeatherData = outPath + "/" + location + "_allWeatherData.json";
        file_cityAllWeatherData = Path.of(filePath_cityAllWeatherData);

        Mono<byte[]> currentWeatherResponse = this.getWeatherByCity(location);
        Mono<byte[]> forecastWeatherResponse = this.getWeatherForecastByCity(location, days);
        Mono<byte[]> astronomyResponse = this.getAstronomyByCity(location);

        Mono<Tuple3<byte[], byte[], byte[]>> combinedMonoResponse = Mono.zip(currentWeatherResponse, forecastWeatherResponse, astronomyResponse)
                                                                                .map(tuple -> Tuples.of(tuple.getT1(), tuple.getT2(), tuple.getT3())
                                                                            );

        combinedMonoResponse.subscribe(tuple -> {
            byte[] bytes1 = tuple.getT1();
            byte[] bytes2 = tuple.getT2();
            byte[] bytes3 = tuple.getT3();

            try {
                Files.write(file_cityAllWeatherData, bytes1, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("JSON response saved to " + filePath_cityAllWeatherData);

                Files.write(file_cityAllWeatherData, bytes2, StandardOpenOption.APPEND);
                System.out.println("JSON response saved to " + filePath_cityAllWeatherData);

                Files.write(file_cityAllWeatherData, bytes3, StandardOpenOption.APPEND);
                System.out.println("JSON response saved to " + filePath_cityAllWeatherData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String allWeatherDataString = currentWeatherResponse.block().toString() + forecastWeatherResponse.block().toString() + astronomyResponse.block().toString();
        Mono<byte[]> allWeatherDataResponse = Mono.just(allWeatherDataString.getBytes());

        //Flux<byte[]> allWeatherDataResponse = Flux.merge(currentWeatherResponse, forecastWeatherResponse, astronomyResponse);
        
        return allWeatherDataResponse;
        
    }*/

    public Flux<Mono<byte[]>> getAllWeatherDataByCity(String location, Integer days) {
        filePath_cityAllWeatherData = outPath + "/" + location + "_allWeatherData.json";
        file_cityAllWeatherData = Path.of(filePath_cityAllWeatherData);

        Mono<byte[]> currentWeatherResponse = this.getWeatherByCity(location);
        Mono<byte[]> forecastWeatherResponse = this.getWeatherForecastByCity(location, days);
        Mono<byte[]> astronomyResponse = this.getAstronomyByCity(location);

        Mono<Tuple3<byte[], byte[], byte[]>> combinedMonoResponse = Mono.zip(currentWeatherResponse, forecastWeatherResponse, astronomyResponse)
                                                                                .map(tuple -> Tuples.of(tuple.getT1(), tuple.getT2(), tuple.getT3())
                                                                            );

        combinedMonoResponse.subscribe(tuple -> {
            byte[] bytes1 = tuple.getT1();
            byte[] bytes2 = tuple.getT2();
            byte[] bytes3 = tuple.getT3();

            try {
                Files.write(file_cityAllWeatherData, bytes1, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("JSON response saved to " + filePath_cityAllWeatherData);

                Files.write(file_cityAllWeatherData, bytes2, StandardOpenOption.APPEND);
                System.out.println("JSON response saved to " + filePath_cityAllWeatherData);

                Files.write(file_cityAllWeatherData, bytes3, StandardOpenOption.APPEND);
                System.out.println("JSON response saved to " + filePath_cityAllWeatherData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<Mono<byte[]>> monoList = List.of(currentWeatherResponse, forecastWeatherResponse, astronomyResponse);
        Flux<Mono<byte[]>> monoFlux = Flux.fromIterable(monoList);
        return monoFlux;
        
    }
}
