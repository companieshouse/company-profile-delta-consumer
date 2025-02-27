package uk.gov.companieshouse.companyprofile.delta.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import consumer.exception.NonRetryableErrorException;
import consumer.exception.RetryableErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;
import uk.gov.companieshouse.api.model.ApiResponse;

@ExtendWith(MockitoExtension.class)
class ApiClientServiceTest {

    private final String contextId = "testContext";
    private final String companyNumber = "test12345";
    private final String uri = "/company/%s/internal";
    private final String deltaAt = "20151025185208001000";

    private ApiClientService apiClientService;
    @Mock
    private ResponseHandler responseHandler;

    @Mock
    private ApiResponse<Void> apiResponse;
    @Mock
    private CompanyProfile companyProfile;

    @BeforeEach
    void setUp() {
        apiClientService = new ApiClientService("testKey", "http://localhost:8888", responseHandler);
    }

    @Test
    void returnOkResponseWhenValidDeleteRequestSentToApi() {
        String expectedUri = String.format(uri, companyNumber);
        when(responseHandler.handleApiResponse(anyString(), anyString(),
                any(CompanyProfileDelete.class))).thenReturn(apiResponse);

        ApiResponse<Void> actualResponse = apiClientService.invokeCompanyProfileDeleteHandler(companyNumber, deltaAt);

        assertEquals(apiResponse, actualResponse);
        verify(responseHandler).handleApiResponse(eq("deleteCompanyProfile"), eq(expectedUri),
                any(CompanyProfileDelete.class));
    }

    @Test
    void return404ResponseWhenInvalidDeleteRequestSentToApi() {
        when(responseHandler.handleApiResponse(anyString(), anyString(),
                any(CompanyProfileDelete.class))).thenThrow(NonRetryableErrorException.class);

        assertThrows(NonRetryableErrorException.class,
                () -> apiClientService.invokeCompanyProfileDeleteHandler(companyNumber, deltaAt));
    }

    @Test
    void return503ResponseWhenInvalidDeleteRequestSentToApi() {
        when(responseHandler.handleApiResponse(anyString(), anyString(),
                any(CompanyProfileDelete.class))).thenThrow(RetryableErrorException.class);

        assertThrows(RetryableErrorException.class,
                () -> apiClientService.invokeCompanyProfileDeleteHandler(companyNumber, deltaAt));
    }

    @Test
    void returnOkResponseWhenValidPutRequestSentToApi() {
        String expectedUri = String.format(uri, companyNumber);
        when(responseHandler.handleApiResponse(anyString(), anyString(),
                any(CompanyProfilePut.class))).thenReturn(apiResponse);

        ApiResponse<Void> actualResponse = apiClientService.invokeCompanyProfilePutHandler(companyNumber,
                companyProfile);

        assertEquals(apiResponse, actualResponse);
        verify(responseHandler).handleApiResponse(eq("putCompanyProfile"), eq(expectedUri),
                any(CompanyProfilePut.class));
    }

    @Test
    void return404ResponseWhenInvalidPutRequestSentToApi() {
        when(responseHandler.handleApiResponse(anyString(), anyString(),
                any(CompanyProfilePut.class))).thenThrow(NonRetryableErrorException.class);

        assertThrows(NonRetryableErrorException.class,
                () -> apiClientService.invokeCompanyProfilePutHandler(companyNumber, companyProfile));
    }

    @Test
    void return503ResponseWhenInvalidPutRequestSentToApi() {
        when(responseHandler.handleApiResponse(anyString(), anyString(),
                any(CompanyProfilePut.class))).thenThrow(RetryableErrorException.class);

        assertThrows(RetryableErrorException.class,
                () -> apiClientService.invokeCompanyProfilePutHandler(companyNumber, companyProfile));
    }
}
