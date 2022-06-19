package com.restaurant.booking.booking.service.config;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.restaurant.model.AverageRatingUpdateRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> producerConfig(){
        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AverageRatingSerializer.class);

        return properties;
    }

    @Bean
    public ProducerFactory<String, AverageRatingUpdateRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, AverageRatingUpdateRequest> kafkaTemplate(ProducerFactory<String, AverageRatingUpdateRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
