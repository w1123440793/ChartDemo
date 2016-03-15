package demo.cn.chartdemo.chart;

import android.graphics.Point;
import android.graphics.RectF;

import demo.cn.chartdemo.Utils.ColumnUtil;
import demo.cn.chartdemo.model.ColumnDataModel;
import demo.cn.chartdemo.model.SubcolumnModel;

/**
 * Description:工具类，计算每列的宽度及每个子柱状图的矩形坐标
 */
public class ColumnChartCaculator {

    private Point mZero;
    private float mChartHeight,mChartWidth;
    private ColumnDataModel mOrginalColumnData;

    /**
     * @param point  坐标原点
     * @param height 图表高度
     * @param width  图表宽度
     * @param mOrginalColumnData
     */
    public ColumnChartCaculator(Point point, float height, float width, ColumnDataModel mOrginalColumnData) {
        this.mZero = point;
        this.mChartHeight = height;
        this.mChartWidth = width;
        this.mOrginalColumnData = mOrginalColumnData;
    }

    /**
     *
     * @param dataModel 柱状图数据
     * @return 每列宽度按
     */
    public float getColumnWidth(ColumnDataModel dataModel){
        float width = 0;
        float total = 0;

        //TODO 设置固定宽高，配置在接口model中？
        boolean caculate = true;
        if (!caculate){
            return 100;
        }

        total = dataModel.isHorizontal() ? mChartHeight : mChartWidth;

        if ((dataModel != null) && (dataModel.getColumnModels() != null)){
            int columns = dataModel.getColumnModels().size();
            width = (total - columns * ColumnUtil.columnSpace) / columns;
            if (width < ColumnUtil.minColumnWidth){
                //TODO 图表可以进行滑动，需要设置标志位
                width = ColumnUtil.minColumnWidth;
            }

            if (width > ColumnUtil.maxColumnWidth){
                width = ColumnUtil.maxColumnWidth;
            }
        }
        return width;
    }

    /**
     *
     * @param firstIndex  列索引
     * @param secondeIndex  子柱状图索引
     * @param height 子柱状图高度，之后根据value和坐标轴进行重新计算
     * @param width 子柱状图宽度
     * @return  子柱状图对应的rect
     */
    public RectF getSubcolumnRect(int firstIndex, int secondeIndex,float height,float width){
        RectF rect = new RectF();
        if (mOrginalColumnData.isHorizontal()){
            float w = 0f;
            for (SubcolumnModel subcolumnModel : mOrginalColumnData.getColumnModels().get(firstIndex).getSubcolumnModels()){
                if (subcolumnModel.getSecondIndex() < secondeIndex){
                    w += subcolumnModel.getValue();
                }
            }
            rect.left =  mZero.x + secondeIndex * ColumnUtil.subColumnSpace + w + ColumnUtil.columnSpace;
            rect.right = rect.left + height;
            rect.bottom = mZero.y + mChartHeight - firstIndex * (ColumnUtil.columnSpace + width) - ColumnUtil.columnSpace ;
            rect.top = rect.bottom - width;
        }else {
            float h = 0f;
            for (SubcolumnModel subcolumnModel : mOrginalColumnData.getColumnModels().get(firstIndex).getSubcolumnModels()){
                if (subcolumnModel.getSecondIndex() < secondeIndex){
                    h += subcolumnModel.getValue();
                }
            }

            rect.left =  mZero.x + (firstIndex) * (ColumnUtil.columnSpace + width) + ColumnUtil.columnSpace;
            rect.bottom = mZero.y + mChartHeight - (ColumnUtil.subColumnSpace ) * (secondeIndex) - h - ColumnUtil.subColumnSpace;
            rect.top = rect.bottom - height;
            rect.right = rect.left + width;
        }

        return rect;
    }

    public Point getmZero() {
        return mZero;
    }

    public float getmChartHeight() {
        return mChartHeight;
    }

    public float getmChartWidth() {
        return mChartWidth;
    }
}
