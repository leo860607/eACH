--V2.0
SELECT 
COALESCE(A.RESULTCODE, '') CONRESULTCODE, COALESCE(A.CLEARINGPHASE, '') CLEARINGPHASE , 
SUBSTR(TRANS_DATE(A.OTXDATE, 'T', '/'),2,9) TXDATE , VARCHAR(SUBSTR(TRANS_DATE(A.BIZDATE, 'T', '/'),2,9)) BIZDATE , 
(CASE WHEN COALESCE(VARCHAR(B.TXDT),'') = '' THEN '' ELSE TO_TWDATETIMEII(VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS')) END) TXDT,
C.BUSINESS_TYPE_ID || '-' || COALESCE((SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = C.BUSINESS_TYPE_ID),'') BIZTYPE,
A.PCODE || '-' || COALESCE(C.EACH_TXN_NAME,'') AS PCODE, VARCHAR(A.OSTAN) AS OSTAN,
COALESCE(A.SENDERBANKID,'') SENDERBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.SENDERBANKID),'') SENDERBANKID_NAME,
COALESCE(A.OUTBANKID,'') AS OUTBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.OUTBANKID),'') OUTBANKID_NAME,
COALESCE(A.INBANKID,'') AS INBANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INBANKID),'') INBANKID_NAME,
COALESCE(A.OUTACCT, B.OUTACCTNO) AS OUTACCTNO, COALESCE(A.INACCT, B.INACCTNO) AS INACCTNO, DECIMAL(A.TXAMT) AS TXAMT,
CASE A.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END RESULTCODE, 
COALESCE(A.INHEAD,'') BANKID, COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INHEAD),'') BANK_NAME
FROM RPONPENDINGTAB AS A LEFT JOIN RPONBLOCKTAB AS B on A.OSTAN = B.STAN AND A.OTXDATE = B.TXDATE   
LEFT JOIN EACH_TXN_CODE AS C ON C.EACH_TXN_ID = A.PCODE
WHERE A.CLEARINGPHASE = '01' AND A.BIZDATE = '20150511' AND A.INACQUIRE = '4530000'
ORDER BY A.CLEARINGPHASE, BANKID, A.PCODE

