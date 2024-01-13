package com.inpress.weather;

import com.inpress.weather.config.WeatherConfig;
import com.inpress.weather.exception.ServiceException;
import com.inpress.weather.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class WeatherServiceTest extends DemoApplicationTests {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private WeatherConfig weatherConfig;

    @MockBean
    private RestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        weatherService = new WeatherServiceImpl(restTemplate, weatherConfig);
    }

    @Test
    void getCurrentWeatherData_InvalidCity_ThrowsServiceException() {
        // Arrange
        String city = "1234";

        // Act and Assert
        ServiceException exception = assertThrows(ServiceException.class,
                () -> weatherService.getCurrentWeatherData(city));

        assertEquals("may be city name is null or have numeric value !!", exception.getMessage());
        assertEquals(400, exception.getStatus());

        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void getCurrentWeatherData_ValidCity_ReturnsWeatherData() throws ServiceException {
        // Arrange
        String city = "London";
        String apiUrl = "https://api.weather.com/current?city=London&appid=d64e2f1c63c80314be2d030aee82ce20";
        String jsonResponse = "{\"temperature\": 20, \"humidity\": 60}";

        // Mock the REST API response
        when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(jsonResponse);

        // Act
        Map<String, Object> weatherData = weatherService.getCurrentWeatherData(city);

        // Assert
        assertNotNull(weatherData);
        assertEquals(20, weatherData.get("temp"));
        assertEquals(60, weatherData.get("humidity"));
    }

    @Test
    public void testGetWeatherForecast() throws ServiceException {
        String city = "London";
        String apiKey = "d64e2f1c63c80314be2d030aee82ce20";

        String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

        // Mock the API response
        String jsonResponse = "{\"city\":{\"name\":\"London\"},\"list\":[{\"dt\":1653397200,\"main\":{\"temp\":283.59,\"feels_like\":282.94},\"weather\":[{\"main\":\"Clouds\",\"description\":\"overcast clouds\"}]}]}";
        when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(jsonResponse);

        // Mock the API key
        when(weatherConfig.getApiKey()).thenReturn(apiKey);

        // Expected result
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("city", Map.of("name", "London"));
        Map<String, Object> forecastInfo = new HashMap<>();
        forecastInfo.put("dt", 1653397200);
        forecastInfo.put("main", Map.of("temp", 283.59, "feels_like", 282.94));
        forecastInfo.put("weather", Map.of("main", "Clouds", "description", "overcast clouds"));
        expectedResult.put("list", forecastInfo);

        // Perform the test
        Map<String, Object> result = weatherService.getWeatherForecastData(city);

        // Assert the result
        assertEquals(expectedResult, result);
    }
}
