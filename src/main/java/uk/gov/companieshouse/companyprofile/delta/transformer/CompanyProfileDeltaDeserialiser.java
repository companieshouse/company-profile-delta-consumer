package uk.gov.companieshouse.companyprofile.delta.transformer;

import static uk.gov.companieshouse.companyprofile.delta.CompanyProfileDeltaConsumerApplication.NAMESPACE;

import consumer.exception.NonRetryableErrorException;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import uk.gov.companieshouse.api.delta.CompanyDeleteDelta;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class CompanyProfileDeltaDeserialiser {
    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private static final String UPSERT_ERROR_MESSAGE = "Unable to deserialise UPSERT delta: [%s]";
    private static final String DELETE_ERROR_MESSAGE = "Unable to deserialise DELETE delta: [%s]";

    private final JsonMapper jsonMapper;

    public CompanyProfileDeltaDeserialiser(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public CompanyDelta deserialiseCompanyDelta(String data) {
        try {
            return jsonMapper.readValue(data, CompanyDelta.class);
        } catch (JacksonException ex) {
            LOGGER.error(String.format(UPSERT_ERROR_MESSAGE, data), ex, DataMapHolder.getLogMap());
            throw new NonRetryableErrorException(String.format(UPSERT_ERROR_MESSAGE, data), ex);
        }
    }

    public CompanyDeleteDelta deserialiseCompanyDeleteDelta(String data) {
        try {
            return jsonMapper.readValue(data, CompanyDeleteDelta.class);
        } catch (JacksonException ex) {
            LOGGER.error(String.format(DELETE_ERROR_MESSAGE, data), ex, DataMapHolder.getLogMap());
            throw new NonRetryableErrorException(String.format(DELETE_ERROR_MESSAGE, data), ex);
        }
    }
}
