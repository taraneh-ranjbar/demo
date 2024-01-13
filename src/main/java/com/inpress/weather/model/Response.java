package com.inpress.weather.model;

public class Response {

    private String result;
    private String description;


    public Response() {
    }

    public Response(String result, String description) {
        this.result = result;
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Response{" +
                ", result='" + result + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
