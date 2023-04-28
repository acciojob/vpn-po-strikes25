package com.driver.model;

import javax.persistence.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String originalIP;
    private String maskedIP;
    private boolean connected;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Country country;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Connection> connectionList = new ArrayList<java.sql.Connection>();

    @ManyToMany
    @JoinColumn
    private List<ServiceProvider> serviceProviderList = new ArrayList<>();

    public User() {
    }

    public User(int id, String username, String password, String originalIP, String maskedIP, boolean connected, Country country, List<java.sql.Connection> connectionList, List<Country> countryList) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.originalIP = originalIP;
        this.maskedIP = maskedIP;
        this.connected = connected;
        this.country = country;
        this.connectionList = connectionList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalIP() {
        return originalIP;
    }

    public void setOriginalIP(String originalIP) {
        this.originalIP = originalIP;
    }

    public String getMaskedIP() {
        return maskedIP;
    }

    public void setMaskedIP(String maskedIP) {
        this.maskedIP = maskedIP;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<java.sql.Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<java.sql.Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }
}
