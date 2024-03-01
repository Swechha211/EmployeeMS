package com.ems.EmployeeMS.exceptions;

public class APIResponse {
    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public APIResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    public APIResponse() {

    }


}
