package com.inpress.weather.Controller;


import com.inpress.weather.exception.ServiceException;
import com.inpress.weather.service.WeatherService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Timed(value = "currentweather.time", description = "time taken to current weather")
    @GetMapping(value = "/current/{city}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "get current data weather", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @Parameters({@Parameter(description = "Get Current weather by city name")})
    public @ResponseBody
    ResponseEntity<Map<String, Object>> getCurrentWeather(@PathVariable String city) throws ServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(weatherService.getCurrentWeatherData(city));
    }

    @Timed(value = "forecastweather.time", description = "time taken to forecast weather")
    @GetMapping(value = "/forecast/{city}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "get forecast data weather", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @Parameters({@Parameter(description = "Get Forecast weather by city name")})
    public @ResponseBody
    ResponseEntity<Map<String, Object>> getWeatherForecast(@PathVariable String city) throws ServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(weatherService.getWeatherForecastData(city));
    }

    @Timed(value = "historicalweather.time", description = "time taken to historical weather")
    @GetMapping(value = "/historical/{city}/{date}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "get historical data weather", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @Parameters({@Parameter(description = "Get Historical weather by city name and date")})
    public @ResponseBody
    ResponseEntity<Map<String, Object>> getHistoricalWeather(@PathVariable String city, @PathVariable String date) throws ServiceException {
        return ResponseEntity.status(HttpStatus.OK).body(weatherService.getHistoricalWeatherData(city, date));
    }

}
