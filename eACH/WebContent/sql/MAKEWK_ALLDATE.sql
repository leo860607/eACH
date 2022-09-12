CREATE PROCEDURE "EACHUSER"."MAKEWK_ALLDATE" (IN iTwYear INTEGER) LANGUAGE SQL
BEGIN
	DECLARE cTwYear         VARCHAR(4)	DEFAULT '';
    DECLARE iWestYear		INTEGER		DEFAULT 0;
	DECLARE westDate		CHAR(10)	DEFAULT '';
	DECLARE twDate			CHAR(10)	DEFAULT '';
	DECLARE dayOfWeek		INTEGER		DEFAULT 1;
    DECLARE cIS_TXN_DATE	CHAR(1)		DEFAULT 'Y';
 
    --民國年 yyyy
    SET cTwYear = RTRIM(CAST(CAST(iTwYear AS CHAR(4)) AS VARCHAR(4)));
    SET cTwYear = REPEAT('0', 4 - LENGTH(cTwYear)) || cTwYear;
    --民國年轉西元年 yyyy
    SET iWestYear = iTwYear + 1911;
    --初始化西元年
    SET westDate = CAST(iWestYear AS CHAR(4)) || '-01-01';
    --初始化民國年
    SET twDate = cTwYear || SUBSTR(westDate,6,2) || SUBSTR(westDate,9,2);
    
    WHILE(iWestYear = YEAR(westDate)) DO
    	SET dayOfWeek = DAYOFWEEK_ISO(westDate);
    
    	IF (dayOfWeek = 6 OR dayOfWeek = 7)
    	THEN SET cIS_TXN_DATE = 'N';
    	ELSE SET cIS_TXN_DATE = 'Y';
		END IF;
		
    	INSERT INTO "EACHUSER".WK_DATE_CALENDAR ( TXN_DATE ,WEEKDAY, IS_TXN_DATE ,CDATE ) Values (twDate , dayOfWeek, cIS_TXN_DATE , CURRENT_DATE);
        
        --SET westDate = CAST(DATE(westDate) + 1 DAY AS CHAR(10));
        SET westDate = VARCHAR_FORMAT(DATE(westDate) + 1 DAY, 'YYYY-MM-DD');
        SET twDate = cTwYear || SUBSTR(westDate,6,2) || SUBSTR(westDate,9,2);
	END WHILE;
END