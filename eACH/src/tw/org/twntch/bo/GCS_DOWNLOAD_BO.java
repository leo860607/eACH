package tw.org.twntch.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.StrUtils;

public class GCS_DOWNLOAD_BO {
	private CodeUtils codeUtils;

	
	//計算NETAMT是否加總為零
	public String sumNETAMT(Map<String,String> param){
		//回傳到頁面的ajax執行結果
		Map<String,String> resultMap = new HashMap<String,String>();
		
		//日期
		String date = StrUtils.isEmpty(param.get("TXDATE"))?"":DateTimeUtils.convertDate(param.get("TXDATE"), "yyyyMMdd", "yyyyMMdd");
		//清算階段代號
		String clearingphase = StrUtils.isEmpty(param.get("CLEARINGPHASE"))?"":param.get("CLEARINGPHASE");
		
		List<String> conditions = new ArrayList<String>();
		String condition = "";
		/* 20150210 HUANGPU 改以營業日(BIZDATE)查詢資料，非交易日期時間(TXDT) */
		if(StrUtils.isNotEmpty(date)){
			conditions.add(" BIZDATE = '" + date + "' ");
		}
		if(StrUtils.isNotEmpty(clearingphase)){
			conditions.add(" CLEARINGPHASE = '" + clearingphase + "' ");
		}
		for(int i = 0; i < conditions.size(); i++){
			condition += conditions.get(i);
			if(i < conditions.size() - 1){
				condition += " AND ";
			}
		}
		System.out.println("condition==>"+condition);
		
		String sumSQL = "SELECT SUM(X.NETAMT) AS NETAMTSUM FROM (SELECT (SUM(RECVAMT+RVSRECVAMT)+SUM(PAYAMT+RVSPAYAMT)) NETAMT FROM RPONCLEARINGTAB "+(StrUtils.isEmpty(condition)? "" : " WHERE " + condition)+") AS X(NETAMT)";
		
		List<Map<String,Object>> queryListMap = codeUtils.queryListMap(sumSQL,null);
		
		System.out.println("queryListMap.size() ="+queryListMap.size()+" ,queryListMap.get(0).get('NETAMTSUM') ="+queryListMap.get(0).get("NETAMTSUM"));
		
		//正常
		if(queryListMap.size() > 0 && queryListMap.get(0).get("NETAMTSUM") != null){
			resultMap.put("result","OK");
			resultMap.put("NETAMTSUM",String.valueOf(queryListMap.get(0).get("NETAMTSUM")));
		}
		//查無資料或查詢過程出現問題
		else if(queryListMap.size() > 0){
			resultMap.put("result","OK");
			resultMap.put("NETAMTSUM","");
		}
		else{
			resultMap.put("result","查無資料");
		}
		//組json回傳至頁面
		String gson = new Gson().toJson(resultMap);
		System.out.println("gson:"+gson);
		
		return gson;
	}
	
	public CodeUtils getCodeUtils() {
		return codeUtils;
	}

	public void setCodeUtils(CodeUtils codeUtils) {
		this.codeUtils = codeUtils;
	}
}
