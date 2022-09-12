package tw.org.twntch.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.FEE_CODE_BO;
import tw.org.twntch.bo.FEE_CODE_NWLVL_BO;
import tw.org.twntch.bo.FEE_CODE_NW_BO;
import tw.org.twntch.form.Fee_Code_NW_Form;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.FEE_CODE_NW;
import tw.org.twntch.po.FEE_CODE_NWLVL;
import tw.org.twntch.po.FEE_CODE_NW_PK;
import tw.org.twntch.po.FEE_CODE_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.CodeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;

public class FEE_CODE_NW_Action extends Action {
	private FEE_CODE_NW_BO fee_code_nw_bo;
	private FEE_CODE_NWLVL_BO fee_code_nwlvl_bo;
	private FEE_CODE_BO fee_code_bo;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("FEE_CODE_NW_Action is start");

		Fee_Code_NW_Form fee_code_nw_form = (Fee_Code_NW_Form) form;
		String ac_key = StrUtils.isEmpty(fee_code_nw_form.getAc_key()) ? "" : fee_code_nw_form.getAc_key();
		String target = StrUtils.isEmpty(fee_code_nw_form.getTarget()) ? "search" : fee_code_nw_form.getTarget();
		fee_code_nw_form.setTarget(target);
		System.out.println("ac_key>>" + ac_key);
		List<FEE_CODE_NW> list_nw = null;
		List<FEE_CODE> list = null;
		
