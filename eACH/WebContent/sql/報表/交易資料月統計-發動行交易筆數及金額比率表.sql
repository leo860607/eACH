with temp1 as ( SELECT * FROM EACHUSER.RPMONTHSUMTAB WHERE YYYYMM >= '201501'  AND YYYYMM <= '201506' AND OP_TYPE='S') 
--列出占所有交易比數超過1%的銀行    
(
    SELECT A.BGBK_ID, B.BGBK_NAME, A.CNT,  DECIMAL(( 100.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ),10,2)   AS  CNT_2, A.AMT,  DECIMAL(( 100.0 * A.AMT / (SELECT SUM(AMT) FROM temp1) ),10,2)   AS  AMT_2
        From ( 
              SELECT VARCHAR(BGBK_ID) BGBK_ID, sum(CNT) CNT, sum(AMT) AMT FROM temp1 Group by BGBK_ID
            ) A 
LEFT JOIN (
    SELECT DISTINCT  BGBK_ID, BGBK_NAME FROM temp1 WHERE BGBK_NAME IS NOT NULL
            ) B ON B.BGBK_ID=A.BGBK_ID
WHERE  ( 100.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ) > 1
)

UNION
--比率小於1%列其他    
(
    SELECT '其他'  AS BGBK_ID, '其他'  AS  BGBK_NAME, SUM(A.CNT) AS CNT,SUM(  DECIMAL(( 100.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ),10,2)  )  AS  CNT_2,SUM( A.AMT)  AS  AMT,SUM(  DECIMAL(( 100.0 * A.AMT / (SELECT SUM(AMT) FROM temp1) ),10,2) )  AS  AMT_2
        From ( 
              SELECT VARCHAR(BGBK_ID) BGBK_ID, sum(CNT) CNT, sum(AMT) AMT FROM temp1 Group by BGBK_ID
            ) A 
WHERE  ( 100.0 * A.CNT / (SELECT SUM(CNT) FROM temp1) ) < 1
)
Order by BGBK_ID