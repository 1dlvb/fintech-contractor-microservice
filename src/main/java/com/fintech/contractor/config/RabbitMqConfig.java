package com.fintech.contractor.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * Configuration class for setting up RabbitMQ exchanges, queues, and bindings.
 * <p>
 * This configuration class defines the necessary beans. Such as exchanges,
 * queues, and bindings, dead-letter queues and message TTL
 * settings to handle message routing and expiration.
 * </p>
 * @author Matushkin Anton
 */
@Configuration
public class RabbitMqConfig {

    @Value("${contractor.exchange.name}")
    private String contractorExchange;

    @Value("${contractor.deals.queue}")
    private String dealsContractorQueue;

    @Value("${contractor.deals.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.dead-letter.exchange}")
    private String deadLetterExchange;

    @Value("${rabbitmq.dead-letter.queue}")
    private String deadLetterQueue;

    @Value("${rabbitmq.dead-letter.routing.key}")
    private String deadLetterRoutingKey;

    @Value("${rabbitmq.dead-letter.message.ttl}")
    private long deadLetterMessageTTL;


    @Value("${rabbitmq.queue.name}")
    private String queueActiveMainBorrowerQueueName;

    /**
     * Defines a queue for contractor deals with dead-letter exchange settings.
     * <p>
     * @return the configured {@link Queue} for contractor deals.
     */
    @Bean
    public Queue dealsContractorQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", deadLetterExchange);
        arguments.put("x-dead-letter-routing-key", deadLetterRoutingKey);

        return new Queue(dealsContractorQueue, true, false, false, arguments);
    }

    /**
     * Defines a queue for active main borrowers.
     * <p>
     * @return the configured {@link Queue} for active main borrowers.
     */
    @Bean
    public Queue activeMainBorrowerQueue() {
        return new Queue(queueActiveMainBorrowerQueueName, true);
    }

    /**
     * Defines a dead-letter queue with message TTL settings.
     * <p>
     * For messages that expire or fail to process are redirected to this queue.
     * </p>
     * @return the configured {@link Queue} for dead letters.
     */
    @Bean
    public Queue deadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", deadLetterMessageTTL);
        arguments.put("x-dead-letter-exchange", contractorExchange);

        return new Queue(deadLetterQueue, true, false, false, arguments);
    }

    /**
     * Defines a topic exchange for contractor-related messages.
     * <p>
     * @return the configured {@link Exchange} for contractor-related messages.
     */
    @Bean
    public Exchange contractorExchange() {
        return new TopicExchange(contractorExchange);
    }

    /**
     * Defines a direct exchange for dead-letter messages.
     * <p>
     * @return the configured {@link DirectExchange} for dead-letter messages.
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchange);
    }

    /**
     * Defines a binding between the contractor deals queue and the contractor exchange.
     * <p>
     * This binding specifies the routing key used to route messages to the contractor deals queue.
     * </p>
     * @return the configured {@link Binding} between the contractor deals queue and the contractor exchange.
     */
    @Bean
    public Binding contractorBinding() {
        return BindingBuilder.bind(dealsContractorQueue())
                .to(contractorExchange())
                .with(routingKey)
                .noargs();
    }

    /**
     * Defines a binding between the dead-letter queue and the dead-letter exchange.
     * <p>
     * This binding specifies the routing key used to route messages to the dead-letter queue.
     * </p>
     * @return the configured {@link Binding} between the dead-letter queue and the dead-letter exchange.
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(deadLetterRoutingKey);
    }

}
