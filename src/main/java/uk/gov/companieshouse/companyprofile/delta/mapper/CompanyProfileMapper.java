package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.delta.CompanyDelta;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {

    MapperUtils mapperUtils = new MapperUtils();

    @Mapping(target = "data.accounts.lastAccounts.madeUpTo", source = "accountingDates.lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.periodEndOn", source = "accountingDates.lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.periodStartOn", source = "accountingDates.lastPeriodStartOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.type", source = "accountType")
    @Mapping(target = "data.accounts.nextAccounts.dueOn", source = "accountingDates.nextDue")
    @Mapping(target = "data.accounts.nextAccounts.periodEndOn", source = "accountingDates.nextPeriodEndOn")
    @Mapping(target = "data.accounts.nextAccounts.periodStartOn", source = "accountingDates.nextPeriodStartOn", dateFormat = "yyyyMMdd")
    //Fields to be added with @AfterMapping: accountOverdue
    public abstract CompanyProfile companyDeltaToCOmpanyProfile(CompanyDelta companyDelta);


    CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);
    
}
