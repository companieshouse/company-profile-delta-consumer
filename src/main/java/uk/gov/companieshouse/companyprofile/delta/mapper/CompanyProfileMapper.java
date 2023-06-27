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
    @Mapping(target = "data.accounts.nextDue", source = "accountingDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextMadeUpTo", source = "accountingDates.nextPeriodEndOn", dateFormat = "yyyyMMdd")

    @Mapping(target = "data.annualReturn.lastMadeUpTo", source = "annualReturnDates.latestMadeUpTo", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.annualReturn.nextDue", source = "annualReturnDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.annualReturn.nextMadeUpTo", source = "annualReturnDates.nextMadeUpTo", dateFormat = "yyyyMMdd")

    //branch company details to be added with DSND-1855

    @Mapping(target = "data.companyName", source = "companyName")
    @Mapping(target = "data.companyNumber", source = "companyNumber")
    @Mapping(target = "data.companyStatus", source = "status")
    @Mapping(target = "data.companyStatusDetail", source = "status")

    @Mapping(target = "data.confirmationStatement.lastMadeUpTo", source = "confirmationStatementDates.latestMadeUpTo", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.confirmationStatement.nextDue", source = "confirmationStatementDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.confirmationStatement.nextMadeUpTo", source = "confirmationStatementDates.nextMadeUpTo", dateFormat = "yyyyMMdd")

    @Mapping(target = "data.dateOfCessation", source = "dateOfDissolution", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.dateOfCreation", source = "creationDate", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.dateOfDissolution", source = "dateOfDissolution", dateFormat = "yyyyMMdd")

    @Mapping(target = "deltaAt", source = "deltaAt")
    @Mapping(target = "data.externalRegistrationNumber", source = "externalRegistrationNumber")

    @Mapping(target = "data.foreignCompanyDetails.accountingRequirement.foreignAccountType", source = "foreignCompany.accReqType")
    //terms of account publication
    //required to publish

    @Mapping(target = "data.foreignCompanyDetails.businessActivity", source = "foreignCompany.businessActivity")
    @Mapping(target = "data.foreignCompanyDetails.companyType", source = "foreignCompany.legalForm")
    @Mapping(target = "data.foreignCompanyDetails.governedBy", source = "foreignCompany.governingLaw")
    @Mapping(target = "data.foreignCompanyDetails.isACreditFinancialInstitution", source = "foreignCompany.creditOrFinancial")
    @Mapping(target = "data.foreignCompanyDetails.legalForm", source = "foreignCompany.legalForm")

    @Mapping(target = "data.foreignCompanyDetails.originatingRegistry.country", source = "foreignCompany.countryOfOrigin")
    @Mapping(target = "data.foreignCompanyDetails.originatingRegistry.name", source = "foreignCompany.parentRegistry")
    @Mapping(target = "data.foreignCompanyDetails.registrationNumber", source = "foreignCompany.registrationNumber")
    
    @Mapping(target = "data.hasInsolvencyHistory", source = "hasInsolvencyHistory")
    @Mapping(target = "data.hasSuperSecurePscs", source = "superSecurePscInd")
    @Mapping(target = "data.isCommunityInterestCompany", source = "cicInd")
    @Mapping(target = "data.jurisdiction", source = "jurisdiction")
    @Mapping(target = "data.lastFullMembersListDate", source = "fullMembersListDate")

    //links

    //officer summary

    @Mapping(target = "data.previousCompanyNames", source = "previousCompanyNames")
    @Mapping(target = "data.previousCompanyNames.ceasedOn", source = "previousCompanyNames.ceasedOn")
    @Mapping(target = "data.previousCompanyNames.effectiveFrom", source = "previousCompanyNames.effectiveFrom")
    @Mapping(target = "data.previousCompanyNames.name", source = "previousCompanyNames.name")

    @Mapping(target = "data.proofStatus", source = "proofStatus")
    //Fields to be added with @AfterMapping: accountOverdue (accounts, annual return, confirmation statement), accountRefDate
    
    @Mapping(target = "data.registeredOfficeAddress", source = "registeredOfficeAddress")
    @Mapping(target = "data.registeredOfficeAddress.addressLine1", source = "registeredOfficeAddress.addressLine1")
    @Mapping(target = "data.registeredOfficeAddress.addressLine2", source = "registeredOfficeAddress.addressLine2")
    @Mapping(target = "data.registeredOfficeAddress.careOf", source = "registeredOfficeAddress.careOfName")
    @Mapping(target = "data.registeredOfficeAddress.country", source = "registeredOfficeAddress.country")
    @Mapping(target = "data.registeredOfficeAddress.locality", source = "registeredOfficeAddress.locality")
    @Mapping(target = "data.registeredOfficeAddress.poBox", source = "registeredOfficeAddress.poBox")
    @Mapping(target = "data.registeredOfficeAddress.postalCode", source = "registeredOfficeAddress.postalCode")
    @Mapping(target = "data.registeredOfficeAddress.region", source = "registeredOfficeAddress.region")
    @Mapping(target = "data.registeredOfficeIsInDispute", source = "registeredOfficeIsInDispute")

    //service address

    @Mapping(target = "data.sicCodes", source = "sicCodes")
    @Mapping(target = "data.subtype", source = "subtype")
    //super secure managing officer count
    @Mapping(target = "data.type", source = "type")
    @Mapping(target = "data.undeliverableRegisteredOfficeAddress", source = "undeliverableRegisteredOfficeAddress")
    //has mortgages
    //parent company number

    
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
