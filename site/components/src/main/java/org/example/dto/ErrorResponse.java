package org.example.dto;

public class ErrorResponse {

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    private String message;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
