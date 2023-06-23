package uk.gov.companieshouse.companyprofile.delta.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;
import uk.gov.companieshouse.api.company.*;
import uk.gov.companieshouse.api.delta.AccountingDates;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.delta.PscStatement;
import uk.gov.companieshouse.api.delta.PscStatementDelta;
import uk.gov.companieshouse.api.psc.Statement;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = { CompanyProfileMapperImpl.class})
public class CompanyProfileMapperTest {
    private ObjectMapper mapper;
    private CompanyDelta companyDelta;
    private CompanyProfile companyProfile;


    @MockBean
    private MapperUtils mapperUtils;
    @Autowired
    CompanyProfileMapper companyProfileMapper;

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ObjectMapper();

        String path = "company-profile-delta-example.json";
        String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));

        companyDelta = mapper.readValue(input, CompanyDelta.class);

    }

    @Test
    public void shouldMapCompanyDeltaToCompanyProfile() {
        CompanyProfile profile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);
        CompanyProfile expectedResult = new CompanyProfile();

        Data expectedData = new Data();
        Accounts expectedAccounts = new Accounts();
        LastAccounts expectedLastAccounts = new LastAccounts();
        NextAccounts expectedNextAccounts = new NextAccounts();

        expectedLastAccounts.setMadeUpTo(LocalDate.parse("20141231", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedLastAccounts.setPeriodEndOn(LocalDate.parse("20141231", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedLastAccounts.setPeriodStartOn(LocalDate.parse("20140101", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedLastAccounts.setType("4");

        expectedNextAccounts.setPeriodStartOn(LocalDate.parse("20150101", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedNextAccounts.setPeriodEndOn(LocalDate.parse("20151231", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedNextAccounts.setDueOn(LocalDate.parse("20160630", DateTimeFormatter.ofPattern( "yyyyMMdd" )));

        expectedAccounts.setLastAccounts(expectedLastAccounts);
        expectedAccounts.setNextAccounts(expectedNextAccounts);

        expectedData.setAccounts(expectedAccounts);

        expectedData.setProofStatus("0");

        PreviousCompanyNames expectedPreviousCompanyName = new PreviousCompanyNames();
        expectedPreviousCompanyName.setEffectiveFrom(LocalDate.parse("19400122", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedPreviousCompanyName.setCeasedOn(LocalDate.parse("19820209", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedPreviousCompanyName.setName("BUNZL PULP & PAPER LIMITED");

        List<PreviousCompanyNames> previousCompanyNames = Arrays.asList(expectedPreviousCompanyName);
        expectedData.setPreviousCompanyNames(previousCompanyNames);

        assertEquals(expectedData, profile.getData());
    }
}
