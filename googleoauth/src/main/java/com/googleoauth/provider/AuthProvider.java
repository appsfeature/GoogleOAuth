package com.googleoauth.provider;


public class AuthProvider {
    private String serverId;

    private static AuthProvider instance;

    public static AuthProvider getInstance() {
        if (instance == null) {
            instance = new AuthProvider();
        }
        return instance;
    }

    public String getServerId() {
        return serverId;
    }

    public void addServerId(String serverId) {
        this.serverId = serverId;
    }

}
