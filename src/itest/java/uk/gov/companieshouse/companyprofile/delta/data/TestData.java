package uk.gov.companieshouse.companyprofile.delta.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.util.FileCopyUtils;

public class TestData {
    public static String getCompanyDelta() {
        String path = "src/itest/resources/json/input/company_profile_delta.json";
        return readFile(path);
    }

    public static String getDeleteData() {
        String path = "src/itest/resources/json/input/company_profile_delete.json";
        return readFile(path);
    }

    public static String getOutputData() {
        String path = "src/itest/resources/json/output/company-profile-expected-output.json";
        return readFile(path);
    }

    private static String readFile(String path) {
        String data;
        try {
            data = FileCopyUtils.copyToString(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (IOException e) {
            data = null;
        }
        return data;
    }
}
