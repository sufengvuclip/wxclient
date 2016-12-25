package com.sf.wxc.repository.db.bigdatawaydb;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-25.
 */
@Repository
public class EntityDao extends BigDataWaydbCommonDao{
    public String getTagUUID(String tag){
        String sql = "select b.uuid from taxonomy_term_field_data a, taxonomy_term_data b where a.vid='tags' and a.name=:tag and a.tid=b.tid";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tag",tag.trim());
        return queryString(sql,params);
    }
}
