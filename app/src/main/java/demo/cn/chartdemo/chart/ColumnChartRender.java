package demo.cn.chartdemo.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import demo.cn.chartdemo.model.ColumnDataModel;
import demo.cn.chartdemo.model.SubcolumnModel;

/**
 * Description: 柱状图渲染器
 */
public class ColumnChartRender {

    private ColumnDataModel mDataModel;
    private ColumnChartCaculator mCaculator;
    private float columnWidth;

    public ColumnChartRender(ColumnDataModel dataModel,ColumnChartCaculator columnChartCaculator) {
        this.mDataModel = dataModel;
        this.mCaculator = columnChartCaculator;
        columnWidth = columnChartCaculator.getColumnWidth(dataModel);
    }

    public ColumnDataModel getmDataModel() {
        return mDataModel;
    }

    public void setmDataModel(ColumnDataModel mDataModel) {
        this.mDataModel = mDataModel;
    }

    public ColumnChartCaculator getmCaculator() {
        return mCaculator;
    }

    public void setmCaculator(ColumnChartCaculator mCaculator) {
        this.mCaculator = mCaculator;
    }

    /**
     * 设置柱坐标范围
     * @param subcolumnModel
     */
    public void setRect(SubcolumnModel subcolumnModel){
        RectF rect = mCaculator.getSubcolumnRect(subcolumnModel.getFirstIndex(),subcolumnModel.getSecondIndex(),subcolumnModel.getValue(),columnWidth);
        subcolumnModel.setRect(rect);
    }
    public synchronized void draw(Canvas canvas,SubcolumnModel subcolumnModel,int x,int y){
        Paint subcolumnPaint = new Paint();
        subcolumnPaint.setColor(subcolumnModel.getColor());
        subcolumnPaint.setAntiAlias(true);
        subcolumnPaint.setStyle(Paint.Style.FILL);
        subcolumnPaint.setStrokeCap(Paint.Cap.SQUARE);

        int clipRestoreCount = canvas.save();
//        canvas.clipRect(subcolumnModel.getRect());
        RectF rect = subcolumnModel.getRect();

        if (rect.right>x&&rect.top<y) {
            float left= rect.left;
            float bottom= rect.bottom;
            if (rect.left<x)
                left=x;
            if (bottom>y)
                bottom=y;
            canvas.drawRect(left,rect.top , rect.right, bottom, subcolumnPaint);
        }
        canvas.restoreToCount(clipRestoreCount);

    }
}
