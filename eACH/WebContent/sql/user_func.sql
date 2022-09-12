CREATE FUNCTION "EACHUSER"."DATE_DIFF" ( Dt1 VARCHAR(17), Dt2 VARCHAR(17) )
	RETURNS VARCHAR(10)
	LANGUAGE SQL
	--RETURN 'Hello World'

Begin ATOMIC
    Declare SecDiff  VARCHAR(10) default '0';
   
    if (length(Dt1) =17 and length(Dt2)=17) then
        set SecDiff=CHAR( timestampdiff (1, char( timestamp(substr(Dt1,1,4) || '-' || substr(Dt1,5,2) || '-' || substr(Dt1,7,2) || '-' || substr(Dt1,9,2)|| '.' || substr(Dt1,11,2) || '.' || substr(Dt1,13,2) || '.' || substr(Dt1,15,3) ) 
                                - 	timestamp(substr(Dt2,1,4) || '-' || substr(Dt2,5,2) || '-' || substr(Dt2,7,2) || '-' || substr(Dt2,9,2)|| '.' || substr(Dt2,11,2) || '.' || substr(Dt2,13,2) || '.' || substr(Dt2,15,3) ))) );

    end if;
    Return SecDiff;
End


CREATE FUNCTION "EACHUSER"."GETBKHEAD" ( BankId VARCHAR(7) )
	RETURNS VARCHAR(90)
	LANGUAGE SQL
Begin ATOMIC
Declare Rtn Varchar(90);
Declare iBankId VARCHAR(7);

SET Rtn = (select bgbk_id || '-' || coalesce(bgbk_name,'') from BANK_GROUP where bgbk_id=iBankId) ; 
if  Rtn is null then 
    SET iBankId=substr(BankId,1,3) || '0000'; 
    SET Rtn = (select bgbk_id || '-' || coalesce(bgbk_name,'') from BANK_GROUP where bgbk_id=iBankId) ; 
end if;
RETURN Rtn;
End

CREATE FUNCTION "EACHUSER"."GETBKHEADID" ( BankId VARCHAR(7) )
	RETURNS VARCHAR(90)
	LANGUAGE SQL
Begin ATOMIC
Declare Rtn Varchar(90);
Declare iBankId VARCHAR(7);

SET Rtn = (select bgbk_id  from BANK_GROUP where bgbk_id=iBankId) ; 
if  Rtn is null then 
    SET iBankId=substr(BankId,1,3) || '0000'; 
    SET Rtn = (select bgbk_id from BANK_GROUP where bgbk_id=iBankId) ; 
end if;
RETURN Rtn;
End

CREATE FUNCTION "EACHUSER"."GETBKNAME" ( iBankId VARCHAR(7) )
	RETURNS VARCHAR(90)
	LANGUAGE SQL
Begin ATOMIC
Declare Rtn Varchar(90);

SET Rtn = (select brbk_id || '-' || coalesce(brbk_name,'') from BANK_BRANCH where brbk_id=iBankId) ; 
if length( coalesce(Rtn,''))=0 then
    SET Rtn = (select bgbk_id || '-' || coalesce(bgbk_name,'') from BANK_GROUP where bgbk_id=iBankId) ; 
end if;
RETURN Rtn;
End


CREATE FUNCTION "EACHUSER"."TO_TWDATE" ( INVALUE VARCHAR(10) ) RETURNS VARCHAR(10) LANGUAGE SQL
BEGIN ATOMIC
    DECLARE dateObj DATE;
    SET dateObj = DATE( SUBSTR(INVALUE,1,4) || '-' || SUBSTR(INVALUE,5,2) || '-' || SUBSTR(INVALUE,7,2) );
    RETURN RIGHT(REPEAT('0',4) || RTRIM(CAST((YEAR( dateObj ) - 1911) AS CHAR(4))), 4) || '/' || CAST(MONTH( dateObj ) AS CHAR(2)) || '/' || CAST(DAY( dateObj ) AS CHAR(2));
END


CREATE FUNCTION "EACHUSER"."TO_TWDATETIME" ( INVALUE VARCHAR(14) ) RETURNS VARCHAR(20) LANGUAGE SQL
BEGIN ATOMIC
    DECLARE dateStr VARCHAR(10);
    DECLARE timeStr VARCHAR(8);
    DECLARE dateObj DATE;
    DECLARE timeObj TIME;
    DECLARE dateTime VARCHAR(20);

    SET dateStr =  SUBSTR(INVALUE,1,4) || '-' || SUBSTR(INVALUE,5,2) || '-' || SUBSTR(INVALUE,7,2);
    SET dateObj = DATE(dateStr);
    SET timeStr = SUBSTR(INVALUE,9,2) || ':' || SUBSTR(INVALUE,11,2) || ':' || SUBSTR(INVALUE,13,2);
        
    RETURN RIGHT(REPEAT('0',4) || RTRIM(CHAR((YEAR( dateObj ) - 1911))), 4) || '/' || REPLACE(RIGHT(VARCHAR(dateObj),5), '-', '/') || ' ' || timeStr;        
END

CREATE FUNCTION "EACHUSER"."ISNUMERIC" (source VARCHAR(40)) RETURNS VARCHAR(40) LANGUAGE SQL 
DETERMINISTIC 
NO EXTERNAL ACTION 
RETURN 
CASE 
WHEN translate(source,'','0123456789.-+') <> '' 
THEN source 
WHEN posstr(ltrim(source),'-') > 1 
OR posstr(ltrim(source),'+') > 1 
THEN source 
WHEN length(rtrim(ltrim(translate(source,'','0123456789 .')))) > 1 
OR length(rtrim(ltrim(translate(source,'','0123456789 -+')))) > 1 
THEN source 
WHEN posstr(ltrim(rtrim(translate(source,'','-+'))),' ') > 0 
THEN source 
WHEN translate(source,'','.-+') = '' 
THEN source 
ELSE STRIP(source,L,'0') 
END 