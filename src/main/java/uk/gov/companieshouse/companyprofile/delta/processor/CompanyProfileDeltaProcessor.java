package uk.gov.companieshouse.companyprofile.delta.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDeleteDelta;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.companyprofile.delta.exception.RetryableErrorException;
import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;
import uk.gov.companieshouse.companyprofile.delta.service.ApiClientService;
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileApiTransformer;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

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
        logger.infoContext(contextId, "Successfully extracted Chs Delta",
                DataMapHolder.getLogMap());
        try {
            companyDelta = objectMapper.readValue(payload.getData(), CompanyDelta.class);
            DataMapHolder.get()
                    .companyNumber(companyDelta.getCompanyNumber());
            logger.infoContext(contextId, "Successfully extracted company profile delta",
                    DataMapHolder.getLogMap());

            companyProfile = transformer.transform(companyDelta);
            logger.infoContext(contextId, "Successfully transformed company profile",
                    DataMapHolder.getLogMap());

            companyProfile.setDeltaAt(companyDelta.getDeltaAt());
        } catch (Exception ex) {
            logger.errorContext(contextId, ex.getMessage(), ex, DataMapHolder.getLogMap());
            throw new RetryableErrorException(
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
        logger.infoContext(contextId,"Successfully extracted Chs delete Delta",
                DataMapHolder.getLogMap());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            companyDeleteDelta = objectMapper.readValue(payload.getData(),
                    CompanyDeleteDelta.class);
            DataMapHolder.get()
                    .companyNumber(companyDeleteDelta.getCompanyNumber());
        } catch (Exception ex) {
            logger.errorContext(contextId, ex.getMessage(), ex, DataMapHolder.getLogMap());
            throw new RetryableErrorException(
                    "Error when extracting company profile delete delta", ex);
        }
        logger.infoContext(contextId, "Sending DELETE request to company-profile-api",
                DataMapHolder.getLogMap());
        apiClientService.invokeCompanyProfileDeleteHandler(
                contextId, companyDeleteDelta.getCompanyNumber(), companyDeleteDelta.getDeltaAt());
    }
}
