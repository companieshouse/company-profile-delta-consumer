package uk.gov.companieshouse.companyprofile.delta.utils;

import static org.springframework.kafka.support.KafkaHeaders.EXCEPTION_CAUSE_FQCN;

import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.FileCopyUtils;
import uk.gov.companieshouse.delta.ChsDelta;

public class TestHelper {

    public Message<ChsDelta> createChsDeltaMessage(boolean isDelete) throws IOException {
        String resource;
        if (isDelete) {
            resource = "company-profile-delete-delta-example.json";
        } else {
            resource = "company-profile-delta-example.json";
        }
        InputStreamReader exampleJsonPayload = new InputStreamReader(
                ClassLoader.getSystemClassLoader().getResourceAsStream(resource));
        String data = FileCopyUtils.copyToString(exampleJsonPayload);

        return buildMessage(data, isDelete);
    }

    public Message<ChsDelta> createInvalidChsDeltaMessage() {
        return buildMessage("This is some invalid data", false);
    }

    private ChsDelta buildDelta(String data, boolean isDelete) {
        return ChsDelta.newBuilder()
                .setData(data)
                .setContextId("MlhhiLMiRZlm2swKYh3IXL9Euqx0")
                .setAttempt(0)
                .setIsDelete(isDelete)
                .build();
    }
    private Message<ChsDelta> buildMessage (String data, boolean isDelete) {
        return MessageBuilder
                .withPayload(buildDelta(data, isDelete))
                .setHeader(KafkaHeaders.RECEIVED_TOPIC, "test")
                .setHeader("COMPANY_PROFILE_DELTA_RETRY_COUNT", 1)
                .build();
    }

    public ProducerRecord<String,Object> buildRecord(String topic, String header) {
        Object obj = new Object();
        RecordHeaders headers = new RecordHeaders();
        headers.add(new RecordHeader(EXCEPTION_CAUSE_FQCN, header.getBytes()));
        ProducerRecord<String,Object> record = new ProducerRecord<>(topic,1,1L, null,obj,headers);
        return record;
    }
}
