package uk.gov.companieshouse.companyprofile.delta.processor;

import consumer.exception.NonRetryableErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import uk.gov.companieshouse.companyprofile.processor.CompanyProfileDeltaProcessor;
import uk.gov.companieshouse.companyprofile.utils.TestHelper;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

import java.io.IOException;

import static org.junit.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CompanyProfileDeltaProcessorTest {
    private uk.gov.companieshouse.companyprofile.processor.CompanyProfileDeltaProcessor processor;

    private TestHelper testHelper = new TestHelper();

    @Mock
    private Logger logger;

    @BeforeEach
    public void setUp() {
        processor = new CompanyProfileDeltaProcessor(logger);
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
        Assertions.assertDoesNotThrow(() -> processor.processDelta(mockChsDeltaMessage));
    }

    @Test
    void When_InvalidChsDeleteDeltaMessage_Expect_NonRetryableError() {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createInvalidChsDeltaMessage();
        assertThrows(NonRetryableErrorException.class, ()->processor.processDeleteDelta(mockChsDeltaMessage));
    }

    @Test
    @DisplayName("Confirms the Processor does not throw when a valid delete ChsDelta is given")
    void When_ValidChsDeleteDeltaMessage_Expect_ProcessorDoesNotThrow() throws IOException {
        Message<ChsDelta> mockChsDeltaMessage = testHelper.createChsDeltaMessage(true);
        Assertions.assertDoesNotThrow(() -> processor.processDeleteDelta(mockChsDeltaMessage));
    }
}
