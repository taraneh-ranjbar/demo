package com.inpress.weather.exception;


public class ServiceException extends Exception {

    private int  status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ServiceException(){
        super();
    }

    public ServiceException(int status) {
        this.status = status;
    }

    public ServiceException(String message, int status) {
        super(message);
        this.status = status;
    }

    public ServiceException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }
}
