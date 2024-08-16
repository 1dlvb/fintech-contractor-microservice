package com.fintech.contractor.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageReceiverService {

    void receive(String message) throws JsonProcessingException;

}
