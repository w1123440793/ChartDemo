package demo.cn.chartdemo.data;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Author  wangchenchen
 * CreateDate 2016/2/16.
 * Email wcc@jusfoun.com
 * Description 折线图绘制相关属性
 */
public class LineChartData {

    private Paint dotPaint;
    private Paint linePaint;
    private Paint grayLinePaint;
    private List<String> xAxis;
    private List<String> yAxis;

    private TextPaint xAxisPaint;
    private TextPaint yAxisPaint;

    public LineChartData(){
        xAxis=new ArrayList<>();
        yAxis=new ArrayList<>();
        dotPaint=new Paint();
        linePaint=new Paint();
        grayLinePaint =new Paint();

        dotPaint = new Paint();
        dotPaint.setColor(Color.RED);
        dotPaint.setAntiAlias(true);
        dotPaint.setStrokeWidth(2);
        dotPaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint();
        linePaint.setColor(Color.LTGRAY);
        linePaint.setAlpha(100);
        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Paint.Style.STROKE);
        PathEffect effect = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
        linePaint.setPathEffect(effect);

        grayLinePaint=new Paint();
        grayLinePaint.setColor(Color.GRAY);
        grayLinePaint.setStrokeWidth(2);
        grayLinePaint.setAntiAlias(true);
        grayLinePaint.setStyle(Paint.Style.FILL);

        xAxisPaint=new TextPaint();
        xAxisPaint.setColor(Color.RED);
        xAxisPaint.setTextAlign(Paint.Align.LEFT);
        xAxisPaint.setTextSize(20);

        yAxisPaint=new TextPaint();
        yAxisPaint.setColor(Color.RED);
        yAxisPaint.setTextAlign(Paint.Align.RIGHT);
        yAxisPaint.setTextSize(20);
    }

    public void setxAxis(List<String> xAxis){
        this.xAxis.clear();
        this.xAxis.addAll(xAxis);
    }

    public void setyAxis(List<String> yAxis){
        this.yAxis.clear();
        this.yAxis.addAll(yAxis);
    }

    public void setDotPaintColor(int color){
        dotPaint.setColor(color);
    }

    public void setLinePaintColor(int color){
        linePaint.setColor(color);
    }

    public void setGrayLinePaintColor(int color){
        grayLinePaint.setColor(color);
    }

    public void setxAxisPaintColor(int color){
        xAxisPaint.setColor(color);
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public List<String> getyAxis() {
        return yAxis;
    }

    public TextPaint getxAxisPaint() {
        return xAxisPaint;
    }

    public TextPaint getyAxisPaint() {
        return yAxisPaint;
    }

    public Paint getDotPaint() {
        return dotPaint;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public Paint getGrayLinePaint() {
        return grayLinePaint;
    }
}
