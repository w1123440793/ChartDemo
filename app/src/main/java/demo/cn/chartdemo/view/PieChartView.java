package demo.cn.chartdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import demo.cn.chartdemo.Utils.MathHelper;
import demo.cn.chartdemo.data.PieData;
import demo.cn.chartdemo.model.PieChartModel;

/**
 * Author  wangchenchen
 * CreateDate 2016/1/20.
 * Email wcc@jusfoun.com
 * Description 饼图
 */
public class PieChartView extends View {

    private List<RectF> rects;
    private Point mCentre;//中心点
    private int radius;//圆半径
    private Context context;
    private RectF maxArc, minArc;//扇形区域
    private RectF maxSelectArc, minSelectArc;//外扩扇形区域
    private RectF maxWithDrawArc, minWithDrawArc;//外扩扇形区域
    private int clickCount = -1, lastCount = -1;//点击到扇形
    private VelocityTracker mVelocity;
    private int index; //0.1 的半径
    private int width, height;
    private PointF txtPoint;//字体位置
    private RectF bottomRect;
    private PieData pieData;

    private boolean isMove = false;
    private Point lastDown = new Point();
    private Point moveDown = new Point();
    private boolean scroll = false;//是否可执行惯性
    private boolean doublePoint = false;
    private boolean isClick = false;
    private float velocity;

    private int animCount = 0;//动画执行频率角度，每一帧5角度

    private float lastRotation;
    private boolean isRotation = false;
    private ValueAnimator animator;

    private float count;//选中饼图外扩大小
    private float clickAngel = 0f;

    private float outCount = 0f;

