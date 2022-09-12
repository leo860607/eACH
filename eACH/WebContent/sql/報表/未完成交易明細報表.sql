--2015024前 新的
SELECT  c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype
 ,(CASE WHEN coalesce(VARCHAR(a.TXDT),'') ='' THEN '' ELSE ('0'||CAST (YEAR(a.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.TXDT) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXDT  
 ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE 
 ,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME 
 ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME
 ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME
--變數
,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME 
--變數 end
 ,OUTACCTNO ,INACCTNO, a.txAmt as txamt,a.newresult ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId
 FROM RPONBLOCKTAB a
  --left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN 
  left join EACH_TXN_CODE c on c.each_txn_id = a.pcode
order by a.CLEARINGPHASE ,a.SENDERBANK, a.PCODE 

--2015024前 舊的
SELECT  c.business_type_id || coalesce((select (business_type_name) from BUSINESS_TYPE where business_type_id=c.business_type_id),'') biztype
 ,(CASE WHEN coalesce(VARCHAR(a.TXDT),'') ='' THEN '' ELSE ('0'||CAST (YEAR(a.TXDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.TXDT) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8)) END ) TXDT  
 ,a.pcode ||'\n'|| coalesce(c.each_txn_name,'')  pcode , a.STAN  
 ,a.senderBankId ||'\n'||coalesce((select brbk_name from bank_Branch where brbk_id=a.senderBankId),'')  senderBankId
 ,a.OUTBANKID ||'\n'|| coalesce((select brbk_name from bank_Branch where brbk_id=a.OUTBANKID),'')  OUTBANKID
 ,a.INBANKID ||'\n'|| coalesce((select brbk_name from bank_Branch where brbk_id=a.INBANKID),'')  INBANKID
//			sql.append(" ,OUTACCTNO ,INACCTNO, STRIP(a.txAmt ,L ,'0') as txamt,(case when b.RESULTCODE='00' then 'A' else 'R' end) RESULTCODE");
 ,OUTACCTNO ,INACCTNO, a.txAmt as txamt,(case when b.RESULTCODE='00' then 'A' else 'R' end) RESULTCODE
 FROM RPONBLOCKTAB a
  left join ONPENDINGTAB b on b.OTXDate=a.txDATE and b.OSTAN=a.STAN 
  left join EACH_TXN_CODE c on c.each_txn_id = a.pcode
  
  
