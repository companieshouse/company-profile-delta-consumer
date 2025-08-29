package uk.gov.companieshouse.companyprofile.delta.mapper;

public enum Term {
    // Order of values here must not be changed, so as to preserve the ordinal mapping
    BY_AGREEMENT("by-agreement"),
    UNTIL_DISSOLUTION("until-dissolution"),
    NONE("none");

    private String mappedValue;

    Term(String mappedValue) {
        this.mappedValue = mappedValue;
    }

    public static String getMappedValue(String termIndex) {
        try {
            return Term.values()[Integer.valueOf(termIndex) - 1].mappedValue;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Null should be returned if term index does not map to an enum value
            return null;
        }
    }
}
