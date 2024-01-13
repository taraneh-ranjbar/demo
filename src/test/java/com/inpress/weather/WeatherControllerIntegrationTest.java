package com.inpress.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WeatherControllerIntegrationTest extends DemoApplicationTests {

    private static final Logger logger = LogManager.getLogger(WeatherControllerIntegrationTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final String cityName = "Tehran";

    @Test
    @Order(1)
    public void testGetCurrentWeather_Success() throws Exception {
        logger.info("start GetCurrentWeather ");
        getCurrentWeatherData(cityName, 200);
        logger.info("finish GetCurrentWeather ");
    }

    @Test
    @Order(2)
    public void testGetWeatherForecastData_Success() throws Exception {
        logger.info("start GetWeatherForecastData ");
        getWeatherForecastData(cityName, 200);
        logger.info("finish GetWeatherForecastData ");
    }

    /* @Test
    @Order(3)
    public void testGetHistoricalWeather() throws Exception {
        String city = "London";
        String date = "2022-03-08T12:30:54";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/historical/{city}/{date}", city, date)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);
        // Assert the response body as per your requirement
    }*/

    void getCurrentWeatherData(String city, int expectedResult) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        logger.info("start get current weather data with data ({})", city);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/current/{city}", city)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(expectedResult, status);
        // Assert the response body as per your requirement
    }

    void getWeatherForecastData(String city, int expectedResult) throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        logger.info("start get forecast weather data with data ({})", city);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/forecast/{city}", city)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(expectedResult, status);
        // Assert the response body as per your requirement
    }

    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
