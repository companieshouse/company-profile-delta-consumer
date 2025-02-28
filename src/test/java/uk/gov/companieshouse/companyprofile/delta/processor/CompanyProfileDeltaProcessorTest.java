package uk.gov.companieshouse.companyprofile.delta.processor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.companyprofile.delta.service.ApiClientService;
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileApiTransformer;
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileDeltaDeserialiser;
import uk.gov.companieshouse.companyprofile.delta.utils.TestHelper;
import uk.gov.companieshouse.delta.ChsDelta;

@ExtendWith(MockitoExtension.class)
class CompanyProfileDeltaProcessorTest {
    private CompanyProfileDeltaProcessor processor;

    private final TestHelper testHelper = new TestHelper();

    @Mock
    private ApiClientService apiClientService;
    @Mock
    private CompanyProfileApiTransformer transformer;

    @BeforeEach
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        CompanyProfileDeltaDeserialiser deserialiser = new CompanyProfileDeltaDeserialiser(mapper);
        processor = new CompanyProfileDeltaProcessor(apiClientService, transformer, deserialiser);
    }

    @Test
    @DisplayName("Confirms the Processor does not throw when a valid ChsDelta is given")
    void When_ValidChsDeltaMessage_Expect_ProcessorDoesNotThrow() throws IOException {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createChsDeltaMessage(false);
        when(transformer.transform(any(CompanyDelta.class))).thenReturn(new CompanyProfile());
        Assertions.assertDoesNotThrow(() -> processor.processDelta(mockChsDeltaMessage));
    }

    @Test
    @DisplayName("Confirms the Processor does not throw when a valid delete ChsDelta is given")
    void When_ValidChsDeleteDeltaMessage_Expect_ProcessorDoesNotThrow() throws IOException {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createChsDeltaMessage(true);
        Assertions.assertDoesNotThrow(() -> processor.processDeleteDelta(mockChsDeltaMessage));
        Mockito.verify(apiClientService).invokeCompanyProfileDeleteHandler(any(), any());
    }
}
