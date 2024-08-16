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

    @Bean
    public Queue dealsContractorQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", deadLetterExchange);
        arguments.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        return new Queue(dealsContractorQueue, true, false, false, arguments);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(deadLetterQueue, true);
    }

    @Bean
    public Exchange contractorExchange() {
        return new TopicExchange(contractorExchange);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchange);
    }

    @Bean
    public Binding binding(Queue dealsContractorQueue, Exchange contractorExchange) {
        return BindingBuilder.bind(dealsContractorQueue)
                .to(contractorExchange)
                .with(routingKey)
                .noargs();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(deadLetterRoutingKey);
    }

}
