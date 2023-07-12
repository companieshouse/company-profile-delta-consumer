package uk.gov.companieshouse.companyprofile.delta.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.http.ApiKeyHttpClient;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.logging.Logger;

@Service
@SuppressWarnings("unchecked")
public class ApiClientService {

    @Value("${api.company-profile-api-key}")
    private String apiKey;

    @Value("${api.api-url}")
    private String url;

    private Logger logger;

    private ResponseHandlerFactory responseHandlerFactory;

    public ApiClientService(Logger logger, ResponseHandlerFactory responseHandlerFactory) {
        this.logger = logger;
        this.responseHandlerFactory = responseHandlerFactory;
    }

    public InternalApiClient getApiClient(String context) {
        InternalApiClient apiClient = new InternalApiClient(this.getHttpClient(context));
        apiClient.setBasePath(url);
        return apiClient;
    }

    public ApiKeyHttpClient getHttpClient(String contextId) {
        ApiKeyHttpClient httpClient = new ApiKeyHttpClient(apiKey);
        httpClient.setRequestId(contextId);
        return httpClient;
    }

    public ApiResponse<Void> invokeCompanyProfileDeleteHandler(String context, String companyNumber) {
        final String uri = String.format("/company/%s", companyNumber);

        CompanyProfileDelete deleteExecuteOp = getApiClient(context).privateDeltaResourceHandler().deleteCompanyProfile(uri, companyNumber);


    }
}
