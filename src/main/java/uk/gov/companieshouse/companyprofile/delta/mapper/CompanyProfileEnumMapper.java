package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.*;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.delta.BooleanFlag;
import uk.gov.companieshouse.api.delta.CompanyDelta;


import javax.validation.constraints.Null;
import java.util.HashMap;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileEnumMapper {

    @Mapping(target = "hasMortgages", source = "hasMortgages")

    public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);
/*
    public CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta) {
        CompanyProfile companyProfile = new CompanyProfile();
        Data data = new Data();
        companyProfile.setData(data);

        return companyProfile;
    }
*/

    @Named("booleanFlagToBoolean")
    protected BooleanFlag map(BooleanFlag value) {
        if(value == null) {
            return null;
        }

        return value;
    }

    /**maps BooleanFlag to Boolean. */
    public Boolean flagToBoolean(BooleanFlag flag) {
        return flag == null ? null : flag.getValue().equals("1");
    }

    /**maps Boolean flag fields into Boolean fields in target. */
    @AfterMapping
    public void mapBooleans(@MappingTarget CompanyProfile target, CompanyDelta source) {
 /*       Data data = target.getData();
        //data.setHasInsolvencyHistory(flagToBoolean(source.getHasInsolvencyHistory()));
        data.hasSuperSecurePscs(flagToBoolean(source.getSuperSecurePscInd()));
        data.isCommunityInterestCompany(flagToBoolean(source.getCicInd()));
        data.registeredOfficeIsInDispute(flagToBoolean(source.getRegisteredOfficeIsInDispute()));
        data.undeliverableRegisteredOfficeAddress(flagToBoolean(source.getUndeliverableRegisteredOfficeAddress()));
        target.setHasMortgages(flagToBoolean(source.getHasMortgages()));
        target.setData(data);*/

    }

    /**Maps enum type to string. */
    @AfterMapping
    public void mapEnums(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();
        //data.setType(MapperUtils.mapType(type));
        HashMap<String,String> typeMap = MapperUtils.getTypeMap();
        if(data == null) {
            data = new Data();
        }
        data.setType(typeMap.getOrDefault(type,null));
        target.setData(data);
    }

    /** Maps enum status to string. */
    @AfterMapping
    public void mapEnumsStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();
        HashMap<String,String> statusMap = MapperUtils.getStatusMap();
        if(data == null) {
            data = new Data();
        }
        data.setType(statusMap.getOrDefault(type,null));
        target.setData(data);
    }

    /**Maps enum status_detail to string. */
    @AfterMapping
    public void mapEnumsStatusDetail(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();
        HashMap<String,String> statusDetailMap = MapperUtils.getStatus_detailMap();
        data.setType(statusDetailMap.get(type));
        target.setData(data);
    }

    /**Maps enum proof_status to string. */
    @AfterMapping
    public void mapEnumsProofStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();

        HashMap<String,String> proofStatusMap = MapperUtils.getProof_statusMap();
        data.setType(proofStatusMap.get(type));
        target.setData(data);

    }

    /**Maps enum jurisdiction to string. */
    @AfterMapping
    public void mapEnumsJurisdiction(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();

        HashMap<String,String> jurisdictionMap = MapperUtils.getJurisdictionMap();
        data.setType(jurisdictionMap.get(type));
        target.setData(data);
    }

    /**Maps enum account_type to string. */
    @AfterMapping
    public void mapEnumsAccountType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();

        HashMap<String,String> accountTypeMap = MapperUtils.getAccountTypeMap();
        data.setType(accountTypeMap.get(type));
        target.setData(data);

    }


}
