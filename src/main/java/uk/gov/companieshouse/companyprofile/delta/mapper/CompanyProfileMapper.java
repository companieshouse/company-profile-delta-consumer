package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.gov.companieshouse.api.company.AccountPeriod;
import uk.gov.companieshouse.api.company.AccountingReferenceDate;
import uk.gov.companieshouse.api.company.AccountingRequirement;
import uk.gov.companieshouse.api.company.Accounts;
import uk.gov.companieshouse.api.company.AnnualReturn;
import uk.gov.companieshouse.api.company.BranchCompanyDetails;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.ConfirmationStatement;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.company.ForeignCompanyDetails;
import uk.gov.companieshouse.api.company.ForeignCompanyDetailsAccounts;
import uk.gov.companieshouse.api.company.LastAccounts;
import uk.gov.companieshouse.api.company.Links;
import uk.gov.companieshouse.api.company.MustFileWithin;
import uk.gov.companieshouse.api.company.NextAccounts;
import uk.gov.companieshouse.api.company.OriginatingRegistry;
import uk.gov.companieshouse.api.company.PreviousCompanyNames;
import uk.gov.companieshouse.api.company.RegisteredOfficeAddress;
import uk.gov.companieshouse.api.delta.BooleanFlag;
import uk.gov.companieshouse.api.delta.CompanyDelta;
import uk.gov.companieshouse.api.delta.ForeignCompany;
import uk.gov.companieshouse.api.delta.SicCodes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileMapper {

    @Mapping(target = "data.accounts.accountingReferenceDate.day", source = "accRefDate")
    @Mapping(target = "data.accounts.accountingReferenceDate.month", source = "accRefDate")

    @Mapping(target = "data.accounts.lastAccounts.type", source = "accountType")

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

    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodFrom.month",
            source = "foreignCompany.requiredToPublish.monthFrom")
    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodFrom.day",
            source = "foreignCompany.requiredToPublish.dayFrom")
    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodTo.month",
            source = "foreignCompany.requiredToPublish.monthTo")
    @Mapping(target = "data.foreignCompanyDetails.accounts.accountPeriodTo.day",
            source = "foreignCompany.requiredToPublish.dayTo")
    @Mapping(target = "data.foreignCompanyDetails.accounts.mustFileWithin.months",
            source = "foreignCompany.requiredToPublish.numberOfMonths")

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
    @Mapping(target = "data.partialDataAvailable", ignore = true)

    
    public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);

    /**maps AccRefDate. */
    @AfterMapping
    public void mapAccRefDate(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        Accounts accounts = Optional.ofNullable(data.getAccounts()).orElse(new Accounts());
        AccountingReferenceDate referenceDate = new AccountingReferenceDate();
        String sourceDate = source.getAccRefDate();
        if (sourceDate != null) {
            if (!"9999".equals(sourceDate)) {
                referenceDate.setDay(sourceDate.substring(0, Math.min(sourceDate.length(), 2)));
                try {
                    referenceDate.setMonth(sourceDate.substring(2,4));
                } catch (IndexOutOfBoundsException ex) {
                    referenceDate.setMonth("");
                }
                accounts.setAccountingReferenceDate(referenceDate);
            } else {
                accounts.setAccountingReferenceDate(null);
            }
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

    /** Maps partial data available based on company number prefix. */
    @AfterMapping
    public void mapPartialDataAvailable(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String companyNumber = source.getCompanyNumber();
        if (companyNumber != null) {
            if (companyNumber.matches("^(IC|SI|NV|AC|SA|NA|PC)\\w{6}$")) {
                data.setPartialDataAvailable("full-data-available-from-"
                        + "financial-conduct-authority");
            } else if (companyNumber.matches("^(RC|SR|NR)\\w{6}$")) {
                data.setPartialDataAvailable("full-data-available-from-the-company");
            } else if (companyNumber.matches("^(NP|NO|IP|SP|RS)\\w{6}$")) {
                data.setPartialDataAvailable("full-data-available-from-financial-"
                        + "conduct-authority-mutuals-public-register");
            }
        }
        target.setData(data);
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

    /**Maps enum type to string. */
    @AfterMapping
    public void mapEnumsType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        if (data == null) {
            data = new Data();
        }
        String typeFlag = source.getType();
        HashMap<String,String> typeMap = MapperUtils.getTypeMap();
        data.setType(typeMap.getOrDefault(typeFlag,null));
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

    /**Maps Foreign Company Account type to string. */
    @AfterMapping
    public void mapForeignAccountType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        if (data.getForeignCompanyDetails() != null) {
            ForeignCompanyDetails foreignCompanyDetails = data.getForeignCompanyDetails();
            if (foreignCompanyDetails.getAccountingRequirement() != null) {
                AccountingRequirement accountingRequirement = foreignCompanyDetails.getAccountingRequirement();

                String foreignAccountType = source.getForeignCompany().getAccReqType();

                HashMap<String,String> foreignAccountTypeMap = MapperUtils.getForeignAccountTypeMap();
                accountingRequirement.setForeignAccountType(foreignAccountTypeMap
                        .getOrDefault(foreignAccountType, null));
                foreignCompanyDetails.setAccountingRequirement(accountingRequirement);
                data.setForeignCompanyDetails(foreignCompanyDetails);
                target.setData(data);
            }
        }
    }

    /**Maps Foreign Company terms of account publication. */
    @AfterMapping
    public void mapTermsOfAccountPublication(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        if (data.getForeignCompanyDetails() != null) {
            ForeignCompanyDetails foreignCompanyDetails = data.getForeignCompanyDetails();
            if (foreignCompanyDetails.getAccountingRequirement() != null) {
                AccountingRequirement accountingRequirement = foreignCompanyDetails.getAccountingRequirement();
                String termsOfAccountPublication = source.getForeignCompany().getTermsOfAccountPublication();

                HashMap<String,String> termsOfAccountPublicationMap = MapperUtils.getTermsOfAccountPublicationMap();
                accountingRequirement.setTermsOfAccountPublication(termsOfAccountPublicationMap
                        .getOrDefault(termsOfAccountPublication, null));
                foreignCompanyDetails.setAccountingRequirement(accountingRequirement);
                data.setForeignCompanyDetails(foreignCompanyDetails);
                target.setData(data);
            }
        }
    }

    /**Maps Foreign Company Credit or Financial boolean. */
    @AfterMapping
    public void mapCreditOrFinancial(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        if (data.getForeignCompanyDetails() != null) {
            ForeignCompanyDetails foreignCompanyDetails = data.getForeignCompanyDetails();

            String isCreditOrFinancial = source.getForeignCompany().getCreditOrFinancial();
            foreignCompanyDetails.setIsACreditFinancialInstitution(!isCreditOrFinancial.equals("0"));

            data.setForeignCompanyDetails(foreignCompanyDetails);
            target.setData(data);
        }
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
        String closureDate = source.getClosureDate();
        String fullMembersListDate = source.getFullMembersListDate();

        LocalDate parsedCreationDate = getParsedDate(dateOfCreation);
        LocalDate parsedDissolutionDate = getParsedDate(dateOfDissolution);
        LocalDate parsedClosureDate = getParsedDate(closureDate);
        LocalDate parsedFullMembersListDate = getParsedDate(fullMembersListDate);


        if (parsedCreationDate != null
                || parsedDissolutionDate != null
                || parsedFullMembersListDate != null) {

            if (parsedCreationDate != null) {
                data.setDateOfCreation(parsedCreationDate);
            }
            if (parsedClosureDate != null) {
                data.setDateOfDissolution(parsedClosureDate);
            } else if (parsedDissolutionDate != null) {
                data.setDateOfDissolution(parsedDissolutionDate);
            }
            if (parsedDissolutionDate != null) {
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

            if (data.getConfirmationStatement() == null) {
                data.setConfirmationStatement(new ConfirmationStatement());
            }

            if (parsedLatestMadeUpToDate != null
                    || parsedNextDueDate != null
                    || parsedNextMadeUpToDate != null) {

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

            if (data.getAccounts() == null) {
                data.setAccounts(new Accounts());
            }

            if (parsedLastAccountsMadeUpTo != null
                    || parsedPeriodStartOn != null) {

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

    /**Mapping for Annual return dates.*/
    @AfterMapping
    public void setAnnualReturnDatesMapping(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();

        if (source.getAnnualReturnDates() != null) {

            String lastMadeUpTo = source.getAnnualReturnDates().getLatestMadeUpTo();
            String nextDue = source.getAnnualReturnDates().getNextDue();
            String nextMadeUpTo = source.getAnnualReturnDates().getNextMadeUpTo();

            LocalDate parsedLastMadeUpTo = getParsedDate(lastMadeUpTo);
            LocalDate parsedNextDue = getParsedDate(nextDue);
            LocalDate parsedNextMadeUpTo = getParsedDate(nextMadeUpTo);

            if (data.getAnnualReturn() == null) {
                data.setAnnualReturn(new AnnualReturn());
            }

            if (parsedLastMadeUpTo != null
                    || parsedNextDue != null
                    || parsedNextMadeUpTo != null) {

                if (parsedLastMadeUpTo != null) {
                    data.getAnnualReturn().setLastMadeUpTo(parsedLastMadeUpTo);
                }
                if (parsedNextDue != null) {
                    data.getAnnualReturn().setNextDue(parsedNextDue);
                }
                if (parsedNextMadeUpTo != null) {
                    data.getAnnualReturn().setNextMadeUpTo(parsedNextMadeUpTo);
                }
                target.setData(data);
            }
        }
    }

    /**Mapping for Next Accounts dates.*/
    @AfterMapping
    public void setNextAccountsDatesMapping(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();

        if (source.getAccountingDates() != null) {

            String dueOn = source.getAccountingDates().getNextDue();
            String periodEndOn = source.getAccountingDates().getNextPeriodEndOn();
            String periodStartOn = source.getAccountingDates().getNextPeriodStartOn();
            String nextDue = source.getAccountingDates().getNextDue();
            String nextMadeUpTo = source.getAccountingDates().getNextPeriodEndOn();

            LocalDate parsedDueOn = getParsedDate(dueOn);
            LocalDate parsedPeriodEndOn = getParsedDate(periodEndOn);
            LocalDate parsedPeriodStartOn = getParsedDate(periodStartOn);
            LocalDate parsedNextDue = getParsedDate(nextDue);
            LocalDate parsedNextMadeUpTo = getParsedDate(nextMadeUpTo);

            if (parsedDueOn != null
                    || parsedPeriodEndOn != null
                    || parsedPeriodStartOn != null
                    || parsedNextDue != null
                    || parsedNextMadeUpTo != null) {

                if (data.getAccounts() == null) {
                    data.setAccounts(new Accounts());
                }

                if (data.getAccounts().getNextAccounts() == null) {
                    data.getAccounts().setNextAccounts(new NextAccounts());
                }

                if (parsedDueOn != null) {
                    data.getAccounts().getNextAccounts().setDueOn(parsedDueOn);
                }
                if (parsedPeriodEndOn != null) {
                    data.getAccounts().getNextAccounts().setPeriodEndOn(parsedPeriodEndOn);
                }
                if (parsedPeriodStartOn != null) {
                    data.getAccounts().getNextAccounts().setPeriodStartOn(parsedPeriodStartOn);
                }
                if (parsedNextDue != null) {
                    data.getAccounts().setNextDue(parsedNextDue);
                }
                if (parsedNextMadeUpTo != null) {
                    data.getAccounts().setNextMadeUpTo(parsedNextMadeUpTo);
                }
                target.setData(data);
            }
        }
    }

    /**removes empty objects from the target to ensure they are not persisted to Mongo. */
    @AfterMapping
    public void removeEmptyObjects(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        if (data.getAccounts() != null) {
            if (Objects.equals(data.getAccounts().getLastAccounts(), new LastAccounts())) {
                data.getAccounts().setLastAccounts(null);
            }
            if (Objects.equals(data.getAccounts().getAccountingReferenceDate(), new AccountingReferenceDate())) {
                data.getAccounts().setAccountingReferenceDate(null);
            }
            if (Objects.equals(data.getAccounts(), new Accounts())) {
                data.setAccounts(null);
            }
        }
        if (Objects.equals(data.getBranchCompanyDetails(), new BranchCompanyDetails())) {
            data.setBranchCompanyDetails(null);
        }
        if (Objects.equals(data.getServiceAddress(), new RegisteredOfficeAddress())) {
            data.setServiceAddress(null);
        }
        ForeignCompanyDetails foreignCompanyDetails = data.getForeignCompanyDetails();
        if (foreignCompanyDetails != null) {

            if (Objects.equals(foreignCompanyDetails.getAccounts(), new ForeignCompanyDetailsAccounts())) {
                foreignCompanyDetails.setAccounts(null);
            } else if (foreignCompanyDetails.getAccounts() != null) {
                if (Objects.equals(foreignCompanyDetails.getAccounts().getAccountPeriodFrom(), new AccountPeriod())) {
                    foreignCompanyDetails.getAccounts().setAccountPeriodFrom(null);
                }
                if (Objects.equals(foreignCompanyDetails.getAccounts().getAccountPeriodTo(), new AccountPeriod())) {
                    foreignCompanyDetails.getAccounts().setAccountPeriodTo(null);
                }
            }
            if (Objects.equals(foreignCompanyDetails.getAccounts(), new ForeignCompanyDetailsAccounts())) {
                foreignCompanyDetails.setAccounts(null);
            }
            if (Objects.equals(foreignCompanyDetails.getAccountingRequirement(), new AccountingRequirement())) {
                foreignCompanyDetails.setAccountingRequirement(null);
            }
            if (Objects.equals(foreignCompanyDetails.getOriginatingRegistry(), new OriginatingRegistry())) {
                foreignCompanyDetails.setOriginatingRegistry(null);
            }
            data.setForeignCompanyDetails(foreignCompanyDetails);
            if (Objects.equals(foreignCompanyDetails, new ForeignCompanyDetails())) {
                data.setForeignCompanyDetails(null);
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
