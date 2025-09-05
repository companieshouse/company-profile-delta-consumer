package uk.gov.companieshouse.companyprofile.delta.mapper;

public enum Subtype {
    CIC("1","community-interest-company"),
    LEGACY_PRIVATE_FUND_LIMITED_PARTNERSHIP("2","private-fund-limited-partnership"),

    // New Limited Partnership subtypes:
    LP("3", "lp"),
    PFLP("4", "pflp"),
    SLP("5", "slp"),
    SPFLP("6", "spflp");

    private String id;
    private String mappedValue;

    Subtype(String id, String mappedValue) {
        this.id = id;
        this.mappedValue = mappedValue;
    }

    public static String getMappedValue(String id) {
        for (Subtype subtype : Subtype.values()) {
            if (subtype.id.equals(id)) {
                return subtype.mappedValue;
            }
        }

        // Null should be returned if subtype index does not map to an enum value
        return null;
    }
}
