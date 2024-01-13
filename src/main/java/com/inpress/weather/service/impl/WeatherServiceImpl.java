package com.inpress.weather.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inpress.weather.config.WeatherConfig;
import com.inpress.weather.exception.ServiceException;
import com.inpress.weather.service.WeatherService;
import com.inpress.weather.util.ConstantName;
import com.inpress.weather.util.ConstantResponse;
import com.inpress.weather.util.DateUtil;
import com.inpress.weather.util.Validate;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final Logger log = LogManager.getLogger(WeatherServiceImpl.class);

    private final RestTemplate restTemplate;
    private final WeatherConfig weatherConfig;

    @Autowired
    public WeatherServiceImpl(RestTemplate restTemplate, WeatherConfig weatherConfig) {
        this.restTemplate = restTemplate;
        this.weatherConfig = weatherConfig;
    }

    @Value("${openweathermap.current.weather.apiurl}")
    private String currentWeatherUrl;

    @Value("${openweathermap.forecast.weather.apiurl}")
    private String forecastWeatherUrl;

    @Value("${openweathermap.historical.weather.apiurl}")
    private String historicalWeatherUrl;

    @Value("${openweathermap.historical.location.apiurl}")
    private String historicalLocationUrl;

    private String retrieveApiKey() {
        return weatherConfig.getApiKey();
    }

    /**
     * @param city
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, Object> getCurrentWeatherData(String city) throws ServiceException {

        log.info("start get current weather service ....");
        if (Validate.checkCityValidate(city) != ConstantResponse.OK) {
            log.error("may be city name {} is null or have numeric value !!", city);
            throw new ServiceException("may be city name is null or have numeric value !!", HttpStatus.SC_BAD_REQUEST);
        }

        String apiUrl = currentWeatherUrl + city + ConstantName.APP_ID + retrieveApiKey();
        log.info("api URL get current weather service {} ", apiUrl);
        String response = restTemplate.getForObject(apiUrl, String.class);
        log.info("response current weather {} ", response);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            log.error("getting INTERNAL_SERVER_ERROR in process getCurrentWeatherData service !!");
            throw new ServiceException("getting INTERNAL_SERVER_ERROR in process getCurrentWeatherData service !!", e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param city
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, Object> getWeatherForecastData(String city) throws ServiceException {

        log.info("start get forecast weather service ....");
        if (Validate.checkCityValidate(city) != ConstantResponse.OK) {
            log.error("may be city name {} is null or have numeric value !!", city);
            throw new ServiceException("may be city name is null or have numeric value !!", HttpStatus.SC_BAD_REQUEST);
        }
        String apiUrl = forecastWeatherUrl + city + ConstantName.APP_ID + retrieveApiKey();
        log.info("api URL get forecast weather service {} ", apiUrl);
        String response = restTemplate.getForObject(apiUrl, String.class);
        log.info("response forecast weather {} ", response);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            log.error("getting INTERNAL_SERVER_ERROR in process getWeatherForecastData service !!");
            throw new ServiceException("getting INTERNAL_SERVER_ERROR in process getWeatherForecastData service !!", e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param city
     * @param date
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, Object> getHistoricalWeatherData(String city, String date) throws ServiceException {
        log.info("start get historical weather service ....");
        if (Validate.checkCityValidate(city) != ConstantResponse.OK) {
            log.error("may be city name {} is null or have numeric value !!", city);
            throw new ServiceException("may be city name is null or have numeric value !!", HttpStatus.SC_BAD_REQUEST);
        }

        String apiUrl = historicalWeatherUrl + retrieveApiKey();
        log.info("api URL get historical weather service {} ", apiUrl);

        // Get the latitude and longitude of the city
        String locationUrl = historicalLocationUrl + city + ConstantName.APP_ID + retrieveApiKey();
        log.info("api URL get historical weather for get lat&lon service {} ", locationUrl);
        List<HashMap<String, Object>> locationResponse = restTemplate.getForObject(locationUrl, List.class);

        if (locationResponse.get(0).get("lat") == null) {
            log.error("lat value is INVALID !!!!");
            throw new ServiceException("lat value is INVALID !!!!", HttpStatus.SC_NOT_FOUND);
        }

        if (locationResponse.get(0).get("lon") == null) {
            log.error("lon value is INVALID !!!!");
            throw new ServiceException("lon value is INVALID !!!!", HttpStatus.SC_NOT_FOUND);
        }

        String lat = String.valueOf(locationResponse.get(0).get("lat"));
        String lon = String.valueOf(locationResponse.get(0).get("lon"));
        log.info("lat={} and lon={} value in get historical weather service . ", lat, lon);

        LocalDateTime dateTime = DateUtil.convertDate(date);
        // Convert LocalDateTime to Unix timestamp
        long timestamp = dateTime.toInstant(ZoneOffset.UTC).getEpochSecond();

        apiUrl = apiUrl.replace("{lat}", lat)
                .replace("{lon}", lon)
                .replace("{date}", String.valueOf(timestamp));

        String response = restTemplate.getForObject(apiUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            log.error("getting INTERNAL_SERVER_ERROR in process getHistoricalWeatherData service !!");
            throw new ServiceException("getting INTERNAL_SERVER_ERROR in process getHistoricalWeatherData service !!", e, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