--查RPONPENDINGTAB
SELECT --REPLACE( coalesce( a.RESP ,'') ,'-' , '\n') CONRESULTCODE
coalesce( a.RESULTCODE ,'') CONRESULTCODE
, coalesce(a.CLEARINGPHASE ,'')  CLEARINGPHASE  
--,(CASE WHEN coalesce(b.TXDATE,'') ='' THEN '' ELSE  SUBSTR(REPLACE(VARCHAR_FORMAT( TIMESTAMP('0'|| CAST((CAST( SUBSTR (b.TXDATE,1,4) AS INTEGER ) -1911)  AS CHAR(3) ) || SUBSTR (b.TXDATE,5,4) ||'000000'),'YYYY-MM-DD HH24:MI:SS'),'-','/'),1,10)   END ) TXDATE
--,(CASE WHEN coalesce(b.BIZDATE,'') ='' THEN '' ELSE  SUBSTR(REPLACE(VARCHAR_FORMAT( TIMESTAMP('0'|| CAST((CAST( SUBSTR (b.BIZDATE,1,4) AS INTEGER ) -1911)  AS CHAR(3) ) || SUBSTR (b.BIZDATE,5,4) ||'000000'),'YYYY-MM-DD HH24:MI:SS'),'-','/'),1,10)   END ) BIZDATE
, RIGHT( RTRIM(CHAR((YEAR(DATE(SUBSTR(b.TXDATE,1,4) || '-' || SUBSTR(b.TXDATE,5,2) || '-' || SUBSTR(b.TXDATE,7,2)) ) - 1911))), 4) || '/' || REPLACE(RIGHT(VARCHAR(DATE(SUBSTR(b.TXDATE,1,4) || '-' || SUBSTR(b.TXDATE,5,2) || '-' || SUBSTR(b.TXDATE,7,2))),5), '-', '/') TXDATE
, RIGHT( RTRIM(CHAR((YEAR(DATE(SUBSTR(b.BIZDATE,1,4) || '-' || SUBSTR(b.BIZDATE,5,2) || '-' || SUBSTR(b.BIZDATE,7,2)) ) - 1911))), 4) || '/' || REPLACE(RIGHT(VARCHAR(DATE(SUBSTR(b.BIZDATE,1,4) || '-' || SUBSTR(b.BIZDATE,5,2) || '-' || SUBSTR(b.BIZDATE,7,2))),5), '-', '/') BIZDATE
--,TO_TWDATEII(b.TXDATE)  TXDATE 
--,TO_TWDATEII(a.BIZDATE) BIZDATE 
 ,(CASE WHEN coalesce(VARCHAR(b.txDT),'') ='' THEN '' ELSE (CAST (YEAR(b.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(b.txdt) AS CHAR(10)),'-','/'),5,6)  ||' '|| SUBSTR(REPLACE(CAST( TIME(b.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT
,c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype ,a.pcode ||'
'|| coalesce(c.each_txn_name,'')  pcode , b.STAN   ,coalesce(a.SENDERBANKID ,'') SENDERBANKID 
, coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME  ,coalesce(a.outBankId,'')  outBankId 
, coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME   ,coalesce(a.inBankId ,'')  inBankId 
, coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME 
 ,OUTACCTNO ,INACCTNO, decimal (a.txamt) as txamt , CASE a.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END RESULTCODE 
 ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME
FROM RPONPENDINGTAB a
LEFT JOIN RPONBLOCKTAB b on a.OSTAN = b.stan AND a.OBIZDATE = b.BIZDATE --不同日期stan有可能重覆故需在加上日期判斷
LEFT JOIN  EACH_TXN_CODE c on c.EACH_TXN_ID = a.PCODE
WHERE   a.BIZDATE = '20150225'
order by a.CLEARINGPHASE ,a.SENDERHEAD, a.PCODE
--有a.BIZDATE表示已有結果



--查ONPENDINGTAB

SELECT  coalesce( b.CONRESULTCODE ,'') CONRESULTCODE, coalesce(a.CLEARINGPHASE ,'')  OCLEARINGPHASE  
,TO_TWDATEII(b.TXDATE)  OTXDATE 
,TO_TWDATEII(a.BIZDATE) OBIZDATE 
--,(CASE WHEN coalesce(VARCHAR(b.txDT),'') ='' THEN '' ELSE ('0'||CAST (YEAR(b.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(b.txdt) AS CHAR(10)),'-','/'),5,6)  ||' '|| SUBSTR(REPLACE(CAST( TIME(b.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT 
, TO_TWDATETIMEII(b.txDT) TXDT 
,c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype ,a.pcode ||'
'|| coalesce(c.each_txn_name,'')  pcode , b.STAN   ,coalesce(a.SENDERBANKID ,'') SENDERBANKID 
, coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME  ,coalesce(a.outBankId,'')  outBankId 
, coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME   ,coalesce(a.inBankId ,'')  inBankId 
, coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME 
 ,OUTACCTNO ,INACCTNO, a.txAmt as txamt, CASE a.RESULTCODE WHEN '01' THEN '失敗' ELSE '成功' END RESP 
 ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME
FROM ONPENDINGTAB a
LEFT JOIN ONBLOCKTAB b on a.OSTAN = b.stan 
LEFT JOIN  EACH_TXN_CODE c on c.EACH_TXN_ID = a.PCODE
WHERE   a.BIZDATE = '20150225'
order by a.OCLEARINGPHASE ,a.SENDERHEAD, a.PCODE
--有a.BIZDATE表示已有結果


--查RPONBLOCKTAB
SELECT  coalesce( a.CONRESULTCODE ,'') CONRESULTCODE, coalesce(a.CLEARINGPHASE ,'')  OCLEARINGPHASE  
,(CASE WHEN coalesce(a.TXDATE,'') ='' THEN '' ELSE  SUBSTR(REPLACE(VARCHAR_FORMAT( TIMESTAMP('0'|| CAST((CAST( SUBSTR (a.TXDATE,1,4) AS INTEGER ) -1911)  AS CHAR(3) ) || SUBSTR (a.TXDATE,5,4) ||'000000'),'YYYY-MM-DD HH24:MI:SS'),'-','/'),1,10)   END ) OTXDATE 
,(CASE WHEN coalesce(a.BIZDATE,'') ='' THEN '' ELSE  SUBSTR(REPLACE(VARCHAR_FORMAT( TIMESTAMP('0'|| CAST((CAST( SUBSTR (a.BIZDATE,1,4) AS INTEGER ) -1911)  AS CHAR(3) ) || SUBSTR (a.BIZDATE,5,4) ||'000000'),'YYYY-MM-DD HH24:MI:SS'),'-','/'),1,10)   END ) OBIZDATE 
,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE ('0'||CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  ||' '|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT 
,c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype ,a.pcode ||'
'|| coalesce(c.each_txn_name,'')  pcode , a.STAN   ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME  ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME   ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME 
 ,OUTACCTNO ,INACCTNO, a.txAmt as txamt, case a.newresult when  'R' then '失敗' else '成功' end  RESP 
 ,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME
FROM RPONBLOCKTAB a  left join EACH_TXN_CODE c on c.each_txn_id = a.pcode 
--變數
WHERE a.RESULTSTATUS = 'P'  AND a.newresult != 'P'  AND a.BIZDATE = '20150225'
order by a.CLEARINGPHASE ,a.SENDERHEAD, a.PCODE
--變數 END
GO