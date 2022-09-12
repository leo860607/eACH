--V3.0
WITH TEMP AS (
    SELECT '一般' AS ACCTCODE, OP_TYPE, BIZDATE, CLEARINGPHASE, BGBK_ID, BRBK_ID, TXN_ID, SENDERID, CNT, AMT, 0 AS RVSCNT, 0 AS RVSAMT
    FROM RPCLEARFEETAB AS X
    WHERE COALESCE(CNT,0) <> 0
    AND BIZDATE = '20150225' AND BGBK_ID = '4530000'
    UNION ALL
    SELECT '沖正' AS ACCTCODE, OP_TYPE, BIZDATE, CLEARINGPHASE, BGBK_ID, BRBK_ID, TXN_ID, SENDERID, 0 AS CNT, 0 AS AMT, RVSCNT, RVSAMT
    FROM RPCLEARFEETAB AS Y
    WHERE COALESCE(RVSCNT,0) <> 0
    AND BIZDATE = '20150225' AND BGBK_ID = '4530000'
)

SELECT
COALESCE(VARCHAR(BGBK_ID),'') AS BGBK_ID, (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BGBK_ID) AS BGBK_NAME,
ACCTCODE, TXN_ID || '-' || (SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = A.TXN_ID) AS TXN_ID, VARCHAR(SENDERID) AS SENDERID,
COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'S'),0) AS FIRECOUNT,
COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN SND_BANK_FEE_DISC ELSE -SND_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS SND_BANK_FEE_DISC,
COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'S'),0) AS FIREAMT,
COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'O'),0) AS DEBITCOUNT,
COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN OUT_BANK_FEE_DISC ELSE -OUT_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS OUT_BANK_FEE_DISC,
COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'O'),0) AS DEBITAMT,
COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(CNT,0) ELSE COALESCE(RVSCNT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'I'),0) AS SAVECOUNT,
COALESCE((SELECT (CASE ACCTCODE WHEN '一般' THEN IN_BANK_FEE_DISC ELSE -IN_BANK_FEE_DISC END) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXN_ID AND START_DATE <= TRANS_DATE('" + bizDate + "','T','')) FETCH FIRST 1 ROWS ONLY),0) AS IN_BANK_FEE_DISC,
COALESCE((SELECT SUM(CASE ACCTCODE WHEN '一般' THEN COALESCE(AMT,0) ELSE COALESCE(RVSAMT,0) END) FROM TEMP WHERE BGBK_ID = A.BGBK_ID AND ACCTCODE = A.ACCTCODE AND TXN_ID = A.TXN_ID AND SENDERID = A.SENDERID AND OP_TYPE = 'I'),0) AS SAVEAMT
FROM (
    SELECT BGBK_ID, ACCTCODE, TXN_ID, SENDERID FROM TEMP GROUP BY BGBK_ID, ACCTCODE, TXN_ID, SENDERID
) AS A
ORDER BY BGBK_ID, ACCTCODE, TXN_ID

--V2.0
WITH TEMP AS (
    SELECT
    SENDERBANKID, OUTBANKID, INBANKID, SENDERHEAD, OUTHEAD, INHEAD, SENDERACQUIRE, OUTACQUIRE, INACQUIRE, CLEARINGPHASE,
    (SELECT BUSINESS_TYPE_ID FROM EACH_TXN_CODE WHERE EACH_TXN_ID = RP.PCODE) AS BUSINESS_TYPE_ID,
    PCODE, PCODE || '-' || (SELECT EACH_TXN_NAME FROM EACH_TXN_CODE WHERE EACH_TXN_ID = RP.PCODE) AS PCODE_DESC,
    TXID, SENDERID, SENDERFEE, OUTFEE, INFEE,    
    (CASE ACCTCODE WHEN '0' THEN '調整' ELSE '一般' END) AS ACCTCODE
    FROM RPONBLOCKTAB RP
    WHERE ((NEWRESULT = 'R' AND ACCTCODE = '0') OR NEWRESULT <> 'R') 
    AND RP.BIZDATE = '20150225'
)

