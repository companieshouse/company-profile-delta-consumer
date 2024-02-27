package uk.gov.companieshouse.companyprofile.delta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanyProfileDeltaConsumerApplication {

    public static final String NAMESPACE = "company-profile-delta-consumer";

    public static void main(String[] args) {
        SpringApplication.run(CompanyProfileDeltaConsumerApplication.class, args);
    }
}
