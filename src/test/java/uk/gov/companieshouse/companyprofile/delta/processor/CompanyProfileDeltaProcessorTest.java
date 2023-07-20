package uk.gov.companieshouse.companyprofile.delta.processor;

import consumer.exception.NonRetryableErrorException;
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
import uk.gov.companieshouse.companyprofile.delta.processor.CompanyProfileDeltaProcessor;
import uk.gov.companieshouse.companyprofile.delta.service.ApiClientService;
import uk.gov.companieshouse.companyprofile.delta.transformer.CompanyProfileApiTransformer;
import uk.gov.companieshouse.companyprofile.delta.utils.TestHelper;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyProfileDeltaProcessorTest {
    private uk.gov.companieshouse.companyprofile.delta.processor.CompanyProfileDeltaProcessor processor;

    private TestHelper testHelper = new TestHelper();

    @Mock
    private Logger logger;
    @Mock
    private ApiClientService apiClientService;
    @Mock
    private CompanyProfileApiTransformer transformer;

    @BeforeEach
    public void setUp() {
        processor = new CompanyProfileDeltaProcessor(logger, apiClientService, transformer);
    }

    @Test
    void When_InvalidChsDeltaMessage_Expect_NonRetryableError() {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createInvalidChsDeltaMessage();
        assertThrows(NonRetryableErrorException.class, ()->processor.processDelta(mockChsDeltaMessage));
    }

    @Test
    @DisplayName("Confirms the Processor does not throw when a valid ChsDelta is given")
    void When_ValidChsDeltaMessage_Expect_ProcessorDoesNotThrow() throws IOException {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createChsDeltaMessage(false);
        when(transformer.transform(any(CompanyDelta.class))).thenReturn(new CompanyProfile());
        Assertions.assertDoesNotThrow(() -> processor.processDelta(mockChsDeltaMessage));
    }

    @Test
    void When_InvalidChsDeleteDeltaMessage_Expect_NonRetryableError() {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createInvalidChsDeltaMessage();
        assertThrows(NonRetryableErrorException.class, ()->processor.processDeleteDelta(mockChsDeltaMessage));
        Mockito.verify(apiClientService, times(0)).invokeCompanyProfileDeleteHandler(any(), any());

    }

    @Test
    @DisplayName("Confirms the Processor does not throw when a valid delete ChsDelta is given")
    void When_ValidChsDeleteDeltaMessage_Expect_ProcessorDoesNotThrow() throws IOException {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createChsDeltaMessage(true);
        Assertions.assertDoesNotThrow(() -> processor.processDeleteDelta(mockChsDeltaMessage));
        Mockito.verify(apiClientService, times(1)).invokeCompanyProfileDeleteHandler(any(), any());
    }
}
