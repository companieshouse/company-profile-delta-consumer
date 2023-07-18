package uk.gov.companieshouse.companyprofile.delta.mapper;

import org.mapstruct.*;
import uk.gov.companieshouse.api.company.Accounts;
import uk.gov.companieshouse.api.company.CompanyProfile;
import uk.gov.companieshouse.api.company.Data;
import uk.gov.companieshouse.api.company.LastAccounts;
import uk.gov.companieshouse.api.delta.BooleanFlag;
import uk.gov.companieshouse.api.delta.CompanyDelta;


import javax.validation.constraints.Null;
import java.util.HashMap;

@Mapper(componentModel = "spring")
public abstract class CompanyProfileEnumMapper {

    public abstract CompanyProfile companyDeltaToCompanyProfile(CompanyDelta companyDelta);

    /**maps BooleanFlag to Boolean. */
    public Boolean flagToBoolean(BooleanFlag flag) {
        return flag == null ? null : flag.getValue().equals("1");
    }

    /**Maps enum type to string. */
    @AfterMapping
    public void mapEnums(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData( );
        if(data == null) {
            data = new Data( ) ;
        }
        BooleanFlag subTypeFlag = source.getSubtype( );
        String subType = null;
        if(subTypeFlag != null){
            subType = subTypeFlag.getValue();
        }
        HashMap<String,String> subTypeMap = MapperUtils.getSubTypeMap( );
        data.setSubtype(subTypeMap.getOrDefault(subType,null));
        target.setData(data);
    }

    /** Maps enum status to string. */
    @AfterMapping
    public void mapEnumsStatus(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String statusType = source.getStatus();
        HashMap<String,String> statusMap = MapperUtils.getStatusMap();
        if(data == null) {
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



    /**Maps enum account_type to string. */
    @AfterMapping
    public void mapEnumsAccountType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();
        String accountType = source.getType();
        //data.setType(MapperUtils.mapType(type));
        HashMap<String,String> accountTypeMap = MapperUtils.getAccountTypeMap();
        if(data == null) {
            data = new Data();
        }
        data.setType(accountTypeMap.getOrDefault(accountType,null));
        target.setData(data);

    }


/**Maps enum account_type to string. *//*

    @AfterMapping
    public void mapEnumsAccountType(@MappingTarget CompanyProfile target, CompanyDelta source) {
        Data data = target.getData();

        String accountType = source.getAccountType();
        Accounts accounts = data.getAccounts();
        LastAccounts lastAccounts = accounts.getLastAccounts();

        HashMap<String,String> accountTypeMap = MapperUtils.getAccountTypeMap();

        lastAccounts.setType(accountTypeMap.getOrDefault(accountType,null));
        accounts.setLastAccounts(lastAccounts);
        data.setAccounts(accounts);

        target.setData(data);

    }

*/

}
