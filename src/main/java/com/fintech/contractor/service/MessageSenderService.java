package com.fintech.contractor.service;

/**
 * Service interface for sending messages to a message broker.
 * <p>
 * @author Matushkin Anton
 */
public interface MessageSenderService {

    /**
     * Sends a message to the specified exchange with the given routing key.
     * <p>
     * This method is used to publish a message to an exchange in a message broker. The exchange
     * and routing key determine how the message will be routed within the broker.
     * </p>
     *
     * @param exchange the name of the exchange to which the message will be sent.
     * @param routingKey the routing key used to route the message within the exchange.
     * @param content the content of the message to be sent.
     */
    void send(String exchange, String routingKey, String content);

}
