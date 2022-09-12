package tw.org.twntch.bo;

import java.util.List;
import java.util.Map;

import tw.org.twntch.db.dao.hibernate.BANK_GROUP_Dao;
import tw.org.twntch.db.dao.hibernate.SYS_PARA_Dao;
import tw.org.twntch.po.SYS_PARA;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.SpringAppCtxHelper;
/**
 * 
 * @author Hugo
 *如果使用的是Arguments的參數，此類別的屬性必須跟Arguments設定的key相同
 */
public class SYS {

public static String RPONBLOCKTAB_CL1_A = "";
public static String RPONBLOCKTAB_CL1_B = "";
public static String RP_CLEARPHASE1_TIME = "";
public static String RP_CLEARPHASE2_TIME = "";
public static String AP1 = "";
public static String AP2 = "";
public static String AP1_ISRUN = "";
public static String AP2_ISRUN = "";
public static String SERVER_IP = "";
private Arguments arguments	 ;
public SYS(){
	System.out.println("arguments>>"+arguments);
	arguments = (Arguments) (arguments== null ?SpringAppCtxHelper.getBean("arguments"):arguments);
	try {
		Map<String, String> map =(Map) arguments.getArgs();
		BeanUtils.setDeclaredPropertyII(this, map);
		doSYS_PARA();
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("SYS.Exception"+e);
	}
	System.out.println("RPONBLOCKTAB_CL1_A>>"+RPONBLOCKTAB_CL1_A);
}

public void doSYS_PARA(){
	SYS_PARA_Dao sys_para_Dao = SpringAppCtxHelper.getBean("sys_para_Dao") ;
	List<SYS_PARA> list = sys_para_Dao.getTopOne();
	if(list != null && list.size() != 0){
		
		for( SYS_PARA po :list){
			RP_CLEARPHASE1_TIME = po.getRP_CLEARPHASE1_TIME();
			RP_CLEARPHASE2_TIME = po.getRP_CLEARPHASE2_TIME();
			AP1 = po.getAP1();
			AP2 = po.getAP2();
			AP1_ISRUN = po.getAP1_ISRUN();
			AP2_ISRUN = po.getAP2_ISRUN();
		}
	}else{
		RP_CLEARPHASE1_TIME = "12:30:00";
		RP_CLEARPHASE2_TIME = "15:30:00";
	}
}

public static String getRPONBLOCKTAB_CL1_A() {
	return RPONBLOCKTAB_CL1_A;
}

public static void setRPONBLOCKTAB_CL1_A(String rPONBLOCKTAB_CL1_A) {
	RPONBLOCKTAB_CL1_A = rPONBLOCKTAB_CL1_A;
}

public static String getRPONBLOCKTAB_CL1_B() {
	return RPONBLOCKTAB_CL1_B;
}

public static void setRPONBLOCKTAB_CL1_B(String rPONBLOCKTAB_CL1_B) {
	RPONBLOCKTAB_CL1_B = rPONBLOCKTAB_CL1_B;
}

public Arguments getArguments() {
	return arguments;
}

public void setArguments(Arguments arguments) {
	this.arguments = arguments;
}






}
