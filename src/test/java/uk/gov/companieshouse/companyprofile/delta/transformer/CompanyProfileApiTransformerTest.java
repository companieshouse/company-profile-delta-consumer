package uk.gov.companieshouse.companyprofile.delta.transformer;

import consumer.exception.NonRetryableErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.delta.PscStatement;
import uk.gov.companieshouse.api.psc.CompanyPscStatement;
import uk.gov.companieshouse.api.psc.Statement;
import uk.gov.companieshouse.companyprofile.delta.mapper.CompanyProfileMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class CompanyProfileApiTransformerTest {

    @Mock
    private CompanyProfileMapper mapper;

    private CompanyProfileApiTransformer transformer;
    private CompanyDelta companyDelta;

    @BeforeEach
    public void setUp() {
        transformer = new CompanyProfileApiTransformer(mapper);
        companyDelta = new CompanyDelta();
    }

    @Test
    public void transformerReturnsCompanyPscStatement() {
        CompanyProfile mockProfile = mock(CompanyProfile.class);
        when(mapper.companyDeltaToCompanyProfile(companyDelta)).thenReturn(mockProfile);
        CompanyProfile actualCompanyProfile = transformer.transform(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();

        assertEquals(mockProfile, actualCompanyProfile);
    }

    @Test
    public void transformerThrowsExceptionCompanyPscStatement() {
        when(mapper.companyDeltaToCompanyProfile(companyDelta)).thenThrow(NonRetryableErrorException.class);

        assertThrows(NonRetryableErrorException.class, ()->
                transformer.transform(companyDelta));
    }
}
