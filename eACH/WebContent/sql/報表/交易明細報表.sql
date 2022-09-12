--交易明細報表
--以發動行所屬操作行角度查詢

--新版 20150520 edit by hugo 修改處理中的狀態判斷 及新增未完成的狀態
SELECT row_number() over (order by TXDT)  as SEQNO
 ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT
 ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE 
 ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt
, (case when a.newresult='R' then '失敗' when a.newresult='P' and a.senderstatus != '1' then '未完成' when a.newresult='P' and a.senderstatus = '1' then '處理中' else '成功' end) Resp
 ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC 
,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME 
 ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME
 ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME
--變數
,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME
--20150527 edit by hugo 成功及未完成才需要計算手續費 其它都算失敗且手續費視為0
--, (case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee
--sql.append(" ,coalesce(a.senderfee,0)  fee ");
, (case when a.newresult ='A' OR (a.newresult='P' and a.senderstatus != '1')  then  coalesce(a.senderfee,0) else 0 end) fee
--變數END
 FROM RPONBLOCKTAB a
 left join EACH_TXN_CODE b on b.each_txn_id = a.pcode 
WHERE 
--變數
a.BIZDATE LIKE '%20150212%' AND a.SENDERACQUIRE='0040000' AND a.CLEARINGPHASE='01'
 order by a.CLEARINGPHASE ,a.SENDERHEAD, a.PCODE
--變數END
--新版 20150216
SELECT row_number() over (order by TXDT)  as SEQNO
 ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT
 ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE 
 ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt
, (case when a.newresult='R' then '失敗' when a.newresult='P' then '處理中' else '成功' end) Resp
 ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC 
,coalesce(a.SENDERBANKID ,'') SENDERBANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERBANKID),'') SENDERBANKID_NAME 
 ,coalesce(a.outBankId,'')  outBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId_NAME
 ,coalesce(a.inBankId ,'')  inBankId , coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId_NAME
--變數
,coalesce(a.SENDERHEAD,'') BANKID , coalesce((select brbk_name from bank_branch where brbk_id=a.SENDERHEAD),'') BANK_NAME
, (case when a.newresult !='R' then  coalesce(a.senderfee,0) else 0 end) fee
--變數END
 FROM RPONBLOCKTAB a
 left join EACH_TXN_CODE b on b.each_txn_id = a.pcode 
WHERE 
--變數
a.BIZDATE LIKE '%20150212%' AND a.SENDERACQUIRE='0040000' AND a.CLEARINGPHASE='01'
 order by a.CLEARINGPHASE ,a.SENDERHEAD, a.PCODE
--變數END


--舊版20150212
SELECT row_number() over (order by TXDT)  as SEQNO
 ,(CASE WHEN coalesce(VARCHAR(a.txDT),'') ='' THEN '' ELSE (CAST (YEAR(a.txDT)-1911 AS CHAR(3)) ||''|| SUBSTR( REPLACE(CAST( DATE(a.txdt) AS CHAR(10)),'-','/'),5,6)  ||'\n'|| SUBSTR(REPLACE(CAST( TIME(a.TXDT) AS CHAR(8)) ,'.',':') ,1,8))  END ) TXDT
 ,a.pcode || coalesce(b.each_txn_name,'')  pcode  ,a.STAN ,a.txid, coalesce((select  txid ||'\n'|| txn_name from TXN_CODE where a.txid=txn_id),'') txn_name ,a.CLEARINGPHASE 
 ,coalesce(a.SENDERBANK,'') || '\n' || coalesce((select bgbk_name from bank_group where bgbk_id=a.SENDERBANK),'') SENDERBANK 
 ,coalesce(a.outBankId,'') || '\n' || coalesce((select brbk_name from bank_branch where brbk_id=a.outBankId),'') outBankId 
 ,coalesce(a.inBankId ,'') || '\n' || coalesce((select brbk_name from bank_branch where brbk_id=a.inBankId),'') inBankId 
 ,a.outAcctNo ,a.inAcctNo ,coalesce(a.senderId,'') ||'\n'|| coalesce(a.company_abbr,'') senderId ,Decimal(a.txAmt) txAmt
, (case when a.newresult='R' then '失敗' when a.newresult='P' then '處理中' else '成功' end) Resp
 ,RTRIM(coalesce(RC1 || '/','        /')) || coalesce(RC2 || '/','        /') || coalesce(RC3 || '\n','        \n') || RTRIM(coalesce(RC4 || '/','        /')) || RTRIM(coalesce(RC5 || '/','        /')) || coalesce(RC6 ,'         ') RC 
 ,coalesce(a.senderfee,0) fee
, a.SENDERACQUIRE
 FROM RPONBLOCKTAB a
 left join EACH_TXN_CODE b on b.each_txn_id = a.pcode 
WHERE 
a.BIZDATE LIKE '%20150212%' AND a.SENDERACQUIRE='0040000' AND a.CLEARINGPHASE='01'
 order by a.CLEARINGPHASE ,a.SENDERBANK, a.PCODE 

