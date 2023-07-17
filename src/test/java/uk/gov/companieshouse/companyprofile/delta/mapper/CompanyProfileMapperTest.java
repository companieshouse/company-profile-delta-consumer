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
import uk.gov.companieshouse.api.company.*;
import uk.gov.companieshouse.api.delta.AccountingDates;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.delta.ConfirmationStatementDates;
import uk.gov.companieshouse.api.delta.PscStatement;
import uk.gov.companieshouse.api.delta.PscStatementDelta;
import uk.gov.companieshouse.api.psc.Statement;

import java.io.IOException;
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

        String expectedOutputPath = "company-profile-expected-output.json";
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
    public void shouldMapEmptyStringsToNullValues() throws Exception, JsonProcessingException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String path = "company-profile-delta-enumMapper-example.json";
        String input = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(path)));

        companyDelta = mapper.readValue(input, CompanyDelta.class);

        String expectedOutputPath = "company-profile-enumMapper-expected-output.json";
        String expectedOutputDataString = FileCopyUtils.copyToString(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(expectedOutputPath)));
        expectedOutputData = mapper.readValue(expectedOutputDataString, Data.class);

        CompanyProfile profile = companyProfileMapper.companyDeltaToCompanyProfile(companyDelta);
        CompanyProfile expectedResult = new CompanyProfile();

        assertEquals(expectedOutputData, profile.getData());

    }
}
