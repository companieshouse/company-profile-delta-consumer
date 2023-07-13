package uk.gov.companieshouse.companyprofile.delta.service;

import java.util.HashMap;
import java.util.Map;


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

    /**
     * fetches api client.
     */
    public InternalApiClient getApiClient(String context) {
        InternalApiClient apiClient = new InternalApiClient(this.getHttpClient(context));
        apiClient.setBasePath(url);
        return apiClient;
    }

    /**
     * fetches HttpClient with context ID passed to it.
     */
    public ApiKeyHttpClient getHttpClient(String contextId) {
        ApiKeyHttpClient httpClient = new ApiKeyHttpClient(apiKey);
        httpClient.setRequestId(contextId);
        return httpClient;
    }

    /**
     * Invokes delete handler for company profile.
     */
    public ApiResponse<Void> invokeCompanyProfileDeleteHandler(String context, String companyNumber) {
        final String uri = String.format("/company/%s", companyNumber);

        CompanyProfileDelete deleteExecuteOp = getApiClient(context)
                .privateDeltaResourceHandler()
                .deleteCompanyProfile(uri);

        Map<String, Object> logMap = createLogMap(companyNumber, "DELETE", uri);
        logger.infoContext(context, String.format("DELETE: %s", uri), logMap);
        ResponseHandler<CompanyProfileDelete> responseHandler = 
                (ResponseHandler<CompanyProfileDelete>) responseHandlerFactory
                        .createResponseHandler(deleteExecuteOp);

        return responseHandler.handleApiResponse(logger, 
                                                 context, 
                                                 "deleteCompanyProfile", 
                                                 uri, 
                                                 deleteExecuteOp);
    }

    // logMaps set to final
    /**
     * logger for request.
     */
    public Map<String, Object> createLogMap(String companyNumber, String method, String path) {
        final Map<String, Object> logMap = new HashMap<>();
        logMap.put("company_number",companyNumber);
        logMap.put("method",method);
        logMap.put("path",path);
        return logMap;
    }
}
