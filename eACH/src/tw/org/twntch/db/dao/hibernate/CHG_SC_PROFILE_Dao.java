package tw.org.twntch.db.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.CHG_SC_PROFILE;
import tw.org.twntch.po.SC_COMPANY_PROFILE;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.StrUtils;

public class CHG_SC_PROFILE_Dao extends HibernateEntityDao<CHG_SC_PROFILE, String> {
	
	public SC_COMPANY_PROFILE getCompanyDataByCompanyId(String companyId){
		SC_COMPANY_PROFILE po = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ( ");
		sql.append("	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SC_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	UNION ");
		sql.append("	SELECT COMPANY_ID, COMPANY_ABBR_NAME, COMPANY_NAME FROM SD_COMPANY_PROFILE WHERE COMPANY_ID = ? ");
		sql.append("	FETCH FIRST 1 ROWS ONLY ");
		sql.append(") ");
		
		try {
			SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter(0, companyId);
			query.setParameter(1, companyId);
			String[] cols = {"COMPANY_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, SC_COMPANY_PROFILE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(SC_COMPANY_PROFILE.class));
			List<SC_COMPANY_PROFILE> list = query.list();
			if(list != null && list.size() > 0){
				po = list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("getCompanyDataByCompanyId.Exception>>"+e);
		}
		return po;
	}
	
	public List<CHG_SC_PROFILE> search(String con1, List val1, String orderSQL){
		List<CHG_SC_PROFILE> list = null;
		
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("A.SD_ITEM_NO, A.COMPANY_ID, A.COMPANY_ABBR_NAME, A.COMPANY_NAME, A.TXN_ID, A.INBANKID, ");
        sql.append("COALESCE((SELECT BRBK_NAME FROM BANK_BRANCH WHERE BRBK_ID = A.INBANKID), '') AS INBANKNAME, ");
        sql.append("A.INBANKACCTNO, A.LAYOUTID, A.DEALY_CHARGE_DAY, ");
        sql.append("VARCHAR_FORMAT(A.START_DATE, 'YYYY/MM/DD') AS START_DATE, ");
        sql.append("VARCHAR_FORMAT(A.STOP_DATE, 'YYYY/MM/DD') AS STOP_DATE, A.NOTE ");
        sql.append("FROM CHANGE_SC_PROFILE AS A ");
        sql.append(StrUtils.isEmpty(con1)?"":"WHERE " + con1);
        sql.append(orderSQL);
        System.out.println("### SQL >> " + sql.toString());
        
        try{
        	SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
        	for(int i = 0; i < val1.size(); i++){
        		query.setParameter(i, (String) val1.get(i));
        	}
        	String[] cols = {"SD_ITEM_NO", "COMPANY_ID", "COMPANY_ABBR_NAME", "COMPANY_NAME",
        	"TXN_ID", "INBANKID", "INBANKNAME", "INBANKACCTNO", "LAYOUTID",
        	"DEALY_CHARGE_DAY", "START_DATE", "STOP_DATE", "NOTE"};
			AutoAddScalar addScalar = new AutoAddScalar();
			addScalar.addScalar(query, CHG_SC_PROFILE.class, cols);
			query.setResultTransformer(Transformers.aliasToBean(CHG_SC_PROFILE.class));
			list = query.list();
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("CHG_SC_PROFILE_Dao.search() => " + e);
        }
        return list;
	}
}
