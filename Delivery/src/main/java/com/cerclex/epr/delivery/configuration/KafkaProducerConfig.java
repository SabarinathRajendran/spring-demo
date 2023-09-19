package com.cerclex.epr.delivery.configuration;

import com.cerclex.epr.events.model.delivery.DeliveryAllocationEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, DeliveryAllocationEvent> producerFactoryDeliveryToTargetMap(){
        Map<String, Object> configProps = new HashMap<>();
        //additional config parameters ....
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String , DeliveryAllocationEvent> KafkaDeliveryToTarget(){
        return new KafkaTemplate<>(producerFactoryDeliveryToTargetMap());
    }
}
