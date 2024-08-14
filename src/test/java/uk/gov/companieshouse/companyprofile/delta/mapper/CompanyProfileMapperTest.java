package uk.gov.companieshouse.companyprofile.delta.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;
import uk.gov.companieshouse.api.company.*;
import uk.gov.companieshouse.api.delta.CompanyDelta;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.companieshouse.api.company.CorporateAnnotation.TypeEnum._100;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = { CompanyProfileMapperImpl.class})
public class CompanyProfileMapperTest {
    private ObjectMapper mapper;
    private CompanyDelta companyDelta;
    private CompanyProfile companyProfile;

    private Data expectedOutputData;

    @Autowired
    CompanyProfileMapper companyProfileMapper;


    @BeforeEach
    public void setUp() throws Exception {
        setUpTestData("company-profile-delta-example.json", "company-profile-expected-output.json");
    }

    private void setUpTestData(String inputPath, String outputPath) throws IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        if (inputPath != null) {
            String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(inputPath)));
            companyDelta = mapper.readValue(input, CompanyDelta.class);
        }
        if (outputPath != null) {
            String expectedOutputDataString = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(outputPath)));
            expectedOutputData = mapper.readValue(expectedOutputDataString, Data.class);
        }
    }
    @Test
    public void shouldMapCompanyDeltaToCompanyProfile() throws JsonProcessingException {
        CompanyProfile profile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        assertEquals(expectedOutputData.toString(), profile.getData().toString());
    }

    @Test
    public void shouldMapCompanyDeltaToCompanyProfileRequiredToPublish() throws IOException {
        setUpTestData("company-profile-delta-required-to-publish-example.json",
                "company-profile-expected-required-to-publish-output.json");

        CompanyProfile profile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        assertEquals(expectedOutputData.toString(), profile.getData().toString());
    }

    @Test
    public void shouldMapAccountTypeEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        Accounts accounts = new Accounts();
        LastAccounts lastAccounts = new LastAccounts();

        accounts.setLastAccounts(lastAccounts);
        expectedData.setAccounts(accounts);


        //Expected field
        expectedData.getAccounts().getLastAccounts().setType("medium");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getAccounts().getLastAccounts().getType(),
                resultProfile.getData().getAccounts().getLastAccounts().getType());
    }

    @Test
    public void shouldMapAccountTypeEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        Accounts accounts = new Accounts();
        LastAccounts lastAccounts = new LastAccounts();

        accounts.setLastAccounts(lastAccounts);
        expectedData.setAccounts(accounts);


        //Expected field
        expectedData.getAccounts().getLastAccounts().setType(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getAccounts().getLastAccounts().getType(),
                resultProfile.getData().getAccounts().getLastAccounts().getType());
    }


    @Test
    public void shouldMapCicIndEnumToCorrectValues() throws IOException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setIsCommunityInterestCompany(true);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getIsCommunityInterestCompany(),
                resultProfile.getData().getIsCommunityInterestCompany());
    }

    @Test
    public void shouldMapCicIndEnumToFalseValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setIsCommunityInterestCompany(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getIsCommunityInterestCompany(),
                resultProfile.getData().getIsCommunityInterestCompany());
    }

    @Test
    public void shouldMapPartialDataAvailableFinancialConductAuthority() throws IOException {
        setUpTestData("company-profile-delta-iccompanynumber-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        expectedData.setPartialDataAvailable("full-data-available-from-financial-conduct-authority");
        expectedProfile.setData(expectedData);

        assertEquals(expectedProfile.getData().getPartialDataAvailable(),
                resultProfile.getData().getPartialDataAvailable());
    }

    @Test
    public void shouldMapPartialDataAvailableFromTheCompany() throws IOException {
        setUpTestData("company-profile-delta-rccompanynumber-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        expectedData.setPartialDataAvailable("full-data-available-from-the-company");
        expectedProfile.setData(expectedData);

        assertEquals(expectedProfile.getData().getPartialDataAvailable(),
                resultProfile.getData().getPartialDataAvailable());
    }

    @Test
    public void shouldMapPartialDataAvailableMutualsPublicRegister() throws IOException {
        setUpTestData("company-profile-delta-npcompanynumber-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        expectedData.setPartialDataAvailable("full-data-available-from-financial-conduct-authority-mutuals-public-register");
        expectedProfile.setData(expectedData);

        assertEquals(expectedProfile.getData().getPartialDataAvailable(),
                resultProfile.getData().getPartialDataAvailable());
    }

    @Test
    public void shouldMapCorpAnnotationTypeEnumToCorrectValues() throws IOException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        expectedProfile.setData(expectedData);

        assertEquals(expectedProfile.getData().getCorporateAnnotation().get(0).getType(),
                resultProfile.getData().getCorporateAnnotation().get(0).getType());
    }

    @Test
    public void shouldMapSubTypeEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setSubtype("private-fund-limited-partnership");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getSubtype(),resultProfile.getData().getSubtype());
    }

    @Test
    public void shouldMapSubTypeEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setSubtype(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getSubtype(),resultProfile.getData().getSubtype());
    }

    @Test
    public void shouldNotFailIfDeltaIsEmpty() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile nullProfile = new CompanyProfile();
        nullProfile.setData(new Data());
        CompanyDelta nullDelta = new CompanyDelta();
        nullDelta.setCompanyNumber("12345678");
        assertDoesNotThrow(() -> companyProfileMapper.mapSicCodes(nullProfile, nullDelta));
        assertDoesNotThrow(() -> companyProfileMapper.mapAccRefDate(nullProfile, nullDelta));
        assertDoesNotThrow(() -> companyProfileMapper.mapPreviousCompanyNames(nullProfile, nullDelta));
        assertDoesNotThrow(() -> companyProfileMapper.companyDeltaToCompanyProfile(nullDelta));
    }

    @Test
    public void shouldMapStatusEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setCompanyStatus("active");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getCompanyStatus(),resultProfile.getData().getCompanyStatus());
    }

    @Test
    public void shouldMapStatusEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setCompanyStatus(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getCompanyStatus(),resultProfile.getData().getCompanyStatus());
    }

    @Test
    public void shouldMapStatusDetailEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setCompanyStatusDetail("transferred-from-uk");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getCompanyStatusDetail(),resultProfile.getData().getCompanyStatusDetail());
    }

    @Test
    public void shouldMapStatusDetailEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setType(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getCompanyStatusDetail(),resultProfile.getData().getCompanyStatusDetail());
    }

    @Test
    public void shouldMapProofStatusEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setProofStatus("paper");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getProofStatus(),resultProfile.getData().getProofStatus());
    }

    @Test
    public void shouldMapProofStatusEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setProofStatus(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getProofStatus(),resultProfile.getData().getProofStatus());
    }

    @Test
    public void shouldMapJurisdictionEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setJurisdiction("england-wales");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getJurisdiction(),resultProfile.getData().getJurisdiction());
    }

    @Test
    public void shouldMapJurisdictionEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setJurisdiction(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getJurisdiction(),resultProfile.getData().getJurisdiction());
    }

    @Test
    public void shouldMapForeignAccountTypeEnumToCorrectValues() {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        ForeignCompanyDetails expectedForeignCompanyDetails = new ForeignCompanyDetails();
        AccountingRequirement expectedAccountingRequirement = new AccountingRequirement();

        expectedData.setForeignCompanyDetails(expectedForeignCompanyDetails);
        expectedForeignCompanyDetails.setAccountingRequirement(expectedAccountingRequirement);

        //Expected field
        expectedData.getForeignCompanyDetails().getAccountingRequirement().setForeignAccountType("accounting-requirements-of-originating-country-apply");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getForeignCompanyDetails()
                        .getAccountingRequirement().getForeignAccountType(),
                resultProfile.getData().getForeignCompanyDetails()
                        .getAccountingRequirement().getForeignAccountType());
    }

    @Test
    public void shouldMapAccountingRequirementToNullWhenAccReqTypeNull() throws IOException {
        setUpTestData("company-profile-delta-enumMapper-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        ForeignCompanyDetails expectedForeignCompanyDetails = new ForeignCompanyDetails();
        AccountingRequirement expectedAccountingRequirement = new AccountingRequirement();

        expectedForeignCompanyDetails.setAccountingRequirement(expectedAccountingRequirement);
        expectedData.setForeignCompanyDetails(expectedForeignCompanyDetails);

        //Expected field
        expectedData.getForeignCompanyDetails().getAccountingRequirement().setForeignAccountType(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertNull(resultProfile.getData().getForeignCompanyDetails().getAccountingRequirement());
    }

    @Test
    public void shouldMapTermsOfAccountPublicationToCorrectValues() {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        ForeignCompanyDetails expectedForeignCompanyDetails = new ForeignCompanyDetails();
        AccountingRequirement expectedAccountingRequirement = new AccountingRequirement();

        expectedData.setForeignCompanyDetails(expectedForeignCompanyDetails);
        expectedForeignCompanyDetails.setAccountingRequirement(expectedAccountingRequirement);

        //Expected field
        expectedData.getForeignCompanyDetails().getAccountingRequirement().setTermsOfAccountPublication("accounts-publication-date-supplied-by-company");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getForeignCompanyDetails()
                        .getAccountingRequirement().getTermsOfAccountPublication(),
                resultProfile.getData().getForeignCompanyDetails()
                        .getAccountingRequirement().getTermsOfAccountPublication());
    }

    @Test
    public void shouldMapForeignAccountIsCreditOrFinancialToBoolean() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        ForeignCompanyDetails expectedForeignCompanyDetails = new ForeignCompanyDetails();

        expectedData.setForeignCompanyDetails(expectedForeignCompanyDetails);

        //Expected field
        expectedData.getForeignCompanyDetails().setIsACreditFinancialInstitution(true);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getForeignCompanyDetails().getIsACreditFinancialInstitution(),
                resultProfile.getData().getForeignCompanyDetails().getIsACreditFinancialInstitution());
    }

    @Test
    public void shouldNotMapForeignCompanyAccountsWhenNull() {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        ForeignCompanyDetails expectedForeignCompanyDetails = new ForeignCompanyDetails();

        expectedData.setForeignCompanyDetails(expectedForeignCompanyDetails);
        expectedData.getForeignCompanyDetails().setAccounts(null);
        expectedProfile.setData(expectedData);

        assertEquals(expectedData.getForeignCompanyDetails().getAccounts(), resultProfile.getData().getForeignCompanyDetails().getAccounts());
    }

    @Test
    public void shouldMapNextAccountTypeEnumToCorrectValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        Accounts accounts = new Accounts();
        NextAccounts nextAccounts = new NextAccounts();

        accounts.setNextAccounts(nextAccounts);
        expectedData.setAccounts(accounts);

        //Expected field
        expectedData.getAccounts().getNextAccounts().setDueOn(LocalDate.parse("20160630", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getAccounts().getNextAccounts().getDueOn(),
                resultProfile.getData().getAccounts().getNextAccounts().getDueOn());
    }

    @Test
    public void shouldMapNextAccountTypeEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        Accounts accounts = new Accounts();
        NextAccounts nextAccounts = new NextAccounts();

        accounts.setNextAccounts(nextAccounts);
        expectedData.setAccounts(accounts);

        //Expected field
        expectedData.getAccounts().getNextAccounts().setPeriodStartOn(null);

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getAccounts().getNextAccounts().getPeriodStartOn(),
                resultProfile.getData().getAccounts().getNextAccounts().getPeriodStartOn());
    }

    @Test
    public void shouldMapAnnualReturnsTypeEnumToCorrectValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        AnnualReturn annualReturn = new AnnualReturn();
        expectedData.setAnnualReturn(annualReturn);

        //Expected field
        expectedData.getAnnualReturn().setNextDue(LocalDate.parse("20150523", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getAnnualReturn().getNextDue(),
                resultProfile.getData().getAnnualReturn().getNextDue());
    }

    @Test
    public void shouldNotReturnEmptyObjects() throws IOException {
        CompanyDelta emptyDelta = new CompanyDelta();
        emptyDelta.setSubtype("1");

        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(emptyDelta);


        //compare values
        assertNull(resultProfile.getData().getBranchCompanyDetails());
        assertNull(resultProfile.getData().getForeignCompanyDetails());
        assertNull(resultProfile.getData().getConfirmationStatement());
        assertNull(resultProfile.getData().getRegisteredOfficeAddress());
        assertNull(resultProfile.getData().getServiceAddress());
        assertNull(resultProfile.getData().getAccounts());
    }

    @Test
    public void shouldMapAnnualReturnsTypeEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        AnnualReturn annualReturn = new AnnualReturn();
        expectedData.setAnnualReturn(annualReturn);

        //Expected field
        expectedData.getAnnualReturn().setLastMadeUpTo(null);

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getAnnualReturn().getLastMadeUpTo(),
                resultProfile.getData().getAnnualReturn().getLastMadeUpTo());
    }

    @Test
    public void shouldMapConfirmationStatementTypeEnumToCorrectValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        ConfirmationStatement confirmationStatement = new ConfirmationStatement();
        expectedData.setConfirmationStatement(confirmationStatement);

        //Expected field
        expectedData.getConfirmationStatement().setLastMadeUpTo(LocalDate.parse("20160606", DateTimeFormatter.ofPattern("yyyyMMdd")));

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getConfirmationStatement().getLastMadeUpTo(),
                resultProfile.getData().getConfirmationStatement().getLastMadeUpTo());
    }

    @Test
    public void shouldMapConfirmationStatementTypeEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        ConfirmationStatement confirmationStatement = new ConfirmationStatement();
        expectedData.setConfirmationStatement(confirmationStatement);

        //Expected field
        expectedData.getConfirmationStatement().setNextDue(null);

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getConfirmationStatement().getNextDue(),
                resultProfile.getData().getConfirmationStatement().getNextDue());
    }

    @Test
    public void shouldMapDissolutionDateTypeEnumToCorrectValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setDateOfDissolution(LocalDate.parse("20200101", DateTimeFormatter.ofPattern("yyyyMMdd")));
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getDateOfDissolution(),
                resultProfile.getData().getDateOfDissolution());
    }

    @Test
    public void shouldMapCreationDateTypeEnumToNullValues() throws IOException {
        setUpTestData("company-profile-delta-dates-example.json", null);
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setDateOfCreation(null);

        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(
                expectedProfile.getData().getBranchCompanyDetails(),
                resultProfile.getData().getDateOfCreation());
    }

    @Test
    public void shouldNotMapReferenceDataIfSourceValueIs9999() throws IOException {
        //given
        setUpTestData("company-profile-delta-9999-ref-date-example.json", null);

        //when
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        //then
        assertNull(resultProfile.getData().getAccounts().getAccountingReferenceDate());
    }

}
