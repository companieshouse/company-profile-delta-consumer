package uk.gov.companieshouse.companyprofile.delta;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import uk.gov.companieshouse.companyprofile.delta.config.AbstractIntegrationTest;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "uk.gov.companieshouse.companyprofile.delta")
@CucumberContextConfiguration
public class CucumberFeaturesRunnerITest extends AbstractIntegrationTest {

}
