package com.yue.sqlfilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author 可乐
 */
@SpringBootApplication
@Slf4j
public class SqlFilterApplication {

    public static void main(String[] args) throws IOException {
//        String field1= "`rowguid`, `taskname`, `catalogcode`, `tasktype`, `bylaw`, `uselevel`, `taskcode`, `taskhandleitem`, `taskstate`, `taskversion`, `deptname`, `depttype`, `deptcode`, `entrustname`, `anticipateday`, `anticipatetype`, `anticipateexplain`, `promiseday`, `promisetype`, `promiseexplain`, `acceptcondition`, `handleflow`, `isfee`, `feebasis`, `servertype`, `projecttype`, `handletype`, `limitscenenum`, `specialprocedure`, `appissinglelogin`, `mobileterminalurl`, `issinglelogin`, `linkaddr`, `transactaddr`, `transacttime`, `linkway`, `superviseway`, `planeffectivedate`, `plancanceldate`, `cd_time`, `areacode`, `is_secret`, `itemid`, `is_reviewtransfer`, `is_multilevelpower`, `ishistory`, `dnhandleflow`, `sxfl`, `isnotifypromise`, `formconfigflag`, `appconfigflag`, `appointmentflag`, `sysid`, `pretrialflag`, `fwcj`, `slsxdw`, `slsx`, `slsxexplain`, `credit_promise_type`, `data_source`";
//        String field2 = "`cd_id`, `RowGuid`, `TaskName`, `CatalogCode`, `LocalCatalogCode`, `TaskType`, `ByLaw`, `UseLevel`, `TaskCode`, `LocalTaskCode`, `TaskHandleItem`, `TaskState`, `TaskVersion`, `DeptName`, `DeptType`, `DeptCode`, `EntrustName`, `AnticipateDay`, `AnticipateType`, `AnticipateExplain`, `PromiseDay`, `PromiseType`, `PromiseExplain`, `AcceptCondition`, `HandleFlow`, `IsFee`, `FeeBasis`, `ServerType`, `ProjectType`, `HandleType`, `LimitSceneNum`, `SpecialProcedure`, `AppIsSingleLogin`, `MobileTerminalUrl`, `IsSingleLogin`, `LinkAddr`, `TransactAddr`, `TransactTime`, `LinkWay`, `SuperviseWay`, `PlanEffectiveDate`, `PlanCancelDate`, `Cd_operation`, `cd_source`, `Cd_time`, `Cd_batch`, `DataSource`, `AREACODE`, `UP_SIGN`, `DN_SIGN`, `dnDataSource`, `is_secret`, `powerid`, `itemid`, `is_reviewtransfer`, `is_multilevelpower`, `ishistory`, `dnhandleflow`, `cgitemid`, `sxfl`, `IsNotifyPromise`, `fwcj`, `SLSXDW`, `SLSX`, `SLSXEXPLAIN`, `credit_promise_type`, `version_date`, `helpviewfileguid`, `linkfind`, `servicecontent`, `mobile_ggfwappiid`, `mobile_ggfwappname`, `ou_areacode`, `itemTag`";
        SpringApplication.run(SqlFilterApplication.class,args);
        log.info("-------------------------------sql-filter项目启动成功---------------------------------------");
    }
}
