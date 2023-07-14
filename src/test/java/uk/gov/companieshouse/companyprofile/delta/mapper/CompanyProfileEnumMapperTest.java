package uk.gov.companieshouse.companyprofile.delta.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.protocol.types.Field;
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

        String path = "company-profile-delta-example.json";
        String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));

        companyDelta = mapper.readValue(input, CompanyDelta.class);


        String expectedOutputPath = "company-profile-expected-output.json";
        String expectedOutputDataString = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(expectedOutputPath)));
        expectedOutputData = mapper.readValue(expectedOutputDataString, Data.class);

    }

    @Test
    public void shouldMapTypeEnumToCorrectString() throws JsonProcessingException {
        companyProfile = new CompanyProfile();
        companyProfile.setData(new Data());

        CompanyProfile profile = companyProfileEnumMapper.companyDeltaToCompanyProfile(companyDelta);

        companyProfileEnumMapper.mapEnums(companyProfile,companyDelta);

        assertEquals(expectedOutputData,profile.getData());


    }




}
