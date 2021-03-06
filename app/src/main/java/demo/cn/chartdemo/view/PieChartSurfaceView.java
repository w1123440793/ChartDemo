package demo.cn.chartdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import demo.cn.chartdemo.chart.PieChart;
import demo.cn.chartdemo.model.PieChartModel;

/**
 * Description
 */
public class PieChartSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private PieChart pieChart;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private PieThread pieThread;
    private List<Integer> colors;
    private List<Integer> grayColors;
    private List<PieChartModel> pieData;
    private int red,green,blue;

    public PieChartSurfaceView(Context context) {
        super(context);
        initView(context);
    }

    public PieChartSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PieChartSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.context = context;
        pieThread = new PieThread();
        colors=new ArrayList<>();
        pieData=new ArrayList<>();
        grayColors=new ArrayList<>();
        Random random=new Random();
        int count=random.nextInt(10);
        while (count==0){
            count=random.nextInt(10);
        }
        for (int i = 0; i <count; i++){
            if (i==0)
                grayColors.add(Color.DKGRAY);
            else{
                if (grayColors.get(i-1).equals(Color.DKGRAY))
                    grayColors.add(Color.GRAY);
                else if (grayColors.get(i-1).equals(Color.GRAY))
                    grayColors.add(Color.LTGRAY);
                else if (grayColors.get(i-1).equals(Color.LTGRAY))
                    grayColors.add(Color.DKGRAY);
            }
            PieChartModel model=new PieChartModel();
            float percent=1f/count*100;
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            model.setPercent(decimalFormat.format(percent)+"%");
            model.setStartAngel(360f / count * i);
            model.setSectorAngel(360f / count);
            red=random.nextInt(255);
            green=random.nextInt(255);
            blue=random.nextInt(255);
            colors.add(Color.argb(255, red, green, blue));
            pieData.add(model);
        }
    }

    public List<PieChartModel> getPieData() {
        return pieData;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public List<Integer> getGrayColors() {
        return grayColors;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pieThread!=null){
            pieThread.setThreadStop();
            pieThread.interrupt();
            pieThread=null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        pieChart = new PieChart(new Point(width / 2, height / 2), this);
        pieChart.setPieChartSurfaceView(this);
        if (pieThread!=null) {
            if (pieThread.getState() == Thread.State.NEW) {
                pieThread.start();
            } else if (pieThread.getState()!= Thread.State.TERMINATED){
                pieThread.resumeThread();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pieThread.setThreadPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pieChart.onTouchEvent(event);
        return true;
    }

    public void resumeThread(){
        pieThread.resumeThread();
    }

    public void setThreadPause(){
        pieThread.setThreadPause();
    }

    class PieThread extends Thread {

        private boolean threadPause = false;
        private boolean threadStop = false;
        private Canvas canvas;

        public void setThreadPause() {
            synchronized (this) {
                threadPause = true;
            }
        }

        public void setThreadStop() {
            synchronized (this) {
                threadStop = true;
            }
        }

        public void resumeThread() {
            synchronized (this) {
                threadPause = false;
                notify();
            }
        }

        @Override
        public void run() {
            while (!threadStop) {
                try {
                    if (!threadPause) {
                        threadPause=true;
                        try{
                        synchronized (surfaceHolder) {
                            canvas = surfaceHolder.lockCanvas();
                            pieChart.onDraw(canvas);
                            Log.e("thread", "threadStart");
                        }

                        }catch (Exception e){

                        }finally {
                            if (canvas!=null)
                                surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                        Thread.sleep(20);
                    } else {
                        synchronized (this) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {

                }
            }
        }
    }

}
