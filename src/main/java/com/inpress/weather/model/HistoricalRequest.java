package com.inpress.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class HistoricalRequest implements Serializable {

    private String city;

    @Schema(name = "date", description = "input LocalDateTime value",example = "2017-03-08T12:30:54")
    @JsonProperty("date")
    private String date;


}
