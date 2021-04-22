package com.accenture.api.services;

import java.util.Optional;

import com.accenture.api.exceptions.OrderException;
import com.accenture.api.models.Client;
import com.accenture.api.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired 
    ClientRepository clientRepository;

    public Client getById(Long id) throws OrderException{
        Optional<Client> clientOptional = clientRepository.findById(id);
        if(clientOptional.isPresent()){
            return clientOptional.get();
        }else{
            throw new OrderException("The client does not exist");
        }
    }
}
