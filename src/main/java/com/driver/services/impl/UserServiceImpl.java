package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username,   String password, String countryName) throws Exception{

        // Important function ;
        User newUser = new User();
        newUser.setPassword(password);
        newUser.setUsername(username);
        newUser.setMaskedIP(null);
        newUser.setConnected(false);

        // To link the country code in Country and User DB ;
        Country country = new Country();
        if(countryName.equalsIgnoreCase("IND")){
            country.setCountryName(CountryName.IND);
            country.setCodes(CountryName.IND.toCode());
        }
        if(countryName.equalsIgnoreCase("USA")){
            country.setCountryName(CountryName.USA);
            country.setCodes(CountryName.USA.toCode());
        }
        if(countryName.equalsIgnoreCase("JPN")){
            country.setCountryName(CountryName.JPN);
            country.setCodes(CountryName.JPN.toCode());
        }
        if(countryName.equalsIgnoreCase("CHI")){
            country.setCountryName(CountryName.CHI);
            country.setCodes(CountryName.CHI.toCode());
        }
        if(countryName.equalsIgnoreCase("AUA")){
            country.setCountryName(CountryName.AUS);
            country.setCodes(CountryName.AUS.toCode());
        }

        newUser.setCountry(country);
        country.setUser(newUser);

        // Here we have simultaneously saved the newUser and also fetched the ID ;
        String code = country.getCodes() + "." + userRepository3.save(newUser).getId();
        newUser.setOriginalIP(code);

        userRepository3.save(newUser);

        return newUser;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User updatedUser = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

        updatedUser.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUserList().add(updatedUser);

        userRepository3.save(updatedUser);

        return updatedUser;
    }
}
