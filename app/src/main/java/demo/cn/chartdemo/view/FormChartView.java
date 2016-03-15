package demo.cn.chartdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Description 表格
 */
public class FormChartView extends View {

    private List<List<PointF>> formHeadPointf;
    private List<List<PointF>> formContentPointf;
    private Context context;
    private int width, height;
    private PointF originPointF;
    private int[] formHeadRanks,formContentRanks;

    private int left,top,right,bottom;
    private int formHeadHeight;
    private Path formHeadPath,formContentPath;
    private Paint formHeadPaint,formContentPaint;

    private TextPaint formHeadText,formContentText;

    public FormChartView(Context context) {
        super(context);
        initViews(context);
    }

    public FormChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public FormChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public FormChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        this.context=context;
        formHeadPointf=new ArrayList<>();
        formContentPointf=new ArrayList<>();
        formHeadRanks=new int[]{2,5};
        formContentRanks=new int[]{20,5};

        originPointF=new PointF();
        formHeadPath=new Path();
        formContentPath=new Path();

        formHeadPaint=new Paint();
        formHeadPaint.setColor(Color.LTGRAY);
        formHeadPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        formHeadPaint.setAlpha(100);

        formContentPaint=new Paint();
        formContentPaint.setColor(Color.BLUE);
        formContentPaint.setStyle(Paint.Style.STROKE);

        formHeadText=new TextPaint();
        formHeadText.setTextSize(20);
        formHeadText.setTextAlign(Paint.Align.CENTER);
        formHeadText.setColor(Color.BLACK);

        formContentText=new TextPaint();
        formContentText.setTextSize(20);
        formContentText.setTextAlign(Paint.Align.CENTER);
        formContentText.setColor(Color.BLACK);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
        left=right=w/10;
        top=bottom=height/10;

        originPointF.x=left;
        originPointF.y=top;
        formHeadHeight=height/15;
        for (int i = 0; i < formHeadRanks[1]; i++) {
            ArrayList<PointF> list=new ArrayList<>();
            for (int j = 0; j <formHeadRanks[0] ; j++) {
                PointF pointF=new PointF();
                pointF.x=i*(width-left-right)/(formHeadRanks[1]-1)+originPointF.x;
                pointF.y=j*formHeadHeight+originPointF.y;
                list.add(pointF);
            }
            formHeadPointf.add(list);
        }

        for (int i = 0; i < formContentRanks[1]; i++) {
            ArrayList<PointF> list=new ArrayList<>();
            for (int j = 0; j <formContentRanks[0] ; j++) {
                PointF pointF=new PointF();
                pointF.x=i*(width-left-right)/(formContentRanks[1]-1)+originPointF.x;
                pointF.y=j*formHeadHeight+originPointF.y+formHeadHeight*(formHeadRanks[0]-1);
                list.add(pointF);
            }
            formContentPointf.add(list);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFormContent(canvas);
        drawFormHead(canvas);
    }

    private PointF lastPointf=new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastPointf.x=event.getX();
                lastPointf.y=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < formContentRanks[1]; i++) {
                    for (int j = 0; j < formContentRanks[0]; j++) {
                        PointF pointf=formContentPointf.get(i).get(j);
                        pointf.y+=event.getY()-lastPointf.y;
                    }
                }
                lastPointf.y=event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 绘制表格头
     * @param canvas
     */
    public void drawFormHead(Canvas canvas){
        formHeadPaint.setColor(Color.LTGRAY);
        formHeadPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        formHeadPath.reset();
        formHeadPath.moveTo(formHeadPointf.get(0).get(0).x, formHeadPointf.get(0).get(0).y);
        formHeadPath.lineTo(formHeadPointf.get(0).get(formHeadRanks[0] - 1).x, formHeadPointf.get(0).get(formHeadRanks[0] - 1).y);
        formHeadPath.lineTo(formHeadPointf.get(formHeadRanks[1] - 1).get(formHeadRanks[0] - 1).x, formHeadPointf.get(formHeadRanks[1] - 1).get(formHeadRanks[0] - 1).y);
        formHeadPath.lineTo(formHeadPointf.get(formHeadRanks[1] - 1).get(0).x, formHeadPointf.get(formHeadRanks[1] - 1).get(0).y);
        formHeadPath.close();
        canvas.drawPath(formHeadPath, formHeadPaint);

        formHeadPaint.setStyle(Paint.Style.STROKE);
        formHeadPaint.setColor(Color.RED);
        formHeadPath.reset();
        formHeadPath.moveTo(originPointF.x, originPointF.y);
        for (int i = 0; i < formHeadRanks[1]; i++) {
            for (int j = 0; j < formHeadRanks[0]; j++) {

                if (j+1<formHeadRanks[0]){
                    formHeadPath.moveTo(formHeadPointf.get(i).get(j).x,formHeadPointf.get(i).get(j).y);
                    formHeadPath.lineTo(formHeadPointf.get(i).get(j+1).x,formHeadPointf.get(i).get(j+1).y);
                }
                if (i+1<formHeadRanks[1]){
                    formHeadPath.moveTo(formHeadPointf.get(i).get(j).x,formHeadPointf.get(i).get(j).y);
                    formHeadPath.lineTo(formHeadPointf.get(i+1).get(j).x,formHeadPointf.get(i+1).get(j).y);
                }

                if(j+1<formHeadRanks[0]&&i+1<formHeadRanks[1]){
                    drawText(canvas,formHeadPointf.get(i).get(j),formHeadPointf.get(i+1).get(j + 1),formHeadText);
                }
            }
        }
        formHeadPath.close();
        canvas.drawPath(formHeadPath, formHeadPaint);

    }

    /**
     * 绘制表格内容
     * @param canvas
     */
    public void drawFormContent(Canvas canvas){
        formContentPath.reset();
        formContentPath.moveTo(originPointF.x,originPointF.y+formHeadHeight*(formHeadRanks[0]-1));
        for (int i = 0; i < formContentRanks[1]; i++) {
            for (int j = 0; j < formContentRanks[0]; j++) {

                if (j+1<formContentRanks[0]){
                    formContentPath.moveTo(formContentPointf.get(i).get(j).x,formContentPointf.get(i).get(j).y);
                    formContentPath.lineTo(formContentPointf.get(i).get(j+1).x,formContentPointf.get(i).get(j+1).y);
                }
                if (i+1<formContentRanks[1]){
                    formContentPath.moveTo(formContentPointf.get(i).get(j).x,formContentPointf.get(i).get(j).y);
                    formContentPath.lineTo(formContentPointf.get(i+1).get(j).x,formContentPointf.get(i+1).get(j).y);
                }

                if(j+1<formContentRanks[0]&&i+1<formContentRanks[1]){
                    drawText(canvas, formContentPointf.get(i).get(j), formContentPointf.get(i + 1).get(j + 1), formContentText);
                }
            }
        }

        formContentPath.close();
        canvas.drawPath(formContentPath,formContentPaint);
    }

    /**
     * 表格内部内容
     * @param canvas
     * @param lt
     * @param rb
     * @param textPaint
     */
    public void drawText(Canvas canvas,PointF lt,PointF rb,TextPaint textPaint){
        float x=lt.x+(rb.x-lt.x)/2;
        float y=lt.y+(rb.y-lt.y)/2;
        canvas.drawText("xxxx",x,y,textPaint);
    }
}
