package uk.gov.companieshouse.companyprofile.delta.steps;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.requestMadeFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.companieshouse.companyprofile.delta.CompanyProfileDeltaConsumerApplication.NAMESPACE;
import static uk.gov.companieshouse.companyprofile.delta.data.TestData.getOutputData;

import com.github.tomakehurst.wiremock.WireMockServer;
import consumer.matcher.RequestMatcher;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import uk.gov.companieshouse.companyprofile.delta.data.TestData;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

public class CompanyProfileSteps {

    private static WireMockServer wireMockServer;
    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private String output;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaConsumer<String, Object> kafkaConsumer;

    @Value("${company-profile.delta.topic}")
    private String topic;

    @Value("${wiremock.server.port}")
    private String port;
    
    public void sendMsgToKafkaTopic(String data) {
        kafkaTemplate.send(topic, data);
    }

    private void configureWireMock(){
        wireMockServer = new WireMockServer(Integer.parseInt(port));
        wireMockServer.start();
        configureFor("localhost", Integer.parseInt(port));
    }

    @Given("the application is running")
    public void theApplicationRunning() {
        assertThat(kafkaTemplate).isNotNull();
    }

    @When("the consumer receives a message")
    public void the_consumer_receives_a_message()  throws Exception {
        configureWireMock();
        stubPutStatement(200);
        ChsDelta delta = new ChsDelta(TestData.getCompanyDelta(), 1, "123456789", false);
        kafkaTemplate.send(topic, delta);
        countDown();
    }

    @When("the consumer receives a delete payload")
    public void theConsumerReceivesDelete() throws Exception {
        configureWireMock();
        stubDeleteStatement(200);
        ChsDelta delta = new ChsDelta(TestData.getDeleteData(), 1, "1", true);
        kafkaTemplate.send(topic, delta);
        countDown();
    }

    @When("the consumer receives an invalid delete payload")
    public void theConsumerReceivesInvalidDelete() throws Exception {
        configureWireMock();
        ChsDelta delta = new ChsDelta("invalid", 1, "1", true);
        kafkaTemplate.send(topic, delta);

        countDown();
    }

    @When("^the consumer receives a delete message but the api returns a (\\d*)$")
    public void theConsumerReceivesDeleteMessageButDataApiReturns(int responseCode) throws Exception{
        configureWireMock();
        stubDeleteStatement(responseCode);
        ChsDelta delta = new ChsDelta(TestData.getDeleteData(), 1, "1", true);
        kafkaTemplate.send(topic, delta);

        countDown();
    }

    @When("an invalid avro message is sent")
    public void invalidAvroMessageIsSent() throws Exception {
        kafkaTemplate.send(topic, "InvalidData");

        countDown();
    }

    @When("a message with invalid data is sent")
    public void messageWithInvalidDataIsSent() throws Exception {
        ChsDelta delta = new ChsDelta("InvalidData", 1, "1", false);
        kafkaTemplate.send(topic, delta);

        countDown();
    }

    @When("the consumer receives a message but the api returns a (\\d*)$")
    public void theConsumerReceivesMessageButDataApiReturns(int responseCode) throws Exception{
        configureWireMock();
        stubPutStatement(responseCode);
        ChsDelta delta = new ChsDelta(TestData.getCompanyDelta(), 1, "1", false);
        kafkaTemplate.send(topic, delta);

        countDown();
    }

    @Then("^the message should be moved to topic (.*)$")
    public void theMessageShouldBeMovedToTopic(String topic) {
        ConsumerRecord<String, Object> singleRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, topic);

        assertThat(singleRecord.value()).isNotNull();
    }

    @Then("^the message should retry (\\d*) times and then error$")
    public void theMessageShouldRetryAndError(int retries) throws InterruptedException {
        Thread.sleep(500);
        ConsumerRecords<String, Object> records = KafkaTestUtils.getRecords(kafkaConsumer);
        Iterable<ConsumerRecord<String, Object>> retryRecords =  records.records("company-profile-delta-retry");
        Iterable<ConsumerRecord<String, Object>> errorRecords =  records.records("company-profile-delta-error");

        int actualRetries = (int) StreamSupport.stream(retryRecords.spliterator(), false).count();
        int errors = (int) StreamSupport.stream(errorRecords.spliterator(), false).count();

        assertThat(actualRetries).isEqualTo(retries);
        assertThat(errors).isEqualTo(1);
    }

    @Then("a DELETE request is sent to the company profile api")
    public void deleteRequestIsSent() {
        verify(1, deleteRequestedFor(urlMatching(
                "/company/00358948/internal")));
    }

    @Then("a PUT request is sent to the company profile api with the transformed data")
    public void aPutRequestIsSent() {
        output = getOutputData();
        verify(1, requestMadeFor(new RequestMatcher(LOGGER,
                output,
                "/company/00358948/internal",
                List.of("data.etag", "deltaAt"))));
    }

    @After
    public void shutdownWiremock(){
        if (wireMockServer != null)
            wireMockServer.stop();
    }

    private void countDown() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(5, TimeUnit.SECONDS);
    }

    private void stubPutStatement(int responseCode) {
        stubFor(put(urlEqualTo(
                "/company/00358948/internal"))
                .willReturn(aResponse().withStatus(responseCode)));
    }

    private void stubDeleteStatement(int responseCode) {
        stubFor(delete(urlEqualTo(
                "/company/00358948/internal"))
                .willReturn(aResponse().withStatus(responseCode)));
    }

}