SELECT
BANKID || '-' || (SELECT BGBK_NAME FROM BANK_GROUP WHERE BGBK_ID = A.BANKID) AS BANKID, CLEARINGPHASE,
BUSINESS_TYPE_ID || '-' || (SELECT BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID) AS BUSINESS_TYPE_NAME,
TXID || '-' || (SELECT TXN_NAME FROM TXN_CODE WHERE TXN_ID = A.TXID) AS TXID_DESC, SENDERID
,(SELECT COUNT(*) FROM TEMP WHERE SENDERHEAD = A.BANKID AND CLEARINGPHASE = A.CLEARINGPHASE AND BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND TXID = A.TXID AND SENDERID = A.SENDERID) AS FIRECOUNT
,COALESCE((SELECT SND_BANK_FEE_DISC FROM FEE_CODE WHERE FEE_ID = A.TXID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXID AND START_DATE <= TRANS_DATE('20150225','T','')) FETCH FIRST 1 ROWS ONLY),0) AS SND_BANK_FEE_DISC
,COALESCE((SELECT SUM(SENDERFEE) FROM TEMP WHERE SENDERHEAD = A.BANKID AND CLEARINGPHASE = A.CLEARINGPHASE AND BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND TXID = A.TXID AND SENDERID = A.SENDERID),0) AS FIREAMT
,(SELECT COUNT(*) FROM TEMP WHERE OUTHEAD = A.BANKID AND CLEARINGPHASE = A.CLEARINGPHASE AND BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND TXID = A.TXID AND SENDERID = A.SENDERID) AS DEBITCOUNT
,COALESCE((SELECT OUT_BANK_FEE_DISC FROM FEE_CODE WHERE FEE_ID = A.TXID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXID AND START_DATE <= TRANS_DATE('20150225','T','')) FETCH FIRST 1 ROWS ONLY),0) AS OUT_BANK_FEE_DISC
,COALESCE((SELECT SUM(OUTFEE) FROM TEMP WHERE OUTHEAD = A.BANKID AND CLEARINGPHASE = A.CLEARINGPHASE AND BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND TXID = A.TXID AND SENDERID = A.SENDERID),0) AS DEBITAMT
,(SELECT COUNT(*) FROM TEMP WHERE INHEAD = A.BANKID AND CLEARINGPHASE = A.CLEARINGPHASE AND BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND TXID = A.TXID AND SENDERID = A.SENDERID) AS SAVECOUNT
,COALESCE((SELECT IN_BANK_FEE_DISC FROM FEE_CODE WHERE FEE_ID = A.TXID AND START_DATE = (SELECT MAX(START_DATE) FROM FEE_CODE WHERE FEE_ID = A.TXID AND START_DATE <= TRANS_DATE('20150225','T','')) FETCH FIRST 1 ROWS ONLY),0) AS IN_BANK_FEE_DISC
,COALESCE((SELECT SUM(INFEE) FROM TEMP WHERE INHEAD = A.BANKID AND CLEARINGPHASE = A.CLEARINGPHASE AND BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND TXID = A.TXID AND SENDERID = A.SENDERID),0) AS SAVEAMT
FROM (
    SELECT DISTINCT SENDERHEAD AS BANKID, CLEARINGPHASE, BUSINESS_TYPE_ID, TXID, SENDERID FROM TEMP
    UNION
    SELECT DISTINCT OUTHEAD, CLEARINGPHASE, BUSINESS_TYPE_ID, TXID, SENDERID FROM TEMP
    UNION
    SELECT DISTINCT INHEAD, CLEARINGPHASE, BUSINESS_TYPE_ID, TXID, SENDERID FROM TEMP
) A
ORDER BY BANKID, CLEARINGPHASE, BUSINESS_TYPE_ID, TXID, SENDERID

