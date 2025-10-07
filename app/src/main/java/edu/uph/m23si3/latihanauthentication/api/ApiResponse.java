package edu.uph.m23si3.latihanauthentication.api;

import java.util.List;

public class ApiResponse<T> {
    private List<T> data;

    public List<T> getData() {
        return data;
    }
}
