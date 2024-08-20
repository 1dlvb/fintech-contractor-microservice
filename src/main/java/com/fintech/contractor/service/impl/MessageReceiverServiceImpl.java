package com.fintech.contractor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.contractor.dto.MainBorrowerDTO;
import com.fintech.contractor.service.ContractorService;
import com.fintech.contractor.service.MessageReceiverService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link MessageReceiverService} interface
 * @author Matushkin Anton
 */
@Service
@RequiredArgsConstructor
public class MessageReceiverServiceImpl implements MessageReceiverService {

    @NonNull
    private ContractorService contractorService;

    @NonNull
    private ObjectMapper objectMapper;
    @Override
    @RabbitListener(queues = {"fintech-rabbitmq-deal-active-main-borrower-queue"})
    public void receive(String message) throws JsonProcessingException {
        MainBorrowerDTO mainBorrowerDTO = objectMapper.readValue(message, MainBorrowerDTO.class);
        contractorService.updateMainBorrower(mainBorrowerDTO);

    }

}
