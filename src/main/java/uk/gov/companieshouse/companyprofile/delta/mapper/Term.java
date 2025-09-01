package uk.gov.companieshouse.companyprofile.delta.mapper;

public enum Term {
    BY_AGREEMENT("1", "by-agreement"),
    UNTIL_DISSOLUTION("2", "until-dissolution"),
    NONE("3", "none");

    private String id;
    private String mappedValue;

    Term(String id, String mappedValue) {
        this.id = id;
        this.mappedValue = mappedValue;
    }

    public static String getMappedValue(String id) {
        for (Term term : Term.values()) {
            if (term.id.equals(id)) {
                return term.mappedValue;
            }
        }

        // Null should be returned if term index does not map to an enum value
        return null;
    }
}
