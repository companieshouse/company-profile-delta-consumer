package uk.gov.companieshouse.companyprofile.delta.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import consumer.exception.NonRetryableErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDeleteDelta;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.companyprofile.delta.service.ApiClientService;
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileApiTransformer;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

import static java.lang.String.format;

@Component
public class CompanyProfileDeltaProcessor {

    private final Logger logger;
    private final ApiClientService apiClientService;
    private final CompanyProfileApiTransformer transformer;

    /**
     * processor constructor.
     */
    @Autowired
    public CompanyProfileDeltaProcessor(Logger logger, 
                                        ApiClientService apiClientService, 
                                        CompanyProfileApiTransformer transformer) {
        this.logger = logger;
        this.apiClientService = apiClientService;
        this.transformer = transformer;
    }

    /**
     * Process company profile Delta message.
     */
    public void processDelta(Message<ChsDelta> message) {
        final ChsDelta payload = message.getPayload();
        final String contextId = payload.getContextId();
        ObjectMapper objectMapper = new ObjectMapper();
        CompanyProfile companyProfile;
        CompanyDelta companyDelta;
        logger.info(format("Successfully extracted Chs Delta with context_id %s",
                contextId));
        try {
            companyDelta = objectMapper.readValue(payload.getData(), CompanyDelta.class);
            logger.trace(format("Successfully extracted company profile delta of %s",
                    companyDelta.toString()));
            companyProfile = transformer.transform(companyDelta);

            companyProfile.setDeltaAt(companyDelta.getDeltaAt());


        } catch (Exception ex) {
            throw new NonRetryableErrorException(
                    "Error when extracting company profile delta", ex);
        }

        apiClientService.invokeCompanyProfilePutHandler(contextId,
                companyDelta.getCompanyNumber(), companyProfile);
    }

    /**
     * Process company profile delete Delta message.
     */
    public void processDeleteDelta(Message<ChsDelta> message) {
        final ChsDelta payload = message.getPayload();
        final String contextId = payload.getContextId();
        CompanyDeleteDelta companyDeleteDelta;
        logger.info(format("Successfully extracted Chs Delta with context_id %s",
                contextId));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            companyDeleteDelta = objectMapper.readValue(payload.getData(), CompanyDeleteDelta.class);
        } catch (Exception ex) {
            throw new NonRetryableErrorException(
                    "Error when extracting company profile delete delta", ex);
        }
        logger.info("Sending DELETE request to company-profile-api");
        apiClientService.invokeCompanyProfileDeleteHandler(contextId, companyDeleteDelta.getCompanyNumber());
    }
}
