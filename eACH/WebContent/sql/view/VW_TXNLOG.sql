DROP VIEW EACHUSER.VW_TXNLOG
GO

CREATE VIEW EACHUSER.VW_TXNLOG
AS 

 

SELECT
COALESCE(TG.ISSUERID , '') AGENT_COMPANY_ID
, COALESCE( (SELECT COMPANY_ABBR_NAME FROM AGENT_PROFILE AP WHERE AP.COMPANY_ID = TG.ISSUERID),'') AS COMPANY_ABBR_NAME  
,(CASE WHEN COALESCE(TG.PCODE ,'') IN ('2505', '2506', '2705', '2706') THEN    COALESCE( OUTNATIONALID  ,'' )ELSE COALESCE(INNATIONALID,'')  END ) AS SND_COMPANY_ID 
,(CASE WHEN COALESCE(TG.PCODE ,'') IN ('2505', '2506', '2705', '2706') THEN    COALESCE( EACHUSER.GETCOMPANY_ABBR(COALESCE( OUTNATIONALID  ,'' )),'') ELSE COALESCE( EACHUSER.GETCOMPANY_ABBR(COALESCE(INNATIONALID,'') ),'') END ) AS SND_COMPANY_ABBR_NAME   
,COALESCE((SELECT TC.TXN_NAME FROM TXN_CODE TC WHERE TC.TXN_ID = TG.TXID ),'') AS TXN_NAME 
, COALESCE((SELECT ETC.EACH_TXN_NAME FROM EACH_TXN_CODE ETC WHERE ETC.EACH_TXN_ID = TG.PCODE ),'') AS PCODE_NAME     
,DECIMAL(EACHUSER.ISNUMERIC(substr(TG.HANDLECHARGE,1,3) || '.' || substr(TG.HANDLECHARGE,4,2))  ,7,2)  AS CUST_FEE 
,(SELECT DECIMAL( COALESCE(FEE , 0.00) , 7,2) FROM AGENT_FEE_CODE WHERE COMPANY_ID =  COALESCE(TG.ISSUERID , '')  AND FEE_ID = COALESCE(TG.TXID , '')   AND START_DATE <= EACHUSER.TRANS_DATE(TG.BIZDATE, 'T', '')  ORDER BY START_DATE DESC  FETCH FIRST 1 ROWS ONLY ) AS AGENT_FEE
, COALESCE(OB.BIZDATE,TG.BIZDATE,'') AS TG_BIZDATE

,( CASE WHEN COALESCE(TG.STAN ,'') <>'' AND  COALESCE(OB.TXDATE ,'') <>''    AND COALESCE(TG.RESPONSECODE ,'') <>'' AND  COALESCE(TG.RESPONSECODE ,'') <> COALESCE(OB.RC2,'')    THEN  'U' 
         WHEN COALESCE(TG.RESPONSECODE ,'') <>'' AND COALESCE(TG.RESPONSECODE ,'') <>'3001' THEN 'R'
         WHEN COALESCE(TG.RESPONSECODE ,'') = '3001' THEN 'A'
         WHEN COALESCE(TG.RESPONSECODE ,'') = '' THEN 'W'
         WHEN COALESCE(OB.RESULTSTATUS ,'') = 'P' AND  COALESCE(OB.SENDERSTATUS , '') = '2' THEN 'P'
    ELSE  'X'END) TG_RESULT
,TG.*
FROM
EACHUSER.TXNLOG TG
LEFT JOIN ONBLOCKTAB OB ON TG.STAN = OB.STAN AND TG.TXDATE = OB.TXDATE AND (COALESCE(OB.TXDATE,'') <> ''  AND COALESCE(OB.STAN,'') <> '')

WHERE COALESCE (TG.PROCESSCODE,'') NOT IN('1000','2000')
GO