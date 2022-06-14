package com.restaurant.booking.table.service.config;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

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
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ReservationSlotSerializer.class);

        return properties;
    }

    //Si usamos un POJO en vez de string lo ponemos como valor en el producer factory
    @Bean
    public ProducerFactory<String, ReservSlotsCreationRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    //Si usamos un POJO en vez de string lo ponemos como valor en el kafka template
    @Bean
    public KafkaTemplate<String, ReservSlotsCreationRequest> kafkaTemplate(ProducerFactory<String, ReservSlotsCreationRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
