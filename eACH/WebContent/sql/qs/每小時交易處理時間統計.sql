WITH TEMP AS ( 	
    SELECT Integer(substr(a.txDT,9,2)) HOURLAP,a.txDT, 	A.NEWRESULT RESULTSTATUS, 	
    (case when length(a.DT_Req_1)= 0 then '0' else a.DT_Req_1 end ) DT_Req_1, 	
    (case when length(a.DT_Req_2)=0  then '0' else a.DT_Req_2 end ) DT_Req_2, 	
    (case when length(a.DT_Rsp_1)=0  then '0' else a.DT_Rsp_1 end ) DT_Rsp_1, 	
    (case when length(a.DT_Rsp_2)=0  then '0' else a.DT_Rsp_2 end ) DT_Rsp_2, 	
    (case when length(a.DT_Con_1)=0  then '0' else a.DT_Con_1 end ) DT_Con_1, 	
    (case when length(a.DT_Con_2)=0  then '0' else a.DT_Con_2 end ) DT_Con_2, 	
    (case when length(DT_Con_2)=0 then (
            case when length(a.DT_Con_1)=0 then (
                case when length( a.DT_Rsp_2)=0 then (
                    case when  length(a.DT_Rsp_1) =0 then (
                        case when length(a.DT_Req_2) =0 then a.DT_Req_1 else a.DT_Req_2 end)
                    else a.DT_Rsp_1 end ) 
                else a.DT_Rsp_2 end ) 
            else a.DT_Con_1 end) 
    else a.DT_Con_2 end ) EndTime 	
    FROM VW_ONBLOCKTAB A 	
    WHERE  A.BIZDATE = '20150216' --��~�� 
    AND  A.PCODE = '2101' --����N�� 
    AND  A.SENDERACQUIRE = '0040000' --�ާ@�� 
    AND  A.CLEARINGPHASE = '01' --�M�ⶥ�q
)
SELECT
A.HOURLAP,A.HOURLAPNAME, --����ɶ�(�C�p��)
(select count(*) from temp Where HOURLAP=a.HOURLAP) TotalCount , --�������
(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='A' ) OKCOUNT , --���\����
(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='R' ) FailCOUNT , --���ѵ���
(select count(*) from temp where HOURLAP=a.HOURLAP and resultstatus='P' ) PendCOUNT , --�O�ɥ��^�е���
(select SUM(Double(Date_Diff(EndTime,DT_Req_1))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) PrcTime , --�浧�����B�z�ɶ�(��)
(select SUM(Double(Date_Diff(DT_Rsp_1,DT_Req_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) DebitTime , --�Ȧ�C�����ڥ����B�z�ɶ�(��)
(select SUM(Double(Date_Diff(DT_Con_1,DT_Rsp_2))) / (1000000*count(*)) from temp where HOURLAP=a.HOURLAP and resultstatus ='A' ) SaveTime , --�Ȧ�C���J�b�����B�z�ɶ�(��)
(select (SUM(Double(Date_Diff(DT_Req_2,DT_Req_1)))+SUM(Double(Date_Diff(DT_Rsp_2,DT_Rsp_1)))+SUM(Double(Date_Diff(DT_Con_2,DT_Con_1)))) / (1000000*Count(*)) from temp where HOURLAP=a.HOURLAP  and resultstatus ='A') ACHPrcTime  --�洫�ҥ����B�z�ɶ�(��)
FROM HOURITEM AS A