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
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileDeltaDeserialiser;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

@Component
public class CompanyProfileDeltaProcessor {

    private final ApiClientService apiClientService;
    private final CompanyProfileApiTransformer transformer;
    private final CompanyProfileDeltaDeserialiser deserialiser;

    /**
     * processor constructor.
     */
    @Autowired
    public CompanyProfileDeltaProcessor(ApiClientService apiClientService,
                                        CompanyProfileApiTransformer transformer,
                                        CompanyProfileDeltaDeserialiser deserialiser) {
        this.apiClientService = apiClientService;
        this.transformer = transformer;
        this.deserialiser = deserialiser;
    }

    /**
     * Process company profile Delta message.
     */
    public void processDelta(Message<ChsDelta> message) {
        final ChsDelta payload = message.getPayload();
        final String contextId = payload.getContextId();
        CompanyDelta companyDelta = deserialiser.deserialiseCompanyDelta(payload.getData());

        DataMapHolder.get().companyNumber(companyDelta.getCompanyNumber());

        CompanyProfile companyProfile = transformer.transform(companyDelta);
        companyProfile.setDeltaAt(companyDelta.getDeltaAt());

        apiClientService.invokeCompanyProfilePutHandler(contextId,
                companyDelta.getCompanyNumber(), companyProfile);
    }

    /**
     * Process company profile delete Delta message.
     */
    public void processDeleteDelta(Message<ChsDelta> message) {
        final ChsDelta payload = message.getPayload();
        final String contextId = payload.getContextId();
        CompanyDeleteDelta companyDeleteDelta = deserialiser.deserialiseCompanyDeleteDelta(payload.getData());

        DataMapHolder.get().companyNumber(companyDeleteDelta.getCompanyNumber());

        apiClientService.invokeCompanyProfileDeleteHandler(
                contextId, companyDeleteDelta.getCompanyNumber(), companyDeleteDelta.getDeltaAt());
    }
}
