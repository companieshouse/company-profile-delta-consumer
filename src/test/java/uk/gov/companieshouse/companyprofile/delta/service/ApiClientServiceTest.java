package uk.gov.companieshouse.companyprofile.delta.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.delta.PrivateDeltaResourceHandler;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;

@ExtendWith(MockitoExtension.class)
class ApiClientServiceTest {
    private static final String COMPANY_NUMBER = "test12345";
    private static final String URI = "/company/%s/internal";
    private static final String DELTA_AT = "20151025185208001000";
    private static final ApiResponse<Void> SUCCESS_RESPONSE = new ApiResponse<>(200, null);

    @InjectMocks
    private ApiClientService apiClientService;

    @Mock
    private ResponseHandler responseHandler;
    @Mock
    private Supplier<InternalApiClient> internalApiClientSupplier;

    @Mock
    private CompanyProfile companyProfile;
    @Mock
    private InternalApiClient internalApiClient;
    @Mock
    private PrivateDeltaResourceHandler privateDeltaResourceHandler;
    @Mock
    private CompanyProfilePut companyProfilePut;
    @Mock
    private CompanyProfileDelete companyProfileDelete;

    @Test
    void shouldSuccessfullySendPutRequestToApi() throws Exception {
        // given
        when(internalApiClientSupplier.get()).thenReturn(internalApiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.putCompanyProfile(anyString(), any(CompanyProfile.class))).thenReturn(
                companyProfilePut);
        when(companyProfilePut.execute()).thenReturn(SUCCESS_RESPONSE);

        final String formattedUri = String.format(URI, COMPANY_NUMBER);

        // when
        apiClientService.invokeCompanyProfilePutHandler(COMPANY_NUMBER, companyProfile);

        // then
        verify(privateDeltaResourceHandler).putCompanyProfile(formattedUri, companyProfile);
        verifyNoMoreInteractions(responseHandler);
    }

    @Test
    void shouldSendPutRequestAndHandleNon200ResponseFromApi() throws Exception {
        // given
        when(internalApiClientSupplier.get()).thenReturn(internalApiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.putCompanyProfile(anyString(), any(CompanyProfile.class))).thenReturn(
                companyProfilePut);
        when(companyProfilePut.execute()).thenThrow(ApiErrorResponseException.class);

        final String formattedUri = String.format(URI, COMPANY_NUMBER);

        // when
        apiClientService.invokeCompanyProfilePutHandler(COMPANY_NUMBER, companyProfile);

        // then
        verify(privateDeltaResourceHandler).putCompanyProfile(formattedUri, companyProfile);
        verify(responseHandler).handle(any(ApiErrorResponseException.class));
    }

    @Test
    void shouldSendPutRequestAndHandleURIValidationExceptionFromApi() throws Exception {
        // given
        when(internalApiClientSupplier.get()).thenReturn(internalApiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.putCompanyProfile(anyString(), any(CompanyProfile.class))).thenReturn(
                companyProfilePut);
        when(companyProfilePut.execute()).thenThrow(URIValidationException.class);

        final String formattedUri = String.format(URI, COMPANY_NUMBER);

        // when
        apiClientService.invokeCompanyProfilePutHandler(COMPANY_NUMBER, companyProfile);

        // then
        verify(privateDeltaResourceHandler).putCompanyProfile(formattedUri, companyProfile);
        verify(responseHandler).handle(any(URIValidationException.class));
    }

    @Test
    void shouldSuccessfullySendDeleteRequestToApi() throws Exception {
        // given
        when(internalApiClientSupplier.get()).thenReturn(internalApiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.deleteCompanyProfile(anyString(), anyString())).thenReturn(companyProfileDelete);
        when(companyProfileDelete.execute()).thenReturn(SUCCESS_RESPONSE);

        final String formattedUri = String.format(URI, COMPANY_NUMBER);

        // when
        apiClientService.invokeCompanyProfileDeleteHandler(COMPANY_NUMBER, DELTA_AT);

        // then
        verify(privateDeltaResourceHandler).deleteCompanyProfile(formattedUri, DELTA_AT);
        verifyNoMoreInteractions(responseHandler);
    }

    @Test
    void shouldSendDeleteRequestAndHandleNon200ResponseFromApi() throws Exception {
        // given
        when(internalApiClientSupplier.get()).thenReturn(internalApiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.deleteCompanyProfile(anyString(), anyString())).thenReturn(companyProfileDelete);
        when(companyProfileDelete.execute()).thenThrow(ApiErrorResponseException.class);

        final String formattedUri = String.format(URI, COMPANY_NUMBER);

        // when
        apiClientService.invokeCompanyProfileDeleteHandler(COMPANY_NUMBER, DELTA_AT);

        // then
        verify(privateDeltaResourceHandler).deleteCompanyProfile(formattedUri, DELTA_AT);
        verify(responseHandler).handle(any(ApiErrorResponseException.class));
    }

    @Test
    void shouldSendDeleteRequestAndHandleURIValidationExceptionFromApi() throws Exception {
        // given
        when(internalApiClientSupplier.get()).thenReturn(internalApiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.deleteCompanyProfile(anyString(), anyString())).thenReturn(companyProfileDelete);
        when(companyProfileDelete.execute()).thenThrow(URIValidationException.class);

        final String formattedUri = String.format(URI, COMPANY_NUMBER);

        // when
        apiClientService.invokeCompanyProfileDeleteHandler(COMPANY_NUMBER, DELTA_AT);

        // then
        verify(privateDeltaResourceHandler).deleteCompanyProfile(formattedUri, DELTA_AT);
        verify(responseHandler).handle(any(URIValidationException.class));
    }
}
