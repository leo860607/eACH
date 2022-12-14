--V5.0
WITH TEMP AS (
     SELECT
     DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1)) / CAST (1000000 AS BIGINT) AS TIME_1,
     DOUBLE(DATE_DIFF(DT_RSP_1, DT_REQ_2)) / CAST (1000000 AS BIGINT) AS TIME_2,
     (DOUBLE(DATE_DIFF(DT_REQ_2, DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_RSP_2, DT_RSP_1))) / CAST (1000000 AS BIGINT) AS TIME_3,
     (CASE C.TXN_TYPE WHEN '4' THEN (DOUBLE(DATE_DIFF(DT_RSP_3, DT_REQ_3)) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_4,
     (CASE C.TXN_TYPE WHEN '4' THEN ((DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1)) + DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3))) / CAST(1000000 AS BIGINT)) ELSE 0 END) AS TIME_5,
     C.*
     FROM (
         SELECT
         (CASE WHEN RC2 = '3001' THEN 1 ELSE 0 END) OKCOUNT,
         (CASE WHEN RC2 NOT IN ('3001','0601') THEN 1 ELSE 0 END) FAILCOUNT,
         (CASE WHEN RC2 = '0601' THEN 1 ELSE 0 END) PENDCOUNT,
         INTEGER(SUBSTR(A.TXDT,9,2)) AS HOURLAP, A.RESULTSTATUS,
         A.PCODE, SUBSTR(COALESCE(A.PCODE,'0000'),4,1) AS TXN_TYPE, A.RC2, A.SENDERSTATUS,
         COALESCE(A.DT_RSP_2, COALESCE(A.DT_RSP_1, COALESCE(A.DT_REQ_2, COALESCE(A.DT_REQ_1, 0)))) AS ENDTIME,
         (CASE WHEN LENGTH(COALESCE(A.DT_REQ_1,''))=0 THEN '0' ELSE A.DT_REQ_1 END ) DT_REQ_1,
         (CASE WHEN LENGTH(COALESCE(A.DT_REQ_2,''))=0 THEN '0' ELSE A.DT_REQ_2 END ) DT_REQ_2,
         (CASE WHEN LENGTH(COALESCE(A.DT_REQ_3,''))=0 THEN '0' ELSE A.DT_REQ_3 END ) DT_REQ_3,
         (CASE WHEN LENGTH(COALESCE(A.DT_RSP_1,''))=0 THEN '0' ELSE A.DT_RSP_1 END ) DT_RSP_1,
         (CASE WHEN LENGTH(COALESCE(A.DT_RSP_2,''))=0 THEN '0' ELSE A.DT_RSP_2 END ) DT_RSP_2,
         (CASE WHEN LENGTH(COALESCE(A.DT_RSP_3,''))=0 THEN '0' ELSE A.DT_RSP_3 END ) DT_RSP_3,
         A.CLEARINGPHASE, A.SENDERACQUIRE, A.OUTACQUIRE, A.INACQUIRE, A.SENDERHEAD, A.OUTHEAD, A.INHEAD
         FROM ONBLOCKTAB AS A
         WHERE ( A.RESULTSTATUS IN ('A', 'R') OR (A.RESULTSTATUS = 'P' AND A.SENDERSTATUS <> '1') ) AND COALESCE(DT_REQ_2,'') <> ''
 		 AND  A.BIZDATE= '20150828' AND A.PCODE = '2501'
        --AND (A.SENDERACQUIRE = '4720000' OR A.OUTACQUIRE = '4720000' OR A.INACQUIRE = '4720000')
        --AND (A.SENDERHEAD = '4720000' OR A.OUTHEAD = '4720000' OR A.INHEAD = '4720000')
     ) AS C
), TEMP_ AS (
     SELECT SENDERACQUIRE AS BANKID FROM TEMP GROUP BY SENDERACQUIRE
     UNION
     SELECT OUTACQUIRE FROM TEMP GROUP BY OUTACQUIRE
     UNION
     SELECT INACQUIRE FROM TEMP GROUP BY INACQUIRE 
), TEMP__ AS (
     SELECT
     (CASE WHEN RC2 <> '0601' AND TIME_1 > TXN_STD_PROC_TIME THEN TIME_1 ELSE 0 END) PRC_FLAG,
     (CASE TXN_TYPE
     WHEN '2' THEN (CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SC_STD_PROC_TIME THEN TIME_2 ELSE 0 END)
     WHEN '4' THEN (CASE WHEN RC2 <> '0601' AND DT_REQ_3 <> 0 AND TIME_4 > BANK_SC_STD_PROC_TIME THEN TIME_4 ELSE 0 END)
     ELSE 0 END) SAVE_FLAG,
     (CASE WHEN TXN_TYPE IN ('1','3','4') THEN (
         CASE WHEN RC2 <> '0601' AND TIME_2 > BANK_SD_STD_PROC_TIME THEN TIME_2 ELSE 0 END
     ) ELSE 0 END) AS DEBIT_FLAG,
     (CASE TXN_TYPE
     WHEN '4' THEN (CASE WHEN RC2 <> '0601' AND TIME_5 > TCH_STD_ECHO_TIME THEN TIME_5 ELSE 0 END)
     ELSE (CASE WHEN RC2 <> '0601' AND TIME_3 > TCH_STD_ECHO_TIME THEN TIME_3 ELSE 0 END)
     END) AS ACHPRC_FLAG, TEMP.*
     FROM TEMP, SYS_PARA
), TEMP___ AS (
     SELECT
     B.BANKID, EACHUSER.GETBKNAME(B.BANKID) AS BANKIDANDNAME,
     (SELECT SUM(OKCOUNT + FAILCOUNT + PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS TOTALCOUNT,
     (SELECT SUM(PENDCOUNT) FROM TEMP AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS PENDCOUNT,
     DECIMAL(COALESCE((SELECT SUM(PRC_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND PRC_FLAG <> 0),0),18,2) PRCTIME,
     DECIMAL(COALESCE((SELECT SUM(SAVE_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND SAVE_FLAG <> 0),0),18,2) SAVETIME,
     DECIMAL(COALESCE((SELECT SUM(DEBIT_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND DEBIT_FLAG <> 0),0),18,2) DEBITTIME,
     DECIMAL(COALESCE((SELECT SUM(ACHPRC_FLAG) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID) AND ACHPRC_FLAG <> 0),0),18,2) ACHPRCTIME,
     (SELECT SUM(CASE WHEN PRC_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS PRCCOUNT,
     (SELECT SUM(CASE WHEN SAVE_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS SAVECOUNT,
     (SELECT SUM(CASE WHEN DEBIT_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS DEBITCOUNT,
     (SELECT SUM(CASE WHEN ACHPRC_FLAG <> 0 THEN 1 ELSE 0 END) FROM TEMP__ AS A WHERE (A.SENDERACQUIRE = B.BANKID OR A.OUTACQUIRE = B.BANKID OR A.INACQUIRE = B.BANKID)) AS ACHPRCCOUNT
     FROM TEMP_ AS B
), TEMP____ AS (
     SELECT
     SUM(TOTALCOUNT) AS TOTALCOUNT, SUM(PENDCOUNT) AS PENDCOUNT,
     SUM(PRCCOUNT) AS PRCCOUNT, SUM(SAVECOUNT) AS SAVECOUNT,
     SUM(DEBITCOUNT) AS DEBITCOUNT, SUM(ACHPRCCOUNT) AS ACHPRCCOUNT,
     SUM(PRCTIME) AS PRCTIME, SUM(SAVETIME) AS SAVETIME,
     SUM(DEBITTIME) AS DEBITTIME, SUM(ACHPRCTIME) AS ACHPRCTIME
     FROM TEMP___ 
)

SELECT RSS.* FROM (
 	SELECT ROWNUMBER() OVER () AS ROWNUMBER, 
    BANKID, BANKIDANDNAME, TOTALCOUNT, PENDCOUNT,
    PRCCOUNT, (CASE WHEN PRCCOUNT > 0 THEN DECIMAL((PRCTIME / PRCCOUNT), 18, 2) ELSE 0 END) AS PRCTIME,
    SAVECOUNT, (CASE WHEN SAVECOUNT > 0 THEN DECIMAL((SAVETIME / SAVECOUNT), 18, 2) ELSE 0 END) AS SAVETIME,
    DEBITCOUNT, (CASE WHEN DEBITCOUNT > 0 THEN DECIMAL((DEBITTIME / DEBITCOUNT), 18, 2) ELSE 0 END) AS DEBITTIME,
    ACHPRCCOUNT, (CASE WHEN ACHPRCCOUNT > 0 THEN DECIMAL((ACHPRCTIME / ACHPRCCOUNT), 18, 2) ELSE 0 END) AS ACHPRCTIME
    FROM TEMP___ AS RS 
) AS RSS 
WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 10

--V4.0
WITH TEMP AS (
    SELECT * FROM (
        SELECT SENDERACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, PCODE, NEWRESULT AS RESULTSTATUS,  
        (case when length(COALESCE(DT_Req_1,''))=0  then '0' else DT_Req_1 end ) DT_Req_1,  
        (case when length(COALESCE(DT_Req_2,''))=0  then '0' else DT_Req_2 end ) DT_Req_2,
        (case when length(COALESCE(DT_Req_3,''))=0  then '0' else DT_Req_3 end ) DT_Req_3,
        (case when length(COALESCE(DT_Rsp_1,''))=0  then '0' else DT_Rsp_1 end ) DT_Rsp_1,  
        (case when length(COALESCE(DT_Rsp_2,''))=0  then '0' else DT_Rsp_2 end ) DT_Rsp_2,
        (case when length(COALESCE(DT_Rsp_3,''))=0  then '0' else DT_Rsp_3 end ) DT_Rsp_3,
        (case when length(COALESCE(DT_Con_1,''))=0  then '0' else DT_Con_1 end ) DT_Con_1,  
        (case when length(COALESCE(DT_Con_2,''))=0  then '0' else DT_Con_2 end ) DT_Con_2, 
        (case when length(COALESCE(DT_Con_3,''))=0  then '0' else DT_Con_3 end ) DT_Con_3,
        (case when length(COALESCE(DT_Con_2,'')) = 0 then
            (case when length(COALESCE(DT_Con_1,'')) = 0 then
                (case when length(COALESCE(DT_Rsp_2,'')) = 0 then
                    (case when length(COALESCE(DT_Rsp_1,'')) = 0 then
                        (case when length(COALESCE(DT_Req_2,'')) = 0 then COALESCE(DT_Req_1,'') else COALESCE(DT_Req_2,'') end)
                        else COALESCE(DT_Rsp_1,'') end)
                else COALESCE(DT_Rsp_2,'') end)
            else COALESCE(DT_Con_1,'') end)
        else COALESCE(DT_Con_2,'') end) EndTime  
        FROM VW_ONBLOCKTAB
        WHERE LENGTH(COALESCE(TIMEOUTCODE, '')) = 0 
        AND  (SENDERACQUIRE= '4720000' OR OUTACQUIRE= '4720000' OR INACQUIRE= '4720000')  
        AND  (SENDERHEAD= '4720000' OR OUTHEAD= '4720000' OR INHEAD= '4720000')
        AND  BIZDATE > '20150101'
    ) AS A LEFT JOIN EACH_TXN_CODE C ON C.EACH_TXN_ID = A.PCODE
    WHERE C.BUSINESS_TYPE_ID IN ('2100')
)

SELECT
A.BANKID || '-' || (SELECT coalesce(BGBK_NAME,'') from bank_group where bgbk_id=A.BANKID) SENDERACQUIRE,
(SELECT COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS FIRECOUNT,
(SELECT (SUM(DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS AVGTIME,
(SELECT SUM(
    (CASE WHEN PCODE LIKE '2%4'
    THEN (DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1))) / 2	
    ELSE DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1))
    END)
) / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID) GROUP BY 'G') AS ACHAVGTIME,
(SELECT SUM(
    (CASE WHEN PCODE LIKE '2%4'
    THEN (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3)))
    ELSE (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_1)))
    END)
) / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID) GROUP BY 'G') AS ACHSAVETIME,
(SELECT (SUM(DOUBLE(DATE_DIFF(DT_CON_2,DT_CON_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS ACHDEBITTIME,
COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE INHEAD = A.BANKID),0) AS INSAVETIME,
COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE OUTHEAD = A.BANKID),0) AS OUTDEBITTIME,
(SELECT TCH_STD_ECHO_TIME FROM SYS_PARA) TCH_STD_ECHO_TIME,
(SELECT PARTY_STD_ECHO_TIME FROM SYS_PARA) PARTY_STD_ECHO_TIME,
(SELECT TXN_STD_PROC_TIME FROM SYS_PARA) TXN_STD_PROC_TIME
FROM (
    SELECT * FROM (
        SELECT ROWNUMBER() OVER( ) AS ROWNUMBER, T.* FROM (
            SELECT SENDERHEAD AS BANKID FROM TEMP GROUP BY SENDERHEAD
            UNION
            SELECT OUTHEAD FROM TEMP GROUP BY OUTHEAD
            UNION
            SELECT INHEAD FROM TEMP GROUP BY INHEAD
        ) AS T
        WHERE (SELECT OPBK_ID FROM BANK_OPBK WHERE BGBK_ID = T.BANKID AND OPBK_ID = '4720000') = '4720000' AND T.BANKID = '4720000'
    ) WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 10
) AS A

--V3.0
WITH TEMP AS (
    SELECT * FROM (
        SELECT SENDERACQUIRE, SENDERHEAD, OUTHEAD, INHEAD, PCODE, NEWRESULT AS RESULTSTATUS,  
        (case when length(COALESCE(DT_Req_1,''))=0  then '0' else DT_Req_1 end ) DT_Req_1,  
        (case when length(COALESCE(DT_Req_2,''))=0  then '0' else DT_Req_2 end ) DT_Req_2,
        (case when length(COALESCE(DT_Req_3,''))=0  then '0' else DT_Req_3 end ) DT_Req_3,
        (case when length(COALESCE(DT_Rsp_1,''))=0  then '0' else DT_Rsp_1 end ) DT_Rsp_1,  
        (case when length(COALESCE(DT_Rsp_2,''))=0  then '0' else DT_Rsp_2 end ) DT_Rsp_2,
        (case when length(COALESCE(DT_Rsp_3,''))=0  then '0' else DT_Rsp_3 end ) DT_Rsp_3,
        (case when length(COALESCE(DT_Con_1,''))=0  then '0' else DT_Con_1 end ) DT_Con_1,  
        (case when length(COALESCE(DT_Con_2,''))=0  then '0' else DT_Con_2 end ) DT_Con_2, 
        (case when length(COALESCE(DT_Con_3,''))=0  then '0' else DT_Con_3 end ) DT_Con_3,
        (case when length(COALESCE(DT_Con_2,'')) = 0 then
            (case when length(COALESCE(DT_Con_1,'')) = 0 then
                (case when length(COALESCE(DT_Rsp_2,'')) = 0 then
                    (case when length(COALESCE(DT_Rsp_1,'')) = 0 then
                        (case when length(COALESCE(DT_Req_2,'')) = 0 then COALESCE(DT_Req_1,'') else COALESCE(DT_Req_2,'') end)
                        else COALESCE(DT_Rsp_1,'') end)
                else COALESCE(DT_Rsp_2,'') end)
            else COALESCE(DT_Con_1,'') end)
        else COALESCE(DT_Con_2,'') end) EndTime  
        FROM VW_ONBLOCKTAB
        WHERE LENGTH(COALESCE(TIMEOUTCODE, '')) = 0 
        AND  (SENDERACQUIRE= '4720000' OR OUTACQUIRE= '4720000' OR INACQUIRE= '4720000')  
        AND  (SENDERHEAD= '4720000' OR OUTHEAD= '4720000' OR INHEAD= '4720000')
        AND  BIZDATE > '20150101'
    ) AS A LEFT JOIN EACH_TXN_CODE C ON C.EACH_TXN_ID = A.PCODE
    WHERE C.BUSINESS_TYPE_ID IN ('2100')
)

SELECT
A.BANKID || '-' || (SELECT coalesce(BGBK_NAME,'') from bank_group where bgbk_id=A.BANKID) SENDERACQUIRE,
(SELECT COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS FIRECOUNT,
(SELECT (SUM(DOUBLE(DATE_DIFF(ENDTIME, DT_REQ_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS AVGTIME,
(SELECT SUM(
    (CASE WHEN PCODE LIKE '2%4'
    THEN (DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1)) + DOUBLE(DATE_DIFF(DT_REQ_3,DT_RSP_1))) / 2	
    ELSE DOUBLE(DATE_DIFF(DT_REQ_2,DT_REQ_1))
    END)
) / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID) GROUP BY 'G') AS ACHAVGTIME,
(SELECT SUM(
    (CASE WHEN PCODE LIKE '2%4'
    THEN (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_3)))
    ELSE (DOUBLE(DATE_DIFF(DT_RSP_2,DT_RSP_1)))
    END)
) / 1000000 / COUNT(*) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID) GROUP BY 'G') AS ACHSAVETIME,
(SELECT (SUM(DOUBLE(DATE_DIFF(DT_CON_2,DT_CON_1))) / 1000000 / COUNT(*)) FROM TEMP WHERE (SENDERHEAD = A.BANKID OR OUTHEAD = A.BANKID OR INHEAD = A.BANKID)) AS ACHDEBITTIME,
COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE INHEAD = A.BANKID),0) AS INSAVETIME,
COALESCE((SELECT (SUM(DOUBLE(DATE_DIFF(DT_RSP_1,DT_REQ_2))) / 1000000 / COUNT(*)) FROM TEMP WHERE OUTHEAD = A.BANKID),0) AS OUTDEBITTIME,
(SELECT TCH_STD_ECHO_TIME FROM SYS_PARA) TCH_STD_ECHO_TIME,
(SELECT PARTY_STD_ECHO_TIME FROM SYS_PARA) PARTY_STD_ECHO_TIME,
(SELECT TXN_STD_PROC_TIME FROM SYS_PARA) TXN_STD_PROC_TIME
FROM (
    SELECT * FROM (
        SELECT ROWNUMBER() OVER( ) AS ROWNUMBER, T.* FROM (
            SELECT SENDERHEAD AS BANKID FROM TEMP GROUP BY SENDERHEAD
            UNION
            SELECT OUTHEAD FROM TEMP GROUP BY OUTHEAD
            UNION
            SELECT INHEAD FROM TEMP GROUP BY INHEAD
        ) AS T
        WHERE T.BANKID = '4720000'
    ) WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 10
) AS A

--V2.0
WITH TEMP AS (  
    SELECT a.senderAcquire, A.SENDERHEAD, A.OUTHEAD, A.INHEAD, a.NEWRESULT RESULTSTATUS,  
    (case when length(COALESCE(a.DT_Req_1,''))=0  then '0' else a.DT_Req_1 end ) DT_Req_1,  
    (case when length(COALESCE(a.DT_Req_2,''))=0  then '0' else a.DT_Req_2 end ) DT_Req_2,  
    (case when length(COALESCE(a.DT_Rsp_1,''))=0  then '0' else a.DT_Rsp_1 end ) DT_Rsp_1,  
    (case when length(COALESCE(a.DT_Rsp_2,''))=0  then '0' else a.DT_Rsp_2 end ) DT_Rsp_2,  
    (case when length(COALESCE(a.DT_Con_1,''))=0  then '0' else a.DT_Con_1 end ) DT_Con_1,  
    (case when length(COALESCE(a.DT_Con_2,''))=0  then '0' else a.DT_Con_2 end ) DT_Con_2,  
    (case when length(COALESCE(DT_Con_2,'')) = 0 then
        (case when length(COALESCE(a.DT_Con_1,'')) = 0 then
            (case when length(COALESCE(a.DT_Rsp_2,'')) = 0 then
                (case when length(COALESCE(a.DT_Rsp_1,'')) = 0 then
                    (case when length(COALESCE(a.DT_Req_2,'')) = 0 then COALESCE(a.DT_Req_1,'') else COALESCE(a.DT_Req_2,'') end)
                    else COALESCE(a.DT_Rsp_1,'') end)
            else COALESCE(a.DT_Rsp_2,'') end)
        else COALESCE(a.DT_Con_1,'') end)
    else COALESCE(a.DT_Con_2,'') end) EndTime  
    FROM VW_ONBLOCKTAB  a   
    left join EACH_TXN_CODE c on c.each_txn_id = a.pcode  
    WHERE a.NEWRESULT = 'A' AND  a.BIZDATE= '20150306'  
    AND  (a.SENDERACQUIRE= '4720000' OR a.OUTACQUIRE= '4720000' OR a.INACQUIRE= '4720000')  
    AND  (a.SENDERHEAD= '4720000' OR a.OUTHEAD= '4720000' OR a.INHEAD= '4720000')  
    AND  c.business_type_id IN('2100')  
) 
SELECT * FROM ( 
    SELECT ROWNUMBER() OVER( ) AS ROWNUMBER, 
    a.SENDERHEAD || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.SENDERHEAD) SENDERACQUIRE,   
    count(*) AS FIRECOUNT,   (SUM(Double(Date_Diff( a.EndTime, a.DT_Req_1))) / 1000000 / COUNT(*)) AS AVGTIME,   
    (SUM(Double(Date_Diff(a.DT_Req_2,a.DT_Req_1))) + SUM(Double(Date_Diff(a.DT_Rsp_2,a.DT_Rsp_1))) + SUM(Double(Date_Diff(a.DT_Con_2,a.DT_Con_1)))) / 1000000 / COUNT(*) ACHAVGTIME,   
    (SUM(Double(Date_Diff(a.DT_Con_2,a.DT_Con_1))) / 1000000 / COUNT(*)) AS ACHSAVETIME,   
    (SUM(Double(Date_Diff(a.DT_Rsp_2,a.DT_Rsp_1))) / 1000000 / COUNT(*)) AS ACHDEBITTIME,   
    (SUM(Double(Date_Diff(a.DT_Con_1,a.DT_Rsp_2))) / 1000000 / COUNT(*)) AS INSAVETIME,   
    (SUM(Double(Date_Diff(a.DT_Rsp_1,a.DT_Req_2))) / 1000000 / COUNT(*)) AS OUTDEBITTIME,   
    (select TCH_STD_ECHO_TIME from SYS_PARA ) TCH_STD_ECHO_TIME,   
    (select PARTY_STD_ECHO_TIME from SYS_PARA ) PARTY_STD_ECHO_TIME,   
    (select TXN_STD_PROC_TIME from SYS_PARA) TXN_STD_PROC_TIME   
    FROM TEMP a   
    group by a.SENDERACQUIRE, A.SENDERHEAD  
) AS TEMP_ WHERE ROWNUMBER >= 1 AND ROWNUMBER <= 10

--V1.0
WITH TEMP AS (  
    SELECT a.senderAcquire,  a.NEWRESULT RESULTSTATUS,  
    (case when length(a.DT_Req_1)=0  then '0' else a.DT_Req_1 end ) DT_Req_1,  
    (case when length(a.DT_Req_2)=0  then '0' else a.DT_Req_2 end ) DT_Req_2,  
    (case when length(a.DT_Rsp_1)=0  then '0' else a.DT_Rsp_1 end ) DT_Rsp_1,  
    (case when length(a.DT_Rsp_2)=0  then '0' else a.DT_Rsp_2 end ) DT_Rsp_2,  
    (case when length(a.DT_Con_1)=0  then '0' else a.DT_Con_1 end ) DT_Con_1,  
    (case when length(a.DT_Con_2)=0  then '0' else a.DT_Con_2 end ) DT_Con_2,  
    (case when   	length(DT_Con_2) =0 then
        (case when  	   length(a.DT_Con_1)=0 then
             (case when length( a.DT_Rsp_2)=0 then
               (case when  length(a.DT_Rsp_1) =0 then
                  (case when length(a.DT_Req_2) =0 then a.DT_Req_1 else a.DT_Req_2 end)
                else a.DT_Rsp_1 end )
            else a.DT_Rsp_2 end )
        else a.DT_Con_1 end)
    else a.DT_Con_2 end) EndTime    
    FROM VW_ONBLOCKTAB  a   left join EACH_TXN_CODE c on c.each_txn_id = a.pcode  
    WHERE   
    a.BIZDATE= '20150216' --??????~?????? 
    AND  a.SENDERACQUIRE= '0040000' --?????@?????? 
    AND  c.business_type_id IN('2100') --???~???????????????O 
)
SELECT
a.senderAcquire || '-' || (select coalesce(bgbk_name,'') from bank_group where bgbk_id=a.senderAcquire) SENDERACQUIRE, --?????@?????? 
count(*)  FIRECOUNT, --?????????????????????  
SUM(Double(Date_Diff( a.EndTime, a.DT_Req_1))) / (1000000 *count(*) )  AVGTIME, --?????????????????????B???z?????????????????????????(??????) 
(SUM(Double(Date_Diff(a.DT_Req_2,a.DT_Req_1)))+SUM(Double(Date_Diff(a.DT_Rsp_2,a.DT_Rsp_1)))+SUM(Double(Date_Diff(a.DT_Con_2,a.DT_Con_1))))  / (1000000*count(*)) ACHAVGTIME,   --???????????????????????q???????????????????????????????(??????)
SUM(Double(Date_Diff(a.DT_Con_2,a.DT_Con_1))) / (1000000 *count(*) )  ACHSAVETIME, --???????????J???b???^???????????????????????????????(??????)  
SUM(Double(Date_Diff(a.DT_Rsp_2,a.DT_Rsp_1))) / (1000000 *count(*) )  ACHDEBITTIME, --?????????????????b???^???????????????????????????????(??????)  
SUM(Double(Date_Diff(a.DT_Con_1,a.DT_Rsp_2))) / (1000000 *count(*) )  INSAVETIME, --???J???b??????^???????????????????????????????(??????)  
SUM(Double(Date_Diff(a.DT_Rsp_1,a.DT_Req_2))) / (1000000 *count(*) )  OUTDEBITTIME, --??????????????^???????????????????????????????(??????)  
(select TCH_STD_ECHO_TIME from SYS_PARA ) TCH_STD_ECHO_TIME,   
(select PARTY_STD_ECHO_TIME from SYS_PARA ) PARTY_STD_ECHO_TIME,   
(select TXN_STD_PROC_TIME from SYS_PARA) TXN_STD_PROC_TIME   
FROM TEMP a   
Where a.resultstatus = 'A'   
group by a.SENDERACQUIRE