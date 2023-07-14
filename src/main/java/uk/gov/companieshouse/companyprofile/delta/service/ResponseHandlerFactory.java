package uk.gov.companieshouse.companyprofile.delta.service;


import org.springframework.stereotype.Component;

import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;

@Component
public class ResponseHandlerFactory {

    /**
     * return the required response handler based on the executor to be parameterised.
     * @param resourceHandler the child of the executor
     * @return parameterised response handler
     */
    public Object createResponseHandler(Object resourceHandler) {
        if (resourceHandler instanceof CompanyProfilePut) {
            return new ResponseHandler<CompanyProfilePut>();
        }
        return new ResponseHandler<CompanyProfileDelete>();
    }

}
