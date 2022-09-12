package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;

public class BASEDATA_DOWNLOAD_BO{
	private CodeUtils codeUtils;
	
	public String getSQL(String functionName){
		String SQL = "";
		
		if(functionName.equals("bgbk")){
			SQL = "SELECT A.BGBK_ID,A.BGBK_NAME,A.OPBK_ID,TRANS_DATE(A.ACTIVE_DATE,'W','') AS ACTIVE_DATE,TRANS_DATE(A.STOP_DATE,'W','') AS STOP_DATE,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2100'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME1,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2200'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME2,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2300'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME3,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2500'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME4,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2800'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME5,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2700'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME6,'     ' AS BLANK FROM BANK_GROUP A  WHERE  A.BGBK_ATTR!='5' AND  A.BGBK_ATTR!='6' ORDER BY A.BGBK_ID,A.BGBK_NAME";
		}
		if(functionName.equals("brbk")){
//			20150921 edit by hugo req byUAT-20150819-02
			SQL = " SELECT BR.BGBK_ID,BR.BRBK_ID,BR.BRBK_NAME,TRANS_DATE(BR.ACTIVE_DATE,'W','') AS ACTIVE_DATE,TRANS_DATE(BR.STOP_DATE,'W','') AS STOP_DATE FROM BANK_BRANCH  BR ";
			SQL += " JOIN BANK_GROUP  BG ON BR.BGBK_ID = BG.BGBK_ID ";
			SQL += " ORDER BY BR.BGBK_ID,BR.BRBK_ID ";
//			SQL = "SELECT BGBK_ID,BRBK_ID,BRBK_NAME,TRANS_DATE(ACTIVE_DATE,'W','') AS ACTIVE_DATE,TRANS_DATE(STOP_DATE,'W','') AS STOP_DATE FROM BANK_BRANCH ORDER BY BGBK_ID,BRBK_ID";
		}
		if(functionName.equals("sd")){
//			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.USER_NO,TRANS_DATE(A.ACTIVE_DATE,'W','') AS ACTIVE_DATE FROM SD_COMPANY_PROFILE A LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID LEFT JOIN BANK_BRANCH C ON A.SND_BRBK_ID=C.BRBK_ID ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME";
			//加入檢核是否在總行、分行檔內
			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.USER_NO,TRANS_DATE(A.ACTIVE_DATE,'W','') AS ACTIVE_DATE FROM SD_COMPANY_PROFILE A LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID JOIN (SELECT * FROM BANK_BRANCH A JOIN BANK_GROUP B ON A.BGBK_ID=B.BGBK_ID) C ON A.SND_BRBK_ID=C.BRBK_ID ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME";
		}
		if(functionName.equals("sc")){
//			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.IPO_COMPANY_ID,TRANS_DATE(A.PROFIT_ISSUE_DATE,'W','') AS PROFIT_ISSUE_DATE FROM SC_COMPANY_PROFILE A LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID LEFT JOIN BANK_BRANCH C ON A.SND_BRBK_ID=C.BRBK_ID ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME";
			//加入檢核是否在總行、分行檔內
			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.IPO_COMPANY_ID,TRANS_DATE(A.PROFIT_ISSUE_DATE,'W','') AS PROFIT_ISSUE_DATE FROM SC_COMPANY_PROFILE A LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID JOIN (SELECT * FROM BANK_BRANCH A JOIN BANK_GROUP B ON A.BGBK_ID=B.BGBK_ID) C ON A.SND_BRBK_ID=C.BRBK_ID WHERE A.IS_SHORT = '' OR A.IS_SHORT IS NULL ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME";
		}
		if(functionName.equals("sdnw")){
//			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.USER_NO,TRANS_DATE(A.ACTIVE_DATE,'W','') AS ACTIVE_DATE FROM SD_COMPANY_PROFILE A LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID LEFT JOIN BANK_BRANCH C ON A.SND_BRBK_ID=C.BRBK_ID ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME";
			//加入檢核是否在總行、分行檔內
			SQL = " SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.USER_NO,TRANS_DATE(A.ACTIVE_DATE,'W','') AS ACTIVE_DATE, " + 
					" COALESCE(( " + 
					"    CASE WHEN A.FEE_TYPE <>' ' THEN A.FEE_TYPE ELSE " + 
					"    ( " + 
					"       SELECT (CASE WHEN HANDLECHARGE = 0  THEN 'A' WHEN HANDLECHARGE > 0 THEN 'B' END) AS FEE_TYPE  FROM FEE_CODE WHERE FEE_ID=A.TXN_ID AND START_DATE <= ( SELECT TRANS_DATE(THISDATE ,'T','') FROM EACHUSER.EACHSYSSTATUSTAB )  ORDER BY START_DATE DESC FETCH FIRST 1 ROW ONLY " + 
					"    ) " + 
					" END ) ,'X') AS FEE_TYPE " + 
					" FROM SD_COMPANY_PROFILE A " + 
					" LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID " + 
					" JOIN (SELECT * FROM BANK_BRANCH A JOIN BANK_GROUP B ON A.BGBK_ID=B.BGBK_ID) C ON A.SND_BRBK_ID=C.BRBK_ID " + 
					" ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME ";
		}
		if(functionName.equals("scnw")){
//			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.IPO_COMPANY_ID,TRANS_DATE(A.PROFIT_ISSUE_DATE,'W','') AS PROFIT_ISSUE_DATE FROM SC_COMPANY_PROFILE A LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID LEFT JOIN BANK_BRANCH C ON A.SND_BRBK_ID=C.BRBK_ID ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME";
			//加入檢核是否在總行、分行檔內
			SQL = "SELECT A.COMPANY_ID,A.COMPANY_ABBR_NAME,A.COMPANY_NAME,A.TXN_ID,B.TXN_NAME,A.SND_BRBK_ID,C.BRBK_NAME,A.IPO_COMPANY_ID,TRANS_DATE(A.PROFIT_ISSUE_DATE,'W','') AS PROFIT_ISSUE_DATE, " + 
					" COALESCE((" + 
					"    CASE WHEN A.FEE_TYPE <>' ' THEN A.FEE_TYPE ELSE " + 
					"    ( " + 
					"       SELECT (CASE WHEN HANDLECHARGE = 0  THEN 'A' WHEN HANDLECHARGE > 0 THEN 'B' END) AS FEE_TYPE  FROM FEE_CODE WHERE FEE_ID=A.TXN_ID AND START_DATE <= ( SELECT TRANS_DATE(THISDATE ,'T','') FROM EACHUSER.EACHSYSSTATUSTAB )  ORDER BY START_DATE DESC FETCH FIRST 1 ROW ONLY " + 
					"    ) " + 
					" END ) ,'X') AS FEE_TYPE " + 
					" FROM SC_COMPANY_PROFILE A " + 
					" LEFT JOIN TXN_CODE B ON A.TXN_ID=B.TXN_ID " + 
					" JOIN (SELECT * FROM BANK_BRANCH A JOIN BANK_GROUP B ON A.BGBK_ID=B.BGBK_ID) C ON A.SND_BRBK_ID=C.BRBK_ID " + 
					" WHERE A.IS_SHORT = '' OR A.IS_SHORT IS NULL ORDER BY A.COMPANY_ID,A.COMPANY_ABBR_NAME ";
		}
		if(functionName.equals("biztype")){
//			20151112 edit by hugo req by UAT-20151027-01
//			SQL = "SELECT BUSINESS_TYPE_ID,BUSINESS_TYPE_NAME FROM BUSINESS_TYPE ORDER BY BUSINESS_TYPE_ID";
			SQL = "SELECT BUSINESS_TYPE_ID,BUSINESS_TYPE_NAME FROM BUSINESS_TYPE WHERE BUSINESS_TYPE_ID LIKE '2%'  ORDER BY BUSINESS_TYPE_ID";
		}
		if(functionName.equals("txncode")){
			SQL = "SELECT TXN_ID,TXN_NAME,BUSINESS_TYPE_ID,TRANS_DATE(ACTIVE_DATE,'W','') AS ACTIVE_DATE FROM TXN_CODE ORDER BY TXN_ID";
		}
		if(functionName.equals("txnerrorcode")){
//			20160204 edit by hugo 銀行端要過濾掉42XX的資料 req by SRS20160122
//			SQL = "SELECT ERROR_ID,ERR_DESC FROM TXN_ERROR_CODE WHERE ERROR_ID!='0000' ORDER BY ERROR_ID";
			SQL = "SELECT ERROR_ID,ERR_DESC FROM TXN_ERROR_CODE WHERE ERROR_ID!='0000' AND ERROR_ID NOT LIKE '42%'  ORDER BY ERROR_ID";
		}
		if(functionName.equals("feecode")){
//			SQL = "SELECT A.FEE_ID,B.TXN_NAME,(CASE WHEN A.SND_BANK_FEE < 0 THEN '-' ELSE '+' END) SND_BANK_FEE_SIGN,ABS(A.SND_BANK_FEE) SND_BANK_FEE,(CASE WHEN A.OUT_BANK_FEE < 0 THEN '-' ELSE '+' END) OUT_BANK_FEE_SIGN,ABS(A.OUT_BANK_FEE) OUT_BANK_FEE,(CASE WHEN A.IN_BANK_FEE < 0 THEN '-' ELSE '+' END) IN_BANK_FEE_SIGN,ABS(A.IN_BANK_FEE) IN_BANK_FEE,(CASE WHEN A.TCH_FEE < 0 THEN '-' ELSE '+' END) TCH_FEE_SIGN,ABS(A.TCH_FEE) TCH_FEE,TRANS_DATE(A.START_DATE,'W','') AS START_DATE FROM FEE_CODE A LEFT JOIN TXN_CODE B ON A.FEE_ID=B.TXN_ID WHERE LENGTH(TRIM(FEE_ID))=3 ORDER BY FEE_ID";
			SQL = "SELECT A.FEE_ID,B.TXN_NAME,(CASE WHEN A.SND_BANK_FEE < 0 THEN '-' ELSE '+' END) SND_BANK_FEE_SIGN,ABS(A.SND_BANK_FEE) SND_BANK_FEE,(CASE WHEN A.OUT_BANK_FEE < 0 THEN '-' ELSE '+' END) OUT_BANK_FEE_SIGN,ABS(A.OUT_BANK_FEE) OUT_BANK_FEE,(CASE WHEN A.IN_BANK_FEE < 0 THEN '-' ELSE '+' END) IN_BANK_FEE_SIGN,ABS(A.IN_BANK_FEE) IN_BANK_FEE,(CASE WHEN A.WO_BANK_FEE < 0 THEN '-' ELSE '+' END) WO_BANK_FEE_SIGN,ABS(A.WO_BANK_FEE) WO_BANK_FEE,(CASE WHEN A.TCH_FEE < 0 THEN '-' ELSE '+' END) TCH_FEE_SIGN,ABS(A.TCH_FEE) TCH_FEE ,(CASE WHEN A.HANDLECHARGE < 0 THEN '-' ELSE '+' END) HANDLECHARGE_SIGN,ABS(A.HANDLECHARGE) HANDLECHARGE , TRANS_DATE(A.START_DATE,'W','') AS START_DATE FROM FEE_CODE A LEFT JOIN TXN_CODE B ON A.FEE_ID=B.TXN_ID WHERE LENGTH(TRIM(FEE_ID))=3 ORDER BY FEE_ID";
		}
		//以前的檔案，現已不需要
//		if(functionName.equals("chg_sc_profile")){
//			SQL = "SELECT SD_ITEM_NO, COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME, TXN_ID, INBANKID, " +
//			"COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = INBANKID),'') AS INBANKNAME, " +
//			"INBANKACCTNO, LAYOUTID, DEALY_CHARGE_DAY, " +
//			"VARCHAR_FORMAT(START_DATE, 'YYYYMMDD') AS START_DATE, " +
//			"VARCHAR_FORMAT(STOP_DATE, 'YYYYMMDD') AS STOP_DATE, NOTE " +
//			"FROM EACHUSER.CHANGE_SC_PROFILE";
//		}
		if(functionName.equals("chg_company_profile")){
			SQL = "SELECT PI.PI_COMPANY_ID, PI.PI_COMPANY_ABBR_NAME,PI.PI_COMPANY_NAME,PI.BILL_TYPE_ID,PI.TXN_ID,FEE.HANDLECHARGE,PI.TYPE_AUZ,PI.TYPE_CARD,PI.TYPE_USER_ACCT,PI.START_DATE,PI.STOP_DATE,(CASE WHEN PI.PI_COMPANY_ID = WO.WO_COMPANY_ID THEN 'Y' ELSE NULL END) IS_WO,WO.INBANK_ID,WO.WO_COMPANY_ABBR_NAME,WO.INBANK_ACCT_NO,WO.TYPE_ACCT,WO.TYPE_WRITE_OFF_NO,WO.TYPE_BARCODE,WO.VIRTUAL_ACC_NOTE,WO.SD_ITEM_NO,WO.FMT_ID FROM PI_COMPANY_PROFILE PI LEFT JOIN WO_COMPANY_PROFILE WO ON ( PI.PI_COMPANY_ID = WO.WO_COMPANY_ID AND PI.BILL_TYPE_ID = WO.BILL_TYPE_ID )LEFT JOIN FEE_CODE FEE ON ( FEE.FEE_ID = PI.TXN_ID AND FEE.START_DATE = PI.START_DATE AND FEE.FEE_ID = WO.TXN_ID) ORDER BY PI.PI_COMPANY_ID";
		}
		
		if(functionName.equals("feecodenw")){
//			SQL = "SELECT A.FEE_ID,B.TXN_NAME,(CASE WHEN A.SND_BANK_FEE < 0 THEN '-' ELSE '+' END) SND_BANK_FEE_SIGN,ABS(A.SND_BANK_FEE) SND_BANK_FEE,(CASE WHEN A.OUT_BANK_FEE < 0 THEN '-' ELSE '+' END) OUT_BANK_FEE_SIGN,ABS(A.OUT_BANK_FEE) OUT_BANK_FEE,(CASE WHEN A.IN_BANK_FEE < 0 THEN '-' ELSE '+' END) IN_BANK_FEE_SIGN,ABS(A.IN_BANK_FEE) IN_BANK_FEE,(CASE WHEN A.TCH_FEE < 0 THEN '-' ELSE '+' END) TCH_FEE_SIGN,ABS(A.TCH_FEE) TCH_FEE,TRANS_DATE(A.START_DATE,'W','') AS START_DATE FROM FEE_CODE A LEFT JOIN TXN_CODE B ON A.FEE_ID=B.TXN_ID WHERE LENGTH(TRIM(FEE_ID))=3 ORDER BY FEE_ID";
			SQL = "(SELECT FCN.FEE_ID , COALESCE(TC.TXN_NAME,'') AS TXN_NAME  ,FCN.FEE_TYPE, FCN.FEE_UID, " + 
					"COALESCE(FCL.FEE_DTNO,'-') AS FEE_DTNO,  " + 
					"COALESCE(FCL.FEE_LVL_BEG_AMT,'0000000000000') AS FEE_LVL_BEG_AMT ,  " + 
					"COALESCE(FCL.FEE_LVL_END_AMT,'0000000000000') AS FEE_LVL_END_AMT,  " + 
					"(CASE WHEN FCN.FEE_TYPE='C' OR FCL.FEE_LVL_TYPE='2' THEN '2' ELSE  '1' END ) AS FEE_LVL_TYPE, " + 
					"(CASE WHEN COALESCE(FCN.SND_BANK_FEE,FCL.SND_BANK_FEE) < 0 THEN '-' ELSE '+' END) SND_BANK_FEE_SIGN,ABS(COALESCE(FCN.SND_BANK_FEE,FCL.SND_BANK_FEE)) SND_BANK_FEE,  " + 
					"(CASE WHEN COALESCE(FCN.OUT_BANK_FEE,FCL.OUT_BANK_FEE) < 0 THEN '-' ELSE '+' END) OUT_BANK_FEE_SIGN,ABS(COALESCE(FCN.OUT_BANK_FEE,FCL.OUT_BANK_FEE)) OUT_BANK_FEE,  " + 
					"(CASE WHEN COALESCE(FCN.IN_BANK_FEE,FCL.IN_BANK_FEE) < 0 THEN '-' ELSE '+' END) IN_BANK_FEE_SIGN,ABS(COALESCE(FCN.IN_BANK_FEE,FCL.IN_BANK_FEE)) IN_BANK_FEE, " + 
					"(CASE WHEN COALESCE(FCN.WO_BANK_FEE,FCL.WO_BANK_FEE) < 0 THEN '-' ELSE '+' END) WO_BANK_FEE_SIGN,ABS(COALESCE(FCN.WO_BANK_FEE,FCL.WO_BANK_FEE)) WO_BANK_FEE, " + 
					"(CASE WHEN COALESCE(FCN.TCH_FEE,FCL.TCH_FEE) < 0 THEN '-' ELSE '+' END) TCH_FEE_SIGN,ABS(COALESCE(FCN.TCH_FEE,FCL.TCH_FEE)) TCH_FEE, " + 
					"(CASE WHEN COALESCE(FCN.HANDLECHARGE,FCL.HANDLECHARGE) < 0 THEN '-' ELSE '+' END) HANDLECHARGE_SIGN,ABS(COALESCE(FCN.HANDLECHARGE,FCL.HANDLECHARGE)) HANDLECHARGE, " + 
					"TRANS_DATE(FCN.START_DATE,'W','') AS START_DATE " + 
					"FROM FEE_CODE_NW FCN  " + 
					"LEFT JOIN TXN_CODE TC ON FCN.FEE_ID = TC.TXN_ID  " + 
					"LEFT  JOIN FEE_CODE_NWLVL FCL ON FCN.FEE_UID = FCL.FEE_UID) " + 
					"UNION " + 
					"(select FC.FEE_ID , COALESCE(TC.TXN_NAME,'') AS TXN_NAME  ," + 
					"(CASE WHEN HANDLECHARGE = 0  THEN 'A' WHEN HANDLECHARGE > 0 THEN 'B' ELSE 'A' END) AS FEE_TYPE," + 
					"'-' AS FEE_UID ," + 
					"'-' AS FEE_DTNO," + 
					"'0000000000000' AS FEE_LVL_BEG_AMT ,  " + 
					"'0000000000000' AS FEE_LVL_END_AMT,  " + 
					"'1' AS FEE_LVL_TYPE, " + 
					"(CASE WHEN FC.SND_BANK_FEE < 0 THEN '-' ELSE '+' END) SND_BANK_FEE_SIGN,ABS(FC.SND_BANK_FEE) SND_BANK_FEE,  " + 
					"(CASE WHEN FC.OUT_BANK_FEE < 0 THEN '-' ELSE '+' END) OUT_BANK_FEE_SIGN,ABS(FC.OUT_BANK_FEE) OUT_BANK_FEE,  " + 
					"(CASE WHEN FC.IN_BANK_FEE < 0 THEN '-' ELSE '+' END) IN_BANK_FEE_SIGN,ABS(FC.IN_BANK_FEE) IN_BANK_FEE, " + 
					"(CASE WHEN FC.WO_BANK_FEE < 0 THEN '-' ELSE '+' END) WO_BANK_FEE_SIGN,ABS( coalesce(FC.WO_BANK_FEE,'0') ) WO_BANK_FEE, " + 
					"(CASE WHEN FC.TCH_FEE < 0 THEN '-' ELSE '+' END) TCH_FEE_SIGN,ABS(FC.TCH_FEE) TCH_FEE, " + 
					"(CASE WHEN FC.HANDLECHARGE < 0 THEN '-' ELSE '+' END) HANDLECHARGE_SIGN,ABS(FC.HANDLECHARGE) HANDLECHARGE, " + 
					"TRANS_DATE(FC.START_DATE,'W','') AS START_DATE " + 
					"from FEE_CODE FC " + 
					"LEFT JOIN TXN_CODE TC ON FC.FEE_ID = TC.TXN_ID)" + 
					"order by FEE_ID,FEE_UID,FEE_DTNO";
		}
		if(functionName.equals("sd_sc_feedata")){
			SQL = "select " +
					 "TXN_TYPE , COMPANY_ID , TXN_ID , SND_BRBK_ID , FEE_ACTIVE_DATE , STOP_DATE ,  "
					 + "( CASE WHEN FEE_TYPE='' AND HANDLECHARGE>0 THEN 'B' WHEN FEE_TYPE='' AND HANDLECHARGE=0 THEN 'A' ELSE FEE_TYPE END ) FEE_TYPE , "
					 + "FEE_LVL_TYPE , BEG_AMT , END_AMT ,  "+
					"(CASE WHEN SND_BANK_FEE < 0 THEN '-' ELSE '+' END) SND_BANK_FEE_SIGN,ABS(SND_BANK_FEE) SND_BANK_FEE,  " + 
					"(CASE WHEN OUT_BANK_FEE < 0 THEN '-' ELSE '+' END) OUT_BANK_FEE_SIGN,ABS(OUT_BANK_FEE) OUT_BANK_FEE,  " + 
					"(CASE WHEN IN_BANK_FEE < 0 THEN '-' ELSE '+' END) IN_BANK_FEE_SIGN,ABS(IN_BANK_FEE) IN_BANK_FEE, " + 
					"(CASE WHEN WO_BANK_FEE < 0 THEN '-' ELSE '+' END) WO_BANK_FEE_SIGN,ABS( coalesce( WO_BANK_FEE,'0')) WO_BANK_FEE ," +
					"(CASE WHEN TCH_FEE < 0 THEN '-' ELSE '+' END) TCH_FEE_SIGN,ABS(TCH_FEE) TCH_FEE, " + 
					" HANDLECHARGE "+
					"FROM TXN_COMPANY_FEE_PROFILE  ORDER BY TXN_TYPE , COMPANY_ID ,TXN_ID , SND_BRBK_ID ,FEE_ACTIVE_DATE , FEE_UID,BEG_AMT ";
		}
		return SQL;
	}
	
