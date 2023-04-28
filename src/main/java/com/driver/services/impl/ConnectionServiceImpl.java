package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user = userRepository2.findById(userId).get();

        if(user.getMaskedIP() != null)
            throw new RuntimeException("Already connected");

        else if(countryName.equalsIgnoreCase(user.getCountry().getCountryName().toString()))
            return user;

        else {
            if(user.getServiceProviderList() == null)
                throw new RuntimeException("Unable to connect");

            List<ServiceProvider> serviceProviderList = serviceProviderRepository2.findAll();

            Comparator<ServiceProvider> serp = (a,b) -> a.getId() < b.getId() ? -1 : 1;

            for(ServiceProvider serviceProvider : serviceProviderList) {
                List<Country> countryList = serviceProvider.getCountryList();
                for(Country country1 : countryList) {
                    if(countryName.equalsIgnoreCase(country1.getCountryName().toString())) {
                        Connection newConnection = new Connection();
                        newConnection.setUser(user);
                        newConnection.setServiceProvider(serviceProvider);

                        user.setMaskedIP(country1.getCodes() + "." + serviceProvider.getId() + "." + userId);
                        user.setConnected(true);

                        List<Connection> connectionList = user.getConnectionList();
                        connectionList.add((Connection) newConnection);

                        List<Connection> connectionsList = serviceProvider.getConnectionList();
                        connectionsList.add((Connection) newConnection);

                        userRepository2.save(user);
                        serviceProviderRepository2.save(serviceProvider);
                    }
                }
            }
        }

        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user = userRepository2.findById(userId).get();
        if(user.isConnected() == false)
            throw new RuntimeException("Already disconnected");

        user.setMaskedIP(null);
        user.setConnected(false);

        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User senderUser = userRepository2.findById(senderId).get();
        User receiverUser = userRepository2.findById(receiverId).get();

        if(receiverUser.isConnected()) {
            String countryCodeReceiver = receiverUser.getMaskedIP().substring(0,3);
            if(countryCodeReceiver.equalsIgnoreCase(senderUser.getCountry().getCodes()))
                return senderUser;
            else {
                String countryName = "";
                if(countryCodeReceiver.equalsIgnoreCase(CountryName.IND.toCode()))
                    countryName = CountryName.IND.toString();
                else if(countryCodeReceiver.equalsIgnoreCase(CountryName.USA.toCode()))
                    countryName = CountryName.USA.toString();
                else if(countryCodeReceiver.equalsIgnoreCase(CountryName.AUS.toCode()))
                    countryName = CountryName.AUS.toString();
                else if(countryCodeReceiver.equalsIgnoreCase(CountryName.CHI.toCode()))
                    countryName = CountryName.CHI.toString();
                else if(countryCodeReceiver.equalsIgnoreCase(CountryName.JPN.toCode()))
                    countryName = CountryName.JPN.toString();

                User aggUser = connect(senderId, countryName);
                if(!aggUser.isConnected())
                    throw new RuntimeException("Cannot establish communication");
                else
                    return aggUser;
            }
        }

        else if(!(senderUser.getCountry().getCodes() == receiverUser.getCountry().getCodes())) {
            throw new RuntimeException("Cannot establish communication");
        }

        else if(senderUser.getCountry().getCodes() == receiverUser.getCountry().getCodes()) {
            return senderUser;
        }

        throw new RuntimeException("Cannot establish communication");
    }
}
