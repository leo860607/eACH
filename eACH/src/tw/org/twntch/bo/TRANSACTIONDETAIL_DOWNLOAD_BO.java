package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class TRANSACTIONDETAIL_DOWNLOAD_BO{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private CodeUtils codeUtils;
	
	//發動行條件 過濾整批資料
	public Map<String,String> getSenderConditionForBat(String date,String opbkId,String clearingphase){
		List<String> senderConditions = new ArrayList<String>();
		List<String> senderConditions2 = new ArrayList<String>();
		Map<String,String> retMap = new HashMap<String,String>();
		String senderCondition = "";
		String senderCondition2 = "";
		
		if(StrUtils.isNotEmpty(date)){
			senderConditions.add(" (A.BIZDATE = '" + date + "') ");
			senderConditions2.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			senderConditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
			senderConditions2.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		senderConditions2.addAll(senderConditions);
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			senderConditions.add(" COALESCE( A.FLBIZDATE , '') = '' ");
			senderConditions.add(" A.SENDERACQUIRE = '" + opbkId + "' ");
			senderConditions2.add(" COALESCE( A.FLBIZDATE , '') <> '' ");
			senderConditions2.add(" ( A.SENDERACQUIRE = '" + opbkId + "' AND substr(COALESCE(A.PCODE,''),4) NOT IN ( '1' ,'2' ,'3' , '4') )");
		}
		for(int i = 0; i < senderConditions.size(); i++){
			senderCondition += senderConditions.get(i);
			if(i < senderConditions.size() - 1){
				senderCondition += " AND ";
			}
		}
		for(int i = 0; i < senderConditions2.size(); i++){
			senderCondition2 += senderConditions2.get(i);
			if(i < senderConditions2.size() - 1){
				senderCondition2 += " AND ";
			}
		}
		logger.debug("senderCondition==>"+senderCondition);
		logger.debug("senderCondition2==>"+senderCondition2);
		retMap.put("sqlPath", senderCondition);
		retMap.put("sqlPath2", senderCondition2);
		return retMap;
	}
	//發動行條件
	public String getSenderCondition(String date,String opbkId,String clearingphase){
		List<String> senderConditions = new ArrayList<String>();
		String senderCondition = "";
		
		if(StrUtils.isNotEmpty(date)){
			senderConditions.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			senderConditions.add(" A.SENDERACQUIRE = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			senderConditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < senderConditions.size(); i++){
			senderCondition += senderConditions.get(i);
			if(i < senderConditions.size() - 1){
				senderCondition += " AND ";
			}
		}
		logger.debug("senderCondition==>"+senderCondition);
		
		return senderCondition;
	}
	
	//扣款行條件
	public Map<String,String> getOutConditionForBat(String date,String opbkId,String clearingphase){
		List<String> outConditions = new ArrayList<String>();
		List<String> outConditions2 = new ArrayList<String>();
		Map<String,String> retMap = new HashMap<String,String>();
		String outCondition = "";
		String outCondition2 = "";
		
		if(StrUtils.isNotEmpty(date)){
			outConditions.add(" (A.BIZDATE = '" + date + "') ");
			outConditions2.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			outConditions.add(" COALESCE( A.FLBIZDATE , '') = '' ");
			outConditions.add(" A.OUTACQUIRE = '" + opbkId + "' ");
			
			outConditions2.add(" A.OUTACQUIRE = '" + opbkId + "' ");
			outConditions2.add(" COALESCE( A.FLBIZDATE , '') <> '' ");
			outConditions2.add("  substr(COALESCE(A.PCODE,''),4) = '1' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			outConditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
			outConditions2.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		
		for(int i = 0; i < outConditions.size(); i++){
			outCondition += outConditions.get(i);
			if(i < outConditions.size() - 1){
				outCondition += " AND ";
			}
		}
		
		for(int i = 0; i < outConditions2.size(); i++){
			outCondition2 += outConditions2.get(i);
			if(i < outConditions2.size() - 1){
				outCondition2 += " AND ";
			}
		}
		logger.debug("outCondition==>"+outCondition);
		logger.debug("outCondition2==>"+outCondition2);
		retMap.put("sqlPath", outCondition);
		retMap.put("sqlPath2", outCondition2);
		return retMap;
	}
	
	//扣款行條件
	public String getOutCondition(String date,String opbkId,String clearingphase){
		List<String> outConditions = new ArrayList<String>();
		String outCondition = "";
		
		if(StrUtils.isNotEmpty(date)){
			outConditions.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			outConditions.add(" A.OUTACQUIRE = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			outConditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		
		for(int i = 0; i < outConditions.size(); i++){
			outCondition += outConditions.get(i);
			if(i < outConditions.size() - 1){
				outCondition += " AND ";
			}
		}
		logger.debug("outCondition==>"+outCondition);
		
		return outCondition;
	}
	
	//入帳行條件
	public Map<String,String> getInConditionForBat(String date,String opbkId,String clearingphase){
		List<String> inConditions = new ArrayList<String>();
		List<String> inConditions2 = new ArrayList<String>();
		Map<String,String> retMap = new HashMap<String,String>();
		String inCondition = "";
		String inCondition2 = "";
			
		if(StrUtils.isNotEmpty(date)){
			inConditions.add(" (A.BIZDATE = '" + date + "') ");
			inConditions2.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			inConditions.add(" A.INACQUIRE = '" + opbkId + "' ");
			inConditions.add(" COALESCE( A.FLBIZDATE , '') = '' ");
			
			inConditions2.add(" A.INACQUIRE = '" + opbkId + "' ");
			inConditions2.add(" COALESCE( A.FLBIZDATE , '') <> '' ");
			inConditions2.add("  substr(COALESCE(A.PCODE,''),4) = '2' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			inConditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < inConditions.size(); i++){
			inCondition += inConditions.get(i);
			if(i < inConditions.size() - 1){
				inCondition += " AND ";
			}
		}
		
		for(int i = 0; i < inConditions2.size(); i++){
			inCondition2 += inConditions2.get(i);
			if(i < inConditions2.size() - 1){
				inCondition2 += " AND ";
			}
		}
		logger.debug("inCondition==>"+inCondition);
		logger.debug("inCondition2==>"+inCondition2);
		retMap.put("sqlPath", inCondition);
		retMap.put("sqlPath2", inCondition2);
		return retMap;
	}
	//銷帳行條件
	public Map<String,String> getWOCondition(String date,String opbkId,String clearingphase){
		List<String> conditions = new ArrayList<String>();
		Map<String,String> retMap = new HashMap<String,String>();
		String condition = "";
		
		if(StrUtils.isNotEmpty(date)){
			conditions.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			conditions.add(" A.WOACQUIRE = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			conditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < conditions.size(); i++){
			condition += conditions.get(i);
			if(i < conditions.size() - 1){
				condition += " AND ";
			}
		}
		
		logger.debug("WO.condition==>"+condition);
		retMap.put("sqlPath", condition);
		return retMap;
	}
	public String getInCondition(String date,String opbkId,String clearingphase){
		List<String> inConditions = new ArrayList<String>();
		String inCondition = "";
		
		if(StrUtils.isNotEmpty(date)){
			inConditions.add(" (A.BIZDATE = '" + date + "') ");
		}
		if(StrUtils.isNotEmpty(opbkId) && !opbkId.equals("all")){
			inConditions.add(" A.INACQUIRE = '" + opbkId + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			inConditions.add(" A.CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < inConditions.size(); i++){
			inCondition += inConditions.get(i);
			if(i < inConditions.size() - 1){
				inCondition += " AND ";
			}
		}
		logger.debug("inCondition==>"+inCondition);
		
		return inCondition;
	}
	
	/**
	 * 強制過濾整批的SQL，過濾原則參考SRS
	 * @param functionName
	 * @param condition
	 * @return
	 */
	public String getSQLForBat(String functionName,String sqlPath ,String sqlPath2){
		StringBuffer fromAndWhere = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		if(functionName.equals("txlist")){
				
			sql.append(" WITH TEMP AS ");
			sql.append(" ( ");
			sql.append(" SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,A.PCODE,A.SENDERBANK,SUBSTR(A.STAN,4,7) AS STAN,A.TXID ");
			sql.append(" ,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID,A.INID,A.OUTID,A.INACCTNO,A.OUTACCTNO,B.USERNO,B.COMPANYID,A.CONMEMO,B.SENDERDATA,A.BIZDATE,A.CLEARINGPHASE,A.RESULTSTATUS ");
			sql.append(" ,(CASE A.ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN,ABS(A.SENDERFEE) AS SENDERFEE ");
			sql.append(" ,(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN,ABS(A.INFEE) AS INFEE ");
			sql.append(" ,(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN,ABS(A.OUTFEE) AS OUTFEE ");
			sql.append(" ,(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN,ABS(EACHFEE) AS EACHFEE,ABS(FEE) AS FEE ");
			sql.append(" FROM RPONBLOCKTAB AS A ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN ");
			sql.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1' ");
			sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
			sql.append(" UNION ALL ");
			sql.append(" SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,A.PCODE,A.SENDERBANK,SUBSTR(A.STAN,4,7) AS STAN,A.TXID ");
			sql.append(" ,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID,A.INID,A.OUTID,A.INACCTNO,A.OUTACCTNO,B.USERNO,B.COMPANYID,A.CONMEMO,B.SENDERDATA,A.BIZDATE,A.CLEARINGPHASE,A.RESULTSTATUS ");
			sql.append(" ,(CASE A.ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN,ABS(A.SENDERFEE) AS SENDERFEE ");
			sql.append(" ,(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN,ABS(A.INFEE) AS INFEE ");
			sql.append(" ,(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN,ABS(A.OUTFEE) AS OUTFEE ");
			sql.append(" ,(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN,ABS(EACHFEE) AS EACHFEE,ABS(FEE) AS FEE ");
			sql.append(" FROM RPONBLOCKTAB AS A ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN ");
			sql.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1' ");
			sql.append((StrUtils.isNotEmpty(sqlPath2)?" AND "+sqlPath2:"")); 
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			
			SQL = sql.toString();
			logger.debug("getSQLForBat.txlist sql===>"+SQL);
		}
		if(functionName.equals("pending")){
			sqlPath=sqlPath.replace("(A.BIZDATE =", "(A.OBIZDATE =");
			sqlPath=sqlPath.replace("A.CLEARINGPHASE =", "A.OCLEARINGPHASE =");
			sqlPath2=sqlPath2.replace("(A.BIZDATE =", "(A.OBIZDATE =");
			sqlPath2=sqlPath2.replace("A.CLEARINGPHASE =", "A.OCLEARINGPHASE =");
			sql.append(" WITH TEMP AS ");
			sql.append(" ( ");
			sql.append(" SELECT ");
			sql.append(" A.OBIZDATE,A.OCLEARINGPHASE,A.TXID,A.PCODE,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID ");
			sql.append(" ,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.RESULTSTATUS ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,A.SENDERBANK,SUBSTR(A.OSTAN,4,7) AS STAN ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -A.TXAMT ELSE A.TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN ");
			sql.append(" ,ABS(A.SENDERFEE) AS SENDERFEE,(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN ");
			sql.append(" ,ABS(A.INFEE) AS INFEE,(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN ");
			sql.append(" ,ABS(A.OUTFEE) AS OUTFEE,(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN ");
			sql.append(" ,ABS(A.EACHFEE) AS EACHFEE,ABS(B.FEE) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON B.BIZDATE=A.OBIZDATE AND B.STAN=A.OSTAN  ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON A.OTXDATE=C.TXDATE AND A.OSTAN=C.STAN  ");
			sql.append(" WHERE 1=1  ");
			sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
			sql.append(" UNION ALL ");
			sql.append(" SELECT ");
			sql.append(" A.OBIZDATE,A.OCLEARINGPHASE,A.TXID,A.PCODE,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID ");
			sql.append(" ,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.RESULTSTATUS ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,A.SENDERBANK,SUBSTR(A.OSTAN,4,7) AS STAN ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -A.TXAMT ELSE A.TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN ");
			sql.append(" ,ABS(A.SENDERFEE) AS SENDERFEE,(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN ");
			sql.append(" ,ABS(A.INFEE) AS INFEE,(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN ");
			sql.append(" ,ABS(A.OUTFEE) AS OUTFEE,(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN ");
			sql.append(" ,ABS(A.EACHFEE) AS EACHFEE,ABS(B.FEE) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON B.BIZDATE=A.OBIZDATE AND B.STAN=A.OSTAN  ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON A.OTXDATE=C.TXDATE AND A.OSTAN=C.STAN  ");
			sql.append(" WHERE 1=1  ");
			sql.append((StrUtils.isNotEmpty(sqlPath2)?" AND "+sqlPath2:""));
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP  ");
			
				
			SQL = sql.toString();
			logger.debug("pending sql===>"+SQL);
		}
		if(functionName.equals("pendresult")){
			sql.append(" WITH TEMP AS ( ");
			sql.append(" SELECT A.BIZDATE AS PENDINGBIZDATE,A.CLEARINGPHASE AS PENDINGCLEARINGPHASE,(CASE A.RESULTCODE WHEN '01' THEN '01' ELSE '00' END) AS RESULTCODE ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT,B.PCODE,B.SENDERBANK,SUBSTR(B.STAN,4,7) AS STAN,B.TXID,B.SENDERBANKID,B.INBANKID,B.OUTBANKID ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -B.TXAMT ELSE B.TXAMT END) AS TXAMT,B.SENDERID,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.BIZDATE,B.CLEARINGPHASE ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.SENDERFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) END) AS SENDERFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.SENDERFEE) ELSE '0.00' END) AS SENDERFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.INFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) END) AS INFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.INFEE) ELSE '0.00' END) AS INFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.OUTFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) END) AS OUTFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.EACHFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) END) AS EACHFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.EACHFEE) ELSE '0.00' END) AS EACHFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(B.FEE) ELSE '0.00' END) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON A.OTXDATE=B.TXDATE AND A.OSTAN=B.STAN ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON B.TXDATE=C.TXDATE AND B.STAN=C.STAN ");
			sql.append(" WHERE COALESCE(A.BIZDATE,'') <> '' AND COALESCE(A.CLEARINGPHASE,'') <> '' ");
			sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
			sql.append(" UNION ALL ");
			sql.append(" SELECT A.BIZDATE AS PENDINGBIZDATE,A.CLEARINGPHASE AS PENDINGCLEARINGPHASE,(CASE A.RESULTCODE WHEN '01' THEN '01' ELSE '00' END) AS RESULTCODE ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT,B.PCODE,B.SENDERBANK,SUBSTR(B.STAN,4,7) AS STAN,B.TXID,B.SENDERBANKID,B.INBANKID,B.OUTBANKID ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -B.TXAMT ELSE B.TXAMT END) AS TXAMT,B.SENDERID,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.BIZDATE,B.CLEARINGPHASE ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.SENDERFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) END) AS SENDERFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.SENDERFEE) ELSE '0.00' END) AS SENDERFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.INFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) END) AS INFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.INFEE) ELSE '0.00' END) AS INFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.OUTFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) END) AS OUTFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.EACHFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) END) AS EACHFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.EACHFEE) ELSE '0.00' END) AS EACHFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(B.FEE) ELSE '0.00' END) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON A.OTXDATE=B.TXDATE AND A.OSTAN=B.STAN ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON B.TXDATE=C.TXDATE AND B.STAN=C.STAN ");
			sql.append(" WHERE COALESCE(A.BIZDATE,'') <> '' AND COALESCE(A.CLEARINGPHASE,'') <> '' ");
			sql.append((StrUtils.isNotEmpty(sqlPath2)?" AND "+sqlPath2:""));
			sql.append(" )   ");
			sql.append(" SELECT * FROM TEMP ");
			SQL = sql.toString();
			logger.debug("pendresult sql===>"+SQL);
		}
		return SQL;
	}
	
	
	/**
	 * 強制過濾整批的SQL，過濾原則參考SRS
	 * @param functionName
	 * @param condition
	 * @return
	 */
	public String getSQLForBat_NW(String functionName,String sqlPath ,String sqlPath2){
		StringBuffer fromAndWhere = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		if(functionName.equals("txlist")){
				
			sql.append(" WITH TEMP AS ");
			sql.append(" ( ");
			sql.append(" SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,A.PCODE,A.SENDERBANK,SUBSTR(A.STAN,4,7) AS STAN,A.TXID ");
			sql.append(" ,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID,A.INID,A.OUTID,A.INACCTNO,A.OUTACCTNO,B.USERNO,B.COMPANYID,A.CONMEMO,B.SENDERDATA,A.BIZDATE,A.CLEARINGPHASE,A.RESULTSTATUS ");
			sql.append(" ,(CASE A.ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE_NW < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN,ABS(A.SENDERFEE_NW) AS SENDERFEE ");
			sql.append(" ,(CASE WHEN A.INFEE_NW < 0 THEN '-' ELSE '+' END) AS INFEESIGN,ABS(A.INFEE_NW) AS INFEE ");
			sql.append(" ,(CASE WHEN A.OUTFEE_NW < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN,ABS(A.OUTFEE_NW) AS OUTFEE ");
			sql.append(" ,(CASE WHEN A.EACHFEE_NW < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN,ABS(EACHFEE_NW) AS EACHFEE,ABS(HANDLECHARGE_NW) AS FEE ");
			sql.append(" FROM RPONBLOCKTAB AS A ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN ");
			sql.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1' ");
			sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
			sql.append(" UNION ALL ");
			sql.append(" SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,A.PCODE,A.SENDERBANK,SUBSTR(A.STAN,4,7) AS STAN,A.TXID ");
			sql.append(" ,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID,A.INID,A.OUTID,A.INACCTNO,A.OUTACCTNO,B.USERNO,B.COMPANYID,A.CONMEMO,B.SENDERDATA,A.BIZDATE,A.CLEARINGPHASE,A.RESULTSTATUS ");
			sql.append(" ,(CASE A.ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE_NW < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN,ABS(A.SENDERFEE_NW) AS SENDERFEE ");
			sql.append(" ,(CASE WHEN A.INFEE_NW < 0 THEN '-' ELSE '+' END) AS INFEESIGN,ABS(A.INFEE_NW) AS INFEE ");
			sql.append(" ,(CASE WHEN A.OUTFEE_NW < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN,ABS(A.OUTFEE_NW) AS OUTFEE ");
			sql.append(" ,(CASE WHEN A.EACHFEE_NW < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN,ABS(EACHFEE_NW) AS EACHFEE,ABS(HANDLECHARGE_NW) AS FEE ");
			sql.append(" FROM RPONBLOCKTAB AS A ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN ");
			sql.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1' ");
			sql.append((StrUtils.isNotEmpty(sqlPath2)?" AND "+sqlPath2:"")); 
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP ");
			
			SQL = sql.toString();
			logger.debug("getSQLForBat_NW.txlist sql===>"+SQL);
		}
		if(functionName.equals("pending")){
			sqlPath=sqlPath.replace("(A.BIZDATE =", "(A.OBIZDATE =");
			sqlPath=sqlPath.replace("A.CLEARINGPHASE =", "A.OCLEARINGPHASE =");
			sqlPath2=sqlPath2.replace("(A.BIZDATE =", "(A.OBIZDATE =");
			sqlPath2=sqlPath2.replace("A.CLEARINGPHASE =", "A.OCLEARINGPHASE =");
			sql.append(" WITH TEMP AS ");
			sql.append(" ( ");
			sql.append(" SELECT ");
			sql.append(" A.OBIZDATE,A.OCLEARINGPHASE,A.TXID,A.PCODE,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID ");
			sql.append(" ,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.RESULTSTATUS ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,A.SENDERBANK,SUBSTR(A.OSTAN,4,7) AS STAN ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -A.TXAMT ELSE A.TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN ");
			sql.append(" ,ABS(A.SENDERFEE) AS SENDERFEE,(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN ");
			sql.append(" ,ABS(A.INFEE) AS INFEE,(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN ");
			sql.append(" ,ABS(A.OUTFEE) AS OUTFEE,(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN ");
			sql.append(" ,ABS(A.EACHFEE) AS EACHFEE,ABS(B.FEE) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON B.BIZDATE=A.OBIZDATE AND B.STAN=A.OSTAN  ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON A.OTXDATE=C.TXDATE AND A.OSTAN=C.STAN  ");
			sql.append(" WHERE 1=1  ");
			sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
			sql.append(" UNION ALL ");
			sql.append(" SELECT ");
			sql.append(" A.OBIZDATE,A.OCLEARINGPHASE,A.TXID,A.PCODE,A.SENDERBANKID,A.INBANKID,A.OUTBANKID,A.SENDERID ");
			sql.append(" ,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.RESULTSTATUS ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,A.SENDERBANK,SUBSTR(A.OSTAN,4,7) AS STAN ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -A.TXAMT ELSE A.TXAMT END) AS TXAMT ");
			sql.append(" ,(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN ");
			sql.append(" ,ABS(A.SENDERFEE) AS SENDERFEE,(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN ");
			sql.append(" ,ABS(A.INFEE) AS INFEE,(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN ");
			sql.append(" ,ABS(A.OUTFEE) AS OUTFEE,(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN ");
			sql.append(" ,ABS(A.EACHFEE) AS EACHFEE,ABS(B.FEE) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON B.BIZDATE=A.OBIZDATE AND B.STAN=A.OSTAN  ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON A.OTXDATE=C.TXDATE AND A.OSTAN=C.STAN  ");
			sql.append(" WHERE 1=1  ");
			sql.append((StrUtils.isNotEmpty(sqlPath2)?" AND "+sqlPath2:""));
			sql.append(" ) ");
			sql.append(" SELECT * FROM TEMP  ");
			
				
			SQL = sql.toString();
			logger.debug("pending sql===>"+SQL);
		}
		if(functionName.equals("pendresult")){
			sql.append(" WITH TEMP AS ( ");
			sql.append(" SELECT A.BIZDATE AS PENDINGBIZDATE,A.CLEARINGPHASE AS PENDINGCLEARINGPHASE,(CASE A.RESULTCODE WHEN '01' THEN '01' ELSE '00' END) AS RESULTCODE ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT,B.PCODE,B.SENDERBANK,SUBSTR(B.STAN,4,7) AS STAN,B.TXID,B.SENDERBANKID,B.INBANKID,B.OUTBANKID ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -B.TXAMT ELSE B.TXAMT END) AS TXAMT,B.SENDERID,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.BIZDATE,B.CLEARINGPHASE ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.SENDERFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) END) AS SENDERFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.SENDERFEE) ELSE '0.00' END) AS SENDERFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.INFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) END) AS INFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.INFEE) ELSE '0.00' END) AS INFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.OUTFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) END) AS OUTFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.EACHFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) END) AS EACHFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.EACHFEE) ELSE '0.00' END) AS EACHFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(B.FEE) ELSE '0.00' END) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON A.OTXDATE=B.TXDATE AND A.OSTAN=B.STAN ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON B.TXDATE=C.TXDATE AND B.STAN=C.STAN ");
			sql.append(" WHERE COALESCE(A.BIZDATE,'') <> '' AND COALESCE(A.CLEARINGPHASE,'') <> '' ");
			sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
			sql.append(" UNION ALL ");
			sql.append(" SELECT A.BIZDATE AS PENDINGBIZDATE,A.CLEARINGPHASE AS PENDINGCLEARINGPHASE,(CASE A.RESULTCODE WHEN '01' THEN '01' ELSE '00' END) AS RESULTCODE ");
			sql.append(" ,VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT,B.PCODE,B.SENDERBANK,SUBSTR(B.STAN,4,7) AS STAN,B.TXID,B.SENDERBANKID,B.INBANKID,B.OUTBANKID ");
			sql.append(" ,(CASE B.ACCTCODE WHEN '0' THEN -B.TXAMT ELSE B.TXAMT END) AS TXAMT,B.SENDERID,B.INID,B.OUTID,B.INACCTNO,B.OUTACCTNO,B.CONMEMO,B.BIZDATE,B.CLEARINGPHASE ");
			sql.append(" ,C.USERNO,C.COMPANYID,C.SENDERDATA ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.SENDERFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) END) AS SENDERFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.SENDERFEE) ELSE '0.00' END) AS SENDERFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.INFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) END) AS INFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.INFEE) ELSE '0.00' END) AS INFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.OUTFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) END) AS OUTFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.EACHFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) END) AS EACHFEESIGN ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(A.EACHFEE) ELSE '0.00' END) AS EACHFEE ");
			sql.append(" ,(CASE A.RESULTCODE WHEN '01' THEN ABS(B.FEE) ELSE '0.00' END) AS FEE ");
			sql.append(" FROM RPONPENDINGTAB A ");
			sql.append(" LEFT JOIN RPONBLOCKTAB B ON A.OTXDATE=B.TXDATE AND A.OSTAN=B.STAN ");
			sql.append(" LEFT JOIN ONBLOCKAPPENDTAB C ON B.TXDATE=C.TXDATE AND B.STAN=C.STAN ");
			sql.append(" WHERE COALESCE(A.BIZDATE,'') <> '' AND COALESCE(A.CLEARINGPHASE,'') <> '' ");
			sql.append((StrUtils.isNotEmpty(sqlPath2)?" AND "+sqlPath2:""));
			sql.append(" )   ");
			sql.append(" SELECT * FROM TEMP ");
			SQL = sql.toString();
			logger.debug("pendresult sql===>"+SQL);
		}
		return SQL;
	}
	
	
	public String getSQLForWO(String sqlPath ){
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		sql.append(" SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,A.PCODE,A.SENDERBANK,SUBSTR(A.STAN,4,7) AS STAN,A.TXID ");
		sql.append(" ,A.SENDERPAYBANKID,A.INPAYBANKID,A.OUTPAYBANKID,A.INACCTNO,A.BIZDATE,A.CLEARINGPHASE,A.RESULTSTATUS ");
		sql.append(" ,(CASE A.ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT  ");
		sql.append(" ,(CASE WHEN A.WOFEE < 0 THEN '-' ELSE '+' END) AS WOFEESIGN,ABS(A.WOFEE) AS WOFEE ");
		sql.append(" ,A.BILLTYPE , COALESCE(  B.BILLDATA,'')  BILLDATA  ,A.TOLLID,A.PFCLASS,A.BILLFLAG ");
		sql.append(" FROM RPONBLOCKTAB AS A  ");
		sql.append(" LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN ");
		sql.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1'  AND A.PCODE LIKE '27%' ");
		sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
		SQL = sql.toString();
		logger.debug("getSQLForWO.txlist sql===>"+SQL);
		return SQL;
	}
	
	public String getSQLForWO_NW(String sqlPath ){
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		sql.append(" SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,A.PCODE,A.SENDERBANK,SUBSTR(A.STAN,4,7) AS STAN,A.TXID ");
		sql.append(" ,A.SENDERPAYBANKID,A.INPAYBANKID,A.OUTPAYBANKID,A.INACCTNO,A.BIZDATE,A.CLEARINGPHASE,A.RESULTSTATUS ");
		sql.append(" ,(CASE A.ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT  ");
		sql.append(" ,(CASE WHEN A.WOFEE_NW < 0 THEN '-' ELSE '+' END) AS WOFEESIGN,ABS(A.WOFEE_NW) AS WOFEE ");
		sql.append(" ,A.BILLTYPE , COALESCE(  B.BILLDATA,'')  BILLDATA  ,A.TOLLID,A.PFCLASS,A.BILLFLAG ");
		sql.append(" FROM RPONBLOCKTAB AS A  ");
		sql.append(" LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN ");
		sql.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1'  AND A.PCODE LIKE '27%' ");
		sql.append((StrUtils.isNotEmpty(sqlPath)?" AND "+sqlPath:""));
		SQL = sql.toString();
		logger.debug("getSQLForWO.txlist sql===>"+SQL);
		return SQL;
	}
	
	
	/**
	 * 未過濾整批的SQL
	 *
	 * @param functionName
	 * @param condition
	 * @return
	 */
	public String getSQL(String functionName,String condition){
		StringBuffer fromAndWhere = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		String SQL = "";
		if(functionName.equals("txlist")){
			fromAndWhere.append("FROM(");
			fromAndWhere.append("SELECT TXDT,PCODE,SENDERBANK,STAN,TXID,SENDERSTATUS,(CASE ACCTCODE WHEN '0' THEN -TXAMT ELSE TXAMT END) AS TXAMT,SENDERBANKID,INBANKID,OUTBANKID,SENDERID,INID,OUTID,INACCTNO,OUTACCTNO,SENDERACQUIRE,INACQUIRE,OUTACQUIRE,(CASE WHEN SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN,ABS(SENDERFEE) AS SENDERFEE,(CASE WHEN INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN,ABS(INFEE) AS INFEE,(CASE WHEN OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN,ABS(OUTFEE) AS OUTFEE,(CASE WHEN EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN,ABS(EACHFEE) AS EACHFEE,ABS(FEE) AS FEE,BIZDATE,CLEARINGPHASE,RESULTSTATUS,TXDATE,CONMEMO");
			fromAndWhere.append(" FROM RPONBLOCKTAB");
			fromAndWhere.append(") AS A LEFT JOIN ONBLOCKAPPENDTAB B ON A.TXDATE=B.TXDATE AND A.STAN=B.STAN");
			fromAndWhere.append(" WHERE A.RESULTSTATUS != 'R' AND A.SENDERSTATUS !='1' ");
			fromAndWhere.append((StrUtils.isNotEmpty(condition)?" AND "+condition:""));
			
			sql.append("SELECT VARCHAR_FORMAT(A.TXDT,'YYYYMMDDHH24MISS') AS TXDT,");
			sql.append("A.PCODE,");
			sql.append("A.SENDERBANK,");
			sql.append("SUBSTR(A.STAN,4,7) AS STAN,");
			sql.append("A.TXID,");
			sql.append("A.TXAMT,");
			sql.append("A.SENDERBANKID,");
			sql.append("A.INBANKID,");
			sql.append("A.OUTBANKID,");
			sql.append("A.SENDERID,");
			sql.append("A.INID,");
			sql.append("A.OUTID,");
			sql.append("A.INACCTNO,");
			sql.append("A.OUTACCTNO,");
			sql.append("B.USERNO,");
			sql.append("B.COMPANYID,");
			sql.append("A.CONMEMO,");
			sql.append("B.SENDERDATA,");
			sql.append("A.SENDERFEESIGN,");
			sql.append("A.SENDERFEE,");
			sql.append("A.INFEESIGN,");
			sql.append("A.INFEE,");
			sql.append("A.OUTFEESIGN,");
			sql.append("A.OUTFEE,");
			sql.append("A.EACHFEESIGN,");
			sql.append("A.EACHFEE,");
			sql.append("A.FEE,");
			sql.append("A.BIZDATE,");
			sql.append("A.CLEARINGPHASE,");
			sql.append("A.RESULTSTATUS ");
			sql.append(fromAndWhere);
			
			SQL = sql.toString();
			logger.debug("txlist sql===>"+SQL);
		}
		if(functionName.equals("pending")){
			fromAndWhere.append("FROM RPONPENDINGTAB A ");
			fromAndWhere.append("LEFT JOIN RPONBLOCKTAB B ON B.BIZDATE=A.OBIZDATE AND B.STAN=A.OSTAN ");
			fromAndWhere.append("LEFT JOIN ONBLOCKAPPENDTAB C ON A.OTXDATE=C.TXDATE AND A.OSTAN=C.STAN WHERE ");
			condition=condition.replace("(A.BIZDATE =", "(A.OBIZDATE =");
			condition=condition.replace("A.CLEARINGPHASE =", "A.OCLEARINGPHASE =");
			fromAndWhere.append(condition);
//			fromAndWhere.append(" AND  A.OCLEARINGPHASE = '01' ");
			
			
//			sql.append("SELECT B.TXDT,");
			sql.append("SELECT VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT,");
			sql.append("A.PCODE,");
			sql.append("A.SENDERBANK,");
			sql.append("SUBSTR(A.OSTAN,4,7) AS STAN,");
			sql.append("A.TXID,");
			sql.append("(CASE B.ACCTCODE WHEN '0' THEN -A.TXAMT ELSE A.TXAMT END) AS TXAMT,");
			sql.append("A.SENDERBANKID,");
			sql.append("A.INBANKID,");
			sql.append("A.OUTBANKID,");
			sql.append("A.SENDERID,"); 
			sql.append("B.INID,"); 
			sql.append("B.OUTID,"); 
			sql.append("B.INACCTNO,"); 
			sql.append("B.OUTACCTNO,");
			sql.append("C.USERNO,");
			sql.append("C.COMPANYID,");
			sql.append("B.CONMEMO,");
			sql.append("C.SENDERDATA,");
			sql.append("(CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) AS SENDERFEESIGN,");
			sql.append("ABS(A.SENDERFEE) AS SENDERFEE,");
			sql.append("(CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) AS INFEESIGN,");
			sql.append("ABS(A.INFEE) AS INFEE,");
			sql.append("(CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) AS OUTFEESIGN,");
			sql.append("ABS(A.OUTFEE) AS OUTFEE,");
			sql.append("(CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) AS EACHFEESIGN,");
			sql.append("ABS(A.EACHFEE) AS EACHFEE,");
			sql.append("ABS(B.FEE) AS FEE,");
			sql.append("A.OBIZDATE,");
			sql.append("A.OCLEARINGPHASE,");
			sql.append("B.RESULTSTATUS ");
			sql.append(fromAndWhere);
			
			SQL = sql.toString();
			logger.debug("pending sql===>"+SQL);
		}
		if(functionName.equals("pendresult")){
			fromAndWhere.append("FROM RPONPENDINGTAB A LEFT JOIN RPONBLOCKTAB B ON A.OTXDATE=B.TXDATE AND A.OSTAN=B.STAN ");
			fromAndWhere.append("LEFT JOIN ONBLOCKAPPENDTAB C ON B.TXDATE=C.TXDATE AND B.STAN=C.STAN ");
			fromAndWhere.append("WHERE COALESCE(A.BIZDATE,'') <> '' AND COALESCE(A.CLEARINGPHASE,'') <> '' ");
			fromAndWhere.append((StrUtils.isEmpty(condition)?"":" AND "+condition));
			
			sql.append("SELECT VARCHAR_FORMAT(B.TXDT,'YYYYMMDDHH24MISS') AS TXDT,");
			sql.append("B.PCODE,");
			sql.append("B.SENDERBANK,");
			sql.append("SUBSTR(B.STAN,4,7) AS STAN,");
			sql.append("B.TXID,");
			sql.append("(CASE B.ACCTCODE WHEN '0' THEN -B.TXAMT ELSE B.TXAMT END) AS TXAMT,");
			sql.append("B.SENDERBANKID,");
			sql.append("B.INBANKID,");
			sql.append("B.OUTBANKID,");
			sql.append("B.SENDERID,");
			sql.append("B.INID,");
			sql.append("B.OUTID,");
			sql.append("B.INACCTNO,");
			sql.append("B.OUTACCTNO,");
			sql.append("C.USERNO,");
			sql.append("C.COMPANYID,");
			sql.append("B.CONMEMO,");
			sql.append("C.SENDERDATA,");
			sql.append("B.BIZDATE,");
			sql.append("B.CLEARINGPHASE,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.SENDERFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.SENDERFEE < 0 THEN '-' ELSE '+' END) END) AS SENDERFEESIGN,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN ABS(A.SENDERFEE) ELSE '0.00' END) AS SENDERFEE,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.INFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.INFEE < 0 THEN '-' ELSE '+' END) END) AS INFEESIGN,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN ABS(A.INFEE) ELSE '0.00' END) AS INFEE,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.OUTFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.OUTFEE < 0 THEN '-' ELSE '+' END) END) AS OUTFEESIGN,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN ABS(A.OUTFEE) ELSE '0.00' END) AS OUTFEE,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN (CASE WHEN -A.EACHFEE < 0 THEN '-' ELSE '+' END) ELSE (CASE WHEN A.EACHFEE < 0 THEN '-' ELSE '+' END) END) AS EACHFEESIGN,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN ABS(A.EACHFEE) ELSE '0.00' END) AS EACHFEE,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN ABS(B.FEE) ELSE '0.00' END) AS FEE,");
			sql.append("A.BIZDATE AS PENDINGBIZDATE,");
			sql.append("A.CLEARINGPHASE AS PENDINGCLEARINGPHASE,");
			sql.append("(CASE A.RESULTCODE WHEN '01' THEN '01' ELSE '00' END) AS RESULTCODE ");
			sql.append(fromAndWhere);
			
			SQL = sql.toString();
			logger.debug("pendresult sql===>"+SQL);
		}
		return SQL;
	}
	
	public Map<String,Object> getColumnMap(String functionName){
		Map<String,Object> columnMap = new HashMap<String,Object>();
		List<String> columnNameList = new ArrayList<String>();
		List<Integer> columnLengthList = new ArrayList<Integer>();
		List<String> columnTypeList = new ArrayList<String>();
		
		if(functionName.equals("txlist")){
			//交易日期時間
			columnNameList.add("TXDT");
			columnLengthList.add(14);
			columnTypeList.add("string");
			//交易類別
			columnNameList.add("PCODE");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//發動單位代號
			columnNameList.add("SENDERBANK");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易追踨序號
			columnNameList.add("STAN");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易金額
			columnNameList.add("TXAMT");
			columnLengthList.add(13);
			columnTypeList.add("decimal(0)");
			//發動行代號
			columnNameList.add("SENDERBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//入帳行代號
			columnNameList.add("INBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//扣款行代號
			columnNameList.add("OUTBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動者統編
			columnNameList.add("SENDERID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//入帳者統一編號
			columnNameList.add("INID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//扣款者統一編號
			columnNameList.add("OUTID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//入帳帳號
			columnNameList.add("INACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//扣款帳號
			columnNameList.add("OUTACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//用戶號碼
			columnNameList.add("USERNO");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//上市櫃公司代號
			columnNameList.add("COMPANYID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//存摺摘要
			columnNameList.add("CONMEMO");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者專用區
			columnNameList.add("SENDERDATA");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動單位手續費正負號
			columnNameList.add("SENDERFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動單位手續費
			columnNameList.add("SENDERFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳單位手續費正負號
			columnNameList.add("INFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳單位手續費
			columnNameList.add("INFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款單位手續費正負號
			columnNameList.add("OUTFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款單位手續費
			columnNameList.add("OUTFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//交換所手續費正負號
			columnNameList.add("EACHFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//交換所手續費
			columnNameList.add("EACHFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//客戶支付手續費
			columnNameList.add("FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//營業日
			columnNameList.add("BIZDATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//清算階段代號
			columnNameList.add("CLEARINGPHASE");
			columnLengthList.add(2);
			columnTypeList.add("string");
			//交易結果
			columnNameList.add("RESULTSTATUS");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("txlist_W")){
			//交易日期時間
			columnNameList.add("TXDT");
			columnLengthList.add(14);
			columnTypeList.add("string");
			//交易追踨序號
			columnNameList.add("STAN");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易金額
			columnNameList.add("TXAMT");
			columnLengthList.add(13);
			columnTypeList.add("decimal(0)");
			//入帳行代號
			columnNameList.add("INPAYBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//入帳帳號
			columnNameList.add("INACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//銷帳單位手續費正負號
			columnNameList.add("WOFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳單位手續費
			columnNameList.add("WOFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//銷帳資料類型
			columnNameList.add("BILLTYPE");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//銷帳資料區
			columnNameList.add("BILLDATA");
			columnLengthList.add(50);
			columnTypeList.add("string");
			//收費業者統一編號
			columnNameList.add("TOLLID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//繳費類別
			columnNameList.add("PFCLASS");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//銷帳單位
			columnNameList.add("BILLFLAG");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("pending")){
			//交易日期時間
			columnNameList.add("TXDT");
			columnLengthList.add(14);
			columnTypeList.add("string");
			//交易類別
			columnNameList.add("PCODE");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//發動單位代號
			columnNameList.add("SENDERBANK");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易追踨序號
			columnNameList.add("STAN");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易金額
			columnNameList.add("TXAMT");
			columnLengthList.add(13);
			columnTypeList.add("decimal(0)");
			//發動行代號
			columnNameList.add("SENDERBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//入帳行代號
			columnNameList.add("INBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//扣款行代號
			columnNameList.add("OUTBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動者統編
			columnNameList.add("SENDERID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//入帳者統一編號
			columnNameList.add("INID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//扣款者統一編號
			columnNameList.add("OUTID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//入帳帳號
			columnNameList.add("INACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//扣款帳號
			columnNameList.add("OUTACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//用戶號碼
			columnNameList.add("USERNO");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//上市櫃公司代號
			columnNameList.add("COMPANYID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//存摺摘要
			columnNameList.add("MEMO");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者專用區
			columnNameList.add("SENDERDATA");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//發動單位手續費正負號
			columnNameList.add("SENDERFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//發動單位手續費
			columnNameList.add("SENDERFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//入帳單位手續費正負號
			columnNameList.add("INFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//入帳單位手續費
			columnNameList.add("INFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//扣款單位手續費正負號
			columnNameList.add("OUTFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//扣款單位手續費
			columnNameList.add("OUTFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//交換所手續費正負號
			columnNameList.add("EACHFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//交換所手續費
			columnNameList.add("EACHFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//客戶支付手續費
			columnNameList.add("FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//營業日
			columnNameList.add("BIZDATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//清算階段代號
			columnNameList.add("CLEARINGPHASE");
			columnLengthList.add(2);
			columnTypeList.add("string");
			//交易結果
			columnNameList.add("RESULTSTATUS");
			columnLengthList.add(1);
			columnTypeList.add("string");
		}
		if(functionName.equals("pendresult")){
			//交易日期時間
			columnNameList.add("TXDT");
			columnLengthList.add(14);
			columnTypeList.add("string");
			//交易類別
			columnNameList.add("PCODE");
			columnLengthList.add(4);
			columnTypeList.add("string");
			//發動單位代號
			columnNameList.add("SENDERBANK");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易追踨序號
			columnNameList.add("STAN");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//交易代號
			columnNameList.add("TXID");
			columnLengthList.add(3);
			columnTypeList.add("string");
			//交易金額
			columnNameList.add("TXAMT");
			columnLengthList.add(13);
			columnTypeList.add("decimal(0)");
			//發動行代號
			columnNameList.add("SENDERBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//入帳行代號
			columnNameList.add("INBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//扣款行代號
			columnNameList.add("OUTBANKID");
			columnLengthList.add(7);
			columnTypeList.add("string");
			//發動者統編
			columnNameList.add("SENDERID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//入帳者統一編號
			columnNameList.add("INID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//扣款者統一編號
			columnNameList.add("OUTID");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//入帳帳號
			columnNameList.add("INACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//扣款帳號
			columnNameList.add("OUTACCTNO");
			columnLengthList.add(16);
			columnTypeList.add("string");
			//用戶號碼
			columnNameList.add("USERNO");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//上市櫃公司代號
			columnNameList.add("COMPANYID");
			columnLengthList.add(6);
			columnTypeList.add("string");
			//存摺摘要
			columnNameList.add("MEMO");
			columnLengthList.add(10);
			columnTypeList.add("string");
			//發動者專用區
			columnNameList.add("SENDERDATA");
			columnLengthList.add(20);
			columnTypeList.add("string");
			//營業日
			columnNameList.add("BIZDATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//清算階段代號
			columnNameList.add("CLEARINGPHASE");
			columnLengthList.add(2);
			columnTypeList.add("string");
			//沖正-發動單位手續費正負號
			columnNameList.add("SENDERFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//沖正-發動單位手續費
			columnNameList.add("SENDERFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//沖正-入帳單位手續費正負號
			columnNameList.add("INFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//沖正-入帳單位手續費
			columnNameList.add("INFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//沖正-扣款單位手續費正負號
			columnNameList.add("OUTFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//沖正-扣款單位手續費
			columnNameList.add("OUTFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//沖正-交換所手續費正負號
			columnNameList.add("EACHFEESIGN");
			columnLengthList.add(1);
			columnTypeList.add("string");
			//沖正-交換所手續費
			columnNameList.add("EACHFEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//沖正-客戶支付手續費
			columnNameList.add("FEE");
			columnLengthList.add(5);
			columnTypeList.add("decimal(2)");
			//未完成交易結果處理營業日
			columnNameList.add("PENDINGBIZDATE");
			columnLengthList.add(8);
			columnTypeList.add("string");
			//未完成交易結果清算階段代號
			columnNameList.add("PENDINGCLEARINGPHASE");
			columnLengthList.add(2);
			columnTypeList.add("string");
			//未完成交易處理結果
			columnNameList.add("RESULTCODE");
			columnLengthList.add(2);
			columnTypeList.add("string");
		}
		
		columnMap.put("columnName",columnNameList);
		columnMap.put("columnLength",columnLengthList);
		columnMap.put("columnType",columnTypeList);
		
		return columnMap;
	}
	public String getFirstRow(String functionName,String type,String date){
		String firstRow = "";
		
		if(functionName.equals("txlist")){
			if(type.equals("S")){
				firstRow = "BOFEA12" + date;
			}
			if(type.equals("B")){
				firstRow = "BOFEA12" + date;
			}
			if(type.equals("I")){
				firstRow = "BOFEA12" + date;
			}
			if(type.equals("W")){
				firstRow = "BOFEA19" + date;
			}
		}
		if(functionName.equals("pending")){
			if(type.equals("S")){
				firstRow = "BOFEA15" + date;
			}
			if(type.equals("B")){
				firstRow = "BOFEA15" + date;
			}
			if(type.equals("I")){
				firstRow = "BOFEA15" + date;
			}
		}
		if(functionName.equals("pendresult")){
			if(type.equals("S")){
				firstRow = "BOFEA18" + date;
			}
			if(type.equals("B")){
				firstRow = "BOFEA18" + date;
			}
			if(type.equals("I")){
				firstRow = "BOFEA18" + date;
			}
		}
		return firstRow;
	}
	public String getLastRow(String functionName,String type,String dataCount){
		String lastRow = "";
		
		if(functionName.equals("txlist")){
			if(type.equals("S")){
				lastRow = "EOFEA12" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("B")){
				lastRow = "EOFEA12" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("I")){
				lastRow = "EOFEA12" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("W")){
				lastRow = "EOFEA19" + codeUtils.padText("number",6,dataCount);
			}
		}
		if(functionName.equals("pending")){
			if(type.equals("S")){
				lastRow = "EOFEA15" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("B")){
				lastRow = "EOFEA15" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("I")){
				lastRow = "EOFEA15" + codeUtils.padText("number",6,dataCount);
			}
		}
		if(functionName.equals("pendresult")){
			if(type.equals("S")){
				lastRow = "EOFEA18" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("B")){
				lastRow = "EOFEA18" + codeUtils.padText("number",6,dataCount);
			}
			if(type.equals("I")){
				lastRow = "EOFEA18" + codeUtils.padText("number",6,dataCount);
			}
		}
		return lastRow;
	}
	public Map<String,Object> getFilenameListAndDataList(String date,String opbkId,String clearingphase){
		Map<String,Object> filenameAndDataMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> queryListMap;
		//Zip裡面各個TXT的檔名
		List<String> filenameList = new ArrayList<String>();
		byte[] byteTXT = null;
		//放置TXT Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		//訊息
		String message = "";
		////////////////////////////////////////////////////////////////////
		//交易資料查詢
		////////////////////////////////////////依發動行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getSenderCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap(getSQLForBat("txlist",getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath") ,getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath2")), null );
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","S",date),getLastRow("txlist","S","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist"),getFirstRow("txlist","S",date),getLastRow("txlist","S",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_S_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢發動行="+opbkId+"的交易資料過程出現問題");
		}
		///////////////////////////////////////////////依扣款行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getOutCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat("txlist", getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","B",date),getLastRow("txlist","B","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist"),getFirstRow("txlist","B",date),getLastRow("txlist","B",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_B_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢扣款行="+opbkId+"的交易資料過程出現問題");
		}
		///////////////////////////////////////////////依入帳行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat("txlist", getInConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getInConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","I",date),getLastRow("txlist","I","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist"),getFirstRow("txlist","I",date),getLastRow("txlist","I",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_I_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢入帳行="+opbkId+"的交易資料過程出現問題");
		}
		///////////////////////////////////////////////依銷帳行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForWO(getWOCondition(date, opbkId, clearingphase).get("sqlPath")) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","W",date),getLastRow("txlist","W","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist_W"),getFirstRow("txlist","W",date),getLastRow("txlist","W",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_W_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢銷帳行="+opbkId+"的交易資料過程出現問題");
		}
		
		
		
		
		///////////////////////////////////////////////////////////////////
		//未完成交易資料查詢
		//////////////////////////////////////////////////////依發動行
//		queryListMap = codeUtils.queryListMap(getSQL("pending",getSenderCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap(getSQLForBat("pending",getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath") ,getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath2")), null );
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pending","S",date),getLastRow("pending","S","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pending"),getFirstRow("pending","S",date),getLastRow("pending","S",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pending_S_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢發動行="+opbkId+"的未完成交易資料過程出現問題");
		}
		//////////////////////////////////////////////////////依扣款行
//		queryListMap = codeUtils.queryListMap(getSQL("pending",getOutCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat("pending", getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pending","B",date),getLastRow("pending","B","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pending"),getFirstRow("pending","B",date),getLastRow("pending","B",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pending_B_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢扣款行="+opbkId+"的未完成交易資料過程出現問題");
		}
		//////////////////////////////////////////////////////依入帳行
		queryListMap = codeUtils.queryListMap(getSQL("pending",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat("pending", getInConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getInConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pending","I",date),getLastRow("pending","I","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pending"),getFirstRow("pending","I",date),getLastRow("pending","I",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pending_I_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢入帳行="+opbkId+"的未完成交易資料過程出現問題");
		}
		//////////////////////////////////////////////////////////////////
		//未完成交易結果查詢
		//////////////////////////////////////////////////////依發動行
//		queryListMap = codeUtils.queryListMap(getSQL("pendresult",getSenderCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap(getSQLForBat("pendresult",getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath") ,getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath2")), null );
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pendresult","S",date),getLastRow("pendresult","S","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pendresult"),getFirstRow("pendresult","S",date),getLastRow("pendresult","S",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pendresult_S_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢發動行="+opbkId+"的未完成交易結果過程出現問題");
		}
		//////////////////////////////////////////////////////依扣款行
//		queryListMap = codeUtils.queryListMap(getSQL("pendresult",getOutCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat("pendresult", getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pendresult","B",date),getLastRow("pendresult","B","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pendresult"),getFirstRow("pendresult","B",date),getLastRow("pendresult","B",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pendresult_B_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢扣款行="+opbkId+"的未完成交易結果過程出現問題");
		}
		//////////////////////////////////////////////////////依入帳行
//		queryListMap = codeUtils.queryListMap(getSQL("pendresult",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat("pendresult", getInConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getInConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pendresult","I",date),getLastRow("pendresult","I","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pendresult"),getFirstRow("pendresult","I",date),getLastRow("pendresult","I",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pendresult_I_"+date+"_"+clearingphase+"_OLD.txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢入帳行="+opbkId+"的未完成交易結果過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////
		filenameAndDataMap.put("filenameList",filenameList);
		filenameAndDataMap.put("dataList",dataList);
		filenameAndDataMap.put("message",message);
		
		return filenameAndDataMap;
	}
	
	
	public Map<String,Object> getFilenameListAndDataList_NW(String date,String opbkId,String clearingphase){
		Map<String,Object> filenameAndDataMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> queryListMap;
		//Zip裡面各個TXT的檔名
		List<String> filenameList = new ArrayList<String>();
		byte[] byteTXT = null;
		//放置TXT Byte[]的List
		List<byte[]> dataList = new ArrayList<byte[]>();
		//訊息
		String message = "";
		////////////////////////////////////////////////////////////////////
		//交易資料查詢
		////////////////////////////////////////依發動行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getSenderCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap(getSQLForBat_NW("txlist",getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath") ,getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath2")), null );
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","S",date),getLastRow("txlist","S","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist"),getFirstRow("txlist","S",date),getLastRow("txlist","S",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_S_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢發動行="+opbkId+"的交易資料過程出現問題");
		}
		///////////////////////////////////////////////依扣款行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getOutCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat_NW("txlist", getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","B",date),getLastRow("txlist","B","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist"),getFirstRow("txlist","B",date),getLastRow("txlist","B",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_B_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢扣款行="+opbkId+"的交易資料過程出現問題");
		}
		///////////////////////////////////////////////依入帳行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat_NW("txlist", getInConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getInConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","I",date),getLastRow("txlist","I","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist"),getFirstRow("txlist","I",date),getLastRow("txlist","I",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_I_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢入帳行="+opbkId+"的交易資料過程出現問題");
		}
		///////////////////////////////////////////////依銷帳行
//		queryListMap = codeUtils.queryListMap(getSQL("txlist",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForWO_NW(getWOCondition(date, opbkId, clearingphase).get("sqlPath")) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("txlist","W",date),getLastRow("txlist","W","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("txlist_W"),getFirstRow("txlist","W",date),getLastRow("txlist","W",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_txlist_W_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢銷帳行="+opbkId+"的交易資料過程出現問題");
		}
		
		
		
		
		///////////////////////////////////////////////////////////////////
		//未完成交易資料查詢
		//////////////////////////////////////////////////////依發動行
//		queryListMap = codeUtils.queryListMap(getSQL("pending",getSenderCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap(getSQLForBat_NW("pending",getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath") ,getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath2")), null );
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pending","S",date),getLastRow("pending","S","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pending"),getFirstRow("pending","S",date),getLastRow("pending","S",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pending_S_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢發動行="+opbkId+"的未完成交易資料過程出現問題");
		}
		//////////////////////////////////////////////////////依扣款行
//		queryListMap = codeUtils.queryListMap(getSQL("pending",getOutCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat_NW("pending", getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pending","B",date),getLastRow("pending","B","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pending"),getFirstRow("pending","B",date),getLastRow("pending","B",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pending_B_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢扣款行="+opbkId+"的未完成交易資料過程出現問題");
		}
		//////////////////////////////////////////////////////依入帳行
		queryListMap = codeUtils.queryListMap( getSQLForBat_NW("pending", getInConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getInConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pending","I",date),getLastRow("pending","I","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pending"),getFirstRow("pending","I",date),getLastRow("pending","I",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pending_I_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢入帳行="+opbkId+"的未完成交易資料過程出現問題");
		}
		//////////////////////////////////////////////////////////////////
		//未完成交易結果查詢
		//////////////////////////////////////////////////////依發動行
//		queryListMap = codeUtils.queryListMap(getSQL("pendresult",getSenderCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap(getSQLForBat_NW("pendresult",getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath") ,getSenderConditionForBat(date,opbkId,clearingphase).get("sqlPath2")), null );
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pendresult","S",date),getLastRow("pendresult","S","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pendresult"),getFirstRow("pendresult","S",date),getLastRow("pendresult","S",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pendresult_S_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢發動行="+opbkId+"的未完成交易結果過程出現問題");
		}
		//////////////////////////////////////////////////////依扣款行
//		queryListMap = codeUtils.queryListMap(getSQL("pendresult",getOutCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat_NW("pendresult", getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getOutConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pendresult","B",date),getLastRow("pendresult","B","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pendresult"),getFirstRow("pendresult","B",date),getLastRow("pendresult","B",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pendresult_B_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢扣款行="+opbkId+"的未完成交易結果過程出現問題");
		}
		//////////////////////////////////////////////////////依入帳行
//		queryListMap = codeUtils.queryListMap(getSQL("pendresult",getInCondition(date,opbkId,clearingphase)),null);
		queryListMap = codeUtils.queryListMap( getSQLForBat_NW("pendresult", getInConditionForBat(date,opbkId,clearingphase).get("sqlPath") , getInConditionForBat(date,opbkId,clearingphase).get("sqlPath2") ) , null);
		//TXT的Byte[]
		if(queryListMap.size() == 0){
			byteTXT = codeUtils.createTXTWithoutData(getFirstRow("pendresult","I",date),getLastRow("pendresult","I","0"));
		}
		if(queryListMap.size() > 0){
			byteTXT = codeUtils.createTXT(queryListMap,getColumnMap("pendresult"),getFirstRow("pendresult","I",date),getLastRow("pendresult","I",String.valueOf(queryListMap.size())));
		}
		//正常
		if(byteTXT != null){
			filenameList.add("eACH_pendresult_I_"+date+"_"+clearingphase+".txt");
			dataList.add(byteTXT);
		}
		//有問題
		else{
			message = codeUtils.appendMessage(message,"查詢入帳行="+opbkId+"的未完成交易結果過程出現問題");
		}
		///////////////////////////////////////////////////////////////////////////
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
