package demo.cn.chartdemo.Utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Author  wangchenchen
 * CreateDate 2016/1/14.
 * Email wcc@jusfoun.com
 * Description 计算辅助类
 */
public class MathHelper {

    private static MathHelper mathHelper;
    private PointF pointF=new PointF();

    private MathHelper(){

    }
    public static MathHelper getInstanse() {
        if (mathHelper == null) {
            mathHelper = new MathHelper();
        }
        return mathHelper;
    }

    /**
     * 判断一个点是否在圆内
     *
     * @param mRadius 半径
     * @param x       x坐标
     * @param y       y坐标
     * @param mCentre 圆心
     * @return true是 false 否
     */
    public boolean isInPie(float mRadius, float x, float y, Point mCentre) {
        int x2 = (int) Math.pow(x - mCentre.x, 2);
        int y2 = (int) Math.pow(y - mCentre.y, 2);
        float radius = (float) Math.sqrt(x2 + y2);
        if (radius <= mRadius)
            return true;
        return false;
    }

    /**
     * 判断一个点是否在圆内
     *
     * @param maxRadius 大圆半径
     * @param minRadius 小圆半径
     * @param x       x坐标
     * @param y       y坐标
     * @param mCentre 圆心
     * @return true是 false 否
     */
    public boolean isInArc(float maxRadius, float minRadius
            , float x, float y, Point mCentre) {
        int x2 = (int) Math.pow(x - mCentre.x, 2);
        int y2 = (int) Math.pow(y - mCentre.y, 2);
        float radius = (float) Math.sqrt(x2 + y2);
        if (radius<=maxRadius&&radius>=minRadius)
            return true;
        return false;
    }

    /**判断一个点是否在一段圆弧内
     *
     * @param maxRadius 大圆半径
     * @param minRadius 小圆半径
     * @param x x坐标
     * @param y y坐标
     * @param mCentre 中心点坐标
     * @param startAngel 开始角度
     * @param endAngel 结束角度
     * @return true 是 false 否
     */
    public boolean isAngelArc(float maxRadius, float minRadius
            , float x, float y, Point mCentre,float startAngel,float endAngel){
        if (startAngel>360)
            startAngel-=360;
        if (startAngel<0)
            startAngel=360+startAngel;
        endAngel+=startAngel;
        if (endAngel>360)
            endAngel-=360;
        if (endAngel<0)
            endAngel+=360;
        int x2 = (int) Math.pow(x - mCentre.x, 2);
        int y2 = (int) Math.pow(y - mCentre.y, 2);
        float radius = (float) Math.sqrt(x2 + y2);
        float b=y-mCentre.y;
        float currentArc= (float) Math.asin(b / radius);
        float angel= (float) Math.toDegrees(currentArc);
        Log.e("angel1",angel+"");
        if (x<mCentre.x){
            angel=180f-angel;
        }
        if (angel<=0){
            angel=360f+angel;
        }
        Log.e("angel2",angel+"");

//        if (b<0){
//            currentArc= (float) (Math.asin(y / radius)+Math.PI);
//        }else if (b>=0){
//            currentArc= (float)Math.asin(y / radius);
//        }
        if (radius<=maxRadius
                &&radius>=minRadius
                ) {
            if (startAngel<endAngel) {
                if (angel <= endAngel&&angel>startAngel)
                    return true;
            }else if (startAngel>endAngel){
                endAngel+=360;
                if (angel<startAngel)
                    angel+=360;
                if (angel <= endAngel)
                    return true;
            }
        }
        return false;
    }

    /**
     * 获取点xy与中心点连线角度
     * @param x x 坐标
     * @param y y 坐标
     * @param mCentre 中心点
     * @return 角度
     */
    public float getAngel(float x,float y,Point mCentre){
        float angel=0;
        float c= (float) Math.sqrt((x - mCentre.x) * (x - mCentre.x)
                + (y - mCentre.y) * (y - mCentre.y));
        float a=mCentre.x-x;
        float b=mCentre.y-y;
        if (b==0&&a>=0){
            angel=0;
        }else if (b==0&&a<0){
            angel=180;
        }else if (a==0&&b>=0){
            angel=90;
        }else if (a==0&&b<0){
            angel=270;
        }else if (b<0){
            angel= (float) (Math.PI+Math.asin(a/c)+90*Math.PI/180);
        }else if (b>0){
            angel= (float) (90*Math.PI/180-Math.asin(a/c));
        }
        angel=(float) (angel*180/Math.PI);
        angel+=180;
        if (angel>360)
            angel-=360;
        return angel;
    }

