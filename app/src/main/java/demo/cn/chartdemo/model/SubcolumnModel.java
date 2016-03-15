package demo.cn.chartdemo.model;

import android.graphics.RectF;

/**
 * Created by Albert on 2016/2/1.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description:子柱状图model
 */
public class SubcolumnModel extends BaseModel {
    private float value;//子柱状图值
    private int originColor;//子柱状图原始色值
    private int color;//当前显示的颜色
    private int unSelectedColor;//子柱状图未选中状态
    private int firstIndex;//一级索引，第几个柱状图
    private int secondIndex;//二级索引，第几个子柱状图

    private RectF rect;//对应的矩形区域

    public SubcolumnModel(SubcolumnModel model) {
        this.value = model.getValue();
        this.originColor = model.getOriginColor();
        this.color = model.getColor();
        this.unSelectedColor = model.getUnSelectedColor();
        this.firstIndex = model.getFirstIndex();
        this.secondIndex = model.getSecondIndex();
        this.rect = model.getRect();
    }

    public SubcolumnModel() {

    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getOriginColor() {
        return originColor;
    }

    public void setOriginColor(int originColor) {
        this.originColor = originColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getUnSelectedColor() {
        return unSelectedColor;
    }

    public void setUnSelectedColor(int unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(int secondIndex) {
        this.secondIndex = secondIndex;
    }

    public RectF getRect() {
        return rect;
    }

    public void setRect(RectF rect) {
        this.rect = rect;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SubcolumnModel){
            SubcolumnModel model = (SubcolumnModel) o;
            return ((this.getFirstIndex() == model.getFirstIndex()) && (this.getSecondIndex() == model.getSecondIndex()));
        }
        return super.equals(o);
    }
}
