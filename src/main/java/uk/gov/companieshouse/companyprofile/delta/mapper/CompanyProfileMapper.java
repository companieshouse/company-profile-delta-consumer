package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.MappingTarget;
import uk.gov.companieshouse.api.company.AccountingReferenceDate;
import uk.gov.companieshouse.api.company.Accounts;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.company.Links;
import uk.gov.companieshouse.api.company.PreviousCompanyNames;
import uk.gov.companieshouse.api.delta.BooleanFlag;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.delta.ForeignCompanyRequiredToPublish;
import uk.gov.companieshouse.api.delta.ForeignCompany;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileMapper {

    @Mapping(target = "data.accounts.accountingReferenceDate.day", source = "accRefDate")
    @Mapping(target = "data.accounts.accountingReferenceDate.month", source = "accRefDate")

    @Mapping(target = "data.accounts.lastAccounts.madeUpTo",
            source = "accountingDates.lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.periodEndOn",
            source = "accountingDates.lastPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.periodStartOn",
            source = "accountingDates.lastPeriodStartOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.lastAccounts.type", source = "accountType")

    @Mapping(target = "data.accounts.nextAccounts.dueOn",
            source = "accountingDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextAccounts.periodEndOn",
            source = "accountingDates.nextPeriodEndOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextAccounts.periodStartOn",
            source = "accountingDates.nextPeriodStartOn", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextDue", source = "accountingDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.accounts.nextMadeUpTo",
            source = "accountingDates.nextPeriodEndOn", dateFormat = "yyyyMMdd")

    @Mapping(target = "data.annualReturn.lastMadeUpTo",
            source = "annualReturnDates.latestMadeUpTo", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.annualReturn.nextDue",
            source = "annualReturnDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.annualReturn.nextMadeUpTo",
            source = "annualReturnDates.nextMadeUpTo", dateFormat = "yyyyMMdd")

    //branch company details to be added with DSND-1855

    @Mapping(target = "data.companyName", source = "companyName")
    @Mapping(target = "data.companyNumber", source = "companyNumber")
    @Mapping(target = "data.companyStatus", source = "status")
    @Mapping(target = "data.companyStatusDetail", source = "status")

    @Mapping(target = "data.confirmationStatement.lastMadeUpTo",
            source = "confirmationStatementDates.latestMadeUpTo", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.confirmationStatement.nextDue",
            source = "confirmationStatementDates.nextDue", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.confirmationStatement.nextMadeUpTo",
            source = "confirmationStatementDates.nextMadeUpTo", dateFormat = "yyyyMMdd")

    @Mapping(target = "data.dateOfCessation", source = "dateOfDissolution", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.dateOfCreation", source = "creationDate", dateFormat = "yyyyMMdd")
    @Mapping(target = "data.dateOfDissolution", source = "dateOfDissolution", dateFormat = "yyyyMMdd")

    @Mapping(target = "deltaAt", source = "deltaAt")
    @Mapping(target = "data.externalRegistrationNumber", source = "externalRegistrationNumber")

    @Mapping(target = "data.foreignCompanyDetails.accountingRequirement.foreignAccountType",
            source = "foreignCompany.accReqType")

    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodFrom.day",
            source = "foreignCompany.requiredToPublish.dayFrom")
    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodFrom.month",
            source = "foreignCompany.requiredToPublish.monthFrom")

    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodTo.day",
            source = "foreignCompany.requiredToPublish.dayTo")
    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodTo.month",
            source = "foreignCompany.requiredToPublish.monthTo")

    @Mapping(target = "data.foreignCompanyDetails.businessActivity",
            source = "foreignCompany.businessActivity")
    @Mapping(target = "data.foreignCompanyDetails.companyType",
            source = "foreignCompany.legalForm")
    @Mapping(target = "data.foreignCompanyDetails.governedBy",
            source = "foreignCompany.governingLaw")
    @Mapping(target = "data.foreignCompanyDetails.isACreditFinancialInstitution",
            source = "foreignCompany.creditOrFinancial")
    @Mapping(target = "data.foreignCompanyDetails.legalForm", source = "foreignCompany.legalForm")

    @Mapping(target = "data.foreignCompanyDetails.originatingRegistry.country",
            source = "foreignCompany.countryOfOrigin")
    @Mapping(target = "data.foreignCompanyDetails.originatingRegistry.name", source = "foreignCompany.parentRegistry")
    @Mapping(target = "data.foreignCompanyDetails.registrationNumber", source = "foreignCompany.registrationNumber")

    @Mapping(target = "data.jurisdiction", source = "jurisdiction")
    @Mapping(target = "data.lastFullMembersListDate", source = "fullMembersListDate", dateFormat = "yyyyMMdd")

    @Mapping(target = "data.proofStatus", source = "proofStatus")
    
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

    @Mapping(target = "data.serviceAddress", source = "serviceAddress")
    @Mapping(target = "data.serviceAddress.addressLine1", source = "serviceAddress.addressLine1")
    @Mapping(target = "data.serviceAddress.addressLine2", source = "serviceAddress.addressLine2")
    @Mapping(target = "data.serviceAddress.careOf", source = "serviceAddress.careOfName")
    @Mapping(target = "data.serviceAddress.country", source = "serviceAddress.country")
    @Mapping(target = "data.serviceAddress.locality", source = "serviceAddress.locality")
    @Mapping(target = "data.serviceAddress.poBox",source = "serviceAddress.poBox")
    @Mapping(target = "data.serviceAddress.postalCode", source = "serviceAddress.postalCode")
    @Mapping(target = "data.serviceAddress.region", source = "serviceAddress.region")

    @Mapping(target = "data.subtype", source = "subtype")
    @Mapping(target = "data.type", source = "type")
    @Mapping(target = "data.undeliverableRegisteredOfficeAddress", source = "undeliverableRegisteredOfficeAddress")
    @Mapping(target = "hasMortgages", source = "hasMortgages")

    
    public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);

    /**maps AccRefDate. */
    @AfterMapping
    public void mappAccRefDate(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        Accounts accounts = data.getAccounts();
        AccountingReferenceDate referenceDate = new AccountingReferenceDate();
        String sourceDate = source.getAccRefDate();
        referenceDate.setDay(sourceDate.substring(0, Math.min(sourceDate.length(), 2)));
        try {
            referenceDate.setMonth(sourceDate.substring(2,4));
        } catch (IndexOutOfBoundsException ex) {
            referenceDate.setMonth("");
        }
        accounts.setAccountingReferenceDate(referenceDate);
        data.setAccounts(accounts);
        target.setData(data);
    }

    /**maps Boolean flag fields into Boolean fields in target. */
    @AfterMapping
    public void mapBooleans(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        data.setHasInsolvencyHistory(flagToBoolean(source.getHasInsolvencyHistory()));
        data.hasSuperSecurePscs(flagToBoolean(source.getSuperSecurePscInd()));
        data.isCommunityInterestCompany(flagToBoolean(source.getCicInd()));
        data.registeredOfficeIsInDispute(flagToBoolean(source.getRegisteredOfficeIsInDispute()));
        data.undeliverableRegisteredOfficeAddress(flagToBoolean(source.getUndeliverableRegisteredOfficeAddress()));
        target.setHasMortgages(flagToBoolean(source.getHasMortgages()));
        target.setData(data);

    }

    /**maps SicCodes. */
    @AfterMapping
    public void mapSicCodes(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        List<String> sicCodes = new ArrayList<>();
        source.getSicCodes().forEach(s -> {
            String[] codes = new String[]{s.getSic1(), s.getSic2(), s.getSic3(), s.getSic4()};
            for (String code : codes) {
                if (code != null && !code.isEmpty()) {
                    sicCodes.add(code);
                }
            }
        });
        data.setSicCodes(sicCodes);
        target.setData(data);
    }

    /**maps BooleanFlag to Boolean. */
    public Boolean flagToBoolean(BooleanFlag flag) {
        return flag == null ? null : flag.getValue().equals("1");
    }

    /**maps previousCompanyNames. */
    @AfterMapping
    public void mapPreviousCompanyNames(@MappingTarget CompanyProfile target, CompanyDelta source) {
        List<PreviousCompanyNames> targetNames = source.getPreviousCompanyNames()
                .stream()
                .map(previousName -> {
                    PreviousCompanyNames targetName = new PreviousCompanyNames();
                    targetName.setName(previousName.getName());
                    targetName.setCeasedOn(LocalDate.parse(previousName.getCeasedOn(),
                            DateTimeFormatter.ofPattern("yyyyMMdd")));
                    targetName.setEffectiveFrom(LocalDate.parse(previousName.getEffectiveFrom(),
                            DateTimeFormatter.ofPattern("yyyyMMdd")));
                    return targetName;
                }).collect(Collectors.toList());
        Data data = target.getData();
        data.setPreviousCompanyNames(targetNames);
        target.setData(data);
    }

    /** add self link for Company Profile. */
    @AfterMapping
    public void setSelfLink(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        data.setCompanyNumber(source.getCompanyNumber());
        Links links = new Links();
        links.setSelf(String.format("/company/%s",data.getCompanyNumber()));
        data.setLinks(links);
        target.setData(data);
    }
    
}