	public String getSQL4Agent(String functionName){
		String SQL = "";
		
		if(functionName.equals("bgbk")){
			SQL = "SELECT A.BGBK_ID,A.BGBK_NAME,A.OPBK_ID,TRANS_DATE(A.ACTIVE_DATE,'W','') AS ACTIVE_DATE,TRANS_DATE(A.STOP_DATE,'W','') AS STOP_DATE,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2100'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME1,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2200'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME2,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2300'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME3,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2500'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME4,(CASE WHEN(SELECT BUSINESS_TYPE_ID FROM BANK_GROUP_BUSINESS B WHERE BUSINESS_TYPE_ID='2800'AND B.BGBK_ID=A.BGBK_ID) IS NULL THEN ' ' ELSE 'Y' END) BUSINESS_TYPE_NAME5,'     ' AS BLANK FROM BANK_GROUP A  WHERE  A.BGBK_ATTR!='5' AND  A.BGBK_ATTR!='6' ORDER BY A.BGBK_ID,A.BGBK_NAME";
		}
		if(functionName.equals("brbk")){
			SQL = " SELECT BR.BGBK_ID,BR.BRBK_ID,BR.BRBK_NAME,TRANS_DATE(BR.ACTIVE_DATE,'W','') AS ACTIVE_DATE,TRANS_DATE(BR.STOP_DATE,'W','') AS STOP_DATE FROM BANK_BRANCH  BR ";
			SQL += " JOIN BANK_GROUP  BG ON BR.BGBK_ID = BG.BGBK_ID ";
			SQL += " ORDER BY BR.BGBK_ID,BR.BRBK_ID ";
		}
		if(functionName.equals("txncode")){
//			20160310 edit by hugo req UAT-2016309-04   ,note: TC.AGENT_TXN_ID 別名為BUSINESS_TYPE_ID 的原因是因 呼叫 getColumnMap(String functionName)
//			無法再判斷是業者端還是銀行端
//			SQL = "SELECT TC.TXN_ID,COALESCE ((SELECT AGENT_TXN_NAME FROM AGENT_TXN_CODE ATC WHERE ATC.AGENT_TXN_ID = TC.AGENT_TXN_ID  ) , '') TXN_NAME, TC.AGENT_TXN_ID,TRANS_DATE(TC.ACTIVE_DATE,'W','') AS ACTIVE_DATE FROM TXN_CODE TC WHERE  COALESCE(TC.AGENT_TXN_ID,'') <>'' ORDER BY TC.TXN_ID";
			SQL = "SELECT TC.TXN_ID,TC.TXN_NAME, COALESCE (TC.AGENT_TXN_ID ,'')  BUSINESS_TYPE_ID ,TRANS_DATE(TC.ACTIVE_DATE,'W','') AS ACTIVE_DATE FROM TXN_CODE TC WHERE  COALESCE(TC.AGENT_TXN_ID,'') <>'' ORDER BY TC.TXN_ID";
		}
		if(functionName.equals("txnerrorcode")){
			SQL = "SELECT ERROR_ID,ERR_DESC FROM TXN_ERROR_CODE WHERE ERROR_ID!='0000' ORDER BY ERROR_ID";
		}
		return SQL;
	}
	
