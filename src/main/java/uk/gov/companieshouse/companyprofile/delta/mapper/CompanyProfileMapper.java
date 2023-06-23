package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDelta;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {

    MapperUtils mapperUtils = new MapperUtils();

    @Mapping(target = "madeUpto", source = "lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "LastAccounts.periodEndOn", source = "lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "LastAccounts.periodStartOn", source = "lastPeriodStartOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "type", source = "type")

    @Mapping(target = "NextAccounts.dueOn", source = "nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "NextAccounts.overdue", source = "accountOverdue")
    @Mapping(target = "NextAccounts.periodEndOn", source = "nextPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "NextAccounts.periodStartOn", source = "nextPeriodStartOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "nextDue", source = "nextDue")
    @Mapping(target = "nextMadeUpTo", source = "nextPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "Accounts.overdue", source = "accountOverdue")



    CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);
    
}
