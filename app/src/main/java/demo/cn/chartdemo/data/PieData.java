package demo.cn.chartdemo.data;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import demo.cn.chartdemo.model.PieChartModel;

/**
 * Description 饼图绘制相关属性
 */
public class PieData {

    private List<Integer> grayColors;//未选中饼图颜色
    private int red, green, blue;//随机生成红绿蓝色值
    private Paint paint;//画笔
    private List<PieChartModel> pieData;//扇形数据
    private List<Integer> colors;//所有扇形颜色

    private int count;
    private TextPaint textPaint;//字体画笔

    public PieData(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        colors=new ArrayList<>();
        pieData=new ArrayList<>();
        grayColors = new ArrayList<>();
        Random random = new Random();
        count = random.nextInt(10);
        while (count < 2) {
            count = random.nextInt(10);//随机生成饼图数量
        }

        //初始化饼图属性
        for (int i = 0; i < count; i++) {
            if (i == 0)
                grayColors.add(Color.DKGRAY);
            else {
                if (grayColors.get(i - 1).equals(Color.DKGRAY))
                    grayColors.add(Color.GRAY);
                else if (grayColors.get(i - 1).equals(Color.GRAY))
                    grayColors.add(Color.LTGRAY);
                else if (grayColors.get(i - 1).equals(Color.LTGRAY))
                    grayColors.add(Color.DKGRAY);
            }

            PieChartModel model = new PieChartModel();
            float percent = 1f / count * 100;
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            model.setPercent(decimalFormat.format(percent) + "%");
            model.setStartAngel(360f / count * i);
            model.setSectorAngel(360f / count);
            pieData.add(model);

            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            colors.add(Color.argb(255, red, green, blue));

        }
    }

    public void setPaintColor(int color){
        paint.setColor(color);
    }

    public List<Integer> getGrayColors() {
        return grayColors;
    }

    public List<PieChartModel> getPieData() {
        return pieData;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getCount() {
        return count;
    }
}
