package uk.gov.companieshouse.companyprofile.delta.service;

import static uk.gov.companieshouse.companyprofile.delta.CompanyProfileDeltaConsumerApplication.NAMESPACE;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfileDelete;
import uk.gov.companieshouse.api.handler.delta.companyprofile.request.CompanyProfilePut;
import uk.gov.companieshouse.api.http.ApiKeyHttpClient;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Service
public class ApiClientService {

    private static final Logger logger = LoggerFactory.getLogger(NAMESPACE);
    private static final String URI = "/company/%s";

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
    public ApiResponse<Void> invokeCompanyProfileDeleteHandler(String context, String companyNumber) {
        final String uri = String.format(URI, companyNumber);

        CompanyProfileDelete deleteExecuteOp = getApiClient(context)
                .privateDeltaResourceHandler()
                .deleteCompanyProfile(uri);

        Map<String, Object> logMap = createLogMap(companyNumber, "DELETE", uri);
        logger.infoContext(context, String.format("DELETE: %s", uri), logMap);

        return responseHandler.handleApiResponse(
                context,
                "deleteCompanyProfile",
                uri,
                deleteExecuteOp);
    }

    /**
     * Invokes put handler for company profile.
     */
    public ApiResponse<Void> invokeCompanyProfilePutHandler(String context, String companyNumber,
            CompanyProfile profile) {
        final String uri = String.format(URI, companyNumber);
        CompanyProfilePut putExecuteOp = getApiClient(context)
                .privateDeltaResourceHandler()
                .putCompanyProfile(uri, profile);

        Map<String, Object> logMap = createLogMap(companyNumber, "PUT", uri);
        logger.infoContext(context, String.format("PUT: %s", uri), logMap);

        return responseHandler.handleApiResponse(context, "putCompanyProfile", uri, putExecuteOp);
    }

    private Map<String, Object> createLogMap(String companyNumber, String method, String path) {
        final Map<String, Object> logMap = new HashMap<>();
        logMap.put("company_number", companyNumber);
        logMap.put("method", method);
        logMap.put("path", path);
        return logMap;
    }

    private InternalApiClient getApiClient(String context) {
        ApiKeyHttpClient httpClient = new ApiKeyHttpClient(apiKey);
        httpClient.setRequestId(context);
        InternalApiClient apiClient = new InternalApiClient(httpClient);
        apiClient.setBasePath(url);
        return apiClient;
    }
}
