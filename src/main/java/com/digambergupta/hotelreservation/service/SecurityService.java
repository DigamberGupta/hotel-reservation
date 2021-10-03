package com.digambergupta.hotelreservation.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}