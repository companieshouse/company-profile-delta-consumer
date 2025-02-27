package uk.gov.companieshouse.companyprofile.delta.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;
import uk.gov.companieshouse.api.http.ApiKeyHttpClient;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;

@Service
public class ApiClientService {

    private static final String URI = "/company/%s/internal";

    private final String apiKey;
    private final String url;
    private final ResponseHandler responseHandler;

    public ApiClientService(@Value("${api.company-profile-api-key}") String apiKey,
            @Value("${api.api-url}") String url, ResponseHandler responseHandler) {
        this.apiKey = apiKey;
        this.url = url;
        this.responseHandler = responseHandler;
    }

    /**
     * Invokes delete handler for company profile.
     */
    public ApiResponse<Void> invokeCompanyProfileDeleteHandler(String companyNumber, String deltaAt) {
        final String uri = String.format(URI, companyNumber);

        CompanyProfileDelete deleteExecuteOp = getApiClient()
                .privateDeltaResourceHandler()
                .deleteCompanyProfile(uri, deltaAt);

        return responseHandler.handleApiResponse(
                "deleteCompanyProfile",
                uri,
                deleteExecuteOp);
    }

    /**
     * Invokes put handler for company profile.
     */
    public ApiResponse<Void> invokeCompanyProfilePutHandler(String companyNumber,
            CompanyProfile profile) {
        final String uri = String.format(URI, companyNumber);
        CompanyProfilePut putExecuteOp = getApiClient()
                .privateDeltaResourceHandler()
                .putCompanyProfile(uri, profile);

        return responseHandler.handleApiResponse("putCompanyProfile", uri, putExecuteOp);
    }

    private InternalApiClient getApiClient() {
        ApiKeyHttpClient httpClient = new ApiKeyHttpClient(apiKey);
        httpClient.setRequestId(DataMapHolder.getRequestId());
        InternalApiClient apiClient = new InternalApiClient(httpClient);
        apiClient.setBasePath(url);
        return apiClient;
    }
}
