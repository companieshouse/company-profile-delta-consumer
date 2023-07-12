package uk.gov.companieshouse.companyprofile.delta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;

import consumer.exception.NonRetryableErrorException;
import consumer.exception.RetryableErrorException;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class ResponseHandlerTest {


    private ResponseHandler responseHandler;

    @Mock
    private Logger logger;

    @Mock
    private CompanyProfileDelete companyProfileDelete;

    @BeforeEach
    public void setUp(){
        responseHandler = new ResponseHandler();
    }

    @Test
    public void returnOkResponseFromDataApi() throws ApiErrorResponseException, URIValidationException {
        ApiResponse<Void> expectedResponse = new ApiResponse<>(200, null, null);
        when(companyProfileDelete.execute()).thenReturn(expectedResponse);

        ApiResponse<Void> response = responseHandler.
                handleApiResponse(logger,null,null,null,companyProfileDelete);
        assertEquals(response, expectedResponse);
    }

    @Test
    public void throwValidationErrorResponse() throws ApiErrorResponseException, URIValidationException {
        when(companyProfileDelete.execute()).thenThrow(new URIValidationException("invalid path"));

        RetryableErrorException thrown = assertThrows(RetryableErrorException.class, ()-> {
            responseHandler.
                    handleApiResponse(logger, null,null,null, companyProfileDelete);
        });
        assertEquals("Invalid path specified", thrown.getMessage());
    }

    @Test
    public void throwApiErrorResponseOn400() throws ApiErrorResponseException, URIValidationException {
        HttpResponseException.Builder builder = new HttpResponseException.Builder(400,
                "BAD_REQUEST",new HttpHeaders());
        when(companyProfileDelete.execute()).thenThrow(new ApiErrorResponseException(builder));

        NonRetryableErrorException thrown = assertThrows(NonRetryableErrorException.class, ()-> {
            responseHandler.
                    handleApiResponse(logger, null,null,null, companyProfileDelete);
        });
        assertEquals("400 BAD_REQUEST response received from company-profile-api", thrown.getMessage());
    }

    @Test
    public void throwErrorResponseOn404() throws ApiErrorResponseException, URIValidationException {
        HttpResponseException.Builder builder = new HttpResponseException.Builder(404,
                "server error",new HttpHeaders());
        when(companyProfileDelete.execute()).thenThrow(new ApiErrorResponseException(builder));

        RetryableErrorException thrown = assertThrows(RetryableErrorException.class, ()-> {
            responseHandler.
                    handleApiResponse(logger, null,null,null, companyProfileDelete);
        });
        assertEquals("server error with 404 NOT_FOUND returned from company-profile-api", thrown.getMessage());
    }

    @Test
    public void throwErrorResponseOn500() {
        ResponseHandler spyHandler = spy(responseHandler);
        doThrow(RetryableErrorException.class).when(spyHandler).handleApiResponse(logger, null,
                null, null, companyProfileDelete);

        RetryableErrorException thrown = assertThrows(RetryableErrorException.class, ()-> {
            spyHandler.
                    handleApiResponse(logger, null,null,null, companyProfileDelete);
        });
    }
    
}
