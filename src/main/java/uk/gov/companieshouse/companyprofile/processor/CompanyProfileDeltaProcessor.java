package uk.gov.companieshouse.companyprofile.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.delta.CompanyDeleteDelta;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.delta.ChsDelta;

@Component
public class CompanyProfileDeltaProcessor {

    public void processDelta(Message<ChsDelta> message) {
        ChsDelta chsDelta = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        CompanyDelta companyDelta;
        try {
            companyDelta = objectMapper.readValue(chsDelta.getData(), CompanyDelta.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void processDeleteDelta(Message<ChsDelta> message) {
        ChsDelta chsDelta = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        CompanyDeleteDelta companyDeleteDelta;
        try {
            companyDeleteDelta = objectMapper.readValue(chsDelta.getData(), CompanyDeleteDelta.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
