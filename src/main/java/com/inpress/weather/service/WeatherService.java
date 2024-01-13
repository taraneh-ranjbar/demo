package com.inpress.weather.service;

import com.inpress.weather.exception.ServiceException;

import java.io.IOException;
import java.util.Map;

public interface WeatherService {

    Map<String, Object> getCurrentWeatherData(String city) throws ServiceException;
    Map<String, Object> getWeatherForecastData(String city) throws ServiceException;
    Map<String, Object> getHistoricalWeatherData(String city, String date) throws ServiceException;


}
