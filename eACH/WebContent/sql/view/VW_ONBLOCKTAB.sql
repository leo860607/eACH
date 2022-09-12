DROP VIEW EACHUSER.VW_ONBLOCKTAB
GO


CREATE VIEW EACHUSER.VW_ONBLOCKTAB
AS 
SELECT

     timestamp(substr(a.TXDT,1,4) || '-' || substr(a.TXDT,5,2) || '-' || substr(a.TXDT,7,2) || '-' || substr(a.TXDT,9,2)|| '.' || substr(a.TXDT,11,2) || '.' || substr(a.TXDT,13,2) )  NewTXDT
    ,DECIMAL(EACHUSER.ISNUMERICII(a.SENDERFEE,a.txdate, a.stan, a.RESULTSTATUS),7,2) NewSENDERFEE
    ,DECIMAL(EACHUSER.ISNUMERICII(a.INFEE, a.txdate, a.stan, a.RESULTSTATUS),7,2) NewINFEE
    ,DECIMAL(EACHUSER.ISNUMERICII(a.OUTFEE, a.txdate, a.stan, a.RESULTSTATUS),7,2) NewOUTFEE
    ,DECIMAL(EACHUSER.ISNUMERICII(a.EACHFEE, a.txdate, a.stan, a.RESULTSTATUS),7,2) NewEACHFEE
    ,CASE WHEN A.GARBAGEDATA IS NULL THEN EACHUSER.ISNUMERIC(a.TXAMT) ELSE a.TXAMT END NewTXAMT
    ,DECIMAL( EACHUSER.ISNUMERICII(substr(a.FEE,1,3) || '.' || substr(a.FEE,4,2), a.txdate, a.stan, a.RESULTSTATUS) , 7,2 ) NewFEE
    ,coalesce( (select  (Case When  (not (BIZDATE is null))  and coalesce(RESULTCODE,'') != '01' then 'A'  Else null end)  from  EACHUSER.ONPENDINGTAB Where otxdate=A.txdate and ostan=A.stan  and oBizdate=A.Bizdate and oclearingphase=A.clearingphase) , (case when a.senderstatus='1' then 'R' else a.ResultStatus end) ) NewRESULT
    ,(CASE WHEN a.RESULTSTATUS = 'P' AND a.SENDERSTATUS ='1' THEN 'R' ELSE a.RESULTSTATUS END ) RP_DSUM_RESULT
,  (SELECT BUSINESS_TYPE_ID FROM EACH_TXN_CODE ETC WHERE a.PCODE = ETC.EACH_TXN_ID FETCH FIRST 1 ROWS ONLY ) AS BUSINESS_TYPE_ID
    ,a.*
FROM
EACHUSER.ONBLOCKTAB A
GO
