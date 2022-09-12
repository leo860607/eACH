package tw.org.twntch.db.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import tw.org.twntch.po.AGENT_TXN_CODE;
import tw.org.twntch.po.FEE_CODE;
import tw.org.twntch.po.TXN_CODE;
import tw.org.twntch.po.TXN_FEE_MAPPING;
import tw.org.twntch.util.AutoAddScalar;
import tw.org.twntch.util.zDateHandler;

public class AGENT_TXN_CODE_Dao extends HibernateEntityDao<AGENT_TXN_CODE, Serializable> {
}