		List<FEE_CODE_NWLVL> lvllist = null;
		if (ac_key.equalsIgnoreCase("search") || ac_key.equalsIgnoreCase("back")) {
			System.out.println("SerchStrs>>" + fee_code_nw_form.getSerchStrs());
			BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
			fee_code_nw_form.setResult("TRUE");
		}
		// (固定,外加) ,百分比
		if (ac_key.equalsIgnoreCase("add")) {
			Map map = new HashMap<>();
			
			if("A".equals(fee_code_nw_form.getFEE_TYPE())||"B".equals(fee_code_nw_form.getFEE_TYPE())) {
				//先判斷此 FEE_ID 在舊版手續費內生效的那筆是屬於固定還是外加 使用checkFeeCodeType function
				String fctype=fee_code_bo.checkFeeCodeType(fee_code_nw_form.getFEE_ID());
				System.out.println("@@@@@fctype:"+fctype);
				//若 新增的type = A 且 fctype = A 則新增到FEE_CODE      AA
				//若 新增的type = A 且 fctype = B 則新增到FEE_CODE_NW   AB
				//===================================================
				//若 新增的type = B 且 fctype = A 則新增到FEE_CODE_NW   BA
				//若 新增的type = B 且 fctype = B 則新增到FEE_CODE      BB
				//===================================================
				//若有一筆個交易代號沒有在新或舊資料庫(應該不會發生)
				//則第一筆新增到舊的                                                              AorB
				switch(fee_code_nw_form.getFEE_TYPE()+fctype) {
				case "AA":
				case "BB":
				case "A":
				case "B":
					FEE_CODE po = new FEE_CODE();
					FEE_CODE_PK pk = new FEE_CODE_PK(fee_code_nw_form.getFEE_ID(), fee_code_nw_form.getSTART_DATE());
					po.setId(pk);
					po.setOUT_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE())?"0":fee_code_nw_form.getOUT_BANK_FEE() ).setScale(2) );
					po.setOUT_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC())?"0":fee_code_nw_form.getOUT_BANK_FEE_DISC() ).setScale(2) );
					po.setIN_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE())?"0":fee_code_nw_form.getIN_BANK_FEE() ).setScale(2) );
					po.setIN_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC())?"0":fee_code_nw_form.getIN_BANK_FEE_DISC() ).setScale(2) );
					po.setTCH_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE())?"0":fee_code_nw_form.getTCH_FEE() ).setScale(2) );
					po.setTCH_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC())?"0":fee_code_nw_form.getTCH_FEE_DISC() ).setScale(2) );
					po.setSND_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE())?"0":fee_code_nw_form.getSND_BANK_FEE() ).setScale(2) );
					po.setSND_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC())?"0":fee_code_nw_form.getSND_BANK_FEE_DISC() ).setScale(2) );
					po.setWO_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE())?"0":fee_code_nw_form.getWO_BANK_FEE() ).setScale(2) );
					po.setWO_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC())?"0":fee_code_nw_form.getWO_BANK_FEE_DISC() ).setScale(2) );
					po.setHANDLECHARGE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE())?"0":fee_code_nw_form.getHANDLECHARGE() ).setScale(2) );
					po.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
					po.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
					po.setCDATE(zDateHandler.getTheDateII());
					map = fee_code_bo.save(po);
					break;
				case "AB":
				case "BA":
					// not null
					FEE_CODE_NW po_n = new FEE_CODE_NW();
					FEE_CODE_NW_PK pk_n = new FEE_CODE_NW_PK(UUID.randomUUID().toString());
					po_n.setId(pk_n);
					po_n.setFEE_ID(fee_code_nw_form.getFEE_ID());
					po_n.setFEE_TYPE(fee_code_nw_form.getFEE_TYPE());
					po_n.setSTART_DATE(fee_code_nw_form.getSTART_DATE());
					//
					po_n.setOUT_BANK_FEE(
							StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE()) ? "0.00" : fee_code_nw_form.getOUT_BANK_FEE());
					po_n.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC()) ? "0.00"
							: fee_code_nw_form.getOUT_BANK_FEE_DISC());
					po_n.setIN_BANK_FEE(
							StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE()) ? "0.00" : fee_code_nw_form.getIN_BANK_FEE());
					po_n.setIN_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC()) ? "0.00"
							: fee_code_nw_form.getIN_BANK_FEE_DISC());
					po_n.setTCH_FEE(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE()) ? "0.00" : fee_code_nw_form.getTCH_FEE());
					po_n.setTCH_FEE_DISC(
							StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC()) ? "0.00" : fee_code_nw_form.getTCH_FEE_DISC());
					po_n.setSND_BANK_FEE(
							StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE()) ? "0.00" : fee_code_nw_form.getSND_BANK_FEE());
					po_n.setSND_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC()) ? "0.00"
							: fee_code_nw_form.getSND_BANK_FEE_DISC());
					po_n.setWO_BANK_FEE(
							StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE()) ? "0.00" : fee_code_nw_form.getWO_BANK_FEE());
					po_n.setWO_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC()) ? "0.00"
							: fee_code_nw_form.getWO_BANK_FEE_DISC());
					po_n.setHANDLECHARGE(
							StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE()) ? "0.00" : fee_code_nw_form.getHANDLECHARGE());
					po_n.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
					po_n.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
					po_n.setCDATE(zDateHandler.getTheDateII());
					System.out.println(po_n.toString());
					map = fee_code_nw_bo.save(po_n);
					break;
				}
			}else {
				// not null
				FEE_CODE_NW po_n = new FEE_CODE_NW();
				FEE_CODE_NW_PK pk_n = new FEE_CODE_NW_PK(UUID.randomUUID().toString());
				po_n.setId(pk_n);
				po_n.setFEE_ID(fee_code_nw_form.getFEE_ID());
				po_n.setFEE_TYPE(fee_code_nw_form.getFEE_TYPE());
				po_n.setSTART_DATE(fee_code_nw_form.getSTART_DATE());
				//

				po_n.setOUT_BANK_FEE(
						StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE()) ? "0.00" : fee_code_nw_form.getOUT_BANK_FEE());
				po_n.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getOUT_BANK_FEE_DISC());
				po_n.setIN_BANK_FEE(
						StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE()) ? "0.00" : fee_code_nw_form.getIN_BANK_FEE());
				po_n.setIN_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getIN_BANK_FEE_DISC());
				po_n.setTCH_FEE(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE()) ? "0.00" : fee_code_nw_form.getTCH_FEE());
				po_n.setTCH_FEE_DISC(
						StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC()) ? "0.00" : fee_code_nw_form.getTCH_FEE_DISC());
				po_n.setSND_BANK_FEE(
						StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE()) ? "0.00" : fee_code_nw_form.getSND_BANK_FEE());
				po_n.setSND_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getSND_BANK_FEE_DISC());
				po_n.setWO_BANK_FEE(
						StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE()) ? "0.00" : fee_code_nw_form.getWO_BANK_FEE());
				po_n.setWO_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getWO_BANK_FEE_DISC());
				po_n.setHANDLECHARGE(
						StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE()) ? "0.00" : fee_code_nw_form.getHANDLECHARGE());
				po_n.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
				po_n.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
				po_n.setCDATE(zDateHandler.getTheDateII());
				System.out.println(po_n.toString());
				map = fee_code_nw_bo.save(po_n);
			}
			
			BeanUtils.populate(fee_code_nw_form, map);
			if (map.get("result").equals("TRUE")) {
				BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
			}
			list_nw = fee_code_nw_bo.search(fee_code_nw_form.getFEE_ID(), fee_code_nw_form.getSTART_DATE());
			fee_code_nw_form.setJsonList(JSONUtils.toJson(list_nw));
			fee_code_nw_form.setScaseary(list_nw);

		}
		// 級距
		if (ac_key.equalsIgnoreCase("add_lvl")) {
			// 新級距
			if ("01".equals(fee_code_nw_form.getFEE_DTNO())) {
				System.out.println("First LVL ADD");
				String uuid = UUID.randomUUID().toString();
				// 新增級距時,寫入主表四個欄位
				FEE_CODE_NW po = new FEE_CODE_NW();
				FEE_CODE_NW_PK pk = new FEE_CODE_NW_PK(uuid);
				po.setId(pk);
				// not null
				po.setFEE_ID(fee_code_nw_form.getFEE_ID());
				po.setFEE_TYPE(fee_code_nw_form.getFEE_TYPE());
				po.setSTART_DATE(fee_code_nw_form.getSTART_DATE());
				
				// 級距表寫入
				FEE_CODE_NWLVL polv = new FEE_CODE_NWLVL();
				// not null
				polv.setFEE_UID(uuid);
				polv.setFEE_ID(fee_code_nw_form.getFEE_ID());
				polv.setFEE_DTNO(fee_code_nw_form.getFEE_DTNO());
				polv.setFEE_LVL_TYPE(fee_code_nw_form.getFEE_LVL_TYPE());
				polv.setFEE_LVL_BEG_AMT(StrUtils.isEmpty(fee_code_nw_form.getFEE_LVL_BEG_AMT()) ? "0.00": fee_code_nw_form.getFEE_LVL_BEG_AMT());
				polv.setFEE_LVL_END_AMT(StrUtils.isEmpty(fee_code_nw_form.getFEE_LVL_END_AMT()) ? "0.00": fee_code_nw_form.getFEE_LVL_END_AMT());
				//
				polv.setOUT_BANK_FEE(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE()) ? "0.00": fee_code_nw_form.getOUT_BANK_FEE());
				polv.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC()) ? "0.00": fee_code_nw_form.getOUT_BANK_FEE_DISC());
				polv.setIN_BANK_FEE(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE()) ? "0.00" : fee_code_nw_form.getIN_BANK_FEE());
				polv.setIN_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC()) ? "0.00": fee_code_nw_form.getIN_BANK_FEE_DISC());
				polv.setTCH_FEE(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE()) ? "0.00" : fee_code_nw_form.getTCH_FEE());
				polv.setTCH_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC()) ? "0.00": fee_code_nw_form.getTCH_FEE_DISC());
				polv.setSND_BANK_FEE(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE()) ? "0.00": fee_code_nw_form.getSND_BANK_FEE());
				polv.setSND_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC()) ? "0.00": fee_code_nw_form.getSND_BANK_FEE_DISC());
				polv.setWO_BANK_FEE(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE()) ? "0.00" : fee_code_nw_form.getWO_BANK_FEE());
				polv.setWO_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC()) ? "0.00": fee_code_nw_form.getWO_BANK_FEE_DISC());
				polv.setHANDLECHARGE(StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE()) ? "0.00": fee_code_nw_form.getHANDLECHARGE());
				polv.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
				polv.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
				polv.setCDATE(zDateHandler.getTheDateII());
				Map map = fee_code_nw_bo.save(po);
				BeanUtils.populate(fee_code_nw_form, map);
				if(map.get("result").equals("TRUE")){
					System.out.println("級距新增寫入主表成功");
					map = fee_code_nwlvl_bo.save(polv);
					BeanUtils.populate(fee_code_nw_form, map);
					if (map.get("result").equals("TRUE")) {
						System.out.println("級距新增寫入副表成功");
						CodeUtils.objectCovert(Fee_Code_NW_Form.class, polv);
							fee_code_nw_form.setTarget("add_lvl_result_p");
							fee_code_nw_form.setFEE_UID(uuid);
					}
				}
				// 第二個級距之後
			} else {
				FEE_CODE_NWLVL polv = new FEE_CODE_NWLVL();
				// not null
				polv.setFEE_UID(fee_code_nw_form.getFEE_UID());
				polv.setFEE_ID(fee_code_nw_form.getFEE_ID());
				polv.setFEE_DTNO(fee_code_nw_form.getFEE_DTNO());
				polv.setFEE_LVL_TYPE(fee_code_nw_form.getFEE_LVL_TYPE());
				polv.setFEE_LVL_BEG_AMT(StrUtils.isEmpty(fee_code_nw_form.getFEE_LVL_BEG_AMT()) ? "0.00"
						: fee_code_nw_form.getFEE_LVL_BEG_AMT());
				polv.setFEE_LVL_END_AMT(StrUtils.isEmpty(fee_code_nw_form.getFEE_LVL_END_AMT()) ? "0.00"
						: fee_code_nw_form.getFEE_LVL_END_AMT());
				//
				polv.setOUT_BANK_FEE(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE()) ? "0.00"
						: fee_code_nw_form.getOUT_BANK_FEE());
				polv.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getOUT_BANK_FEE_DISC());
				polv.setIN_BANK_FEE(
						StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE()) ? "0.00" : fee_code_nw_form.getIN_BANK_FEE());
				polv.setIN_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getIN_BANK_FEE_DISC());
				polv.setTCH_FEE(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE()) ? "0.00" : fee_code_nw_form.getTCH_FEE());
				polv.setTCH_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getTCH_FEE_DISC());
				polv.setSND_BANK_FEE(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE()) ? "0.00"
						: fee_code_nw_form.getSND_BANK_FEE());
				polv.setSND_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getSND_BANK_FEE_DISC());
				polv.setWO_BANK_FEE(
						StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE()) ? "0.00" : fee_code_nw_form.getWO_BANK_FEE());
				polv.setWO_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC()) ? "0.00"
						: fee_code_nw_form.getWO_BANK_FEE_DISC());
				polv.setHANDLECHARGE(StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE()) ? "0.00"
						: fee_code_nw_form.getHANDLECHARGE());
				polv.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
				polv.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
				polv.setCDATE(zDateHandler.getTheDateII());

				Map map = fee_code_nwlvl_bo.save(polv);
				BeanUtils.populate(fee_code_nw_form, map);
				if (map.get("result").equals("TRUE")) {
//					BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
					CodeUtils.objectCovert(Fee_Code_NW_Form.class, polv);
					fee_code_nw_form.setTarget("add_lvl_result_p");
					fee_code_nw_form.setFEE_UID(polv.getFEE_UID());
				}
			}
		}
		
		if (ac_key.equalsIgnoreCase("add_more")) {
			
			System.out.println("add more fee_code_nw_form before >> "+ fee_code_nw_form.toString());
			//交易金額(起)為上一個級距的交易金額(迄)+1
			int begAmt = Integer.valueOf(fee_code_nw_form.getFEE_LVL_END_AMT());
			fee_code_nw_form.setFEE_LVL_BEG_AMT(String.valueOf(begAmt+1));
			//級距序號+1
			int dtno = Integer.valueOf(fee_code_nw_form.getFEE_DTNO());
			String str_dtno = String.valueOf(dtno+1);
			if(str_dtno.length()<2) {
				str_dtno="0"+str_dtno;
			}
			fee_code_nw_form.setFEE_DTNO(str_dtno);
			fee_code_nw_form.setTarget("add_lvl_more_p");
			System.out.println("add more fee_code_nw_form after >> "+ fee_code_nw_form.toString());
		}
		if (ac_key.equalsIgnoreCase("edit")) {
			// 普通的修改
			BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getEdit_params()));
			list = fee_code_bo.search(fee_code_nw_form.getFEE_ID(),fee_code_nw_form.getSTART_DATE());
			for(FEE_CODE po : list){
				BeanUtils.copyProperties(fee_code_nw_form, po);
				BigDecimal zero = new BigDecimal("0"); 
				if(po.getHANDLECHARGE().compareTo(zero)==1) {
					fee_code_nw_form.setFEE_TYPE("B");
				}else {
					fee_code_nw_form.setFEE_TYPE("A");
				}

			}
			System.out.println("edit_fee_code_nw_form:"+fee_code_nw_form.toString());
			fee_code_nw_form.setTarget("edit_p");
		}
		if (ac_key.equalsIgnoreCase("edit_nw")) {
			// 普通的修改
			BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getEdit_params()));
			FEE_CODE_NW po = new FEE_CODE_NW();
			po = fee_code_nw_bo.findByNPK(fee_code_nw_form.getFEE_UID());
			BeanUtils.copyProperties(fee_code_nw_form, po);
			System.out.println("edit_fee_code_nw_form:"+fee_code_nw_form.toString());
			fee_code_nw_form.setTarget("edit_p");
		}
		if (ac_key.equalsIgnoreCase("edit_lvl")) {
			BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getEdit_params()));
			// 級距修改
			FEE_CODE_NWLVL polv = new FEE_CODE_NWLVL();
			polv = fee_code_nwlvl_bo.findByUK(fee_code_nw_form.getFEE_UID(), fee_code_nw_form.getFEE_DTNO());
			BeanUtils.copyProperties(fee_code_nw_form, polv);
			fee_code_nw_form.setTarget("add_lvl_edit_p");
		}
		if (ac_key.equalsIgnoreCase("update_nw")) {
			FEE_CODE_NW po = new FEE_CODE_NW();
			FEE_CODE_NW_PK pk = new FEE_CODE_NW_PK(fee_code_nw_form.getFEE_UID());
			po.setId(pk);
			// not null
			po.setFEE_ID(fee_code_nw_form.getFEE_ID());
			po.setFEE_TYPE(fee_code_nw_form.getFEE_TYPE());
			po.setSTART_DATE(fee_code_nw_form.getSTART_DATE());
			//
			po.setOUT_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE()) ? "0.00" : fee_code_nw_form.getOUT_BANK_FEE());
			po.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getOUT_BANK_FEE_DISC());
			po.setIN_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE()) ? "0.00" : fee_code_nw_form.getIN_BANK_FEE());
			po.setIN_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getIN_BANK_FEE_DISC());
			po.setTCH_FEE(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE()) ? "0.00" : fee_code_nw_form.getTCH_FEE());
			po.setTCH_FEE_DISC(
					StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC()) ? "0.00" : fee_code_nw_form.getTCH_FEE_DISC());
			po.setSND_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE()) ? "0.00" : fee_code_nw_form.getSND_BANK_FEE());
			po.setSND_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getSND_BANK_FEE_DISC());
			po.setWO_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE()) ? "0.00" : fee_code_nw_form.getWO_BANK_FEE());
			po.setWO_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getWO_BANK_FEE_DISC());
			po.setHANDLECHARGE(
					StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE()) ? "0.00" : fee_code_nw_form.getHANDLECHARGE());
			po.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
			po.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
			Map map = fee_code_nw_bo.update(po);
			if (map.get("result").equals("TRUE")) {
				BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
			}
			BeanUtils.populate(fee_code_nw_form, map);
		}
		if (ac_key.equalsIgnoreCase("update")) {
			FEE_CODE po = new FEE_CODE();
			FEE_CODE_PK pk = new FEE_CODE_PK(fee_code_nw_form.getFEE_ID(),fee_code_nw_form.getSTART_DATE());
			po.setId(pk);
			po.setOUT_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE())?"0":fee_code_nw_form.getOUT_BANK_FEE() ).setScale(2) );
			po.setOUT_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC())?"0":fee_code_nw_form.getOUT_BANK_FEE_DISC() ).setScale(2) );
			po.setIN_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE())?"0":fee_code_nw_form.getIN_BANK_FEE() ).setScale(2) );
			po.setIN_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC())?"0":fee_code_nw_form.getIN_BANK_FEE_DISC() ).setScale(2) );
			po.setTCH_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE())?"0":fee_code_nw_form.getTCH_FEE() ).setScale(2) );
			po.setTCH_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC())?"0":fee_code_nw_form.getTCH_FEE_DISC() ).setScale(2) );
			po.setSND_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE())?"0":fee_code_nw_form.getSND_BANK_FEE() ).setScale(2) );
			po.setSND_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC())?"0":fee_code_nw_form.getSND_BANK_FEE_DISC() ).setScale(2) );
			po.setWO_BANK_FEE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE())?"0":fee_code_nw_form.getWO_BANK_FEE() ).setScale(2) );
			po.setWO_BANK_FEE_DISC( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC())?"0":fee_code_nw_form.getWO_BANK_FEE_DISC() ).setScale(2) );
			po.setHANDLECHARGE( new BigDecimal( StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE())?"0":fee_code_nw_form.getHANDLECHARGE() ).setScale(2) );
			po.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
			po.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
			Map map = fee_code_bo.update(po);
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
			}
			BeanUtils.populate(fee_code_nw_form, map);
		}
		if (ac_key.equalsIgnoreCase("update_lvl")) {
			FEE_CODE_NWLVL polvl = fee_code_nwlvl_bo.findByUK(fee_code_nw_form.getFEE_UID(),fee_code_nw_form.getFEE_DTNO());
			System.out.println("update_lvl polvl >> "+ polvl.toString());
			//
			polvl.setFEE_LVL_TYPE(fee_code_nw_form.getFEE_LVL_TYPE());
			polvl.setFEE_LVL_BEG_AMT(StrUtils.isEmpty(fee_code_nw_form.getFEE_LVL_BEG_AMT()) ? "0.00"
					: fee_code_nw_form.getFEE_LVL_BEG_AMT());
			polvl.setFEE_LVL_END_AMT(StrUtils.isEmpty(fee_code_nw_form.getFEE_LVL_END_AMT()) ? "0.00"
					: fee_code_nw_form.getFEE_LVL_END_AMT());
			polvl.setOUT_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE()) ? "0.00" : fee_code_nw_form.getOUT_BANK_FEE());
			polvl.setOUT_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getOUT_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getOUT_BANK_FEE_DISC());
			polvl.setIN_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE()) ? "0.00" : fee_code_nw_form.getIN_BANK_FEE());
			polvl.setIN_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getIN_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getIN_BANK_FEE_DISC());
			polvl.setTCH_FEE(StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE()) ? "0.00" : fee_code_nw_form.getTCH_FEE());
			polvl.setTCH_FEE_DISC(
					StrUtils.isEmpty(fee_code_nw_form.getTCH_FEE_DISC()) ? "0.00" : fee_code_nw_form.getTCH_FEE_DISC());
			polvl.setSND_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE()) ? "0.00" : fee_code_nw_form.getSND_BANK_FEE());
			polvl.setSND_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getSND_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getSND_BANK_FEE_DISC());
			polvl.setWO_BANK_FEE(
					StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE()) ? "0.00" : fee_code_nw_form.getWO_BANK_FEE());
			polvl.setWO_BANK_FEE_DISC(StrUtils.isEmpty(fee_code_nw_form.getWO_BANK_FEE_DISC()) ? "0.00"
					: fee_code_nw_form.getWO_BANK_FEE_DISC());
			polvl.setHANDLECHARGE(
					StrUtils.isEmpty(fee_code_nw_form.getHANDLECHARGE()) ? "0.00" : fee_code_nw_form.getHANDLECHARGE());
			polvl.setFEE_DESC(fee_code_nw_form.getFEE_DESC());
			polvl.setACTIVE_DESC(fee_code_nw_form.getACTIVE_DESC());
			Map map = fee_code_nwlvl_bo.update(polvl);
			if (map.get("result").equals("TRUE")) {
//				BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
				BeanUtils.populate(fee_code_nw_form, map);
				CodeUtils.objectCovert(Fee_Code_NW_Form.class, polvl);
			}
			fee_code_nw_form.setTarget("add_lvl_result_p");
			
		}
		
		if (ac_key.equalsIgnoreCase("delete")) {
			Map map = fee_code_bo.delete(fee_code_nw_form.getFEE_ID(), fee_code_nw_form.getSTART_DATE());
			if(map.get("result").equals("TRUE")){
				BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
			}
			BeanUtils.populate(fee_code_nw_form, map);		
		}
		
		if (ac_key.equalsIgnoreCase("delete_nw")) {
			// 非級距刪除
			Map map = fee_code_nw_bo.delete(fee_code_nw_form.getFEE_UID(), fee_code_nw_form.getFEE_ID(),
					fee_code_nw_form.getSTART_DATE());
			if (map.get("result").equals("TRUE")) {
				BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
			}
			BeanUtils.populate(fee_code_nw_form, map);
		}
		
		if (ac_key.equalsIgnoreCase("delete_lvl")) {
			//修改成刪除全部
			Map map1 = fee_code_nwlvl_bo.deleteLVL(fee_code_nw_form.getFEE_UID());
			Map map2 = fee_code_nw_bo.delete(fee_code_nw_form.getFEE_UID(), fee_code_nw_form.getFEE_ID(),fee_code_nw_form.getSTART_DATE());
			BeanUtils.populate(fee_code_nw_form, map2);
			//			//級距刪除
//			//級距在副表剩幾筆 , 如果只剩一筆的話 , 連同主表一起刪掉
//			int lvlrows = fee_code_nwlvl_bo.lvlrowcnt(fee_code_nw_form.getFEE_UID());
//			System.out.println("lvlrows >> "+lvlrows);
//			if(lvlrows>1) {
//				Map map = fee_code_nwlvl_bo.delete(fee_code_nw_form.getFEE_UID(), fee_code_nw_form.getFEE_DTNO());
//				if (map.get("result").equals("TRUE")) {
//					BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
//				}
//				BeanUtils.populate(fee_code_nw_form, map);
//			}else {
//				Map map_nw = fee_code_nw_bo.delete(fee_code_nw_form.getFEE_UID(), fee_code_nw_form.getFEE_ID(),
//						fee_code_nw_form.getSTART_DATE());
//				Map map_lvl = fee_code_nwlvl_bo.delete(fee_code_nw_form.getFEE_UID(), fee_code_nw_form.getFEE_DTNO());
//				if (map_nw.get("result").equals("TRUE") && map_lvl.get("result").equals("TRUE")) {
//					BeanUtils.populate(fee_code_nw_form, JSONUtils.json2map(fee_code_nw_form.getSerchStrs()));
//				}
//				BeanUtils.populate(fee_code_nw_form, map_nw);
//			}
			
		}
		target = StrUtils.isEmpty(fee_code_nw_form.getTarget()) ? "" : fee_code_nw_form.getTarget();

		// FEE_ID下拉清單
		// fee_code_nw_form.setIdList(fee_code_nw_bo.getIdList());
		fee_code_nw_form.setIdList(fee_code_nw_bo.getIdListJoinTxnName());
		return mapping.findForward(target);
	}
	public FEE_CODE_NW_BO getFee_code_nw_bo() {
		return fee_code_nw_bo;
	}

	public void setFee_code_nw_bo(FEE_CODE_NW_BO fee_code_nw_bo) {
		this.fee_code_nw_bo = fee_code_nw_bo;
	}

	public FEE_CODE_NWLVL_BO getFee_code_nwlvl_bo() {
		return fee_code_nwlvl_bo;
	}

	public void setFee_code_nwlvl_bo(FEE_CODE_NWLVL_BO fee_code_nwlvl_bo) {
		this.fee_code_nwlvl_bo = fee_code_nwlvl_bo;
	}
	public FEE_CODE_BO getFee_code_bo() {
		return fee_code_bo;
	}
	public void setFee_code_bo(FEE_CODE_BO fee_code_bo) {
		this.fee_code_bo = fee_code_bo;
	}
}
