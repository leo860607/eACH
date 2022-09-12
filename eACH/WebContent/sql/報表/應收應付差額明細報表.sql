 --20150302 後新的
SELECT coalesce( b.CTBK_ID ,'')  CTBK_ID , coalesce((SELECT BGBK_NAME  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBGBK_NAME 
, coalesce((SELECT CTBK_ACCT  FROM BANK_GROUP  WHERE BGBK_ID = b.CTBK_ID )  ,'') CTBK_ACCT ,VARCHAR(a.CLEARINGPHASE) CLEARINGPHASE 
,SUM(a.RECVCNT) RECVCNT , SUM(a.RECVAMT) RECVAMT ,SUM(a.PAYCNT) PAYCNT , SUM(a.PAYAMT ) PAYAMT ,SUM(a.RVSRECVCNT) RVSRECVCNT , SUM(a.RVSRECVAMT) RVSRECVAMT ,SUM(a.RVSPAYCNT) RVSPAYCNT ,SUM(a.RVSPAYAMT) RVSPAYAMT 
,SUM(a.RECVAMT+a.RVSRECVAMT) in_tol, SUM(a.PAYAMT+a.RVSPAYAMT) out_tol,SUM((a.RECVAMT+a.RVSRECVAMT)+(a.PAYAMT+a.RVSPAYAMT)) dif_tol
FROM EACHUSER.RPONCLEARINGTAB a
LEFT JOIN BANK_GROUP b ON a.BGBK_ID = b.BGBK_ID
WHERE a.BIZDATE = '20150225'
GROUP BY b.CTBK_ID  ,CLEARINGPHASE
ORDER BY CLEARINGPHASE ,  b.CTBK_ID

--20150302 前舊的
WITH TEMP AS (SELECT      (case when a.ACCTCODE='0' then '0' else '1' end) ACCTCODE  
			     ,a.CLEARINGPHASE   			
			     , GetBkHeadId(a.INCLEARING) INCLEARING  			
			     , GetBkHeadId(a.OUTCLEARING) OUTCLEARING  			
			     ,a.TXAMT  
			     ,a.RESULTSTATUS   
			     ,b.TXN_TYPE   			
			     FROM RPONBLOCKTAB  A  
			     left join txn_code b on b.TXN_ID=a.TXID   			
			 )  

			  SELECT  GetBkHead(a.bgbk_Id)     bgbk_Id a.CLEARINGPHASE   			
			   ,(SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id  and ACCTCODE='1'  and TXN_TYPE='SD') OutAmt  //代收金額(A)
			   ,(SELECT Sum(TXAMT)  from TEMP where  OUTCLEARING=a.bgbk_Id  and ACCTCODE='0'  and TXN_TYPE='SD') OutBackAmt   //代收沖正金額(B)  代收案件(應收)差額(A-B)
			   ,(SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id  and ACCTCODE='1' and TXN_TYPE='SC') INAmt  //代付金額(C)
			   ,(SELECT Sum(TXAMT)  from TEMP where  INCLEARING=a.bgbk_Id  and ACCTCODE='0' and TXN_TYPE='SC' ) InBackAmt  //代付金額沖正(D) 代付案件(應付)差額(C-D) 淨差額 = (A-B) - (C-D)
			  FROM (select distinct INCLEARING bgbk_Id from Temp  //
			  union select distinct OUTCLEARING bgbk_Id from Temp)  a  //
			  order by a.bgbk_Id  desc