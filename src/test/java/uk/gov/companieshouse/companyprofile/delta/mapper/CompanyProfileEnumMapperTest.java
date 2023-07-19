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
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.delta.BooleanFlag;
import uk.gov.companieshouse.api.delta.CompanyDelta;

import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {CompanyProfileEnumMapperImpl.class})
public class CompanyProfileEnumMapperTest {

    private ObjectMapper mapper;

    private CompanyDelta companyDelta;

    private CompanyProfile companyProfile;

    private Data expectedOutputData;

    @Autowired
    CompanyProfileEnumMapper companyProfileEnumMapper;

    @BeforeEach
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String path = "company-profile-delta-enumMapper-example.json";
        String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));

        companyDelta = mapper.readValue(input, CompanyDelta.class);


        String expectedOutputPath = "company-profile-enumMapper-expected-output.json";
        String expectedOutputDataString = FileCopyUtils.copyToString
                (new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(expectedOutputPath)));
        expectedOutputData = mapper.readValue(expectedOutputDataString, Data.class);

    }
    @Test
    public void shouldMapAccountTypeEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        companyDelta1.setType("");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        expectedData.setType(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        assertEquals(expectedProfile.getData().getType(),resultProfile.getData().getType());
    }

    @Test
    public void shouldMapAccountTypeEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        companyDelta1.setType("A");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        expectedData.setType("total-exemption-small");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        assertEquals(expectedProfile.getData().getType(),resultProfile.getData().getType());
    }

    @Test
    public void shouldNotMapAccountTypeEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        companyDelta1.setType("A");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        expectedData.setType("active");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);
        assertNotEquals(expectedProfile.getData().getType(),resultProfile.getData().getType());

    }

    @Test
    public void shouldMapJurisdictionEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setJurisdiction("");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        // the expected string
        expectedData.setJurisdiction(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getJurisdiction(),resultProfile.getData().getJurisdiction());
    }

    @Test
    public void shouldMapJurisdictionEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setJurisdiction("2");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        // the expected string
        expectedData.setJurisdiction("wales");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getJurisdiction(),resultProfile.getData().getJurisdiction());
    }

    @Test
    public void shouldNotMapJurisdictionEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setJurisdiction("2");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        // the expected string
        expectedData.setJurisdiction("england-wales");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertNotEquals(expectedProfile.getData().getJurisdiction(),resultProfile.getData().getJurisdiction());
    }

    @Test
    public void shouldMapStatusEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        // the expected string
        expectedData.setCompanyStatus(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getCompanyStatus(),resultProfile.getData().getCompanyStatus());
    }

    @Test
    public void shouldMapStatusEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("2");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setCompanyStatus("liquidation");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getCompanyStatus(),resultProfile.getData().getCompanyStatus());
    }

    @Test
    public void shouldNotMapStatusEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("2");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setCompanyStatus("dissolved");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertNotEquals(expectedProfile.getData().getCompanyStatus(),resultProfile.getData().getCompanyStatus());
    }

    @Test
    public void shouldMapStatusDetailEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setCompanyStatusDetail(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getCompanyStatusDetail(),resultProfile.getData().getCompanyStatusDetail());
    }

    @Test
    public void shouldMapStatusDetailEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("5");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setCompanyStatusDetail("transferred-from-uk");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getCompanyStatusDetail(),resultProfile.getData().getCompanyStatusDetail());
    }

    @Test
    public void shouldNotMapStatusDetailEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("5");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setCompanyStatusDetail("active-proposal-to-strike-off");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertNotEquals(expectedProfile.getData().getCompanyStatusDetail(),resultProfile.getData().getCompanyStatusDetail());
    }

    @Test
    public void shouldMapProofStatusEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setProofStatus("");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setProofStatus(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getProofStatus(),resultProfile.getData().getProofStatus());
    }

    @Test
    public void shouldMapProofStatusEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setProofStatus("1");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setProofStatus("paper");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getProofStatus(),resultProfile.getData().getProofStatus());
    }

    @Test
    public void shouldNotMapProofStatusEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setProofStatus("1");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setProofStatus("pending");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertNotEquals(expectedProfile.getData().getProofStatus(),resultProfile.getData().getProofStatus());
    }

    @Test
    public void shouldMapSubTypeEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setSubtype(null);

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setSubtype(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getSubtype(),resultProfile.getData().getSubtype());
    }

    @Test
    public void shouldMapSubTypeEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setSubtype(BooleanFlag._1);
        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setSubtype("private-fund-limited-partnership");
        expectedProfile.setData(expectedData);
        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);
        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getSubtype(),resultProfile.getData().getSubtype());
    }

    @Test
    public void shouldNotMapSubTypeEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setSubtype(BooleanFlag._1);
        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        //the expected string
        expectedData.setSubtype("community-interest-company");
        expectedProfile.setData(expectedData);
        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);
        //check the expected vs outcome
        assertNotEquals(expectedProfile.getData().getSubtype(),resultProfile.getData().getSubtype());

    }

}
