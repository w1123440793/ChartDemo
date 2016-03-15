package demo.cn.chartdemo.view;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import demo.cn.chartdemo.Utils.ColumnUtil;
import demo.cn.chartdemo.chart.AxesRender;
import demo.cn.chartdemo.chart.ColumnChartCaculator;
import demo.cn.chartdemo.chart.ColumnChartRender;
import demo.cn.chartdemo.chart.ColumnTouchHandler;
import demo.cn.chartdemo.model.ColumnDataModel;
import demo.cn.chartdemo.model.ColumnModel;
import demo.cn.chartdemo.model.SubcolumnModel;

/**
 * Created by Albert on 2016/2/1.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description:柱状图view
 */
public class ColumnChartView extends View{

    //坐标原点
    private Point mZero;

    private ColumnChartCaculator mCaculator;
    private ColumnChartRender mRender;
    private AxesRender mAxesRender;
    private ColumnTouchHandler mHandler;

    private Canvas mCanvas;
    private Point mTranslate=new Point(0,0);


    private boolean subcolumnDown;//是否有子柱状图有down事件
    private boolean subcolumnClick;//是否有子柱状图有点击事件

    //数据
    private ColumnDataModel mColumnData,mOrginalColumnData;

    private SubcolumnModel mSelectedSubcolumn;

    //坐标轴画笔
    Paint mAxisPaint;


    //图表宽高
    private float mChartWidth,mChartHeight;


    public ColumnChartView(Context context) {
        super(context);
        initView();
    }

    public ColumnChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ColumnChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColumnChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView(){
        mZero = new Point();
        mAxisPaint = new Paint();
        mAxisPaint.setColor(Color.RED);
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStrokeWidth(2);
        mAxisPaint.setStyle(Paint.Style.FILL);

        this.mColumnData = new ColumnDataModel();
        this.mOrginalColumnData = new ColumnDataModel();
    }

    public void setmTranslate(float x,float y) {
        this.mTranslate.x +=x;
        this.mTranslate.y+=y;
    }

    public ColumnDataModel getmColumnData() {
        return mColumnData;
    }

    public void setmColumnData(ColumnDataModel mColumnData) {
        this.mColumnData.setColumnModels(mColumnData.getColumnModels());
        this.mOrginalColumnData.setColumnModels(mColumnData.getColumnModels());
        this.mColumnData.setHorizontal(mColumnData.isHorizontal());
        this.mOrginalColumnData.setHorizontal(mColumnData.isHorizontal());

    }

    public boolean isSubcolumnDown() {
        return subcolumnDown;
    }

    public void setSubcolumnDown(boolean subcolumnDown) {
        this.subcolumnDown = subcolumnDown;
    }

    public boolean isSubcolumnClick() {
        return subcolumnClick;
    }

    public void setSubcolumnClick(boolean subcolumnClick) {
        this.subcolumnClick = subcolumnClick;
    }

    private void drawColumnChart(Canvas canvas){
        if (mColumnData != null){
            for (ColumnModel columnModel : mColumnData.getColumnModels()){
                for (SubcolumnModel subcolumnModel : columnModel.getSubcolumnModels()){
                    mRender.draw(canvas,subcolumnModel,mZero.x, (int) mChartHeight);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.drawColor(Color.WHITE);
        drawAxis(canvas);
        drawColumnChart(canvas);
        drawLines(canvas);

    }

    /**
     * TODO 绘制坐标文字
     * @param canvas
     */
    private void drawAxis(Canvas canvas){
        mAxesRender.draw(canvas, mTranslate);
    }

    /**
     * TODO:绘制坐标系虚线
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        canvas.drawLine(mZero.x, mZero.y, mZero.x, mZero.y + mChartHeight, mAxisPaint);
        canvas.drawLine(mZero.x, mZero.y + mChartHeight, mZero.x + mChartWidth, mZero.y + mChartHeight, mAxisPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mZero.x = w / 10;
        mZero.y = 0;
        mChartWidth = w * 9 / 10;
        mChartHeight = h * 9 / 10;
        mCaculator = new ColumnChartCaculator(mZero,mChartHeight,mChartWidth,mColumnData);
        mRender = new ColumnChartRender(mOrginalColumnData,mCaculator);
        mAxesRender = new AxesRender(mOrginalColumnData,mCaculator);

        for (ColumnModel columnModel : mOrginalColumnData.getColumnModels()) {
            for (SubcolumnModel subcolumnModel : columnModel.getSubcolumnModels()) {
                mRender.setRect(subcolumnModel);
            }
        }
        //设置移动限定范围,左 原点x坐标，上 距离屏幕上200像素，右距离屏幕右边100像素，下，x轴
        RectF rectF=new RectF(mZero.x, ColumnUtil.offsetTop,w - ColumnUtil.offsetRight,mChartHeight);
        mHandler = new ColumnTouchHandler(mColumnData,this,rectF);

        mHandler.setListener(new ColumnTouchHandler.ValueSelectedListener() {
            @Override
            public void valueSelected(SubcolumnModel subcolumnModel) {
                if (listener!=null)
                    listener.onClick();
                updateView(subcolumnModel);
            }

            @Override
            public void valueDown(boolean target) {
                setSubcolumnDown(target);
            }
        });

        ValueAnimator scaleAnimation = ObjectAnimator.ofFloat(0f, 1f);
        scaleAnimation.setDuration(1000);
        /*scaleAnimation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                List<ColumnModel> modelList = new ArrayList<>();
                for (ColumnModel columnModel : mOrginalColumnData.getColumnModels()) {
                    ColumnModel newcolumnModel = new ColumnModel(columnModel);
                    List<SubcolumnModel> subcolumnModelList = new ArrayList<>();
                    for (SubcolumnModel subcolumnModel : columnModel.getSubcolumnModels()) {
                        SubcolumnModel newsubcolumnModel = new SubcolumnModel(subcolumnModel);
                        newsubcolumnModel.setValue(subcolumnModel.getValue() * input);
                        subcolumnModelList.add(newsubcolumnModel);
                    }
                    newcolumnModel.setSubcolumnModels(subcolumnModelList);
                    modelList.add(newcolumnModel);
                }
                mColumnData.setColumnModels(modelList);


                invalidate();
                return input;
            }
        });*/
        scaleAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = (float) animation.getAnimatedValue();
                List<ColumnModel> modelList = new ArrayList<>();
                for (ColumnModel columnModel : mOrginalColumnData.getColumnModels()) {
                    ColumnModel newcolumnModel = new ColumnModel(columnModel);
                    List<SubcolumnModel> subcolumnModelList = new ArrayList<>();
                    for (SubcolumnModel subcolumnModel : columnModel.getSubcolumnModels()) {
                        SubcolumnModel newsubcolumnModel = new SubcolumnModel(subcolumnModel);
                        newsubcolumnModel.setValue(subcolumnModel.getValue() * ratio);
                        mRender.setRect(newsubcolumnModel);
                        subcolumnModelList.add(newsubcolumnModel);
                    }
                    newcolumnModel.setSubcolumnModels(subcolumnModelList);
                    modelList.add(newcolumnModel);
                }
                mColumnData.setColumnModels(modelList);
                invalidate();
            }
        });

