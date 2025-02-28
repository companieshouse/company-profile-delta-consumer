package uk.gov.companieshouse.companyprofile.delta.config;

import consumer.deserialization.AvroDeserializer;
import consumer.serialization.AvroSerializer;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.http.ApiKeyHttpClient;
import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.environment.impl.EnvironmentReaderImpl;
import uk.gov.companieshouse.kafka.serialization.SerializerFactory;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    private final String apiKey;
    private final String apiUrl;

    public ApplicationConfig(@Value("${api.company-profile-api-key}") String apiKey,
            @Value("${api.api-url}") String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Bean
    SerializerFactory serializerFactory() {
        return new SerializerFactory();
    }

    @Bean
    EnvironmentReader environmentReader() {
        return new EnvironmentReaderImpl();
    }

    @Bean
    AvroSerializer serializer() {
        return new AvroSerializer();
    }

    @Bean
    AvroDeserializer<ChsDelta> deserializer() {
        return new AvroDeserializer<>(ChsDelta.class);
    }

    @Bean
    Supplier<InternalApiClient> internalApiClientSupplier() {
        return () -> {
            ApiKeyHttpClient apiKeyHttpClient = new ApiKeyHttpClient(apiKey);
            apiKeyHttpClient.setRequestId(DataMapHolder.getRequestId());

            InternalApiClient internalApiClient = new InternalApiClient(apiKeyHttpClient);
            internalApiClient.setBasePath(apiUrl);

            return internalApiClient;
        };
    }
}
