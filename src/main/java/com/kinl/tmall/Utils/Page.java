package com.kinl.tmall.Utils;

import java.util.List;

public class Page {

    private Integer pageSize;           //页大小
    private Integer pageIndex;          //当前页号
    private Integer totalPageCount;     //页总数
    private Integer record;             //记录总数
    private List datas;             //对象

    public Page(Integer pageSize, Integer pageIndex) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(Integer record) {
        this.totalPageCount = ((record%pageSize) == 0 ? (record/pageSize) : (record/pageSize)+1);
    }

    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    public Integer getStartIndex(){
        return (this.pageIndex-1)*pageSize;
    }
}
