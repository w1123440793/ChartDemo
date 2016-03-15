package demo.cn.chartdemo.model;

import java.util.List;

/**
 * Description:柱状图model
 */

public class ColumnModel extends BaseModel {
    private List<SubcolumnModel> subcolumnModels;
    //TODO X坐标轴显示数据,之后根据具体需求定义坐标轴数据和柱状图数据模型
    private String name;
    private int textsize;
    private int textcolor;

    public ColumnModel() {

    }

    public ColumnModel(ColumnModel model) {
        this.subcolumnModels = model.getSubcolumnModels();
        this.name = model.getName();
    }

    public List<SubcolumnModel> getSubcolumnModels() {
        return subcolumnModels;
    }

    public void setSubcolumnModels(List<SubcolumnModel> subcolumnModels) {
        this.subcolumnModels = subcolumnModels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTextsize() {
        return textsize;
    }

    public void setTextsize(int textsize) {
        this.textsize = textsize;
    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }
}
