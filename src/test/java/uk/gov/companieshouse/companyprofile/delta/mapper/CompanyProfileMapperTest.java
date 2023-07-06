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
import uk.gov.companieshouse.api.delta.ConfirmationStatementDates;
import uk.gov.companieshouse.api.delta.PscStatement;
import uk.gov.companieshouse.api.delta.PscStatementDelta;
import uk.gov.companieshouse.api.psc.Statement;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.never;
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
        AnnualReturn expectedAnnualReturn = new AnnualReturn();
        ConfirmationStatement expectedConfirmationStatement = new ConfirmationStatement();
        AccountingRequirement expectedAccountingRequirement = new AccountingRequirement();
        ForeignCompanyDetails expectedForeignCompanyDetails = new ForeignCompanyDetails();
        OriginatingRegistry expectedOriginatingRegistry = new OriginatingRegistry();
        RegisteredOfficeAddress expectedRegisteredOfficeAddress = new RegisteredOfficeAddress();
        RegisteredOfficeAddress expectedServiceAddress = new RegisteredOfficeAddress();

        expectedLastAccounts.setMadeUpTo(LocalDate.parse("20141231", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedLastAccounts.setPeriodEndOn(LocalDate.parse("20141231", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedLastAccounts.setPeriodStartOn(LocalDate.parse("20140101", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedLastAccounts.setType("4");

        expectedNextAccounts.setDueOn(LocalDate.parse("20160630", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedNextAccounts.setPeriodEndOn(LocalDate.parse("20151231", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedNextAccounts.setPeriodStartOn(LocalDate.parse("20150101", DateTimeFormatter.ofPattern( "yyyyMMdd" )));
        expectedAccounts.setNextDue(LocalDate.parse("20160630", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedAccounts.setNextMadeUpTo(LocalDate.parse("20151231", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedAnnualReturn.setLastMadeUpTo(LocalDate.parse("20150523", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedAnnualReturn.setNextDue(LocalDate.parse("20150523", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedAnnualReturn.setNextMadeUpTo(LocalDate.parse("20150523", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedData.setCompanyName("BUNZL PUBLIC LIMITED COMPANY");
        expectedData.setCompanyNumber("00358948");
        expectedData.setCompanyStatus("0");
        expectedData.setCompanyStatusDetail("0");

        expectedConfirmationStatement.setLastMadeUpTo(LocalDate.parse("20160606", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedConfirmationStatement.setNextDue(LocalDate.parse("20160606", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedConfirmationStatement.setNextMadeUpTo(LocalDate.parse("20160523", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedData.setDateOfCessation(LocalDate.parse("20190101", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedData.setDateOfCreation(LocalDate.parse("19400122", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedData.setDateOfDissolution(LocalDate.parse("20190101", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedData.setExternalRegistrationNumber("12345");
        profile.setDeltaAt("20190612181928152002");

        expectedAccountingRequirement.setForeignAccountType("1");

        expectedForeignCompanyDetails.setBusinessActivity("testBusinessActivity");
        expectedForeignCompanyDetails.setCompanyType("1");
        expectedForeignCompanyDetails.setGovernedBy("something");
        expectedForeignCompanyDetails.setIsACreditFinancialInstitution(true);
        expectedForeignCompanyDetails.setLegalForm("1");

        expectedOriginatingRegistry.setCountry("country");
        expectedOriginatingRegistry.setName("name");
        expectedForeignCompanyDetails.setRegistrationNumber("12345");

        expectedData.setJurisdiction("1");
        expectedData.setLastFullMembersListDate(LocalDate.parse("20140523", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedData.setProofStatus("0");

        expectedData.setRegisteredOfficeAddress(expectedRegisteredOfficeAddress);
        expectedRegisteredOfficeAddress.setAddressLine1("1 North Road");
        expectedRegisteredOfficeAddress.setAddressLine2("line2");
        expectedRegisteredOfficeAddress.setCareOf("name");
        expectedRegisteredOfficeAddress.setLocality("Cardiff");
        expectedRegisteredOfficeAddress.setCountry("Wales");
        expectedRegisteredOfficeAddress.setPostalCode("CF14 3UA");
        expectedRegisteredOfficeAddress.setPoBox("12121");
        expectedRegisteredOfficeAddress.setRegion("region");
        expectedData.setRegisteredOfficeIsInDispute(false);

        expectedData.setServiceAddress(expectedServiceAddress);
        expectedServiceAddress.setAddressLine1("1 South Road");
        expectedServiceAddress.setAddressLine2("secondLine");
        expectedServiceAddress.setCareOf("name");
        expectedServiceAddress.setLocality("Swansea");
        expectedServiceAddress.setCountry("Wales");
        expectedServiceAddress.setPostalCode("CF15 3UB");
        expectedServiceAddress.setPoBox("21212");
        expectedServiceAddress.setRegion("region");

        expectedData.setSubtype("0");
        expectedData.setType("3");
        expectedData.setUndeliverableRegisteredOfficeAddress(false);
        profile.setHasMortgages(false);

        expectedData.setAccounts(expectedAccounts);
        expectedAccounts.setLastAccounts(expectedLastAccounts);
        expectedAccounts.setNextAccounts(expectedNextAccounts);
        expectedForeignCompanyDetails.setOriginatingRegistry(expectedOriginatingRegistry);
        expectedForeignCompanyDetails.setAccountingRequirement(expectedAccountingRequirement);

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
