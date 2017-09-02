package cn.e3.search.pojo;

import java.io.Serializable;
import java.util.List;

public class SolrPageBean implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1951307670105459385L;
    // 1.当前页
    private Integer curPage;
    // 2.总页数
    private Integer totalPage;
    // 3.总记录数
    private Integer recoredCount;
    // 4.当前页总记录
    private List<SearchItem> sList;
    public Integer getCurPage() {
        return curPage;
    }
    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }
    public Integer getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
    public Integer getRecoredCount() {
        return recoredCount;
    }
    public void setRecoredCount(Integer recoredCount) {
        this.recoredCount = recoredCount;
    }
    public List<SearchItem> getsList() {
        return sList;
    }
    public void setsList(List<SearchItem> sList) {
        this.sList = sList;
    }
}
