package uk.gov.companieshouse.companyprofile.delta.mapper;

import uk.gov.companieshouse.api.delta.CorporateAnnotation;

import java.util.HashMap;

import static uk.gov.companieshouse.api.company.CorporateAnnotation.TypeEnum.OTHER;

public class MapperUtils {

    /** Create a hashmap for IsCommunityInterestCompany. */

    public static HashMap<String,Boolean> getIsCommunityInterestCompanyMap() {
        HashMap<String,Boolean> isCommunityInterestCompanyMap = new HashMap<>();
        isCommunityInterestCompanyMap.put("0",false);
        isCommunityInterestCompanyMap.put("1",true);
        return isCommunityInterestCompanyMap;
    }

    /**Create a hashmap for subtype.*/
    public static HashMap<String,String> getSubTypeMap() {
        HashMap<String,String> subTypeMap = new HashMap<>();
        subTypeMap.put("1","community-interest-company");
        subTypeMap.put("2","private-fund-limited-partnership");
        return subTypeMap;
    }

    /**Create a hashmap for  proof_status.*/
    public static HashMap<String,String> getProof_statusMap() {
        HashMap<String,String> proofStatusMap = new HashMap<>();

        proofStatusMap.put("1","paper");
        proofStatusMap.put("2","electronic");
        proofStatusMap.put("3","statutory");
        proofStatusMap.put("4","in-dispute");
        proofStatusMap.put("5","pending");

        return proofStatusMap;
    }

    /**Create a hashmap for jurisdictions .*/
    public static HashMap<String,String> getJurisdictionMap() {

        HashMap<String,String> jurisdictionMap = new HashMap<>();
        jurisdictionMap.put("1","england-wales");
        jurisdictionMap.put("2","wales");
        jurisdictionMap.put("3","scotland");
        jurisdictionMap.put("4","northern-ireland");
        jurisdictionMap.put("5","european-union");
        jurisdictionMap.put("6","united-kingdom");
        jurisdictionMap.put("7","england");
        jurisdictionMap.put("8","noneu");
        return jurisdictionMap;
    }

    /**Create a hashmap for status_detail.*/
    public static HashMap<String, String> getStatus_detailMap() {
        HashMap<String,String> statusDetailMap = new HashMap<>();

        statusDetailMap.put("5","transferred-from-uk");
        statusDetailMap.put("Q","active-proposal-to-strike-off");
        statusDetailMap.put("R","petition-to-restore-dissolved");
        statusDetailMap.put("X","transformed-to-se");
        statusDetailMap.put("Z","converted-to-plc");
        statusDetailMap.put("AA","converted-to-uk-societas");
        statusDetailMap.put("AB","converted-to-ukeig");

        return statusDetailMap;

    }

    /**Create a hashmap for status.*/
    public static HashMap<String,String> getStatusMap() {
        HashMap<String,String> statusMap = new HashMap<>();
        statusMap.put("0","active");
        statusMap.put("1","dissolved");
        statusMap.put("2","liquidation");
        statusMap.put("3","receivership");
        statusMap.put("4","converted-closed");
        statusMap.put("5","active");
        statusMap.put("7","converted-closed");
        statusMap.put("8","open");
        statusMap.put("9","closed");
        statusMap.put("A","receivership");
        statusMap.put("C","insolvency-proceedings");
        statusMap.put("E","insolvency-proceedings");
        statusMap.put("F","receivership");
        statusMap.put("G","receivership");
        statusMap.put("H","insolvency-proceedings");
        statusMap.put("I","voluntary-arrangement");
        statusMap.put("J","insolvency-proceedings");
        statusMap.put("K","insolvency-proceedings");
        statusMap.put("L","insolvency-proceedings");
        statusMap.put("M","administration");
        statusMap.put("N","insolvency-proceedings");
        statusMap.put("O","insolvency-proceedings");
        statusMap.put("P","insolvency-proceedings");
        statusMap.put("Q","active");
        statusMap.put("R","dissolved");
        statusMap.put("S","insolvency-proceedings");
        statusMap.put("T","administration");
        statusMap.put("U","insolvency-proceedings");
        statusMap.put("V","insolvency-proceedings");
        statusMap.put("W","insolvency-proceedings");
        statusMap.put("X","converted-closed");
        statusMap.put("Z","converted-closed");
        statusMap.put("AA","active");
        statusMap.put("AB","active");
        statusMap.put("AC","registered");
        statusMap.put("AD","removed");

        return statusMap;

    }

