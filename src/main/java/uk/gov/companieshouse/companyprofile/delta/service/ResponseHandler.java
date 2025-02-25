package uk.gov.companieshouse.companyprofile.delta.service;

import static uk.gov.companieshouse.companyprofile.delta.CompanyProfileDeltaConsumerApplication.NAMESPACE;

import consumer.exception.NonRetryableErrorException;
import consumer.exception.RetryableErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Service
public class ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(NAMESPACE);
    private static final String API_ERROR_MSG = "%s response received from company-profile-api";

    public ApiResponse<Void> handleApiResponse(String operation, String uri,
            Executor<ApiResponse<Void>> executor) {
        try {
            return executor.execute();
        } catch (URIValidationException ex) {
            String msg = "Invalid path specified";
            logger.error(msg, ex, DataMapHolder.getLogMap());
            throw new NonRetryableErrorException(msg, ex);
        } catch (ApiErrorResponseException ex) {
            HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode());
            String errMsg = String.format(API_ERROR_MSG, ex.getStatusCode());

            if (HttpStatus.CONFLICT.equals(httpStatus) || HttpStatus.BAD_REQUEST.equals(httpStatus)) {
                logger.error(errMsg, ex, DataMapHolder.getLogMap());
                throw new NonRetryableErrorException(errMsg, ex);
            } else {
                logger.info(errMsg, DataMapHolder.getLogMap());
                throw new RetryableErrorException(errMsg, ex);
            }
        } catch (Exception ex) {
            String msg = "error response";
            logger.error(msg, ex, DataMapHolder.getLogMap());
            throw new RetryableErrorException(ex);
        }
    }
}
