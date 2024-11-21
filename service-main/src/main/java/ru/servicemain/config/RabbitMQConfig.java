package ru.servicemain.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
@DependsOn("dataSource")
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "alerts.exchange";

    private final RabbitAdmin rabbitAdmin;

    public RabbitMQConfig(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue fireQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        return new Queue("alerts.fire", true, false, false, args);
    }

    @Bean
    public Queue floodQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 9);
        return new Queue("alerts.flood", true, false, false, args);
    }

    @Bean
    public Queue earthquakeQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 8);
        return new Queue("alerts.earthquake", true, false, false, args);
    }

    @Bean
    public Queue stormQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 5);
        return new Queue("alerts.storm", true, false, false, args);
    }

    @Bean
    public Queue windQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 3);
        return new Queue("alerts.wind", true, false, false, args);
    }

    @Bean
    public Binding bindingFire(@Qualifier("fireQueue") Queue fireQueue, TopicExchange exchange) {
        return BindingBuilder.bind(fireQueue).to(exchange).with("disaster.fire");
    }

    @Bean
    public Binding bindingFlood(@Qualifier("floodQueue") Queue floodQueue, TopicExchange exchange) {
        return BindingBuilder.bind(floodQueue).to(exchange).with("disaster.flood");
    }

    @Bean
    public Binding bindingEarthquake(@Qualifier("earthquakeQueue") Queue earthquakeQueue, TopicExchange exchange) {
        return BindingBuilder.bind(earthquakeQueue).to(exchange).with("disaster.earthquake");
    }

    @Bean
    public Binding bindingStorm(@Qualifier("stormQueue") Queue stormQueue, TopicExchange exchange) {
        return BindingBuilder.bind(stormQueue).to(exchange).with("disaster.storm");
    }

    @Bean
    public Binding bindingWind(@Qualifier("windQueue") Queue windQueue, TopicExchange exchange) {
        return BindingBuilder.bind(windQueue).to(exchange).with("disaster.wind");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeRabbitMQ() {
        System.out.println("Initializing RabbitMQ...");
        rabbitAdmin.initialize();
    }
}
