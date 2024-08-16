package com.fintech.contractor.service.impl;

import com.fintech.contractor.service.MessageSenderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSenderServiceImpl implements MessageSenderService {

    @NonNull
    private AmqpTemplate amqpTemplate;

    @Override
    public void send(String exchange, String routingKey, String message) {
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }

}
