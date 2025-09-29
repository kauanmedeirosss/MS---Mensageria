package io.github.kauanmedeirosss.subscriber.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

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
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(
            ConnectionFactory connectionFactory,
            RetryOperationsInterceptor retryOperationsInterceptor
    ){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryOperationsInterceptor);
        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() throws Exception{
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    Queue createQueue() throws Exception{
        Queue q = QueueBuilder.durable(queueName).build();
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    Queue boqQueue() throws Exception{
        Queue q = QueueBuilder.durable("boq." + queueName).build();
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    public RepublishMessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate){
        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(rabbitTemplate);
        recoverer.setErrorRoutingKeyPrefix("boq.");
        return recoverer;
    }

    @Bean
    public RetryOperationsInterceptor retryOperationsInterceptor(
            RepublishMessageRecoverer recoverer) {
        RetryOperationsInterceptor interceptor = RetryInterceptorBuilder
                .stateless()
                .maxAttempts(2)
                .backOffOptions(2000, 1, 100000)
                .recoverer((MethodInvocationRecoverer<?>) recoverer)
                .build();
        return interceptor;
    }

}