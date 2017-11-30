package com.taotao.search.bean;

import java.util.List;

/**
 * @author liu_mw
 * @date 2017/11/30 15:08
 */
public class SearchResult {
    private Long total;
    private List<?> list;

    public SearchResult() {
    }

    public SearchResult(Long total, List<?> list) {
        this.total = total;
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
