package com.fintech.contractor.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Service interface for receiving and processing messages.
 * @author Matushkin Anton
 */
public interface MessageReceiverService {

    /**
     * Processes a received message.
     * <p>
     * This method is called when a new message is received. The message is typically in JSON format and needs to be
     * deserialized or processed accordingly.
     * </p>
     *
     * @param message the message to be processed.
     * @throws JsonProcessingException if there is an error processing the message.
     */
    void receive(String message) throws JsonProcessingException;

}
