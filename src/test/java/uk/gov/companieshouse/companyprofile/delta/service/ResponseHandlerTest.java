package uk.gov.companieshouse.companyprofile.delta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import consumer.exception.NonRetryableErrorException;
import consumer.exception.RetryableErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;

@ExtendWith(MockitoExtension.class)
class ResponseHandlerTest {

    private final ResponseHandler responseHandler = new ResponseHandler();

    @Mock
    private CompanyProfileDelete companyProfileDelete;

    @Test
    void returnOkResponseFromDataApi() throws ApiErrorResponseException, URIValidationException {
        ApiResponse<Void> expectedResponse = new ApiResponse<>(200, null, null);
        when(companyProfileDelete.execute()).thenReturn(expectedResponse);

        ApiResponse<Void> response = responseHandler.handleApiResponse(null, null, null, companyProfileDelete);
        assertEquals(response, expectedResponse);
    }

    @Test
    void throwValidationErrorResponse() throws ApiErrorResponseException, URIValidationException {
        when(companyProfileDelete.execute()).thenThrow(new URIValidationException("invalid path"));

        RetryableErrorException thrown = assertThrows(RetryableErrorException.class,
                () -> responseHandler.handleApiResponse(null, null, null, companyProfileDelete));
        assertEquals("Invalid path specified", thrown.getMessage());
    }

    @Test
    void throwNonRetryableErrorOn400() throws ApiErrorResponseException, URIValidationException {
        HttpResponseException.Builder builder = new HttpResponseException.Builder(400,
                "bad request", new HttpHeaders());
        when(companyProfileDelete.execute()).thenThrow(new ApiErrorResponseException(builder));

        NonRetryableErrorException thrown = assertThrows(NonRetryableErrorException.class,
                () -> responseHandler.handleApiResponse(null, null, null, companyProfileDelete));
        assertEquals("400 response received from company-profile-api", thrown.getMessage());
    }

    @Test
    void throwApiErrorResponseOn409() throws ApiErrorResponseException, URIValidationException {
        HttpResponseException.Builder builder = new HttpResponseException.Builder(409,
                "conflict", new HttpHeaders());
        when(companyProfileDelete.execute()).thenThrow(new ApiErrorResponseException(builder));

        NonRetryableErrorException thrown = assertThrows(NonRetryableErrorException.class,
                () -> responseHandler.handleApiResponse(null, null, null, companyProfileDelete));
        assertEquals("409 response received from company-profile-api", thrown.getMessage());
    }

    @Test
    void throwRetryableErrorOn404() throws ApiErrorResponseException, URIValidationException {
        HttpResponseException.Builder builder = new HttpResponseException.Builder(404,
                "not found", new HttpHeaders());
        when(companyProfileDelete.execute()).thenThrow(new ApiErrorResponseException(builder));

        RetryableErrorException thrown = assertThrows(RetryableErrorException.class,
                () -> responseHandler.handleApiResponse(null, null, null, companyProfileDelete));
        assertEquals("404 response received from company-profile-api", thrown.getMessage());
    }

    @Test
    void throwERetryableErrorOn500() {
        ResponseHandler spyHandler = spy(responseHandler);
        doThrow(RetryableErrorException.class).when(spyHandler).handleApiResponse(null,
                null, null, companyProfileDelete);

        assertThrows(RetryableErrorException.class,
                () -> spyHandler.handleApiResponse(null, null, null, companyProfileDelete));
    }
}
