package uk.gov.companieshouse.companyprofile.delta.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileCopyUtils;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.company.Accounts;
import uk.gov.companieshouse.api.company.LastAccounts;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.company.PreviousCompanyNames;
import uk.gov.companieshouse.api.delta.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

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
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String path = "company-profile-delta-example.json";
        String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));

        companyDelta = mapper.readValue(input, CompanyDelta.class);

        String expectedOutputPath = "company-profile-selflink-expected-output.json";
        String expectedOutputDataString = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(expectedOutputPath)));
        expectedOutputData = mapper.readValue(expectedOutputDataString, Data.class);

    }

    public void setUpforNull() throws IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String path = "company-profile-delta-enumMapper-example.json";
        String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));

        companyDelta = mapper.readValue(input, CompanyDelta.class);

        String expectedOutputPath = "company-profile-enumMapper-expected-output.json";
        String expectedOutputDataString = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(expectedOutputPath)));
        expectedOutputData = mapper.readValue(expectedOutputDataString, Data.class);
    }

    @Test
    public void shouldMapCompanyDeltaToCompanyProfile() throws JsonProcessingException {
        CompanyProfile profile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);
        CompanyProfile expectedResult = new CompanyProfile();

        assertEquals(expectedOutputData, profile.getData());
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
        setUpforNull();
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
    public void shouldMapSubTypeEnumToCorrectValues() throws JsonProcessingException {
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setSubtype("community-interest-company");
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getSubtype(),resultProfile.getData().getSubtype());
    }

    @Test
    public void shouldMapSubTypeEnumToNullValues() throws IOException {
        setUpforNull();
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
        setUpforNull();
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
        setUpforNull();
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
        setUpforNull();
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
        setUpforNull();
        CompanyProfile resultProfile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();

        //Expected field
        expectedData.setJurisdiction(null);
        expectedProfile.setData(expectedData);

        //compare values
        assertEquals(expectedProfile.getData().getJurisdiction(),resultProfile.getData().getJurisdiction());
    }
}
