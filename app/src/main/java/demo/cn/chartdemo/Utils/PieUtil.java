package demo.cn.chartdemo.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 */
public class PieUtil extends View {

    private List<RectF> list;
    public PieUtil(Context context) {
        super(context);
    }

    public PieUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieUtil(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context){
        list=new ArrayList<>();
    }

    public void refresh(List<Integer> colors){
        for (int i = 0; i < colors.size(); i++) {
            RectF rect=new RectF();

        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
