package uk.gov.companieshouse.companyprofile.delta.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.companyprofile.delta.service.ApiClientService;
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileApiTransformer;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

@Component
public class CompanyProfileDeltaProcessor {

    private final CompanyProfileApiTransformer transformer;
    private final Logger logger;

    private ApiClientService apiClientService;

    /**
     * Constructor for the processor.
     * @param logger
     * @param apiClientService
     * @param transformer
    */
    @Autowired
    public CompanyProfileDeltaProcessor(Logger logger, ApiClientService apiClientService, CompanyProfileApiTransformer transformer) {
        this.logger = logger;
        this.apiClientService = apiClientService;
        this.transformer = transformer;
    }

    public void processDelta(Message<ChsDelta> chsDelta) {
        final MessageHeaders headers = chsDelta.getHeaders();
        final ChsDelta payload = chsDelta.getPayload();
        final String contextId = payload.getContextId();
    }
    
}
