package uk.gov.companieshouse.companyprofile.delta.transformer;

import static uk.gov.companieshouse.companyprofile.delta.CompanyProfileDeltaConsumerApplication.NAMESPACE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import consumer.exception.NonRetryableErrorException;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;
import uk.gov.companieshouse.companyprofile.delta.mapper.CompanyProfileMapper;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class CompanyProfileApiTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private static final String TRANSFORM_ERROR_MESSAGE = "Error transforming company profile";

    private final CompanyProfileMapper mapper;

    /**
     * Constructor for the transformer.
     * @param mapper returns the company profile api object.
     */
    @Autowired
    public CompanyProfileApiTransformer(CompanyProfileMapper mapper) {
        this.mapper = mapper;
    }

    /**transforms CompanyDelta into CompanyProfile. */
    public CompanyProfile transform(CompanyDelta companyDelta) {
        try {
            return mapper.companyDeltaToCompanyProfile(companyDelta);
        } catch (Exception exception) {
            LOGGER.error(TRANSFORM_ERROR_MESSAGE, DataMapHolder.getLogMap());
            throw new NonRetryableErrorException(exception);
        }
    }
    
}
