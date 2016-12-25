package com.sf.wxc.repository.db;

import com.sf.wxc.util.ConvertUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Su Feng on 2016/11/2.
 */
public abstract class CommonDao {
    protected abstract EntityManager getEntityManager();

    public String queryString(String sql,Map<String, Object> params){
        Session session = getEntityManager().unwrap(org.hibernate.Session.class);
        SQLQuery query = session.createSQLQuery(sql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        Object ret = query.uniqueResult();
        return ret==null?null:ret.toString();
    }

    @SuppressWarnings("unchecked")
    public List<?> queryListEntity(String sql, Map<String, Object> params, Class<?> clazz){
        Session session = getEntityManager().unwrap(org.hibernate.Session.class);
        SQLQuery query = session.createSQLQuery(sql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result =  query.list();
        if (clazz != null) {
            List<Object>  entityList = convert(clazz, result);
            return entityList;
        }
        return result;
    }

    private List<Object> convert(Class<?> clazz, List<Map<String, Object>> list) {
        List<Object> result;
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        result = new ArrayList<Object>();
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (Map<String, Object> map : list) {
                Object obj = clazz.newInstance();
                for (String key:map.keySet()) {
                    String attrName = key.toLowerCase();
                    for (PropertyDescriptor prop : props) {
                        attrName = removeUnderLine(attrName);
                        if (!attrName.equalsIgnoreCase(prop.getName())) {
                            continue;
                        }
                        Method method = prop.getWriteMethod();
                        Object value = map.get(key);
                        //System.out.println("method:"+(method==null?"null":method.getName()) +" value:"+value+" key:"+key);

                        value = ConvertUtils.convert(value,prop.getPropertyType());

                        method.invoke(obj,value);
                    }
                }
                result.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("data convert error! "+clazz.getName());
        }
        return result;
    }

    private String removeUnderLine(String attrName) {
        if(attrName.contains("_")) {
            String[] names = attrName.split("_");
            String firstPart = names[0];
            String otherPart = "";
            for (int i = 1; i < names.length; i++) {
                String word = names[i].replaceFirst(names[i].substring(0, 1), names[i].substring(0, 1).toUpperCase());
                otherPart += word;
            }
            attrName = firstPart + otherPart;
        }
        return attrName;
    }

    /**
     * get total count
     * @param sql
     * @param params
     * @return
     */
    public Integer getCountBy(String sql,Map<String, Object> params){
        Query query = getEntityManager().createNativeQuery(sql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        BigInteger bigInteger  = (BigInteger) query.getSingleResult();
        return bigInteger.intValue();
    }

    /**
     * delete or update
     * @param sql
     * @param params
     * @return
     */
    public Integer deleteOrUpDate(String sql,Map<String, Object> params){
        Query query = getEntityManager().createNativeQuery(sql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.executeUpdate();
    }
}
