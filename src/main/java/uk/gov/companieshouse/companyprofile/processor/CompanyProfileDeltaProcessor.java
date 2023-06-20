package uk.gov.companieshouse.companyprofile.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import consumer.exception.NonRetryableErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.delta.CompanyDeleteDelta;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

import static java.lang.String.format;

@Component
public class CompanyProfileDeltaProcessor {

    private final Logger logger;

    @Autowired
    public CompanyProfileDeltaProcessor(Logger logger) {
        this.logger = logger;
    }

    /**
     * Process company profile Delta message.
     */
    public void processDelta(Message<ChsDelta> message) {
        final ChsDelta payload = message.getPayload();
        final String contextId = payload.getContextId();
        ObjectMapper objectMapper = new ObjectMapper();
        CompanyDelta companyDelta;
        logger.info(format("Successfully extracted Chs Delta with context_id %s",
                contextId));
        try {
            companyDelta = objectMapper.readValue(payload.getData(), CompanyDelta.class);
            logger.trace(format("Successfully extracted company profile delta of %s",
                    companyDelta.toString()));
        } catch (Exception ex) {
            throw new NonRetryableErrorException(
                    "Error when extracting company profile delta", ex);
        }
    }

    /**
     * Process company profile delete Delta message.
     */
    public void processDeleteDelta(Message<ChsDelta> message) {
        final ChsDelta payload = message.getPayload();
        final String contextId = payload.getContextId();
        logger.info(format("Successfully extracted Chs Delta with context_id %s",
                contextId));
        ObjectMapper objectMapper = new ObjectMapper();
        CompanyDeleteDelta companyDeleteDelta;
        try {
            companyDeleteDelta = objectMapper.readValue(payload.getData(), CompanyDeleteDelta.class);
        } catch (Exception ex) {
            throw new NonRetryableErrorException(
                    "Error when extracting company profile delete delta", ex);
        }
    }
}
