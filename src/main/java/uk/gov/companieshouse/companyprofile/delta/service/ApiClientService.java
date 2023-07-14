package uk.gov.companieshouse.companyprofile.delta.service;

import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;
import uk.gov.companieshouse.api.http.ApiKeyHttpClient;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class ApiClientService {

    private Logger logger;

    @Value("${api.company-profile-api-key}")
    private String apiKey;

    @Value("${api.api-url}")
    private String url;

    private ResponseHandlerFactory responseHandlerFactory;

    public ApiClientService(Logger logger, ResponseHandlerFactory responseHandlerFactory) {
        this.logger = logger;
        this.responseHandlerFactory = responseHandlerFactory;
    }

    public ApiKeyHttpClient getHttpClient(String contextId) {
        ApiKeyHttpClient httpClient = new ApiKeyHttpClient(apiKey);
        httpClient.setRequestId(contextId);
        return httpClient;
    }

    public InternalApiClient getApiClient(String context) {
        InternalApiClient apiClient = new InternalApiClient(this.getHttpClient(context));
        apiClient.setBasePath(url);
        return apiClient;
    }

    public ApiResponse<Void> invokeCompanyProfilePutHandler(String context, String companyNumber,
                                                          CompanyProfile profile) {
        final String uri = String.format("/company/%s", companyNumber);
        CompanyProfilePut putExecuteOp = getApiClient(context)
                .privateDeltaResourceHandler()
                .putCompanyProfile(uri, profile);

        Map<String, Object> logMap = createLogMap(companyNumber, "PUT", uri);
        logger.infoContext(context, String.format("PUT: %s", uri), logMap);

        ResponseHandler<CompanyProfilePut> responseHandler =
                (ResponseHandler<CompanyProfilePut>) responseHandlerFactory.createResponseHandler(putExecuteOp);
        return responseHandler.handleApiResponse(logger, context, "putCompanyProfile", uri, putExecuteOp);
    }

    public Map<String, Object> createLogMap(String companyNumber, String method, String path) {
        final Map<String, Object> logMap = new HashMap<>();
        logMap.put("company_number",companyNumber);
        logMap.put("method",method);
        logMap.put("path",path);
        return logMap;
    }
}
