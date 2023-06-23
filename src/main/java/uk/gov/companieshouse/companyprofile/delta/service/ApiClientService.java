package uk.gov.companieshouse.companyprofile.delta.service;

import org.springframework.stereotype.Service;

import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.logging.Logger;

@Service
@SuppressWarnings("unchecked")
public class ApiClientService {

    private Logger logger;

    private ResponseHandlerFactory responseHandlerFactory;

    public ApiClientService(Logger logger, ResponseHandlerFactory responseHandlerFactory) {
        this.logger = logger;
        this.responseHandlerFactory = responseHandlerFactory;
    }
}
