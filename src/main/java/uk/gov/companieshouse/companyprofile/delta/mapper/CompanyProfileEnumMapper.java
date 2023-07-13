package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.delta.CompanyDelta;

import java.util.HashMap;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileEnumMapper {

    //public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);

    @AfterMapping
    public void mapEnums(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();
        //data.setType(MapperUtils.mapType(type));
        HashMap<String,String> typeMap = MapperUtils.getTypeMap();
        data.setType(typeMap.get(type));
        target.setData(data);

    }

    @AfterMapping
    public void mapEnumsStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();
        HashMap<String,String> statusMap = MapperUtils.getStatusMap();
        data.setType(statusMap.get(type));
        target.setData(data);
    }

    @AfterMapping
    public void mapEnumsStatusDetail(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();
        HashMap<String,String> statusDetailMap = MapperUtils.getStatus_detailMap();
        data.setType(statusDetailMap.get(type));
        target.setData(data);
    }

    @AfterMapping
    public void mapEnumsProofStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();

        HashMap<String,String> ProofStatusMap = MapperUtils.getProof_statusMap();
        data.setType(ProofStatusMap.get(type));
        target.setData(data);

    }

    @AfterMapping
    public void mapEnumsJurisdiction(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();

        HashMap<String,String> jurisdictionMap = MapperUtils.getJurisdictionMap();
        data.setType(jurisdictionMap.get(type));
        target.setData(data);
    }

    @AfterMapping
    public void mapEnumsAccountType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String type = source.getType();

        HashMap<String,String> accountTypeMap = MapperUtils.getAccountTypeMap();
        data.setType(accountTypeMap.get(type));
        target.setData(data);

    }


}
