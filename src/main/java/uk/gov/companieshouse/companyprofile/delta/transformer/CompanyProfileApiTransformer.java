package uk.gov.companieshouse.companyprofile.delta.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import consumer.exception.NonRetryableErrorException;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.companyprofile.delta.mapper.CompanyProfileMapper;

@Component
public class CompanyProfileApiTransformer {
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
            CompanyProfile companyProfile = mapper.companyDeltaToCompanyProfile(companyDelta);

            return companyProfile;
        } catch (Exception exception) {
            throw new NonRetryableErrorException(exception);
        }
    }
    
}
