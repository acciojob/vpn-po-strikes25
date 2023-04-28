package com.driver.services;

import com.driver.model.Admin;
import com.driver.model.ServiceProvider;

public interface AdminService {
    public Admin addServiceProvider(int adminId, String providerName);

    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception;
}