--V1.0
WITH TEMP AS (
    SELECT
    ETC.BUSINESS_TYPE_ID,(ETC.BUSINESS_TYPE_ID || '-' || ETC.BUSINESS_TYPE_NAME) AS BUSINESS_TYPE_NAME,
    RP.PCODE,(RP.PCODE || '-' || ETC.EACH_TXN_NAME) AS PCODE_DESC,
    (CASE RP.ACCTCODE WHEN '1' THEN '一般' ELSE '調整' END) AS ACCTCODE,    
    RP.TXID,COALESCE(RP.TXID || '-' || TC.TXN_NAME, RP.TXID) AS TXID_DESC,
    (CASE RP.CLEARINGPHASE WHEN '01' THEN '第一階段' WHEN '02' THEN '第二階段' ELSE '未定義' END) AS CLEARINGPHASE,
    RP.SENDERID, RP.SENDERBANKID, RP.OUTBANKID, RP.INBANKID,
    COALESCE(TFM.SND_BANK_FEE_DISC,0) AS SND_BANK_FEE_DISC, COALESCE(TFM.OUT_BANK_FEE_DISC,0) AS OUT_BANK_FEE_DISC, 
    COALESCE(TFM.IN_BANK_FEE_DISC,0) AS IN_BANK_FEE_DISC, RP.SENDERHEAD, RP.OUTHEAD, RP.INHEAD, 
    COALESCE(RP.SENDERFEE,0) AS SENDERFEE, COALESCE(RP.OUTFEE,0) AS OUTFEE, COALESCE(RP.INFEE,0) AS INFEE
    FROM RPONBLOCKTAB RP LEFT JOIN (
        SELECT A.EACH_TXN_ID, A.EACH_TXN_NAME, B.BUSINESS_TYPE_ID, B.BUSINESS_TYPE_NAME
        FROM EACH_TXN_CODE A JOIN BUSINESS_TYPE B ON A.BUSINESS_TYPE_ID = B.BUSINESS_TYPE_ID
    ) ETC ON RP.PCODE = ETC.EACH_TXN_ID
    LEFT JOIN TXN_CODE TC ON RP.TXID = TC.TXN_ID
    LEFT JOIN (
        SELECT
        iTFM.TXN_ID, iTFM.FEE_ID, iTFM.START_DATE, 
        FC.OUT_BANK_FEE_DISC, FC.IN_BANK_FEE_DISC, FC.TCH_FEE_DISC, FC.SND_BANK_FEE_DISC
        FROM (
            SELECT TXN_ID, FEE_ID, MAX(START_DATE) AS START_DATE
            FROM TXN_FEE_MAPPING GROUP BY TXN_ID, FEE_ID
        ) iTFM LEFT JOIN FEE_CODE FC ON iTFM.FEE_ID = FC.FEE_ID AND iTFM.START_DATE = FC.START_DATE
    )TFM ON RP.TXID = TFM.TXN_ID
    ORDER BY ETC.BUSINESS_TYPE_ID ASC, RP.PCODE ASC, RP.ACCTCODE DESC
)
SELECT 
DISTINCT 'G' AS FAKE_GROUP, BUSINESS_TYPE_NAME, PCODE_DESC, CLEARINGPHASE, SENDERID
,(SELECT COUNT(*) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"SENDERHEAD =" + conStr_2 + "AND ") + "BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS FIRECOUNT
,COALESCE((SELECT SUM(SENDERFEE) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"SENDERHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID " + (StrUtils.isEmpty(conStr_2)?"":" GROUP BY SENDERHEAD ") + "),0) AS FIREAMT
,(SELECT COUNT(*) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"OUTHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS DEBITCOUNT
,COALESCE((SELECT SUM(OUTFEE) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"OUTHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID " + (StrUtils.isEmpty(conStr_2)?"":" GROUP BY OUTHEAD ") + ") ,0) AS DEBITAMT
,(SELECT COUNT(*) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"INHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID) AS SAVECOUNT
,COALESCE((SELECT SUM(INFEE) FROM TEMP WHERE " + (StrUtils.isEmpty(conStr_2)?"":"INHEAD =" + conStr_2 + "AND ") + " BUSINESS_TYPE_ID = A.BUSINESS_TYPE_ID AND CLEARINGPHASE = A.CLEARINGPHASE AND PCODE_DESC = A.PCODE_DESC AND SENDERID = A.SENDERID " + (StrUtils.isEmpty(conStr_2)?"":" GROUP BY INHEAD ") + ") ,0) AS SAVEAMT
FROM TEMP A
ORDER BY BUSINESS_TYPE_NAME ASC, PCODE_DESC ASC, CLEARINGPHASE ASC, SENDERID ASC