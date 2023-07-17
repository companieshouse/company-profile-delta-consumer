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
/*    @Test
    public void shouldMapTypeEnumToNullValues() throws JsonProcessingException {
        companyProfile = new CompanyProfile();
        companyProfile.setData(new Data());

        CompanyProfile profile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta);

        companyProfileEnumMapper.mapEnums(companyProfile,companyDelta);

        assertEquals(expectedOutputData,profile.getData());
    }*/
    @Test
    public void shouldMapTypeEnumToNullValues() throws JsonProcessingException {
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
    public void shouldMapTypeEnumToCorrectValues() throws JsonProcessingException {
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
    public void shouldNotMapTypeEnumToIncorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        companyDelta1.setType("A");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        expectedData.setType("active");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);
        assertNotEquals(expectedProfile.getData().getType(),resultProfile.getData().getType());

        //assertEquals(expectedProfile.getData().getType(),resultProfile.getData().getType());
    }

    @Test
    public void shouldMapStatusEnumToNullValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        // the expected string
        expectedData.setStatus(null);
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getStatus(),resultProfile.getData().getStatus());
    }

    @Test
    public void shouldMapStatusEnumToCorrectValues() throws JsonProcessingException {
        CompanyDelta companyDelta1 = new CompanyDelta();
        //set the enum value to test
        companyDelta1.setStatus("AA");

        CompanyProfile expectedProfile = new CompanyProfile();
        Data expectedData = new Data();
        // the expected string
        expectedData.setStatus("active");
        expectedProfile.setData(expectedData);

        CompanyProfile resultProfile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta1);

        //check the expected vs outcome
        assertEquals(expectedProfile.getData().getStatus(),resultProfile.getData().getStatus());
    }
}