	public Map<String,Object> getColumnMap(String functionName){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		
		if(functionName.equals("bgbk")){
			//總行代號
			columnNameList.add("BGBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//總行名稱
			columnNameList.add("BGBK_NAME");
			columnLengthList.add(30);
			columnTypeList.add("string");
			//操作行代號
			columnNameList.add("OPBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//停用日期
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//帳單扣款類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME1");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳類加單位
			columnNameList.add("BUSINESS_TYPE_NAME2");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//數位錢包類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME3");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//網路購物類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME4");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//自動充值類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME5");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//繳費類類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME6");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//保留欄位
			columnNameList.add("BLANK");
			columnLengthList.add(4);
			columnTypeList.add("string");
		}
		if(functionName.equals("brbk")){
			//總行代號
			columnNameList.add("BGBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//分行代號
			columnNameList.add("BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//銀行名稱
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(30);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//停用日期
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("sd")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(140);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(30);
			columnTypeList.add("string");
			//用戶號碼說明(用戶號碼)
			columnNameList.add("USER_NO");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("sdnw")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(140);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(30);
			columnTypeList.add("string");
			//用戶號碼說明(用戶號碼)
			columnNameList.add("USER_NO");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("sc")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(140);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(30);
			columnTypeList.add("string");
			//上市公司代號
			columnNameList.add("IPO_COMPANY_ID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//現金股息發放日期
			columnNameList.add("PROFIT_ISSUE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("scnw")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(140);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(30);
			columnTypeList.add("string");
			//上市公司代號
			columnNameList.add("IPO_COMPANY_ID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//現金股息發放日期
			columnNameList.add("PROFIT_ISSUE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//收費類型
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("biztype")){
			//業務類別代號
			columnNameList.add("BUSINESS_TYPE_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//業務類別名稱
			columnNameList.add("BUSINESS_TYPE_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
		}
		if(functionName.equals("txncode")){
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//適用業務類別
			columnNameList.add("BUSINESS_TYPE_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("txnerrorcode")){
			//回應代碼
			columnNameList.add("ERROR_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//回應代碼描述
			columnNameList.add("ERR_DESC");
			columnLengthList.add(100);
			columnTypeList.add("string");
		}
		if(functionName.equals("feecode")){
			//交易代號(手續費代號)
			columnNameList.add("FEE_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動行手續費正負號
			columnNameList.add("SND_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費
			columnNameList.add("SND_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款行手續費正負號
			columnNameList.add("OUT_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款行手續費
			columnNameList.add("OUT_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳行手續費正負號
			columnNameList.add("IN_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳行手續費
			columnNameList.add("IN_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//票交所手續費正負號
			columnNameList.add("TCH_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//票交所手續費
			columnNameList.add("TCH_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//啟用日期
			columnNameList.add("START_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
//			20160205 note by hugo req by 李建利  客戶支付手續費最小為0 故不需標示正負號
			//客戶支付手續費正負號
//			columnNameList.add("HANDLECHARGE_SIGN");
//			columnLengthList.add(1);
//			columnTypeList.add("string");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳行手續費正負號
			columnNameList.add("WO_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行手續費
			columnNameList.add("WO_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
		}
		//以前的檔案，現已不需要
//		if(functionName.equals("chg_sc_profile")){
//			//代收項目代號
//			columnNameList.add("SD_ITEM_NO");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//業者統一編號
//			columnNameList.add("COMPANY_ID");
//			columnLengthList.add(10);
//			columnTypeList.add("string");
//			//業者簡稱
//			columnNameList.add("COMPANY_ABBR_NAME");
//			columnLengthList.add(10);
//			columnTypeList.add("string");
//			//業者名稱
//			columnNameList.add("COMPANY_NAME");
//			columnLengthList.add(140);
//			columnTypeList.add("string");
//			//交易代號
//			columnNameList.add("TXN_ID");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//入帳行代號
//			columnNameList.add("INBANKID");
//			columnLengthList.add(7);
//			columnTypeList.add("string");
//			//入帳行名稱
//			columnNameList.add("INBANKNAME");
//			columnLengthList.add(30);
//			columnTypeList.add("string");
//			//入帳行帳號
//			columnNameList.add("INBANKACCTNO");
//			columnLengthList.add(16);
//			columnTypeList.add("string");
//			//格式代號
//			columnNameList.add("LAYOUTID");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//可收逾期繳費天數
//			columnNameList.add("DEALY_CHARGE_DAY");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//啟用日期
//			columnNameList.add("START_DATE");
//			columnLengthList.add(8);
//			columnTypeList.add("string");
//			//停用日期
//			columnNameList.add("STOP_DATE");
//			columnLengthList.add(8);
//			columnTypeList.add("string");
//		}
		if(functionName.equals("chg_company_profile")){
			//收費業者統編
			columnNameList.add("PI_COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//收費業者簡稱
			columnNameList.add("PI_COMPANY_ABBR_NAME");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//收費業者名稱
			columnNameList.add("PI_COMPANY_NAME");
			columnLengthList.add(140);
			columnTypeList.add("string");
			//繳費類別代號
			columnNameList.add("BILL_TYPE_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//繳費工具類型-約定授權
			columnNameList.add("TYPE_AUZ");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//繳費工具類型 -晶片金融卡
			columnNameList.add("TYPE_CARD");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//繳費工具類型 -本人帳戶繳本人帳單
			columnNameList.add("TYPE_USER_ACCT");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("START_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//停用日期
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//支援銷帳行銷帳
			columnNameList.add("IS_WO");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行代號
			columnNameList.add("INBANK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//銷帳行名稱
			columnNameList.add("WO_COMPANY_ABBR_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//入帳行帳號
			columnNameList.add("INBANK_ACCT_NO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//銷帳資料類型-虛擬帳號
			columnNameList.add("TYPE_ACCT");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳資料類型-銷帳編號
			columnNameList.add("TYPE_WRITE_OFF_NO");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳資料類型-三段式條碼
			columnNameList.add("TYPE_BARCODE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//虛擬帳號欄位說明
			columnNameList.add("VIRTUAL_ACC_NOTE");
			columnLengthList.add(100);
			columnTypeList.add("string");
			//條碼-代收項目
			columnNameList.add("SD_ITEM_NO");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//條碼-解析格式代號
			columnNameList.add("FMT_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//保留
			columnNameList.add("");
			columnLengthList.add(136);
			columnTypeList.add("string");
		}
		if(functionName.equals("feecodenw")){
			//交易代號(手續費代號)
			columnNameList.add("FEE_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//交易類型
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//級距類型
			columnNameList.add("FEE_LVL_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//級距金額(起)
			columnNameList.add("FEE_LVL_BEG_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//級距金額(迄)
			columnNameList.add("FEE_LVL_END_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//發動行手續費正負號
			columnNameList.add("SND_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費
			columnNameList.add("SND_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款行手續費正負號
			columnNameList.add("OUT_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款行手續費
			columnNameList.add("OUT_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳行手續費正負號
			columnNameList.add("IN_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳行手續費
			columnNameList.add("IN_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//票交所手續費正負號
			columnNameList.add("TCH_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//票交所手續費
			columnNameList.add("TCH_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//啟用日期
			columnNameList.add("START_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
//			20160205 note by hugo req by 李建利  客戶支付手續費最小為0 故不需標示正負號
			//客戶支付手續費正負號
//			columnNameList.add("HANDLECHARGE_SIGN");
//			columnLengthList.add(1);
//			columnTypeList.add("string");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳行手續費正負號
			columnNameList.add("WO_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行手續費
			columnNameList.add("WO_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
		}
		if(functionName.equals("sd_sc_feedata")){
			//TXN_TYPE
			columnNameList.add("TXN_TYPE");
			columnLengthList.add(2);
			columnTypeList.add("string");
			//COMPANY_ID
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//交易代號(手續費代號)
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//SND_BRBK_ID
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//手續費啟用日
			columnNameList.add("FEE_ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//手續費停用日
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//金額級距起
			columnNameList.add("BEG_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//金額級距迄
			columnNameList.add("END_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//手續費類型
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//收費類型
			columnNameList.add("FEE_LVL_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費正負號
			columnNameList.add("SND_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費
			columnNameList.add("SND_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款行手續費正負號
			columnNameList.add("OUT_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款行手續費
			columnNameList.add("OUT_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳行手續費正負號
			columnNameList.add("IN_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳行手續費
			columnNameList.add("IN_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳行手續費正負號
			columnNameList.add("WO_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行手續費
			columnNameList.add("WO_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//票交所手續費正負號
			columnNameList.add("TCH_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//票交所手續費
			columnNameList.add("TCH_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//保留位  將資料補至長度160
			columnNameList.add("REMAINCOLUMN");
			columnLengthList.add(59);
			columnTypeList.add("string");
		}
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	
	
	public Map<String,Object> getColumnMapUTF8(String functionName){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		
		if(functionName.equals("bgbk")){
			//總行代號
			columnNameList.add("BGBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//總行名稱
			columnNameList.add("BGBK_NAME");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//操作行代號
			columnNameList.add("OPBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//停用日期
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//帳單扣款類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME1");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳類加單位
			columnNameList.add("BUSINESS_TYPE_NAME2");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//數位錢包類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME3");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//網路購物類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME4");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//自動充值類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME5");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//繳費類類參加單位
			columnNameList.add("BUSINESS_TYPE_NAME6");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//保留欄位
			columnNameList.add("BLANK");
			columnLengthList.add(4);
			columnTypeList.add("string");
		}
		if(functionName.equals("brbk")){
			//總行代號
			columnNameList.add("BGBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//分行代號
			columnNameList.add("BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//銀行名稱
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//停用日期
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("sd")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(280);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//用戶號碼說明(用戶號碼)
			columnNameList.add("USER_NO");
			columnLengthList.add(120);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("sdnw")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(280);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//用戶號碼說明(用戶號碼)
			columnNameList.add("USER_NO");
			columnLengthList.add(120);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("sc")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(280);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//上市公司代號
			columnNameList.add("IPO_COMPANY_ID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//現金股息發放日期
			columnNameList.add("PROFIT_ISSUE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("scnw")){
			//發動者統編
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者中文簡稱
			columnNameList.add("COMPANY_ABBR_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動者名稱
			columnNameList.add("COMPANY_NAME");
			columnLengthList.add(280);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//發動行代號(發動分行代號)
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動行名稱(分行名稱)
			columnNameList.add("BRBK_NAME");
			columnLengthList.add(60);
			columnTypeList.add("string");
			//上市公司代號
			columnNameList.add("IPO_COMPANY_ID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//現金股息發放日期
			columnNameList.add("PROFIT_ISSUE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//收費類型
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("biztype")){
			//業務類別代號
			columnNameList.add("BUSINESS_TYPE_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//業務類別名稱
			columnNameList.add("BUSINESS_TYPE_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
		}
		if(functionName.equals("txncode")){
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//適用業務類別
			columnNameList.add("BUSINESS_TYPE_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
		}
		if(functionName.equals("txnerrorcode")){
			//回應代碼
			columnNameList.add("ERROR_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//回應代碼描述
			columnNameList.add("ERR_DESC");
			columnLengthList.add(200);
			columnTypeList.add("string");
		}
		if(functionName.equals("feecode")){
			//交易代號(手續費代號)
			columnNameList.add("FEE_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//發動行手續費正負號
			columnNameList.add("SND_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費
			columnNameList.add("SND_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款行手續費正負號
			columnNameList.add("OUT_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款行手續費
			columnNameList.add("OUT_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳行手續費正負號
			columnNameList.add("IN_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳行手續費
			columnNameList.add("IN_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//票交所手續費正負號
			columnNameList.add("TCH_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//票交所手續費
			columnNameList.add("TCH_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//啟用日期
			columnNameList.add("START_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
//			20160205 note by hugo req by 李建利  客戶支付手續費最小為0 故不需標示正負號
			//客戶支付手續費正負號
//			columnNameList.add("HANDLECHARGE_SIGN");
//			columnLengthList.add(1);
//			columnTypeList.add("string");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳行手續費正負號
			columnNameList.add("WO_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行手續費
			columnNameList.add("WO_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
		}
		//以前的檔案，現已不需要
//		if(functionName.equals("chg_sc_profile")){
//			//代收項目代號
//			columnNameList.add("SD_ITEM_NO");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//業者統一編號
//			columnNameList.add("COMPANY_ID");
//			columnLengthList.add(10);
//			columnTypeList.add("string");
//			//業者簡稱
//			columnNameList.add("COMPANY_ABBR_NAME");
//			columnLengthList.add(10);
//			columnTypeList.add("string");
//			//業者名稱
//			columnNameList.add("COMPANY_NAME");
//			columnLengthList.add(140);
//			columnTypeList.add("string");
//			//交易代號
//			columnNameList.add("TXN_ID");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//入帳行代號
//			columnNameList.add("INBANKID");
//			columnLengthList.add(7);
//			columnTypeList.add("string");
//			//入帳行名稱
//			columnNameList.add("INBANKNAME");
//			columnLengthList.add(30);
//			columnTypeList.add("string");
//			//入帳行帳號
//			columnNameList.add("INBANKACCTNO");
//			columnLengthList.add(16);
//			columnTypeList.add("string");
//			//格式代號
//			columnNameList.add("LAYOUTID");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//可收逾期繳費天數
//			columnNameList.add("DEALY_CHARGE_DAY");
//			columnLengthList.add(3);
//			columnTypeList.add("string");
//			//啟用日期
//			columnNameList.add("START_DATE");
//			columnLengthList.add(8);
//			columnTypeList.add("string");
//			//停用日期
//			columnNameList.add("STOP_DATE");
//			columnLengthList.add(8);
//			columnTypeList.add("string");
//		}
		if(functionName.equals("chg_company_profile")){
			//收費業者統編
			columnNameList.add("PI_COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//收費業者簡稱
			columnNameList.add("PI_COMPANY_ABBR_NAME");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//收費業者名稱
			columnNameList.add("PI_COMPANY_NAME");
			columnLengthList.add(280);
			columnTypeList.add("string");
			//繳費類別代號
			columnNameList.add("BILL_TYPE_ID");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//繳費工具類型-約定授權
			columnNameList.add("TYPE_AUZ");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//繳費工具類型 -晶片金融卡
			columnNameList.add("TYPE_CARD");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//繳費工具類型 -本人帳戶繳本人帳單
			columnNameList.add("TYPE_USER_ACCT");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//啟用日期
			columnNameList.add("START_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//停用日期
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//支援銷帳行銷帳
			columnNameList.add("IS_WO");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行代號
			columnNameList.add("INBANK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//銷帳行名稱
			columnNameList.add("WO_COMPANY_ABBR_NAME");
			columnLengthList.add(80);
			columnTypeList.add("string");
			//入帳行帳號
			columnNameList.add("INBANK_ACCT_NO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//銷帳資料類型-虛擬帳號
			columnNameList.add("TYPE_ACCT");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳資料類型-銷帳編號
			columnNameList.add("TYPE_WRITE_OFF_NO");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳資料類型-三段式條碼
			columnNameList.add("TYPE_BARCODE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//虛擬帳號欄位說明
			columnNameList.add("VIRTUAL_ACC_NOTE");
			columnLengthList.add(200);
			columnTypeList.add("string");
			//條碼-代收項目
			columnNameList.add("SD_ITEM_NO");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//條碼-解析格式代號
			columnNameList.add("FMT_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//保留
			columnNameList.add("");
			columnLengthList.add(136);
			columnTypeList.add("string");
		}
		if(functionName.equals("feecodenw")){
			//交易代號(手續費代號)
			columnNameList.add("FEE_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易項目
			columnNameList.add("TXN_NAME");
			columnLengthList.add(40);
			columnTypeList.add("string");
			//交易類型
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//級距類型
			columnNameList.add("FEE_LVL_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//級距金額(起)
			columnNameList.add("FEE_LVL_BEG_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//級距金額(迄)
			columnNameList.add("FEE_LVL_END_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//發動行手續費正負號
			columnNameList.add("SND_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費
			columnNameList.add("SND_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款行手續費正負號
			columnNameList.add("OUT_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款行手續費
			columnNameList.add("OUT_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳行手續費正負號
			columnNameList.add("IN_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳行手續費
			columnNameList.add("IN_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//票交所手續費正負號
			columnNameList.add("TCH_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//票交所手續費
			columnNameList.add("TCH_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//啟用日期
			columnNameList.add("START_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
//			20160205 note by hugo req by 李建利  客戶支付手續費最小為0 故不需標示正負號
			//客戶支付手續費正負號
//			columnNameList.add("HANDLECHARGE_SIGN");
//			columnLengthList.add(1);
//			columnTypeList.add("string");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳行手續費正負號
			columnNameList.add("WO_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行手續費
			columnNameList.add("WO_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
		}
		if(functionName.equals("sd_sc_feedata")){
			//TXN_TYPE
			columnNameList.add("TXN_TYPE");
			columnLengthList.add(2);
			columnTypeList.add("string");
			//COMPANY_ID
			columnNameList.add("COMPANY_ID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//交易代號(手續費代號)
			columnNameList.add("TXN_ID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//SND_BRBK_ID
			columnNameList.add("SND_BRBK_ID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//手續費啟用日
			columnNameList.add("FEE_ACTIVE_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//手續費停用日
			columnNameList.add("STOP_DATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//金額級距起
			columnNameList.add("BEG_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//金額級距迄
			columnNameList.add("END_AMT");
			columnLengthList.add(13);
			columnTypeList.add("number");
			//手續費類型
			columnNameList.add("FEE_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//收費類型
			columnNameList.add("FEE_LVL_TYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費正負號
			columnNameList.add("SND_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動行手續費
			columnNameList.add("SND_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款行手續費正負號
			columnNameList.add("OUT_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款行手續費
			columnNameList.add("OUT_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳行手續費正負號
			columnNameList.add("IN_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳行手續費
			columnNameList.add("IN_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳行手續費正負號
			columnNameList.add("WO_BANK_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳行手續費
			columnNameList.add("WO_BANK_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//票交所手續費正負號
			columnNameList.add("TCH_FEE_SIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//票交所手續費
			columnNameList.add("TCH_FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//客戶支付手續費
			columnNameList.add("HANDLECHARGE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//保留位  將資料補至長度160
			columnNameList.add("REMAINCOLUMN");
			columnLengthList.add(59);
			columnTypeList.add("string");
		}
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	
	
	
	
	public String getFirstRow(String functionName){
		String firstRow = "";
		
		if(functionName.equals("bgbk")){
			firstRow = "BOFEA01" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("brbk")){
			firstRow = "BOFEA02" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("sd")){
			firstRow = "BOFEA05" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("sc")){
			firstRow = "BOFEA06" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("biztype")){
			firstRow = "BOFEA07" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("txncode")){
			firstRow = "BOFEA03" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("txnerrorcode")){
			firstRow = "BOFEA08" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("feecode")){
			firstRow = "BOFEA04" + DateTimeUtils.getDateShort(new Date());
		}
		//以前的檔案，現已不需要
//		if(functionName.equals("chg_sc_profile")){
//			firstRow = "BOFEA09" + DateTimeUtils.getDateShort(new Date());
//		}
		if(functionName.equals("chg_company_profile")){
			firstRow = "BOFEA10" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("feecodenw")){
			firstRow = "BOFEA11" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("sdnw")){
			firstRow = "BOFEA12" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("scnw")){
			firstRow = "BOFEA13" + DateTimeUtils.getDateShort(new Date());
		}
		if(functionName.equals("sd_sc_feedata")){
			firstRow = "BOFEA14" + DateTimeUtils.getDateShort(new Date());
		}
		return firstRow;
	}
	public String getLastRow(String functionName,String dataCount){
		String lastRow = "";
		
		if(functionName.equals("bgbk")){
			lastRow = "EOFEA01" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("brbk")){
			lastRow = "EOFEA02" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("sd")){
			lastRow = "EOFEA05" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("sc")){
			lastRow = "EOFEA06" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("biztype")){
			lastRow = "EOFEA07" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("txncode")){
			lastRow = "EOFEA03" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("txnerrorcode")){
			lastRow = "EOFEA08" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("feecode")){
			lastRow = "EOFEA04" + codeUtils.padText("number",6,dataCount);
		}
		//以前的檔案，現已不需要
//		if(functionName.equals("chg_sc_profile")){
//			lastRow = "EOFEA09" + codeUtils.padText("number",6,dataCount);
//		}
		if(functionName.equals("chg_company_profile")){
			lastRow = "EOFEA10" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("feecodenw")){
			lastRow = "EOFEA11" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("sdnw")){
			lastRow = "EOFEA12" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("scnw")){
			lastRow = "EOFEA13" + codeUtils.padText("number",6,dataCount);
		}
		if(functionName.equals("sd_sc_feedata")){
			lastRow = "EOFEA14" + codeUtils.padText("number",6,dataCount);
		}
		
		return lastRow;
	}
	
	public Map<String,Object> getFilenameListAndDataList(){
		Map<String,Object> filenameAndDataMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> queryListMap;
		//Zip裡面各個TXT的檔名
		List<String> filenameList = new ArrayList<String>();
		byte[] byteTXT = null;
		//放置TXT Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		//訊息
		String message = "";
		/////////////////////////////////////////////////////////////////////////////
		//總行資料
		queryListMap = codeUtils.queryListMap(getSQL("bgbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("bgbk"),getLastRow("bgbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("bgbk"),getFirstRow("bgbk"),getLastRow("bgbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("bgbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢總行資料過程出現問題");
		}
		//////////////////////////////////////////////////////////////////////////////
		//分行資料
		queryListMap = codeUtils.queryListMap(getSQL("brbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("brbk"),getLastRow("brbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("brbk"),getFirstRow("brbk"),getLastRow("brbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("brbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢分行資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//代收發動者基本資料
		queryListMap = codeUtils.queryListMap(getSQL("sd"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sd"),getLastRow("sd","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("sd"),getFirstRow("sd"),getLastRow("sd",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("sd.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢代收發動者基本資料過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//代付發動者基本資料
		queryListMap = codeUtils.queryListMap(getSQL("sc"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
		byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sc"),getLastRow("sc","0"));
		}
		if(queryListMap.size() > 0){
		byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("sc"),getFirstRow("sc"),getLastRow("sc",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
		filenameList.add("sc.txt");
		dataList.add(byteTXT);
		}
		//有問題
		else{
		message = codeUtils.appendMessage(message,"查詢代付發動者基本資料_新過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//業務類別資料
		queryListMap = codeUtils.queryListMap(getSQL("biztype"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("biztype"),getLastRow("biztype","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("biztype"),getFirstRow("biztype"),getLastRow("biztype",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("biztype.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢業務類別資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//交易代號資料
		queryListMap = codeUtils.queryListMap(getSQL("txncode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txncode"),getLastRow("txncode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txncode"),getFirstRow("txncode"),getLastRow("txncode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txncode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢交易代號資料過程出現問題");
		}
		////////////////////////////////////////////////////////////////////////////////
		//回應代碼
		queryListMap = codeUtils.queryListMap(getSQL("txnerrorcode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txnerrorcode"),getLastRow("txnerrorcode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txnerrorcode"),getFirstRow("txnerrorcode"),getLastRow("txnerrorcode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txnerrorcode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢回應代碼過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//手續費資料
		queryListMap = codeUtils.queryListMap(getSQL("feecode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("feecode"),getLastRow("feecode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("feecode"),getFirstRow("feecode"),getLastRow("feecode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("feecode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢手續費資料過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//by HUANGPU 繳費入帳業者基本資料
		//以前的檔案，現已不需要
//		queryListMap = codeUtils.queryListMap(getSQL("chg_sc_profile"),null);
//		//TXT的Byte[]
//		if(queryListMap.size() == 0){
//			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("chg_sc_profile"),getLastRow("chg_sc_profile","0"));
//		}
//		if(queryListMap.size() > 0){
//			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("chg_sc_profile"),getFirstRow("chg_sc_profile"),getLastRow("chg_sc_profile",String.valueOf(queryListMap.size())));
//		}
//		//正常
//		if(byteTXT != null){
//			filenameList.add("chg_sc_profile.txt");
//			dataList.add(byteTXT);
//		}
//		//有問題
//		else{
//			message = codeUtils.appendMessage(message,"查詢繳費入帳業者基本資料過程出現問題");
//		}
		///////////////////////////////////////////////////////////////////////////////
		//收費業者檔
		queryListMap = codeUtils.queryListMap(getSQL("chg_company_profile"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
		byteTXT = codeUtils.createTXTWithoutData(getFirstRow("chg_company_profile"),getLastRow("chg_company_profile","0"));
		}
		if(queryListMap.size() > 0){
		byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("chg_company_profile"),getFirstRow("chg_company_profile"),getLastRow("chg_company_profile",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
		filenameList.add("chg_company_profile.txt");
		dataList.add(byteTXT);
		}
		//有問題
		else{
		message = codeUtils.appendMessage(message,"查詢收費業者資料過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		// 新版手續費資料
		queryListMap = codeUtils.queryListMap(getSQL("feecodenw"), null);
		// TXT的Byte[]
		if (queryListMap.size() == 0) {
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("feecodenw"), getLastRow("feecodenw", "0"));
		}
		if (queryListMap.size() > 0) {
			byteTXT = codeUtils.createTXT(queryListMap, getColumnMap("feecodenw"), getFirstRow("feecodenw"),
					getLastRow("feecodenw", String.valueOf(queryListMap.size())));
		}
		// 正常
		if (byteTXT != null) {
			filenameList.add("feecodenw.txt");
			dataList.add(byteTXT);
		}
		// 有問題
		else {
			message = codeUtils.appendMessage(message, "查詢新版手續費資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//代收發動者基本資料_新
		queryListMap = codeUtils.queryListMap(getSQL("sdnw"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sdnw"),getLastRow("sdnw","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("sdnw"),getFirstRow("sdnw"),getLastRow("sdnw",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("sdnw.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
		message = codeUtils.appendMessage(message,"查詢代收發動者基本資料_新過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//代付發動者基本資料_新
		queryListMap = codeUtils.queryListMap(getSQL("scnw"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("scnw"),getLastRow("scnw","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("scnw"),getFirstRow("scnw"),getLastRow("scnw",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("scnw.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢代付發動者基本資料_新過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//代收代付業者收費類型歷程下載檔
		queryListMap = codeUtils.queryListMap(getSQL("sd_sc_feedata"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
		byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sd_sc_feedata"),getLastRow("sd_sc_feedata","0"));
		}
		if(queryListMap.size() > 0){
		byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("sd_sc_feedata"),getFirstRow("sd_sc_feedata"),getLastRow("sd_sc_feedata",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
		filenameList.add("sd_sc_feedata.txt");
		dataList.add(byteTXT);
		}
		//有問題
		else{
		message = codeUtils.appendMessage(message,"查詢代收代付業者收費類型歷程");
		}
		
		filenameAndDataMap.put("filenameList",filenameList);
		filenameAndDataMap.put("dataList",dataList);
		filenameAndDataMap.put("message",message);
		
		return filenameAndDataMap;
	}
	
	
	
	public Map<String,Object> getFilenameListAndDataList_UTF8(){
		Map<String,Object> filenameAndDataMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> queryListMap;
		//Zip裡面各個TXT的檔名
		List<String> filenameList = new ArrayList<String>();
		byte[] byteTXT = null;
		//放置TXT Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		//訊息
		String message = "";
		/////////////////////////////////////////////////////////////////////////////
		//總行資料
		queryListMap = codeUtils.queryListMap(getSQL("bgbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("bgbk"),getLastRow("bgbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("bgbk"),getFirstRow("bgbk"),getLastRow("bgbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("bgbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢總行資料過程出現問題");
		}
		//////////////////////////////////////////////////////////////////////////////
		//分行資料
		queryListMap = codeUtils.queryListMap(getSQL("brbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("brbk"),getLastRow("brbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("brbk"),getFirstRow("brbk"),getLastRow("brbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("brbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢分行資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//代收發動者基本資料
		queryListMap = codeUtils.queryListMap(getSQL("sd"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sd"),getLastRow("sd","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("sd"),getFirstRow("sd"),getLastRow("sd",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("sd.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢代收發動者基本資料過程出現問題");
		}		
		///////////////////////////////////////////////////////////////////////////////
		//代付發動者基本資料
		queryListMap = codeUtils.queryListMap(getSQL("sc"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sc"),getLastRow("sc","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("sc"),getFirstRow("sc"),getLastRow("sc",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("sc.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢代付發動者基本資料過程出現問題");
		}
		////////////////////////////////////////////////////////////////////////////////
		//業務類別資料
		queryListMap = codeUtils.queryListMap(getSQL("biztype"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("biztype"),getLastRow("biztype","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("biztype"),getFirstRow("biztype"),getLastRow("biztype",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("biztype.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢業務類別資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//交易代號資料
		queryListMap = codeUtils.queryListMap(getSQL("txncode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txncode"),getLastRow("txncode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("txncode"),getFirstRow("txncode"),getLastRow("txncode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txncode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢交易代號資料過程出現問題");
		}
		////////////////////////////////////////////////////////////////////////////////
		//回應代碼
		queryListMap = codeUtils.queryListMap(getSQL("txnerrorcode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txnerrorcode"),getLastRow("txnerrorcode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("txnerrorcode"),getFirstRow("txnerrorcode"),getLastRow("txnerrorcode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txnerrorcode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢回應代碼過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//手續費資料
		queryListMap = codeUtils.queryListMap(getSQL("feecode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("feecode"),getLastRow("feecode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("feecode"),getFirstRow("feecode"),getLastRow("feecode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("feecode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢手續費資料過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//by HUANGPU 繳費入帳業者基本資料
		//以前的檔案，現已不需要
//		queryListMap = codeUtils.queryListMap(getSQL("chg_sc_profile"),null);
//		//TXT的Byte[]
//		if(queryListMap.size() == 0){
//			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("chg_sc_profile"),getLastRow("chg_sc_profile","0"));
//		}
//		if(queryListMap.size() > 0){
//			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("chg_sc_profile"),getFirstRow("chg_sc_profile"),getLastRow("chg_sc_profile",String.valueOf(queryListMap.size())));
//		}
//		//正常
//		if(byteTXT != null){
//			filenameList.add("chg_sc_profile.txt");
//			dataList.add(byteTXT);
//		}
//		//有問題
//		else{
//			message = codeUtils.appendMessage(message,"查詢繳費入帳業者基本資料過程出現問題");
//		}
		///////////////////////////////////////////////////////////////////////////////
		//收費業者檔
		queryListMap = codeUtils.queryListMap(getSQL("chg_company_profile"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
		byteTXT = codeUtils.createTXTWithoutData(getFirstRow("chg_company_profile"),getLastRow("chg_company_profile","0"));
		}
		if(queryListMap.size() > 0){
		byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("chg_company_profile"),getFirstRow("chg_company_profile"),getLastRow("chg_company_profile",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
		filenameList.add("chg_company_profile.txt");
		dataList.add(byteTXT);
		}
		//有問題
		else{
		message = codeUtils.appendMessage(message,"查詢收費業者資料過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		// 新版手續費資料
		queryListMap = codeUtils.queryListMap(getSQL("feecodenw"), null);
		// TXT的Byte[]
		if (queryListMap.size() == 0) {
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("feecodenw"), getLastRow("feecodenw", "0"));
		}
		if (queryListMap.size() > 0) {
			byteTXT = codeUtils.createTXT_UTF8(queryListMap, getColumnMapUTF8("feecodenw"), getFirstRow("feecodenw"),
					getLastRow("feecodenw", String.valueOf(queryListMap.size())));
		}
		// 正常
		if (byteTXT != null) {
			filenameList.add("feecodenw.txt");
			dataList.add(byteTXT);
		}
		// 有問題
		else {
			message = codeUtils.appendMessage(message, "查詢新版手續費資料過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//代收發動者基本資料_新
		queryListMap = codeUtils.queryListMap(getSQL("sdnw"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sdnw"),getLastRow("sdnw","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("sdnw"),getFirstRow("sdnw"),getLastRow("sdnw",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("sdnw.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢代收發動者基本資料_新過程出現問題");
		}
		/////////////////////////////////////////////////////////////////////////////////
		//代付發動者基本資料_新
		queryListMap = codeUtils.queryListMap(getSQL("scnw"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("scnw"),getLastRow("scnw","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("scnw"),getFirstRow("scnw"),getLastRow("scnw",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("scnw.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢代付發動者基本資料_新過程出現問題");
		}
		////////////////////////////////////////////////////////////////////////////////
		//代收代付業者收費類型歷程下載檔
		queryListMap = codeUtils.queryListMap(getSQL("sd_sc_feedata"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("sd_sc_feedata"),getLastRow("sd_sc_feedata","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("sd_sc_feedata"),getFirstRow("sd_sc_feedata"),getLastRow("sd_sc_feedata",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
		filenameList.add("sd_sc_feedata.txt");
		dataList.add(byteTXT);
		}
		//有問題
		else{
		message = codeUtils.appendMessage(message,"查詢代收代付業者收費類型歷程");
		}
		
		filenameAndDataMap.put("filenameList",filenameList);
		filenameAndDataMap.put("dataList",dataList);
		filenameAndDataMap.put("message",message);
		
		return filenameAndDataMap;
	}
	
	
	/**
	 * 代理業者用
	 * 只需要總行檔 分行檔 回應代碼 交易代號(需過濾掉代理業者交易類別=null的資料)
	 * @return
	 */
	public Map<String,Object> getFilenameListAndDataList4Agent(){
		Map<String,Object> filenameAndDataMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> queryListMap;
		//Zip裡面各個TXT的檔名
		List<String> filenameList = new ArrayList<String>();
		byte[] byteTXT = null;
		//放置TXT Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		//訊息
		String message = "";
		/////////////////////////////////////////////////////////////////////////////
		//總行資料
		queryListMap = codeUtils.queryListMap(getSQL4Agent("bgbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("bgbk"),getLastRow("bgbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("bgbk"),getFirstRow("bgbk"),getLastRow("bgbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("bgbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢總行資料過程出現問題");
		}
		//////////////////////////////////////////////////////////////////////////////
		//分行資料
		queryListMap = codeUtils.queryListMap(getSQL4Agent("brbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("brbk"),getLastRow("brbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("brbk"),getFirstRow("brbk"),getLastRow("brbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("brbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢分行資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//交易代號資料
		queryListMap = codeUtils.queryListMap(getSQL4Agent("txncode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txncode"),getLastRow("txncode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txncode"),getFirstRow("txncode"),getLastRow("txncode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txncode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢交易代號資料過程出現問題");
		}
		////////////////////////////////////////////////////////////////////////////////
		//回應代碼
		queryListMap = codeUtils.queryListMap(getSQL4Agent("txnerrorcode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txnerrorcode"),getLastRow("txnerrorcode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txnerrorcode"),getFirstRow("txnerrorcode"),getLastRow("txnerrorcode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txnerrorcode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢回應代碼過程出現問題");
		}
		filenameAndDataMap.put("filenameList",filenameList);
		filenameAndDataMap.put("dataList",dataList);
		filenameAndDataMap.put("message",message);
		
		return filenameAndDataMap;
	}
	
	public Map<String,Object> getFilenameListAndDataList4Agent_UTF8(){
		Map<String,Object> filenameAndDataMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> queryListMap;
		//Zip裡面各個TXT的檔名
		List<String> filenameList = new ArrayList<String>();
		byte[] byteTXT = null;
		//放置TXT Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		//訊息
		String message = "";
		/////////////////////////////////////////////////////////////////////////////
		//總行資料
		queryListMap = codeUtils.queryListMap(getSQL4Agent("bgbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("bgbk"),getLastRow("bgbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("bgbk"),getFirstRow("bgbk"),getLastRow("bgbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("bgbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢總行資料過程出現問題");
		}
		//////////////////////////////////////////////////////////////////////////////
		//分行資料
		queryListMap = codeUtils.queryListMap(getSQL4Agent("brbk"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("brbk"),getLastRow("brbk","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("brbk"),getFirstRow("brbk"),getLastRow("brbk",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("brbk.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢分行資料過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////////
		//交易代號資料
		queryListMap = codeUtils.queryListMap(getSQL4Agent("txncode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txncode"),getLastRow("txncode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("txncode"),getFirstRow("txncode"),getLastRow("txncode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txncode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢交易代號資料過程出現問題");
		}
		////////////////////////////////////////////////////////////////////////////////
		//回應代碼
		queryListMap = codeUtils.queryListMap(getSQL4Agent("txnerrorcode"),null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txnerrorcode"),getLastRow("txnerrorcode","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT_UTF8(queryListMap,getColumnMapUTF8("txnerrorcode"),getFirstRow("txnerrorcode"),getLastRow("txnerrorcode",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("txnerrorcode.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢回應代碼過程出現問題");
		}
		filenameAndDataMap.put("filenameList",filenameList);
		filenameAndDataMap.put("dataList",dataList);
		filenameAndDataMap.put("message",message);
		
		return filenameAndDataMap;
	}
	
	
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}
	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
}
