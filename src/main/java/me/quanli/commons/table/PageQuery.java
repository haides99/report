package me.quanli.commons.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import me.quanli.commons.dao.Dao;

/**
 * Created by admin on 2016/8/20.
 */
public abstract class PageQuery {

    protected ThreadLocal<Long> localStart = new ThreadLocal<Long>();

    protected ThreadLocal<Long> localLength = new ThreadLocal<Long>();

    /**
     * 对于全表排序，获取带有排序的查询语句
     *
     * @param ql     原查询语句
     * @param params 前端由datatable产生的参数
     * @return
     */
    public String toSortQL(String ql, Map<String, String> params) {
        String sortCol = params.get("iSortCol_0");
        String sortDir = params.get("sSortDir_0");
        String sColumns = params.get("sColumns");
        if (StringUtils.isEmpty(sortCol)) {
            return ql;
        }
        int sortIndex = 0;
        try {
            sortIndex = Integer.parseInt(sortCol);
        } catch (NumberFormatException e) {
            return ql;
        }

        if (StringUtils.isEmpty(sortDir)) {
            return ql;
        }
        sortDir = sortDir.equalsIgnoreCase("asc") ? "ASC" : "DESC";

        if (StringUtils.isEmpty(sColumns)) {
            return ql;
        }
        String[] cols = sColumns.split(",");
        if (sortIndex >= cols.length) {
            return ql;
        }
        String colName = cols[sortIndex];
        if (StringUtils.isEmpty(colName)) {
            return ql;
        }
        String pOrderStr = "\\s*(o|O)(r|R)(d|D)(e|E)(r|R)\\s+(b|B)(y|Y).*$";
        ql = ql.replaceFirst(pOrderStr, "");
        ql += " ORDER BY " + colName + " " + sortDir;
        return ql;
    }

    /**
     * 分页排序，对部分数据进行排序
     *
     * @param aaData 要返回前端的数据
     * @param params 前端由datatable产生的参数
     */
    public void sort(List<List<Object>> aaData, Map<String, String> params) {
        String sortCol = params.get("iSortCol_0");
        String sortDir = params.get("sSortDir_0");
        if (StringUtils.isEmpty(sortCol)) {
            return;
        }
        int sortIndex = 0;
        try {
            sortIndex = Integer.parseInt(sortCol);
        } catch (NumberFormatException e) {
            return;
        }

        if (StringUtils.isEmpty(sortDir)) {
            return;
        }
        final int _sortIndex = sortIndex;
        final String _sortDir = sortDir;
        Collections.sort(aaData, new Comparator<List<Object>>() {
            @SuppressWarnings("unchecked")
            public int compare(List<Object> o1, List<Object> o2) {
                try {
                    Comparable<Object> v1 = (Comparable<Object>) o1
                            .get(_sortIndex);
                    Comparable<Object> v2 = (Comparable<Object>) o2
                            .get(_sortIndex);
                    if (_sortDir.equalsIgnoreCase("asc")) {
                        return v1.toString().compareTo(v2.toString());
                    } else if (_sortDir.equalsIgnoreCase("desc")) {
                        return v2.compareTo(v1);
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    public Map<String, Object> queryPage(Map<String, String> params,
                                         Dao dao) {
        Map<String, Object> result = new HashMap<String, Object>();
        PageData<Object> pageData = new PageData<Object>(0, 20);
        String iDisplayStart = params.get("iDisplayStart");
        String iDisplayLength = params.get("iDisplayLength");
        if (StringUtils.hasText(iDisplayStart)) {
            Long _start = Long.parseLong(iDisplayStart);
            localStart.set(_start);
            pageData.setOffset(_start);
        }
        if (StringUtils.hasText(iDisplayLength)) {
            Long _length = Long.parseLong(iDisplayLength);
            localLength.set(_length);
            pageData.setLimit(_length);
        }
        List<Object> paramValues = new ArrayList<Object>();
        String hql = buildQuery(params, paramValues);
        hql = toSortQL(hql, params);

        pageData.setTotal(dao.findCount(hql, paramValues.toArray()));
        pageData.setData(dao.find(hql, paramValues.toArray()));

        int sEcho = Integer.parseInt(params.get("sEcho"));
        result.put("sEcho", sEcho + 1);
        result.put("aaData", toRowDatas(pageData.getData()));
        result.put("iTotalRecords", pageData.getTotal());
        result.put("iTotalDisplayRecords", pageData.getTotal());
        return result;
    }

    public Map<String, Object> queryPageBySQL(Map<String, String> params,
                                              Dao dao) {
        Map<String, Object> result = new HashMap<String, Object>();
        PageData<Object> pageData = new PageData<Object>();
        String iDisplayStart = params.get("iDisplayStart");
        String iDisplayLength = params.get("iDisplayLength");
        if (StringUtils.hasText(iDisplayStart)) {
            Long _start = Long.parseLong(iDisplayStart);
            localStart.set(_start);
            pageData.setOffset(_start);
        }
        if (StringUtils.hasText(iDisplayLength)) {
            Long _length = Long.parseLong(iDisplayLength);
            localLength.set(_length);
            pageData.setLimit(_length);
        }
        List<Object> paramValues = new ArrayList<Object>();
        String sql = buildQuery(params, paramValues);
        sql = toSortQL(sql, params);

        pageData.setTotal(dao.findCountBySQL(sql, paramValues.toArray()));
        pageData.setData(dao.findBySQL(sql, paramValues.toArray()));

        int sEcho = Integer.parseInt(params.get("sEcho"));
        result.put("sEcho", sEcho + 1);
        result.put("aaData", toRowDatas(pageData.getData()));
        result.put("iTotalRecords", pageData.getTotal());
        result.put("iTotalDisplayRecords", pageData.getTotal());
        return result;
    }

    public List<?> queryAll(Map<String, String> params, Dao dao) {
        List<Object> paramValues = new ArrayList<Object>();
        String hql = buildQuery(params, paramValues);
        List<Object> list = dao.find(hql, paramValues.toArray());
        return (List<?>) toRowDatas(list);
    }

    protected Object toRowDatas(List<Object> datas) {
        return datas;
    }

    protected abstract String buildQuery(Map<String, String> params,
                                         List<Object> paramValues);

}
