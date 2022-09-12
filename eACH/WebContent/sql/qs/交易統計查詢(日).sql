--V5.0
WITH TEMP AS (
    SELECT TXDATE, STAN, NEWTXAMT, SENDERACQUIRE, OUTACQUIRE, INACQUIRE,
    BIZDATE, CLEARINGPHASE, PCODE, RESULTSTATUS AS NEWRESULT, ACCTCODE AS ACCTCODE,
    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD
    FROM VW_ONBLOCKTAB
    WHERE SENDERSTATUS <> '1'
    AND CLEARINGPHASE = '02'
    AND (SENDERACQUIRE = '4530000' OR OUTACQUIRE = '4530000' OR INACQUIRE = '4530000')
    AND (SENDERHEAD = '4530000' OR OUTHEAD = '4530000' OR INHEAD = '4530000')
    AND PCODE = '2101'
    --AND RESULTSTATUS = 'P'
    AND BIZDATE >= '20150313' AND BIZDATE <= '20150313'
)

SELECT
A.BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE A.BANKID = BGBK_ID),'') AS BANKHEAD
,A.PCODE, (CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE '未完成' END) AS RESULTSTATUS
,(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS FIRECOUNT
,COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS FIREAMT
,(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS DEBITCOUNT
,COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS DEBITAMT
,(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS SAVECOUNT
,COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS SAVEAMT
FROM (
    SELECT ROWNUMBER() OVER() AS ROWNUMBER, T.* FROM (
        SELECT SENDERHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP GROUP BY SENDERHEAD, PCODE, NEWRESULT
        UNION
        SELECT OUTHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY OUTHEAD, PCODE, NEWRESULT
        UNION
        SELECT INHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY INHEAD, PCODE, NEWRESULT
    ) AS T 
    WHERE (SELECT OPBK_ID FROM BANK_OPBK WHERE BGBK_ID = BANKID AND OPBK_ID = '4530000') = '4530000'
    AND T.BANKID = '4530000'
) AS A
WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 2

--V4.0 (排除沖正資料，並顯示交易當初狀態(RESULTSTATUS))
WITH TEMP AS (
    SELECT TXDATE, STAN, NEWTXAMT, SENDERACQUIRE, OUTACQUIRE, INACQUIRE,
    BIZDATE, CLEARINGPHASE, PCODE, RESULTSTATUS AS NEWRESULT, ACCTCODE AS ACCTCODE,
    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD
    FROM VW_ONBLOCKTAB
    WHERE SENDERSTATUS <> '1'
    AND CLEARINGPHASE = '02'
    AND (SENDERACQUIRE = '4530000' OR OUTACQUIRE = '4530000' OR INACQUIRE = '4530000')
    AND (SENDERHEAD = '4530000' OR OUTHEAD = '4530000' OR INHEAD = '4530000')
    AND PCODE = '2101'
    --AND RESULTSTATUS = 'P'
    AND BIZDATE >= '20150313' AND BIZDATE <= '20150313'
)

SELECT
A.BANKID || '-' || COALESCE((SELECT BGBK_NAME FROM BANK_GROUP WHERE A.BANKID = BGBK_ID),'') AS BANKHEAD
,A.PCODE, (CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE '未完成' END) AS RESULTSTATUS
,(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS FIRECOUNT
,COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS FIREAMT
,(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS DEBITCOUNT
,COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS DEBITAMT
,(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS SAVECOUNT
,COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS SAVEAMT
FROM (
    SELECT ROWNUMBER() OVER() AS ROWNUMBER, T.* FROM (
        SELECT SENDERHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP GROUP BY SENDERHEAD, PCODE, NEWRESULT
        UNION
        SELECT OUTHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY OUTHEAD, PCODE, NEWRESULT
        UNION
        SELECT INHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY INHEAD, PCODE, NEWRESULT
    ) AS T 
    WHERE (SELECT OPBK_ID FROM BANK_GROUP WHERE BGBK_ID = BANKID) = '4530000'
    AND T.BANKID = '4530000'
) AS A
WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 2

--V3.0
WITH TEMP AS (
    SELECT TXDATE, STAN,
    --ISNUMERICII(NEWTXAMT, TXDATE, STAN, RESULTSTATUS) AS NEWTXAMT,
    NEWTXAMT,
    SENDERACQUIRE,OUTACQUIRE,INACQUIRE,
    BIZDATE, CLEARINGPHASE, PCODE, (CASE '[by search condition]' WHEN '' THEN NEWRESULT ELSE RESULTSTATUS END) AS NEWRESULT, COALESCE(ACCTCODE,'1') AS ACCTCODE,
    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD
    FROM VW_ONBLOCKTAB
    WHERE  BIZDATE >= '20150302'  AND  BIZDATE <= '20150306'  AND  PCODE = '2101'
    AND  (SENDERACQUIRE = '9970018' OR OUTACQUIRE = '9970018' OR INACQUIRE = '9970018')
    AND  (SENDERHEAD = '1200000' OR OUTHEAD = '1200000' OR INHEAD = '1200000')
    AND  CLEARINGPHASE = '02'  AND  NEWRESULT = 'R'
    UNION ALL
    SELECT TXDATE, STAN,  NEWTXAMT, SENDERACQUIRE,OUTACQUIRE,INACQUIRE,     NEWBIZDATE, NEWCLRPHASE, PCODE, NEWRESULT,
    COALESCE(ACCTCODE,'1') AS ACCTCODE,     COALESCE(SENDERHEAD,'') AS SENDERHEAD, COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD
    FROM VW_ONPENDING_EC
    WHERE  NEWBIZDATE >= '20150302'  AND  NEWBIZDATE <= '20150306'  AND  PCODE = '2101'
    AND  (SENDERACQUIRE = '9970018' OR OUTACQUIRE = '9970018' OR INACQUIRE = '9970018')
    AND  (SENDERHEAD = '1200000' OR OUTHEAD = '1200000' OR INHEAD = '1200000')
    AND  NEWCLRPHASE = '02'  AND  NEWRESULT = 'R'
)

SELECT
A.BANKID || '-' || (SELECT COALESCE(BGBK_NAME,'') FROM BANK_GROUP WHERE A.BANKID = BGBK_ID) AS BANKHEAD, A.PCODE AS PCODE, 
(CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE '未完成' END) AS RESULTSTATUS, 
(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS FIRECOUNT, 
COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS FIREAMT, 
(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS DEBITCOUNT, 
COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS DEBITAMT, 
(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS SAVECOUNT, 
COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS SAVEAMT
FROM (
    SELECT * FROM (
        SELECT ROWNUMBER() OVER() AS ROWNUMBER, T.* FROM (
            SELECT SENDERHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP GROUP BY SENDERHEAD, PCODE, NEWRESULT
            UNION
            SELECT OUTHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY OUTHEAD, PCODE, NEWRESULT
            UNION
            SELECT INHEAD, PCODE, NEWRESULT FROM TEMP GROUP BY INHEAD, PCODE, NEWRESULT
        ) AS T LEFT JOIN BANK_GROUP BG ON T.BANKID = BG.BGBK_ID
        WHERE PCODE = '2101' AND  BANKID = '0120000' AND NEWRESULT = 'R' AND BG.OPBK_ID = '9970018'
    ) WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 10
) AS A

--V2.0
WITH TEMP AS (
    SELECT TXDATE, STAN, ISNUMERICII(NEWTXAMT, TXDATE, STAN, RESULTSTATUS) AS NEWTXAMT, SENDERACQUIRE,OUTACQUIRE,INACQUIRE, 
    BIZDATE, CLEARINGPHASE, PCODE, (CASE '[by search condition]' WHEN '' THEN NEWRESULT ELSE RESULTSTATUS END) AS NEWRESULT, COALESCE(ACCTCODE,'1') AS ACCTCODE,
    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD
    FROM VW_ONBLOCKTAB
    WHERE  BIZDATE >= '20150225'  AND  BIZDATE <= '20150226'  AND  PCODE = '2101'  AND  (SENDERACQUIRE = '0040000' OR OUTACQUIRE = '0040000' OR INACQUIRE = '0040000')  AND  (SENDERHEAD = '0040000' OR OUTHEAD = '0040000' OR INHEAD = '0040000')  AND CLEARINGPHASE = '01' AND NEWRESULT = 'A'
    UNION ALL
    SELECT TXDATE, STAN,  NEWTXAMT, SENDERACQUIRE,OUTACQUIRE,INACQUIRE, 
    NEWBIZDATE, NEWCLRPHASE, PCODE, NEWRESULT, COALESCE(ACCTCODE,'1') AS ACCTCODE,
    COALESCE(SENDERHEAD,'') AS SENDERHEAD,COALESCE(OUTHEAD,'') AS OUTHEAD,COALESCE(INHEAD,'') AS INHEAD
    FROM VW_ONPENDING_EC
    WHERE  NEWBIZDATE >= '20150225'  AND  NEWBIZDATE <= '20150226'  AND  PCODE = '2101'  AND  (SENDERACQUIRE = '0040000' OR OUTACQUIRE = '0040000' OR INACQUIRE = '0040000')  AND  (SENDERHEAD = '0040000' OR OUTHEAD = '0040000' OR INHEAD = '0040000')  AND NEWCLRPHASE = '01' AND NEWRESULT = 'A'
)

SELECT
A.BANKID || '-' || (SELECT COALESCE(BGBK_NAME,'') FROM BANK_GROUP WHERE A.BANKID = BGBK_ID) AS BANKHEAD, A.PCODE,
(CASE A.NEWRESULT WHEN 'A' THEN '成功' WHEN 'R' THEN '失敗' ELSE '未完成' END) AS RESULTSTATUS,
(SELECT COUNT(*) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS FIRECOUNT,
COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.SENDERHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS FIREAMT,
(SELECT COUNT(*) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS DEBITCOUNT,
COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.OUTHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS DEBITAMT,
(SELECT COUNT(*) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT) AS SAVECOUNT,
COALESCE((SELECT SUM(COALESCE(NEWTXAMT,0)) FROM TEMP AS B WHERE B.INHEAD = A.BANKID AND B.PCODE = A.PCODE AND B.NEWRESULT = A.NEWRESULT),0) AS SAVEAMT
FROM (
    SELECT DISTINCT SENDERHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP
    UNION
    SELECT DISTINCT OUTHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP
    UNION
    SELECT DISTINCT INHEAD AS BANKID, PCODE, NEWRESULT FROM TEMP
) AS A LEFT JOIN BANK_GROUP BG ON A.BANKID = BG.BGBK_ID
WHERE  A.PCODE = '2101'  AND  BG.OPBK_ID = '0040000'  AND  A.BANKID = '0040000'  AND  A.NEWRESULT = 'A'

--V1.0
WITH TEMP AS (     
    SELECT TXDATE,PCODE,COALESCE(SENDERACQUIRE,SENDERBANKID) SENDERACQUIRE,INACQUIRE,NEWRESULT,NEWTXAMT,RC1,RC2,RC3,RC4,RC5,RC6,SENDERHEAD     
    FROM VW_ONBLOCKTAB     
    WHERE  BIZDATE = '20150216' --��~�� 
    AND  PCODE = '2101' --����N��
    AND  (SENDERACQUIRE = '0040000' OR OUTACQUIRE = '0040000' OR INACQUIRE = '0040000') --�ާ@��
    AND  (SENDERHEAD = '0040000' OR OUTHEAD = '0040000' OR INHEAD = '0040000')  --�`��N�� 
    AND  CLEARINGPHASE = '01' --�M�ⶥ�q 
    AND  NEWRESULT = 'A' --�B�z���G
)
SELECT
A.PCODE || ( SELECT COALESCE(EACH_TXN_NAME,'') FROM EACH_TXN_CODE WHERE EACH_TXN_ID=A.PCODE) PCODE, --������O
A.SENDERACQUIRE || '-' || (SELECT COALESCE(BGBK_NAME,'') FROM BANK_GROUP WHERE BGBK_ID=A.SENDERACQUIRE) SENDERACQUIRE, --�o�ʦ�
A.SENDERHEAD || '-' || (SELECT COALESCE(BGBK_NAME,'') FROM BANK_GROUP WHERE BGBK_ID=A.SENDERHEAD) SENDERHEAD, --�o�ʦ�����`��
(CASE WHEN A.NEWRESULT='A' THEN '���\' WHEN A.NEWRESULT='R' THEN '����' WHEN A.NEWRESULT='P' THEN '������' ELSE 'NA' END) RESULTSTATUS,  --�B�z���G
COUNT(*) FIRECOUNT ,SUM(DECIMAL(A.NEWTXAMT)) FIREAMT, --�o�ʳ�쵧�ơB���B
(SELECT COUNT(*) FROM TEMP B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE = A.SENDERACQUIRE AND B.NEWRESULT=A.NEWRESULT  AND LENGTH(RC3)>0)  DEBITCOUNT, --���ڳ�쵧��
(SELECT SUM(DECIMAL(B.NEWTXAMT)) FROM TEMP B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE = A.SENDERACQUIRE AND B.NEWRESULT=A.NEWRESULT AND LENGTH(RC3)>0)  DEBITAMT, --���ڳ����B
(SELECT COUNT(*) FROM TEMP B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE = A.SENDERACQUIRE AND B.NEWRESULT=A.NEWRESULT  AND LENGTH(RC2)>0) SAVECOUNT, --�J�b��쵧��
(SELECT SUM(DECIMAL(B.NEWTXAMT)) FROM TEMP B WHERE B.PCODE=A.PCODE AND B.SENDERACQUIRE = A.SENDERACQUIRE AND B.NEWRESULT=A.NEWRESULT  AND LENGTH(RC2)>0) SAVEAMT --�J�b�����B
FROM TEMP A 
GROUP BY A.SENDERACQUIRE, A.SENDERHEAD, A.PCODE, A.NEWRESULT