
CREATE VIEW EACHUSER.VW_ONPENDING_EC
AS 
SELECT
     timestamp(substr(a.TXDT,1,4) || '-' || substr(a.TXDT,5,2) || '-' || substr(a.TXDT,7,2) || '-' || substr(a.TXDT,9,2)|| '.' || substr(a.TXDT,11,2) || '.' || substr(a.TXDT,13,2) )  NewTXDT
    ,0-DECIMAL(EACHUSER.ISNUMERICII(a.SENDERFEE,a.txdate, a.stan, a.RESULTSTATUS),7,2) NewSENDERFEE
    ,0-DECIMAL(EACHUSER.ISNUMERICII(a.INFEE, a.txdate, a.stan, a.RESULTSTATUS),7,2) NewINFEE
    ,0-DECIMAL(EACHUSER.ISNUMERICII(a.OUTFEE, a.txdate, a.stan, a.RESULTSTATUS),7,2) NewOUTFEE
    ,0-DECIMAL(EACHUSER.ISNUMERICII(a.EACHFEE, a.txdate, a.stan, a.RESULTSTATUS),7,2) NewEACHFEE
    ,0-DECIMAL( EACHUSER.ISNUMERIC(a.TXAMT) )  NewTXAMT
    ,0-DECIMAL( EACHUSER.ISNUMERICII(substr(a.FEE,1,3) || '.' || substr(a.FEE,4,2), a.txdate, a.stan, a.RESULTSTATUS) ) NewFEE
    ,'R' NewRESULT
    ,B.BIZDATE NewBizDate
    ,B.Clearingphase NewClrPhase      
     ,a.*
       
FROM
EACHUSER.ONPENDINGTAB B
left join EACHUSER.ONBLOCKTAB A on A.txdate=B.otxdate  and A.Stan=B.oStan and A.Clearingphase=B.oClearingphase and A.BIZDATE = B.oBIZDATE 
Where B.RESULTCODE='01' and A.ACCTCODE='0'
GO