    public PieChartView(Context context) {
        super(context);
        initView(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void clickView(boolean is) {
        lastCount = clickCount;
        if (is) {
            clickCount += 1;
            if (clickCount == pieData.getCount())
                clickCount = 0;
        } else {
            clickCount -= 1;
            if (clickCount < 0)
                clickCount = pieData.getCount() - 1;
        }
        clickAngel = pieData.getPieData().get(clickCount).getStartAngel() + pieData.getPieData().get(clickCount).getSectorAngel() / 2;
        if (clickAngel >= 360)
            clickAngel -= 360;
        outCount = 0f;
        if (clickAngel >= 270 && clickAngel <= 360) {
            clickAngel = 360 - clickAngel + 90;
        } else if (clickAngel >= 90 && clickAngel <= 270) {
            clickAngel = 90 - clickAngel;
        } else if (clickAngel >= 0 && clickAngel <= 90) {
            clickAngel = 90 - clickAngel;
        }
        isRotation = true;
        rotationAnim();
        postInvalidate();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {

        this.context = context;
        bottomRect = new RectF();
        pieData = new PieData();
        maxWithDrawArc = new RectF();
        minWithDrawArc = new RectF();
        rects = new ArrayList<>();
        txtPoint = new PointF();
        animator = new ValueAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        mCentre = new Point(w / 2, h / 2);
        radius = Math.min(mCentre.x, mCentre.y);
        maxArc = new RectF();
        minArc = new RectF();
        maxSelectArc = new RectF();
        minSelectArc = new RectF();
        index = (int) (0.1f * radius);
        //设置饼图大小
        maxArc.set(mCentre.x - 6 * index, mCentre.y - 6 * index
                , mCentre.x + 6 * index, mCentre.y + 6 * index);
        minArc.set(mCentre.x - 4 * index, mCentre.y - 4 * index
                , mCentre.x + 4 * index, mCentre.y + 4 * index);
        maxSelectArc.set(mCentre.x - 6.5f * index, mCentre.y - 6.5f * index
                , mCentre.x + 6.5f * index, mCentre.y + 6.5f * index);
        minSelectArc.set(mCentre.x - 4.5f * index, mCentre.y - 4.5f * index
                , mCentre.x + 4.5f * index, mCentre.y + 4.5f * index);
        //选中饼图外扩大小
        count = index / 2;

        for (int i = 0; i < pieData.getCount(); i++) {
            float angel = pieData.getPieData().get(i).getStartAngel() + pieData.getPieData().get(i).getSectorAngel() / 2 - 90;//转动到扇形中心垂直向下
            RectF rectf = new RectF();
            float left = width / 2 - angel / 360f * width - 20;
            float top = mCentre.y + radius + 20;
            float right = width / 2 - angel / 360f * width + 20;
            float bottom = mCentre.y + radius + 60;
            if (left > width) {
                left -= width;
                right -= width;
            } else if (right < 0) {
                left += width;
                right += width;
            }
            rectf.set(left, top, right, bottom);
            rects.add(rectf);
        }
        bottomRect.set(0, mCentre.y + radius, width, h);
        mVelocity = VelocityTracker.obtain();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawColor(Color.WHITE);//绘制背景
        Log.d("TAG", "onDraw 方法");
//        if (rectCount <= (0.7 * radius)) {
//            paint.setColor(Color.BLUE);
//            canvas.drawRoundRect(maxArc, rectCount, rectCount, paint);
//            rectCount += 5f;
//        } else
        if (animCount <= 360) {//饼图出现动画，从0度绘制到360度

            pieData.setPaintColor(Color.BLUE);
//            canvas.drawRoundRect(maxArc, rectCount, rectCount, paint);
            startAnim(canvas);
            postInvalidate();
        } else {

            if (clickCount != -1 && outCount <= count) {
                outAnim(canvas);
            } else {

                if (clickCount != -1 && isRotation) {
//               if (clickCount != -1 && Math.abs(clickAngel - countAngel) > 0) {
//
//                    //点击后旋转动画，如果点击部分中心点在左边逆时针转动，在右边顺时针转动
//                    float count=Math.abs(clickAngel-countAngel)<5?Math.abs(clickAngel - countAngel):5;
                    for (int i = 0; i < pieData.getCount(); i++) {
                        //另一种转动方式
                        /*float angel = pieData.getPieData().get(i).getStartAngel();
                        RectF rectF=rects.get(i);
                        if (clickAngel > 0) {
                            angel += count;
                            rectF.left+=count/360f*width;
                            rectF.right+=count/360f*width;
                            if (angel >= 360)
                                angel -= 360;
                        } else {
                            angel -= count;
                            rectF.left-=count/360f*width;
                            rectF.right-=count/360f*width;
                            if (angel < 0)
                                angel += 360;
                        }
                        if (rectF.left > width) {
                            rectF.left -= width;
                            rectF.right -= width;
                        } else if (rectF.right < 0) {
                            rectF.left += width;
                            rectF.right += width;
                        }
                        pieData.getPieData().get(i).setStartAngel(angel);*/

                        if (i == clickCount) {
                            pieData.setPaintColor(pieData.getColors().get(clickCount));
                            canvas.drawArc(maxSelectArc, pieData.getPieData().get(i).getStartAngel()
                                    , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());

                        } else {
                            if (clickCount == lastCount)
                                pieData.setPaintColor(pieData.getColors().get(i));
                            else
                                pieData.setPaintColor(pieData.getGrayColors().get(i));
                            canvas.drawArc(maxArc, pieData.getPieData().get(i).getStartAngel()
                                    , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                            pieData.setPaintColor(Color.WHITE);
                            canvas.drawArc(minArc, pieData.getPieData().get(i).getStartAngel()
                                    , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());

                        }
                    }
//                    pieData.setPaintColor(Color.WHITE);
                    /*if (clickAngel >= 0) {
                        countAngel += count;
                    } else
                        countAngel -= count;*/
//                    postInvalidate();
                } else {
                    //根据手势旋转，根据手势运动旋转饼图
                    clickAngel = 0;
                    float angel;
                    if (clickCount == lastCount)
                        clickCount = -1;
                    for (int i = 0; i < pieData.getCount(); i++) {
                        if (clickCount != -1) {

                            if (i == clickCount) {
                                pieData.setPaintColor(pieData.getColors().get(clickCount));
                                canvas.drawArc(maxSelectArc, pieData.getPieData().get(i).getStartAngel()
                                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());

                            } else {
                                if (clickCount == lastCount)
                                    pieData.setPaintColor(pieData.getColors().get(i));
                                else
                                    pieData.setPaintColor(pieData.getGrayColors().get(i));
                                canvas.drawArc(maxArc, pieData.getPieData().get(i).getStartAngel()
                                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                                pieData.setPaintColor(Color.WHITE);
                                canvas.drawArc(minArc, pieData.getPieData().get(i).getStartAngel()
                                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                            }

                        } else {
                            pieData.setPaintColor(pieData.getColors().get(i));
                            canvas.drawArc(maxArc, pieData.getPieData().get(i).getStartAngel()
                                    , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                            pieData.setPaintColor(Color.WHITE);
                            canvas.drawArc(minArc, pieData.getPieData().get(i).getStartAngel()
                                    , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                        }
                    }
                }
            }
        }
        pieData.setPaintColor(Color.WHITE);
        canvas.drawCircle(mCentre.x, mCentre.y, 4 * index, pieData.getPaint());//绘制中心圆，成为环形饼图
        if (lastCount != clickCount && lastCount >= 0) {
            canvas.drawArc(minWithDrawArc, pieData.getPieData().get(lastCount).getStartAngel()
                    , pieData.getPieData().get(lastCount).getSectorAngel(), true, pieData.getPaint());
        }
        if (clickCount >= 0 && clickCount < pieData.getCount()) {//判断是否有外扩饼图
            if (clickCount != lastCount) {
                pieData.getTextPaint().setTextAlign(Paint.Align.CENTER);
                canvas.drawArc(minSelectArc, pieData.getPieData().get(clickCount).getStartAngel()
                        , pieData.getPieData().get(clickCount).getSectorAngel(), true, pieData.getPaint());
                canvas.drawText("第" + clickCount + "个" + pieData.getPieData().get(clickCount).getPercent()
                        , mCentre.x, mCentre.y, pieData.getTextPaint());
            } else if (lastCount == clickCount) {
                canvas.drawArc(minWithDrawArc, pieData.getPieData().get(clickCount).getStartAngel()
                        , pieData.getPieData().get(clickCount).getSectorAngel(), true, pieData.getPaint());
            }
        }

        for (int i = 0; i < pieData.getCount(); i++) {
            if (clickCount >= 0) {
                if (clickCount == i)
                    pieData.setPaintColor(pieData.getColors().get(i));
                else
                    pieData.setPaintColor(pieData.getGrayColors().get(i));
            } else {
                pieData.setPaintColor(pieData.getColors().get(i));
            }
            canvas.drawRect(rects.get(i), pieData.getPaint());
        }
        canvas.restore();
        drawText(canvas);
    }

    /**
     * 外扩与收缩动画
     */
    public void outAnim(Canvas canvas) {
        minSelectArc.set(mCentre.x - 4 * index - outCount, mCentre.y - 4 * index - outCount
                , mCentre.x + 4 * index + outCount, mCentre.y + 4 * index + outCount);
        maxSelectArc.set(mCentre.x - 6 * index - outCount, mCentre.y - 6 * index - outCount
                , mCentre.x + 6 * index + outCount, mCentre.y + 6 * index + outCount);
        minWithDrawArc.set(mCentre.x - 4.5f * index + outCount, mCentre.y - 4.5f * index + outCount
                , mCentre.x + 4.5f * index - outCount, mCentre.y + 4.5f * index - outCount);
        maxWithDrawArc.set(mCentre.x - 6.5f * index + outCount, mCentre.y - 6.5f * index + outCount
                , mCentre.x + 6.5f * index - outCount, mCentre.y + 6.5f * index - outCount);

        for (int i = 0; i < pieData.getCount(); i++) {
            if (i == clickCount && clickCount != lastCount) {
                pieData.setPaintColor(pieData.getColors().get(i));
                canvas.drawArc(maxSelectArc, pieData.getPieData().get(i).getStartAngel()
                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());

            } else if (i == lastCount && clickCount != lastCount && lastCount != -1) {
                pieData.setPaintColor(pieData.getColors().get(i));
                canvas.drawArc(maxWithDrawArc, pieData.getPieData().get(i).getStartAngel()
                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());

            } else if (i == clickCount && clickCount == lastCount) {
                pieData.setPaintColor(pieData.getColors().get(i));
                canvas.drawArc(maxWithDrawArc, pieData.getPieData().get(i).getStartAngel()
                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
            } else {

                pieData.setPaintColor(pieData.getGrayColors().get(i));
                canvas.drawArc(maxArc, pieData.getPieData().get(i).getStartAngel()
                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                pieData.setPaintColor(Color.WHITE);
                canvas.drawArc(minArc, pieData.getPieData().get(i).getStartAngel(),
                        pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());
                pieData.setPaintColor(Color.WHITE);
            }
        }
        if (Math.abs(count - outCount) < 3) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        outCount += 3f;
        postInvalidate();
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        pieData.getTextPaint().setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < rects.size(); i++) {
            if (clickCount != -1) {
                if (i != clickCount) {
                    pieData.getTextPaint().setColor(pieData.getGrayColors().get(i));

                } else {
                    pieData.getTextPaint().setColor(pieData.getColors().get(i));
                }
            } else {
                pieData.getTextPaint().setColor(pieData.getColors().get(i));
            }
            canvas.save();
            canvas.rotate(60, (rects.get(i).right - rects.get(i).left) / 2 + rects.get(i).left, rects.get(i).bottom + 50);
            canvas.drawText(pieData.getPieData().get(i).getPercent(),
                    (rects.get(i).right - rects.get(i).left) / 2 + rects.get(i).left, rects.get(i).bottom + 50, pieData.getTextPaint());
            canvas.restore();
        }
    }

    /**
     * 重置饼图
     */
    public void reset() {
        clickCount = -1;
        animCount=0;
        postInvalidate();
    }

    /**
     * 旋转动画
     */
    public void rotationAnim() {
        lastRotation = 0;
        animator.setFloatValues(0, clickAngel);
        animator.setDuration(1000);
        animator.setStartDelay(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getCurrentPlayTime() >= 1000) {
                    isRotation = false;
                    return;
                }
                float rotation = (Float) animation.getAnimatedValue() - lastRotation;
                for (int i = 0; i < pieData.getCount(); i++) {
                    float angel = pieData.getPieData().get(i).getStartAngel();
                    RectF rectF = rects.get(i);
                    angel += rotation;
                    rectF.left -= rotation / 360f * width;
                    rectF.right -= rotation / 360f * width;
                    if (angel >= 360)
                        angel -= 360;
                    if (angel <= 0)
                        angel += 360;
                    if (rectF.left > width) {
                        rectF.left -= width;
                        rectF.right -= width;
                    } else if (rectF.right < 0) {
                        rectF.left += width;
                        rectF.right += width;
                    }
                    pieData.getPieData().get(i).setStartAngel(angel);
                }
                lastRotation = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        if (!animator.isRunning())
            animator.start();

    }

    /**
     * 开始动画
     *
     * @param canvas
     */
    private void startAnim(Canvas canvas) {
        Log.d("TAG", "执行画图动画");
        int curcentCount = 0;
        if (animCount <= 360) {
            for (int i = 0; i < pieData.getCount(); i++) {
                if (i == 0) {
                    if (animCount < pieData.getPieData().get(i).getStartAngel()
                            + pieData.getPieData().get(i).getSectorAngel()) {
                        curcentCount = 0;
                    }
                } else {
                    //第一个圆环以5角度循环刷新完成之后，再进行第二个
                    if (animCount <= pieData.getPieData().get(i).getStartAngel() + pieData.getPieData().get(i).getSectorAngel()
                            && animCount > pieData.getPieData().get(i).getStartAngel())
                        curcentCount = i;
                }
            }
        }
        for (int i = 0; i < curcentCount + 1; i++) {
            if (i == curcentCount) {
                pieData.setPaintColor(pieData.getColors().get(i));
                canvas.drawArc(maxArc, pieData.getPieData().get(i).getStartAngel(), animCount
                        - pieData.getPieData().get(curcentCount).getStartAngel(), true, pieData.getPaint());

            } else {
                pieData.setPaintColor(pieData.getColors().get(i));
                canvas.drawArc(maxArc, pieData.getPieData().get(i).getStartAngel()
                        , pieData.getPieData().get(i).getSectorAngel(), true, pieData.getPaint());

            }
        }
        animCount += 5;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocity.addMovement(event);
        mVelocity.computeCurrentVelocity(1000);
        if (event.getPointerCount() >= 2) {
            doublePoint = true;
            return true;

        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (isRotation)
                    break;
                isMove = false;
                doublePoint = false;
                isClick = false;
                clickAngel = 0;
                lastDown.x = (int) event.getX();
                lastDown.y = (int) event.getY();
                moveDown.x = (int) event.getX();
                moveDown.y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (doublePoint || isRotation)
                    break;
                moveDown.x = (int) event.getX();
                moveDown.y = (int) event.getY();

                if (bottomRect.contains(event.getX(), event.getY())) {
                    float x = moveDown.x - lastDown.x;
                    if (Math.abs(x) > 10) {
                        for (int i = 0; i < rects.size(); i++) {
                            RectF rect = rects.get(i);
                            rect.left += x;
                            rect.right += x;
                            if (rect.left > width) {
                                rect.left -= width;
                                rect.right -= width;
                            } else if (rect.right < 0) {
                                rect.left += width;
                                rect.right += width;
                            }
                            float angel = pieData.getPieData().get(i).getStartAngel();
                            angel -= x / (float) width * 360f;
                            if (angel >= 360)
                                angel -= 360;
                            if (angel < 0)
                                angel += 360;
                            pieData.getPieData().get(i).setStartAngel(angel);
                        }
                        isMove = true;
                        lastDown.x = moveDown.x;
                        lastDown.y = moveDown.y;
                        postInvalidate();
                    }
                }
                if (MathHelper.getInstanse().isAngelArc(0.8f * radius, 4 * index, event.getX()
                        , event.getY(), mCentre, 0, 360)) {
                    if (Math.sqrt(Math.pow(moveDown.x - lastDown.x, 2) + Math.pow(moveDown.y - lastDown.y, 2)) > 5) {
                        float fromAngel = MathHelper.getInstanse().getAngel(lastDown.x, lastDown.y, mCentre);
                        float toAngel = MathHelper.getInstanse().getAngel(moveDown.x, moveDown.y, mCentre);
                        for (int i = 0; i < pieData.getCount(); i++) {
                            float angel = pieData.getPieData().get(i).getStartAngel();
                            angel += (toAngel - fromAngel);
                            float x = (toAngel - fromAngel) / 360f * width;
                            rects.get(i).left -= x;
                            rects.get(i).right -= x;
                            if (rects.get(i).left > width) {
                                rects.get(i).left -= width;
                                rects.get(i).right -= width;
                            } else if (rects.get(i).right < 0) {
                                rects.get(i).left += width;
                                rects.get(i).right += width;
                            }
                            if (angel > 360)
                                angel -= 360;
                            if (angel < 0)
                                angel += 360;
                            pieData.getPieData().get(i).setStartAngel(angel);
                        }

//                    pieChartView.postInvalidate();
                        isMove = true;
                        postInvalidate();
                    }

                    lastDown.x = moveDown.x;
                    lastDown.y = moveDown.y;
                }
                break;
            case MotionEvent.ACTION_UP:

                if (doublePoint || isRotation)
                    break;
                scroll = false;
                if (isMove) {
                    Log.e("velocity", "velocityX" + mVelocity.getXVelocity() + "velocityY" + mVelocity.getYVelocity());
                    velocity = MathHelper.getInstanse().vectorToScalarScroll(mVelocity.getXVelocity(), mVelocity.getYVelocity()
                            , event.getX() - mCentre.x, event.getY() - mCentre.y);
                    scroll = true;
                    return true;
                }
                velocity = 0;
                if (MathHelper.getInstanse().isInPie(6 * index, event.getX(), event.getY(), mCentre)) {
                    lastCount = clickCount;
                    for (int i = 0; i < pieData.getCount(); i++) {
                        PieChartModel model = pieData.getPieData().get(i);

                        if (MathHelper.getInstanse().isAngelArc(0.7f * radius, 4 * index, event.getX()
                                , event.getY(), mCentre, model.getStartAngel(), model.getSectorAngel())) {
                            clickCount = i;
                            isClick = true;
                            break;
                        }
                    }

                } else {
                    if (clickCount != -1) {
                        lastCount = clickCount;
                        isClick = false;
                        outCount = 0;
                        postInvalidate();
                    }
                }
                if (!isClick) {
                    for (int i = 0; i < rects.size(); i++) {
                        RectF rectF = new RectF(rects.get(i).left - 100, rects.get(i).top - 100, rects.get(i).right + 100, rects.get(i).bottom + 100);
                        if (rectF.contains(event.getX(), event.getY())) {
                            lastCount = clickCount;
                            clickCount = i;
                            isClick = true;
                            break;
                        }
                    }
                }
                if (clickCount != -1 && isClick) {
                    clickAngel = pieData.getPieData().get(clickCount).getStartAngel()
                            + pieData.getPieData().get(clickCount).getSectorAngel() / 2;
                    if (clickAngel >= 360)
                        clickAngel -= 360;
                    outCount = 0f;
                    if (clickAngel >= 270 && clickAngel <= 360) {
                        clickAngel = 360 - clickAngel + 90;
                    } else if (clickAngel >= 90 && clickAngel <= 270) {
                        clickAngel = 90 - clickAngel;
                    } else if (clickAngel >= 0 && clickAngel <= 90) {
                        clickAngel = 90 - clickAngel;
                    }
                    if (clickCount != lastCount) {
                        isRotation = true;
                        rotationAnim();
                    }
                    postInvalidate();
                    if (listener != null)
                        listener.onClick();
                }
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (!scroll)
            return;
        if (Math.abs(velocity) <= 100)
            return;
        velocity *= 0.9;
        for (int i = 0; i < rects.size(); i++) {
            RectF rect = rects.get(i);
            rect.left -= velocity / 100;
            rect.right -= velocity / 100;

            if (rect.left > width) {
                rect.left -= width;
                rect.right -= width;
            } else if (rect.right < 0) {
                rect.left += width;
                rect.right += width;
            }
            float angel = pieData.getPieData().get(i).getStartAngel();
            angel += velocity / 100 / (float) width * 360f;
            if (angel >= 360)
                angel -= 360;
            if (angel < 0)
                angel += 360;
            pieData.getPieData().get(i).setStartAngel(angel);
        }
        postInvalidate();
    }

    public static interface OnClickPieListener {
        public void onClick();
    }

    private OnClickPieListener listener;

    public void setListener(OnClickPieListener listener) {
        this.listener = listener;
    }
}