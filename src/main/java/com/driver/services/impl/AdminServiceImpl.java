package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    public Admin register(String username, String password) {
        Admin newAdmin = new Admin();
        newAdmin.setPassword(password);
        newAdmin.setUsername(username);

        adminRepository1.save(newAdmin);
        return newAdmin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin = adminRepository1.findById(adminId).get();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setAdmin(admin);
        serviceProvider.setName(providerName);

        admin.getServiceProviderList().add(serviceProvider);

        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();

        Country newCountry = new Country();
        String code = countryName.substring(0,3).toUpperCase();
        if(code.equals("IND")) {
            newCountry.setCountryName(CountryName.IND);
            newCountry.setCodes("001");
        }
        else if(code.equals("USA")){
            newCountry.setCountryName(CountryName.USA);
            newCountry.setCodes("002");
        }
        else if(code.equals("AUS")){
            newCountry.setCountryName(CountryName.AUS);
            newCountry.setCodes("003");
        }
        else if(code.equals("JPN")) {
            newCountry.setCountryName(CountryName.JPN);
            newCountry.setCodes("005");
        }
        else if(code.equals("CHI")) {
            newCountry.setCountryName(CountryName.CHI);
            newCountry.setCodes("004");
        }
        else
            throw new RuntimeException("Country not found");

        newCountry.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(newCountry);

        serviceProviderRepository1.save(serviceProvider);
        countryRepository1.save(newCountry);

        return serviceProvider;
    }
}
