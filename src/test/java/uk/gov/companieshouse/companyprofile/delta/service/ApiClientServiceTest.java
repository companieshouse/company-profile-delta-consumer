package uk.gov.companieshouse.companyprofile.delta.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import consumer.exception.NonRetryableErrorException;
import consumer.exception.RetryableErrorException;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class ApiClientServiceTest {

    private final String contextId = "testContext";
    private final String companyNumber = "test12345";

    private final String uri = "/company/%s";

    private ApiClientService apiClientService;

    @Mock
    private Logger logger;

    @Mock
    private ResponseHandlerFactory responseHandlerFactory;

    @Mock
    private ApiResponse<Void> apiResponse;

    @Mock
    ResponseHandler<CompanyProfileDelete> deleteResponseHandler;
    @Mock
    ResponseHandler<CompanyProfilePut> putResponseHandler;
    @Mock
    CompanyProfile companyProfile;

    NonRetryableErrorException nonRetryableErrorException;

    @BeforeEach
    public void setUp(){
        apiClientService = new ApiClientService(logger, responseHandlerFactory);
        ReflectionTestUtils.setField(apiClientService, "apiKey", "testKey");
        ReflectionTestUtils.setField(apiClientService, "url", "http://localhost:8888");
    }

    @Test
    public void returnOkResponseWhenValidDeleteRequestSentToApi() {
        String expectedUri = String.format(uri, companyNumber);
        when(responseHandlerFactory.createResponseHandler(any())).thenReturn(deleteResponseHandler);
        when(deleteResponseHandler.handleApiResponse(any(), anyString(), anyString(), anyString(), any(CompanyProfileDelete.class))).thenReturn(apiResponse);

        ApiResponse<Void> actualResponse = apiClientService.invokeCompanyProfileDeleteHandler(contextId, companyNumber);

        assertEquals(apiResponse, actualResponse);
        verify(deleteResponseHandler).handleApiResponse(any(), eq("testContext"), eq("deleteCompanyProfile"), eq(expectedUri), any(CompanyProfileDelete.class));
    }

    @Test
    public void return404ResponseWhenInvalidDeleteRequestSentToApi() {
        when(responseHandlerFactory.createResponseHandler(any())).thenReturn(deleteResponseHandler);
        when(deleteResponseHandler.handleApiResponse(any(), anyString(), anyString(), anyString(), any(CompanyProfileDelete.class))).thenThrow(NonRetryableErrorException.class);

        assertThrows(NonRetryableErrorException.class, () -> apiClientService.invokeCompanyProfileDeleteHandler(contextId, companyNumber));
    }

    @Test
    public void return503ResponseWhenInvalidDeleteRequestSentToApi() {
        when(responseHandlerFactory.createResponseHandler(any())).thenReturn(deleteResponseHandler);
        when(deleteResponseHandler.handleApiResponse(any(), anyString(), anyString(), anyString(), any(CompanyProfileDelete.class))).thenThrow(RetryableErrorException.class);

        assertThrows(RetryableErrorException.class, () -> apiClientService.invokeCompanyProfileDeleteHandler(contextId, companyNumber));
    }

    @Test
    public void returnOkResponseWhenValidPutRequestSentToApi() {
        String expectedUri = String.format(uri, companyNumber);
        when(responseHandlerFactory.createResponseHandler(any())).thenReturn(putResponseHandler);
        when(putResponseHandler.handleApiResponse(any(), anyString(), anyString(), anyString(), any(CompanyProfilePut.class))).thenReturn(apiResponse);

        ApiResponse<Void> actualResponse = apiClientService.invokeCompanyProfilePutHandler(contextId, companyNumber, companyProfile);

        assertEquals(apiResponse, actualResponse);
        verify(putResponseHandler).handleApiResponse(any(), eq("testContext"), eq("putCompanyProfile"), eq(expectedUri), any(CompanyProfilePut.class));
    }

    @Test
    public void return404ResponseWhenInvalidPutRequestSentToApi() {
        when(responseHandlerFactory.createResponseHandler(any())).thenReturn(putResponseHandler);
        when(putResponseHandler.handleApiResponse(any(), anyString(), anyString(), anyString(), any(CompanyProfilePut.class))).thenThrow(NonRetryableErrorException.class);

        assertThrows(NonRetryableErrorException.class, () -> apiClientService.invokeCompanyProfilePutHandler(contextId, companyNumber, companyProfile));
    }

    @Test
    public void return503ResponseWhenInvalidPutRequestSentToApi() {
        when(responseHandlerFactory.createResponseHandler(any())).thenReturn(putResponseHandler);
        when(putResponseHandler.handleApiResponse(any(), anyString(), anyString(), anyString(), any(CompanyProfilePut.class))).thenThrow(RetryableErrorException.class);

        assertThrows(RetryableErrorException.class, () -> apiClientService.invokeCompanyProfilePutHandler(contextId, companyNumber, companyProfile));
    }
    
}