    /**Create a hashmap for account_type.*/
    public static HashMap<String,String> getAccountTypeMap() {
        HashMap<String,String> accountTypeMap = new HashMap<>();

        accountTypeMap.put("0", null);
        accountTypeMap.put("1","full");
        accountTypeMap.put("2","small");
        accountTypeMap.put("3","medium");
        accountTypeMap.put("4","group");
        accountTypeMap.put("5","dormant");
        accountTypeMap.put("6","interim");
        accountTypeMap.put("7","initial");
        accountTypeMap.put("8","no-accounts-type-available");
        accountTypeMap.put("9","total-exemption-full");
        accountTypeMap.put("A","total-exemption-small");
        accountTypeMap.put("B","partial-exemption");
        accountTypeMap.put("C","audit-exemption-subsidiary");
        accountTypeMap.put("D","filing-exemption-subsidiary");
        accountTypeMap.put("E","micro-entity");
        accountTypeMap.put("F","audited-abridged");
        accountTypeMap.put("G","unaudited-abridged");

        return accountTypeMap;
    }

    /**Create a hashmap for Foreign company account type.*/
    public static HashMap<String,String> getForeignAccountTypeMap() {
        HashMap<String,String> foreignAccountTypeMap = new HashMap<>();

        foreignAccountTypeMap.put("1", "accounting-requirements-of-originating-country-apply");
        foreignAccountTypeMap.put("2", "accounting-requirements-of-originating-country-apply");
        foreignAccountTypeMap.put("3", "accounting-requirements-of-originating-country-do-not-apply");
        foreignAccountTypeMap.put("4", "accounting-requirements-of-originating-country-do-not-apply");

        return foreignAccountTypeMap;
    }

    /**Create a hashmap for Foreign company terms of account publication.*/
    public static HashMap<String,String> getTermsOfAccountPublicationMap() {
        HashMap<String,String> termsOfAccountPublication = new HashMap<>();

        termsOfAccountPublication.put("1", "accounts-publication-date-supplied-by-company");
        termsOfAccountPublication.put("2", "accounting-publication-date-does-not-need-to-be-supplied-by-company");
        termsOfAccountPublication.put("3", "accounting-reference-date-allocated-by-companies-house");
        termsOfAccountPublication.put("4", "accounting-reference-date-allocated-by-companies-house");

        return termsOfAccountPublication;
    }

    /**
     * Create a hashmap for corporate_annotation_type.
     */
    public static HashMap<String, String> getCorpAnnotationTypeMap() {
        HashMap<String, String> corpAnnotationTypeMap = new HashMap<>();
        corpAnnotationTypeMap.put("100", String.valueOf(OTHER));
        return corpAnnotationTypeMap;
    }

    /**Create a hashmap for type.*/
    public static HashMap<String,String> getTypeMap() {
        HashMap<String,String> typeMap = new HashMap<>();
        typeMap.put("1","private-unlimited");
        typeMap.put("2","ltd");
        typeMap.put("3","plc");
        typeMap.put("4","old-public-company");
        typeMap.put("5","private-limited-guarant-nsc-limited-exemption");
        typeMap.put("6","limited-partnership");
        typeMap.put("7","private-limited-guarant-nsc");
        typeMap.put("8","converted-or-closed");
        typeMap.put("9","private-unlimited-nsc");
        typeMap.put("10","private-limited-shares-section-30-exemption");
        typeMap.put("11","assurance-company");
        typeMap.put("12","oversea-company");
        typeMap.put("13","eeig-establishment");
        typeMap.put("14","icvc-securities");
        typeMap.put("15","icvc-warrant");
        typeMap.put("16","icvc-umbrella");
        typeMap.put("17","industrial-and-provident-society");
        typeMap.put("18","northern-ireland");
        typeMap.put("19","northern-ireland-other");
        typeMap.put("20","llp");
        typeMap.put("21","royal-charter");
        typeMap.put("22","investment-company-with-variable-capital");
        typeMap.put("23","unregistered-company");
        typeMap.put("24","llp");
        typeMap.put("25","other");
        typeMap.put("26","united-kingdom-societas");
        typeMap.put("27","registered-society-non-jurisdictional");
        typeMap.put("28","uk-establishment");
        typeMap.put("29","protected-cell-company");
        typeMap.put("30","scottish-partnership");
        typeMap.put("31","charitable-incorporated-organisation");
        typeMap.put("32","scottish-charitable-incorporated-organisation");
        typeMap.put("33","further-education-or-sixth-form-college-corporation");
        typeMap.put("34","european-public-limited-liability-company-se");
        typeMap.put("35","eeig");
        typeMap.put("36","ukeig");
        typeMap.put("37","registered-overseas-entity");
        return typeMap;
    }
}
