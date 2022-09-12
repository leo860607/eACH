package tw.org.twntch.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import tw.org.twntch.bo.TXN_CODE_BO;
import tw.org.twntch.bo.TXN_RETURNDAY_BO;
import tw.org.twntch.form.Txn_Returnday_Form;
import tw.org.twntch.po.RETURN_DAY;
import tw.org.twntch.po.RETURN_DAY_PK;
import tw.org.twntch.util.BeanUtils;
import tw.org.twntch.util.DateTimeUtils;
import tw.org.twntch.util.JSONUtils;
import tw.org.twntch.util.StrUtils;
import tw.org.twntch.util.zDateHandler;


public class TXN_RETURNDAY_Action extends Action {
	
	private TXN_CODE_BO txn_code_bo;
	private TXN_RETURNDAY_BO txn_returnday_bo;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Txn_Returnday_Form txn_returnday_form = (Txn_Returnday_Form) form ;
		String target = StrUtils.isEmpty(txn_returnday_form.getTarget())?"":txn_returnday_form.getTarget();
		String ac_key = StrUtils.isEmpty(txn_returnday_form.getAc_key())?"":txn_returnday_form.getAc_key();
		System.out.println("start TXN_RETURNDAY_Action >> " + ac_key);
		if(StrUtils.isNotEmpty(ac_key)){
			if(ac_key.equals("search") || ac_key.equals("back")){
				System.out.println("SerchStrs>>"+txn_returnday_form.getSerchStrs());
				BeanUtils.populate(txn_returnday_form, JSONUtils.json2map(txn_returnday_form.getSerchStrs()));
				txn_returnday_form.setTxnIdList(txn_returnday_bo.getTxnIdList());
			}else if(ac_key.equals("history")){
				txn_returnday_form.setScaseary(txn_returnday_bo.getHistory());
				target = "history_p";
			}else if(ac_key.equals("new")){
				//Get txnIdList (新增、編輯時應帶入所有代收(SD)的交易項目)
				txn_returnday_form.setTxnIdList(txn_code_bo.getIdListByTxnType("SD"));
				target = "add_p";
			}else if(ac_key.equals("add")){
				RETURN_DAY po = new RETURN_DAY();
				BeanUtils.copyProperties(po, txn_returnday_form);
				po.setId(new RETURN_DAY_PK(txn_returnday_form.getTXN_ID(), txn_returnday_form.getACTIVE_DATE()));
				Map map = txn_returnday_bo.save(po);
				txn_returnday_form.setResult(ac_key + "_" + map.get("result"));
				txn_returnday_form.setMsg((String) map.get("msg"));
				//Get txnIdList
				txn_returnday_form.setTxnIdList(txn_returnday_bo.getTxnIdList());
				target = "add_p";
			}else if(ac_key.equals("edit")){
//				String txnId = txn_returnday_form.getTXN_ID() == null? txn_returnday_form.getSTXN_ID() : txn_returnday_form.getTXN_ID();
//				txn_returnday_form.setTXN_ID(txnId);
//				System.out.println("EDIT (" + txnId + " / " + txn_returnday_form.getACTIVE_DATE() + ")");
				BeanUtils.populate(txn_returnday_form, JSONUtils.json2map(txn_returnday_form.getEdit_params()));
				List<RETURN_DAY> list = txn_returnday_bo.search(txn_returnday_form.getTXN_ID(), txn_returnday_form.getACTIVE_DATE() , txn_returnday_form.getSerchStrs());
				if(list != null && list.size() == 1){
					RETURN_DAY po = list.get(0);
					BeanUtils.copyProperties(txn_returnday_form, po);
					txn_returnday_form.setACTIVE_DATE(po.getId().getACTIVE_DATE());
					
					if(Integer.valueOf(po.getId().getACTIVE_DATE()) > Integer.valueOf(zDateHandler.getTWDate())){
						txn_returnday_form.setIS_EDITABLE(true);
					}else{
						txn_returnday_form.setIS_EDITABLE(false);
					}
					txn_returnday_form.setTxnIdList(txn_code_bo.getIdListByTxnType("SD"));
					target = "edit_p";
				}else{
					Map map = new HashMap();
					map.put("result", "FALSE");
					map.put("msg", "查無此資料");
					BeanUtils.copyProperties(txn_returnday_form, map);
					target = "";
				}
			}else if(ac_key.equals("update")){
				Map map = txn_returnday_bo.update(txn_returnday_form.getTXN_ID(), txn_returnday_form.getACTIVE_DATE(), BeanUtils.describe(txn_returnday_form));
				BeanUtils.populate(txn_returnday_form, map);
				//Get txnIdList
//				txn_returnday_form.setTxnIdList(txn_returnday_bo.getTxnIdList());
			}else if(ac_key.equals("delete")){
				Map map = txn_returnday_bo.delete(txn_returnday_form.getTXN_ID(), txn_returnday_form.getACTIVE_DATE());
				txn_returnday_form.setResult(ac_key + "_" + map.get("result"));
				txn_returnday_form.setMsg((String) map.get("msg"));
				//Get txnIdList
//				txn_returnday_form.setTxnIdList(txn_returnday_bo.getTxnIdList());
			}
		}
		
		if(StrUtils.isEmpty(target)){
			target = "search";
			//Get txnIdList
//			txn_returnday_form.setTxnIdList(txn_returnday_bo.getTxnIdList());
			txn_returnday_form.setTxnIdList(txn_returnday_bo.getTxnIdList());
		}
		//Get txnIdList
		
		System.out.println("forward to>>" + target);
		return (mapping.findForward(target));
	}

	public TXN_CODE_BO getTxn_code_bo() {
		return txn_code_bo;
	}

	public void setTxn_code_bo(TXN_CODE_BO txn_code_bo) {
		this.txn_code_bo = txn_code_bo;
	}

	public TXN_RETURNDAY_BO getTxn_returnday_bo() {
		return txn_returnday_bo;
	}

	public void setTxn_returnday_bo(TXN_RETURNDAY_BO txn_returnday_bo) {
		this.txn_returnday_bo = txn_returnday_bo;
	}
	
}
