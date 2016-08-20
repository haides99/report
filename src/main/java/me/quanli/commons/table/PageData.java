package me.quanli.commons.table;

import java.util.List;

public class PageData<T> {

    public Long total;

    public Long offset;

    public Long limit;

    public List<T> data;

    public PageData() {

    }

    public PageData(long offset, long limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
