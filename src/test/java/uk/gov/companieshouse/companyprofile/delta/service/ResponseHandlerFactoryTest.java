package uk.gov.companieshouse.companyprofile.delta.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;

public class ResponseHandlerFactoryTest {

    ResponseHandlerFactory responseHandlerFactory;

    @BeforeEach
    void setUp(){
        responseHandlerFactory = new ResponseHandlerFactory();
    }

    @Test
    void deleteExecuteOpReturnsResponseHandler(){
        CompanyProfileDelete deleteExecutor = new CompanyProfileDelete(null, null, null, null);

        assert(responseHandlerFactory.createResponseHandler(deleteExecutor) instanceof ResponseHandler<?>);
    }
    
}
