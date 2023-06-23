package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.MappingTarget;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.company.PreviousCompanyNames;
import uk.gov.companieshouse.api.delta.CompanyDelta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileMapper {

    MapperUtils mapperUtils = new MapperUtils();

    @Mapping(target = "data.accounts.lastAccounts.madeUpTo", source = "accountingDates.lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.periodEndOn", source = "accountingDates.lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.periodStartOn", source = "accountingDates.lastPeriodStartOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.type", source = "accountType")

    @Mapping(target = "data.accounts.nextAccounts.dueOn", source = "accountingDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextAccounts.periodEndOn", source = "accountingDates.nextPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextAccounts.periodStartOn", source = "accountingDates.nextPeriodStartOn", dateFormat = "yyyyMMdd")

    @Mapping(target = "data.proofStatus", source = "proofStatus")
    //Fields to be added with @AfterMapping: accountOverdue, accountRefDate
    public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);


    @AfterMapping
    public void mapPreviousCompanyNames(@MappingTarget CompanyProfile target, CompanyDelta source) {
        CompanyDelta delta = source;
        List<PreviousCompanyNames> targetNames= source.getPreviousCompanyNames()
                .stream()
                .map(previousName -> {
                    PreviousCompanyNames targetName = new PreviousCompanyNames();
                    targetName.setName(previousName.getName());
                    targetName.setCeasedOn(LocalDate.parse(previousName.getCeasedOn(), DateTimeFormatter.ofPattern( "yyyyMMdd" )));
                    targetName.setEffectiveFrom(LocalDate.parse(previousName.getEffectiveFrom(), DateTimeFormatter.ofPattern( "yyyyMMdd" )));
                    return targetName;
                }).collect(Collectors.toList());
        Data data = target.getData();
        data.setPreviousCompanyNames(targetNames);
        target.setData(data);
    }
    
}