    /**
     * 根据圆心坐标与角度计算圆心射线与圆交点坐标
     *
     * @param angel  角度
     * @param mCentre 圆心
     * @param radius 圆半径
     * @return 点坐标
     */
    public PointF getPoint(float angel,float radius,Point mCentre){

        if (angel >= 360)
            angel -= 360;
        if (angel<=0)
            angel+=360;
        float X=mCentre.x;
        float Y=mCentre.y;
        float arcAngle = (float) (Math.PI * (angel / 180.0f));
        if (Float.compare(arcAngle, 0.0f) < 0) {
            pointF.x = pointF.y = 0;
        } else if (Float.compare(angel, 0.0f) == 0) {
            pointF.x = X + radius;
            pointF.y = Y;
        } else if (Float.compare(angel, 90.0f) == 0) {
            pointF.x = X;
            pointF.y = Y + radius;
        } else if (Float.compare(angel, 180.0f) == 0) {
            pointF.x = X - radius;
            pointF.y = Y;
        } else if (Float.compare(angel, 270.0f) == 0) {
            pointF.x = X;
            pointF.y = Y - radius;
        } else if (Float.compare(angel, 0.0f) > 0
                && Float.compare(angel, 90.0f) < 0) {
            pointF.x = (float) (X + Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y + Math.sin(arcAngle) * radius);

        } else if (Float.compare(angel, 90.0f) > 0
                && Float.compare(angel, 180.0f) < 0) {
            angel = 180 - angel;
            arcAngle = (float) (Math.PI * (angel / 180.0f));
            pointF.x = (float) (X - Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y + Math.sin(arcAngle) * radius);
        } else if (Float.compare(angel, 180.0f) > 0
                && Float.compare(angel, 270.0f) < 0) {
            angel -= 180;
            arcAngle = (float) (Math.PI * (angel / 180.0f));
            pointF.x = (float) (X - Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y - Math.sin(arcAngle) * radius);
        } else if (Float.compare(angel, 270.0f) > 0
                && Float.compare(angel, 360.0f) < 0) {
            angel = 360 - angel;
            arcAngle = (float) (Math.PI * (angel / 180.0f));
            pointF.x = (float) (X + Math.cos(arcAngle) * radius);
            pointF.y = (float) (Y - Math.sin(arcAngle) * radius);
        }

        return pointF;
    }

    /**
     * 获取手指抬起后惯性方向及速度
     * @param dx x轴方向速度
     * @param dy y轴方向速度
     * @param x 手指抬起x坐标
     * @param y 手指抬起y坐标
     * @return 手指抬起时方向与速度
     */
    public float vectorToScalarScroll(float dx, float dy, float x, float y) {
        // get the length of the vector
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        // decide if the scalar should be negative or positive by finding
        // the dot product of the vector perpendicular to (x,y).
        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }

    /**
     * 获取一条线和y轴交点y坐标
     * @param startX 线开始x坐标
     * @param startY 线开始y坐标
     * @param endX 线结束x坐标
     * @param endY 线结束y坐标
     * @param index
     * @return
     */
    public float getPointF(float startX,float startY,float endX,float endY,float index){
        float y=index/(endX-startX)*(endY-startY);
        return y;
    }

    private Point lastDown=new Point();
    public void limitDragScope(float[] tran,RectF rect,RectF limitRect,MotionEvent event,boolean isHorizontal){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastDown.x= (int) event.getX();
                lastDown.y= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int x= (int) event.getX();
                int y= (int) event.getY();
                if (!isHorizontal) {
                    if (x - lastDown.x > 0) {

                        if (tran[0] + x - lastDown.x >= limitRect.left - rect.left)
                            tran[0] = 0;
                        else
                            tran[0] += x - lastDown.x;
                    } else if (x - lastDown.x < 0) {
                        if (limitRect.right-rect.right>0)
                            break;
                        if (tran[0] + x - lastDown.x <= limitRect.right - rect.right) {
                            tran[0] = limitRect.right - rect.right;
                        } else
                            tran[0] += x - lastDown.x;
                    }
                }else {
                    if (y-lastDown.y<0){
                        if (tran[1]+y-lastDown.y<=limitRect.bottom-rect.bottom)
                            tran[1]=0;
                        else
                            tran[1]+=y-lastDown.y;
                    }else if (y-lastDown.y>0){
                        Log.e("tran", (limitRect.top) + "");
                        if (limitRect.top-rect.top<0)
                            break;
                        if (tran[1]+y-lastDown.y>=limitRect.top-rect.top){
                            tran[1]=limitRect.top-rect.top;
                        }else
                            tran[1]+=y-lastDown.y;
                    }
                }
                lastDown.x=x;
                lastDown.y=y;
                break;
        }
//        if (rect.top<=limitRect.top){
//            tran[1]=rect.top-limitRect.top;
//        }
//        if (rect.bottom>=limitRect.bottom){
//            tran[1]=rect.bottom-limitRect.bottom;
//        }
    }

    //计算点到直线的最短距离
    public float pointToLine(float x1, float y1, float x2, float y2, float x0,
                             float y0) {
        double space = 0;
        double a, b, c;
        a = lineSpace(x1, y1, x2, y2);// 线段的长度
        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
            space = 0;
            return (float) space;
        }
        if (a <= 0.000001) {
            space = b;
            return (float) space;
        }
        if (c * c >= a * a + b * b) {
            space = b;
            return (float) space;
        }
        if (b * b >= a * a + c * c) {
            space = c;
            return (float) space;
        }
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return (float) space;
    }
    // 计算两点之间的距离
    public double lineSpace(float x1, float y1, float x2, float y2) {
        double lineLength = 0;
        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
                * (y1 - y2));
        return lineLength;
    }

}
