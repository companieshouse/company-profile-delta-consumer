package uk.gov.companieshouse.companyprofile.delta.service;

import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;

@Service
public class ApiClientService {

    private static final String URI = "/company/%s/internal";

    private final ResponseHandler responseHandler;
    private final Supplier<InternalApiClient> internalApiClientSupplier;

    public ApiClientService(ResponseHandler responseHandler, Supplier<InternalApiClient> internalApiClientSupplier) {
        this.responseHandler = responseHandler;
        this.internalApiClientSupplier = internalApiClientSupplier;
    }

    /**
     * Invokes delete handler for company profile.
     */
    public void invokeCompanyProfileDeleteHandler(String companyNumber, String deltaAt) {
        final String formattedUri = String.format(URI, companyNumber);
        try {
            internalApiClientSupplier.get()
                    .privateDeltaResourceHandler()
                    .deleteCompanyProfile(formattedUri, deltaAt)
                    .execute();
        } catch (ApiErrorResponseException e) {
            responseHandler.handle(e);
        } catch (URIValidationException e) {
            responseHandler.handle(e);
        }
    }

    /**
     * Invokes put handler for company profile.
     */
    public void invokeCompanyProfilePutHandler(String companyNumber, CompanyProfile profile) {
        final String formattedUri = String.format(URI, companyNumber);
        try {
            internalApiClientSupplier.get()
                    .privateDeltaResourceHandler()
                    .putCompanyProfile(formattedUri, profile)
                    .execute();
        } catch (ApiErrorResponseException e) {
            responseHandler.handle(e);
        } catch (URIValidationException e) {
            responseHandler.handle(e);
        }
    }
}