        scaleAnimation.start();

    }

    private void updateView(SubcolumnModel subcolumnModel){
        setSubcolumnClick(true);
        mSelectedSubcolumn = subcolumnModel;
        for (ColumnModel columnModel : mColumnData.getColumnModels()){
            for (SubcolumnModel model : columnModel.getSubcolumnModels()){
                if (model.equals(subcolumnModel)){
                    model.setColor(model.getOriginColor());
                }else {
                    model.setColor(model.getUnSelectedColor());
                }
            }
        }
        postInvalidate();
    }

    /**
     * 复位
     */
    public void reset(){
        for (ColumnModel columnModel : mColumnData.getColumnModels()){
            for (SubcolumnModel subcolumnModel : columnModel.getSubcolumnModels()){
                subcolumnModel.setColor(subcolumnModel.getOriginColor());
            }
        }
        invalidate();
    }

    /**
     * 选中子柱状图之间进行切换
     * @param step 1：右上  -1 ：左下
     */
    public void step(int step){
        int columnIndex = mSelectedSubcolumn.getFirstIndex();
        int subcolumnIndex = mSelectedSubcolumn.getSecondIndex();
        if (step > 0){
            if ((subcolumnIndex + step) > mColumnData.getColumnModels().get(columnIndex).getSubcolumnModels().size() - 1){
                //已经是当前柱状图的最后一个子图
                if ((columnIndex + 1) > mColumnData.getColumnModels().size() - 1){
                    //已经是最后一个柱状图
                    columnIndex = 0;
                    subcolumnIndex = 0;
                }else {
                    columnIndex += 1;
                    subcolumnIndex = 0;
                }
            }else {
                subcolumnIndex += 1;
            }
        }else {
            if ((subcolumnIndex + step) < 0){
                //已经是当前柱状图的第一个子图
                if ((columnIndex - 1) < 0){
                    //已经是第一个柱状图
                    columnIndex = mColumnData.getColumnModels().size() - 1;
                    subcolumnIndex = mColumnData.getColumnModels().get(columnIndex).getSubcolumnModels().size() - 1;
                }else {
                    columnIndex -= 1;
                    subcolumnIndex = mColumnData.getColumnModels().get(columnIndex).getSubcolumnModels().size() - 1;
                }
            }else {
                subcolumnIndex += -1;
            }
        }

        for (int i = 0; i < mColumnData.getColumnModels().size(); i++){
            ColumnModel column = mColumnData.getColumnModels().get(i);
            for (int j = 0; j < column.getSubcolumnModels().size(); j++) {
                SubcolumnModel subcolumnValue = column.getSubcolumnModels().get(j);
                if (!((i == columnIndex) && (j == subcolumnIndex))){
                    subcolumnValue.setColor(subcolumnValue.getUnSelectedColor());
                }else {
                    subcolumnValue.setColor(subcolumnValue.getOriginColor());
                }
            }
        }

        mSelectedSubcolumn = mColumnData.getColumnModels().get(columnIndex).getSubcolumnModels().get(subcolumnIndex);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mHandler.handleEvent(event);
    }

    public static interface OnColumnClickListener{
        public void onClick();
    }

    private OnColumnClickListener listener;

    public void setListener(OnColumnClickListener listener){
        this.listener=listener;
    }
}
