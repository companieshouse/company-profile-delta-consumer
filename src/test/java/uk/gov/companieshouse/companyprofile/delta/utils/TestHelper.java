package uk.gov.companieshouse.companyprofile.delta.utils;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.FileCopyUtils;
import uk.gov.companieshouse.delta.ChsDelta;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


public class TestHelper {

    public Message<ChsDelta> createChsDeltaMessage(boolean isDelete) throws IOException {
        String resource;
        if (isDelete) {
            resource = "company-profile-delete-delta-example.json";
        } else {
            resource = "company-profile-delta-example.json";
        }
        InputStreamReader exampleJsonPayload = new InputStreamReader(
                Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(resource)));
        String data = FileCopyUtils.copyToString(exampleJsonPayload);

        return buildMessage(data, isDelete);
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

}
