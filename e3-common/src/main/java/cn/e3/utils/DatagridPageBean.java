package cn.e3.utils;

import java.io.Serializable;
import java.util.List;

public class DatagridPageBean implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -4549166907006706998L;
    //easyUI分页返回值构造对象
    //total
    //rows
    private Long total;
    private List<?> rows;
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List<?> getRows() {
        return rows;
    }
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
