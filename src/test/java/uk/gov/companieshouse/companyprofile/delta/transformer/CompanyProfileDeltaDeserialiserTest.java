package uk.gov.companieshouse.companyprofile.delta.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import consumer.exception.NonRetryableErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import uk.gov.companieshouse.api.delta.CompanyDeleteDelta;
import uk.gov.companieshouse.api.delta.CompanyDelta;

@ExtendWith(MockitoExtension.class)
class CompanyProfileDeltaDeserialiserTest {

    private static final String COMPANY_PROFILE_DELTA = "company profile delta json string";
    private static final String COMPANY_PROFILE_DELETE_DELTA = "company profile delete delta json string";

    @InjectMocks
    private CompanyProfileDeltaDeserialiser deserialiser;

    @Mock
    private JsonMapper jsonMapper;

    @Mock
    private CompanyDelta expectedDelta;

    @Mock
    private CompanyDeleteDelta expectedDeleteDelta;

    @Test
    void shouldDeserialiseToCompanyDelta() {
        // given
        when(jsonMapper.readValue(anyString(), eq(CompanyDelta.class))).thenReturn(expectedDelta);

        // when
        CompanyDelta actual = deserialiser.deserialiseCompanyDelta(COMPANY_PROFILE_DELTA);

        // then
        assertEquals(expectedDelta, actual);
        verify(jsonMapper).readValue(COMPANY_PROFILE_DELTA, CompanyDelta.class);
    }

    @Test
    void shouldThrowNonRetryableExceptionWhenJacksonExceptionThrown() {
        // given
        when(jsonMapper.readValue(anyString(), eq(CompanyDelta.class))).thenThrow(mock(JacksonException.class));

        // when
        Executable executable = () -> deserialiser.deserialiseCompanyDelta(COMPANY_PROFILE_DELTA);

        // then
        NonRetryableErrorException actual = assertThrows(NonRetryableErrorException.class, executable);
        assertEquals("Unable to deserialise UPSERT delta: [company profile delta json string]",
                actual.getMessage());
        verify(jsonMapper).readValue(COMPANY_PROFILE_DELTA, CompanyDelta.class);
    }

    @Test
    void shouldDeserialiseToCompanyDeleteDelta() {
        // given
        when(jsonMapper.readValue(anyString(), eq(CompanyDeleteDelta.class))).thenReturn(expectedDeleteDelta);

        // when
        CompanyDeleteDelta actual = deserialiser.deserialiseCompanyDeleteDelta(COMPANY_PROFILE_DELETE_DELTA);

        // then
        assertEquals(expectedDeleteDelta, actual);
        verify(jsonMapper).readValue(COMPANY_PROFILE_DELETE_DELTA, CompanyDeleteDelta.class);
    }

    @Test
    void shouldThrowNonRetryableExceptionWhenJacksonExceptionThrownFromDeleteDelta() {
        // given
        when(jsonMapper.readValue(anyString(), eq(CompanyDeleteDelta.class))).thenThrow(mock(JacksonException.class));

        // when
        Executable executable = () -> deserialiser.deserialiseCompanyDeleteDelta(COMPANY_PROFILE_DELETE_DELTA);

        // then
        NonRetryableErrorException actual = assertThrows(NonRetryableErrorException.class, executable);
        assertEquals("Unable to deserialise DELETE delta: [company profile delete delta json string]",
                actual.getMessage());
        verify(jsonMapper).readValue(COMPANY_PROFILE_DELETE_DELTA, CompanyDeleteDelta.class);
    }

}
