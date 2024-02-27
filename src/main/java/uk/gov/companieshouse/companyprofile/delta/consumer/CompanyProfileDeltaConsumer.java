package uk.gov.companieshouse.companyprofile.delta.consumer;

import consumer.exception.NonRetryableErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.FixedDelayStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import uk.gov.companieshouse.companyprofile.delta.logging.DataMapHolder;
import uk.gov.companieshouse.companyprofile.delta.processor.CompanyProfileDeltaProcessor;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;

@Component
public class CompanyProfileDeltaConsumer {

    private final Logger logger;
    private final CompanyProfileDeltaProcessor deltaProcessor;

    @Autowired
    public CompanyProfileDeltaConsumer(CompanyProfileDeltaProcessor deltaProcessor, Logger logger) {
        this.deltaProcessor = deltaProcessor;
        this.logger = logger;
    }

    /**
     * Receives Main topic messages.
     */
    @RetryableTopic(attempts = "${company-profile.delta.retry-attempts}",
            backoff = @Backoff(delayExpression = "${company-profile.delta.backoff-delay}"),
            fixedDelayTopicStrategy = FixedDelayStrategy.SINGLE_TOPIC,
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-error",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            autoCreateTopics = "false",
            exclude = NonRetryableErrorException.class)
    @KafkaListener(topics = "${company-profile.delta.topic}",
            groupId = "${company-profile.delta.group-id}",
            containerFactory = "listenerContainerFactory")
    public void receiveMainMessages(Message<ChsDelta> message,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition,
                                    @Header(KafkaHeaders.OFFSET) String offset) {
        ChsDelta chsDelta = message.getPayload();
        logger.infoContext(chsDelta.getContextId(),
                "Started processing a company profile delta", DataMapHolder.getLogMap());
        if (chsDelta.getIsDelete()) {
            deltaProcessor.processDeleteDelta(message);
        } else {
            deltaProcessor.processDelta(message);
        }
    }
}
