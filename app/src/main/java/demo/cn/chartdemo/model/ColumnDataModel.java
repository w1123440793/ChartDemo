package demo.cn.chartdemo.model;

import java.util.List;

/**
 * Description: 柱状图数据model
 */
public class ColumnDataModel extends BaseModel {
    private List<ColumnModel> columnModels;
    private boolean horizontal;//是否是横向堆积图

    public List<ColumnModel> getColumnModels() {
        return columnModels;
    }

    public void setColumnModels(List<ColumnModel> columnModels) {
        this.columnModels = columnModels;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }
}
