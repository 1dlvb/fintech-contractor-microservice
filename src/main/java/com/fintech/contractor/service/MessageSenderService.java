package com.fintech.contractor.service;

public interface MessageSenderService {

    void send(String exchange, String routingKey, String message);

}
