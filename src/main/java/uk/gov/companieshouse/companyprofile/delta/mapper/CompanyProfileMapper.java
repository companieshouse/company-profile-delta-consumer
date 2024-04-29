package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.gov.companieshouse.api.company.AccountingReferenceDate;
import uk.gov.companieshouse.api.company.Accounts;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.ConfirmationStatement;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.company.LastAccounts;
import uk.gov.companieshouse.api.company.Links;
import uk.gov.companieshouse.api.company.PreviousCompanyNames;
import uk.gov.companieshouse.api.delta.BooleanFlag;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.delta.SicCodes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileMapper {

    @Mapping(target = "data.accounts.accountingReferenceDate.day", source = "accRefDate")
    @Mapping(target = "data.accounts.accountingReferenceDate.month", source = "accRefDate")

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

    @Mapping(target = "data.branchCompanyDetails.parentCompanyName", 
            source = "parentCompanyName")
    @Mapping(target = "data.branchCompanyDetails.parentCompanyNumber",
            source = "parentCompanyNumber")
    @Mapping(target = "data.branchCompanyDetails.businessActivity", 
            source = "businessActivity")

    @Mapping(target = "data.companyName", source = "companyName")
    @Mapping(target = "data.companyNumber", source = "companyNumber")
    @Mapping(target = "data.companyStatus", source = "status")
    @Mapping(target = "data.companyStatusDetail", source = "status")
    @Mapping(target = "data.corporateAnnotationType", source = "corporateAnnotationType")

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
    @Mapping(target = "data.superSecureManagingOfficerCount", source = "superSecureManagingOfficerCount")
    @Mapping(target = "data.undeliverableRegisteredOfficeAddress", source = "undeliverableRegisteredOfficeAddress")
    @Mapping(target = "hasMortgages", source = "hasMortgages")
    @Mapping(target = "parentCompanyNumber", source = "parentCompanyNumber")

    
    public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);

    /**maps AccRefDate. */
    @AfterMapping
    public void mappAccRefDate(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        Accounts accounts = Optional.ofNullable(data.getAccounts()).orElse(new Accounts());
        AccountingReferenceDate referenceDate = new AccountingReferenceDate();
        String sourceDate = source.getAccRefDate();
        if (sourceDate != null) {
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
        List<SicCodes> sourceCodes = source.getSicCodes();
        if (sourceCodes != null) {
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
    }

    /**maps BooleanFlag to Boolean. */
    public Boolean flagToBoolean(BooleanFlag flag) {
        return flag == null ? null : flag.getValue().equals("1");
    }

    /**maps previousCompanyNames. */
    @AfterMapping
    public void mapPreviousCompanyNames(@MappingTarget CompanyProfile target, CompanyDelta source) {
        if (source.getPreviousCompanyNames() != null) {
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
    }

    /**Map isComunnityInterestCompany to string. */
    @AfterMapping
    public void mapCicInd(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        BooleanFlag cicIndFlag = source.getCicInd();
        String cicIndType = null;
        if (cicIndFlag != null) {
            cicIndType = cicIndFlag.getValue();
        }
        HashMap<String, Boolean> cicIndMap = MapperUtils.getIsCommunityInterestCompanyMap();
        data.setIsCommunityInterestCompany(cicIndMap.getOrDefault(cicIndType, null));
        target.setData(data);
    }

    /**Maps enum account_type to string. */
    @AfterMapping
    public void mapEnumsAccountType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();

        Accounts accounts = data.getAccounts();
        LastAccounts lastAccounts = accounts.getLastAccounts();

        String accountType = source.getAccountType();
        HashMap<String,String> accountTypeMap = MapperUtils.getAccountTypeMap();

        lastAccounts.setType(accountTypeMap.getOrDefault(accountType,null));
        accounts.setLastAccounts(lastAccounts);
        data.setAccounts(accounts);

        target.setData(data);

    }

    /**Maps enum type to string. */
    @AfterMapping
    public void mapEnums(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        if (data == null) {
            data = new Data();
        }
        String subTypeFlag = source.getSubtype();
        HashMap<String,String> subTypeMap = MapperUtils.getSubTypeMap();
        data.setSubtype(subTypeMap.getOrDefault(subTypeFlag,null));
        target.setData(data);
    }

    /** Maps enum status to string. */
    @AfterMapping
    public void mapEnumsStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String statusType = source.getStatus();
        HashMap<String,String> statusMap = MapperUtils.getStatusMap();
        if (data == null) {
            data = new Data();
        }
        data.setCompanyStatus(statusMap.getOrDefault(statusType,null));
        target.setData(data);
    }

    /**Maps enum status_detail to string. */
    @AfterMapping
    public void mapEnumsStatusDetail(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String statusDetailType = source.getStatus();
        HashMap<String,String> statusDetailMap = MapperUtils.getStatus_detailMap();
        data.setCompanyStatusDetail(statusDetailMap.getOrDefault(statusDetailType,null));
        target.setData(data);
    }

    /**Maps enum corporate_annotation_type to string. */
    @AfterMapping
    public void mapEnumsCorpAnnotationType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String corpAnnotationType = String.valueOf(source.getCorporateAnnotationType());
        HashMap<String,String> corpAnnotationMap = MapperUtils.getCorpAnnotationTypeMap();
        data.setCorporateAnnotationType(corpAnnotationMap.getOrDefault(corpAnnotationType, null));
        target.setData(data);
    }

    /**Maps enum proof_status to string. */
    @AfterMapping
    public void mapEnumsProofStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String proofStatusType = source.getProofStatus();

        HashMap<String,String> proofStatusMap = MapperUtils.getProof_statusMap();
        data.setProofStatus(proofStatusMap.getOrDefault(proofStatusType,null));
        target.setData(data);

    }

    /**Maps enum jurisdiction to string. */
    @AfterMapping
    public void mapEnumsJurisdiction(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String jurisdictionType = source.getJurisdiction();

        HashMap<String,String> jurisdictionMap = MapperUtils.getJurisdictionMap();

        data.setJurisdiction(jurisdictionMap.getOrDefault(jurisdictionType,null));
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

    /**Mapping for Dates.*/
    @AfterMapping
    public void setDateMapping(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String dateOfCreation = source.getCreationDate();
        String dateOfDissolution = source.getDateOfDissolution();
        String fullMembersListDate = source.getFullMembersListDate();

        LocalDate parsedCreationDate = getParsedDate(dateOfCreation);
        LocalDate parsedDissolutionDate = getParsedDate(dateOfDissolution);
        LocalDate parsedFullMembersListDate = getParsedDate(fullMembersListDate);


        if (parsedCreationDate != null
                || parsedDissolutionDate != null
                || parsedFullMembersListDate != null) {

            if (parsedCreationDate != null) {
                data.setDateOfCreation(parsedCreationDate);
            }
            if (parsedDissolutionDate != null) {
                data.setDateOfDissolution(parsedDissolutionDate);
                data.setDateOfCessation(parsedDissolutionDate);
            }

            if (parsedFullMembersListDate != null) {
                data.setLastFullMembersListDate(parsedFullMembersListDate);
            }
            target.setData(data);
        }
    }

    /**Mapping for Confirmation Statement Dates.*/
    @AfterMapping
    public void setConfirmationStatementDatesMapping(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();

        if (source.getConfirmationStatementDates() != null) {

            String latestMadeUpToDate = source.getConfirmationStatementDates().getLatestMadeUpTo();
            String nextDueDate = source.getConfirmationStatementDates().getNextDue();
            String nextMadeUpToDate = source.getConfirmationStatementDates().getNextMadeUpTo();

            LocalDate parsedLatestMadeUpToDate = getParsedDate(latestMadeUpToDate);
            LocalDate parsedNextDueDate = getParsedDate(nextDueDate);
            LocalDate parsedNextMadeUpToDate = getParsedDate(nextMadeUpToDate);

            if (parsedLatestMadeUpToDate != null
                    || parsedNextDueDate != null
                    || parsedNextMadeUpToDate != null) {

                if (data.getConfirmationStatement() == null) {
                    data.setConfirmationStatement(new ConfirmationStatement());
                }

                if (parsedLatestMadeUpToDate != null) {
                    data.getConfirmationStatement().setLastMadeUpTo(parsedLatestMadeUpToDate);
                }
                if (parsedNextDueDate != null) {
                    data.getConfirmationStatement().setNextDue(parsedNextDueDate);
                }
                if (parsedNextMadeUpToDate != null) {
                    data.getConfirmationStatement().setNextMadeUpTo(parsedNextMadeUpToDate);
                }
                target.setData(data);
            }
        }
    }

    /**Mapping for LastAccounts.*/
    @AfterMapping
    public void setLastAccountsDatesMapping(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();

        if (source.getAccountingDates() != null) {

            String lastAccountsMadeUpTo = source.getAccountingDates().getLastPeriodEndOn();
            String periodStartOn = source.getAccountingDates().getLastPeriodStartOn();

            LocalDate parsedLastAccountsMadeUpTo = getParsedDate(lastAccountsMadeUpTo);
            LocalDate parsedPeriodStartOn = getParsedDate(periodStartOn);

            if (parsedLastAccountsMadeUpTo != null
                    || parsedPeriodStartOn != null) {

                if (data.getAccounts() == null) {
                    data.setAccounts(new Accounts());
                }

                if (parsedLastAccountsMadeUpTo != null) {
                    data.getAccounts().getLastAccounts().setMadeUpTo(parsedLastAccountsMadeUpTo);
                    data.getAccounts().getLastAccounts().setPeriodEndOn(parsedLastAccountsMadeUpTo);
                }
                if (parsedPeriodStartOn != null) {
                    data.getAccounts().getLastAccounts().setPeriodStartOn(parsedPeriodStartOn);
                }
                target.setData(data);
            }
        }
    }

    private static LocalDate getParsedDate(String date) {
        return Optional.ofNullable(date)
                .filter(s -> !s.isEmpty())
                .map(s -> LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyyMMdd")))
                .orElse(null);
    }

}
