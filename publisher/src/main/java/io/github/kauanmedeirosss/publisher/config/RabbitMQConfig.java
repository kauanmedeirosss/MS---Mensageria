package io.github.kauanmedeirosss.publisher.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbbitmq.host}")
    private String hostname;

    @Value("${rabbbitmq.username}")
    private String userName;

    @Value("${rabbbitmq.password}")
    private String password;

    @Value("${rabbbitmq.port}")
    private int port;

    @Value("${rabbitmq.queuename}")
    private String queueName;

    @Bean
    public CachingConnectionFactory connectionFactory() throws Exception{
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception{
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

}
