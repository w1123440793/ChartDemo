package demo.cn.chartdemo.chart;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import de.greenrobot.event.EventBus;
import demo.cn.chartdemo.Utils.MathHelper;
import demo.cn.chartdemo.event.SubcolumnSelectEvent;
import demo.cn.chartdemo.model.ColumnDataModel;
import demo.cn.chartdemo.model.ColumnModel;
import demo.cn.chartdemo.model.SelectedModel;
import demo.cn.chartdemo.model.SubcolumnModel;
import demo.cn.chartdemo.view.ColumnChartView;

/**
 * Created by Albert on 2016/2/2.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description: 柱状图点击事件处理handler
 */
public class ColumnTouchHandler {

    private ColumnDataModel mDataModel;
    private SelectedModel downSelect, upSelect;
    private ColumnChartView view;
    private RectF limitRectF;
    private RectF scopeRectF;
    private float mTran[] = new float[]{0, 0};
    private float lastTran[] = new float[]{0, 0};
    private boolean isMove=false;
    private PointF downPoint=new PointF();

    public ColumnTouchHandler(ColumnDataModel dataModel, ColumnChartView columnChartView, RectF limitRectF) {
        this.mDataModel = dataModel;
        this.view = columnChartView;
        this.limitRectF = limitRectF;
        scopeRectF = new RectF();

        if (!mDataModel.isHorizontal()) {
            for (int i = 0; i < mDataModel.getColumnModels().size(); i++) {
                for (int j = 0; j < mDataModel.getColumnModels().get(i).getSubcolumnModels().size(); j++) {
                    if (i == 0 && j == 0) {
                        scopeRectF.left = mDataModel.getColumnModels().get(i).getSubcolumnModels().get(j).getRect().left;
                    } else if (i == mDataModel.getColumnModels().size() - 1 && j == 0)
                        scopeRectF.right = mDataModel.getColumnModels().get(i).getSubcolumnModels().get(j).getRect().right;
                }
            }
        }else {
            for (int i = 0; i < mDataModel.getColumnModels().size(); i++) {
                for (int j = 0; j < mDataModel.getColumnModels().get(i).getSubcolumnModels().size(); j++) {
                    if (i == 0 && j == 0) {
                        scopeRectF.bottom = mDataModel.getColumnModels().get(i).getSubcolumnModels().get(j).getRect().bottom;
                    } else if (i == mDataModel.getColumnModels().size() - 1 && j == 0)
                        scopeRectF.top = mDataModel.getColumnModels().get(i).getSubcolumnModels().get(j).getRect().top;
                }
            }
        }
    }

    public SelectedModel getDownSelect() {
        return downSelect;
    }

    public void setDownSelect(SelectedModel downSelect) {
        this.downSelect = downSelect;
    }

    public SelectedModel getUpSelect() {
        return upSelect;
    }

    public void setUpSelect(SelectedModel upSelect) {
        this.upSelect = upSelect;
    }

    /**
     * TODO 之后还需要进行滑动的处理
     *
     * @param event
     * @return 事件是否被消费
     */
    public boolean handleEvent(MotionEvent event) {
        boolean consumed = true;
        MathHelper.getInstanse().limitDragScope(mTran, scopeRectF, limitRectF, event,mDataModel.isHorizontal());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove=false;
                SelectedModel downModel = checkEvent(event);
                setDownSelect(downModel);
                listener.valueDown(downModel != null);
                downPoint.x=event.getX();
                downPoint.y=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isMove)
                    return true;
                SelectedModel upModel = checkEvent(event);
                setUpSelect(upModel);
                handleClick();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX()-downPoint.x)>10
                        ||Math.abs(event.getY()-downPoint.y)>10)
                    isMove=true;
                if (!mDataModel.isHorizontal()) {
                    for (int i = 0; i < mDataModel.getColumnModels().size(); i++) {
                        ColumnModel columnModel = mDataModel.getColumnModels().get(i);
                        for (int j = 0; j < columnModel.getSubcolumnModels().size(); j++) {
                            SubcolumnModel model = columnModel.getSubcolumnModels().get(j);
                            RectF rect = model.getRect();
                            rect.left += mTran[0] - lastTran[0];
                            rect.right += mTran[0] - lastTran[0];
                        }
                    }
                    view.setmTranslate(mTran[0]-lastTran[0],0);
                    lastTran[0] = mTran[0];
                }else {
                    for (int i = 0; i < mDataModel.getColumnModels().size(); i++) {
                        ColumnModel columnModel = mDataModel.getColumnModels().get(i);
                        for (int j = 0; j < columnModel.getSubcolumnModels().size(); j++) {
                            SubcolumnModel model = columnModel.getSubcolumnModels().get(j);
                            RectF rect = model.getRect();
                            rect.top += mTran[1] - lastTran[1];
                            rect.bottom += mTran[1] - lastTran[1];
                        }
                    }
                    view.setmTranslate(0,mTran[1]-lastTran[1]);
                    lastTran[1] = mTran[1];
                }
                view.postInvalidate();
                break;
        }
        return consumed;
    }

    /**
     * Description:判断操作是否是“点击子柱状图”
     */
    private void handleClick() {
        if ((getDownSelect() != null) && (getUpSelect() != null) && (getDownSelect().equals(getUpSelect()))) {
            int firstIndex = getUpSelect().getFirstIndex();
            int secondIndex = getUpSelect().getSecondIndex();
            ColumnModel columnModel = mDataModel.getColumnModels().get(firstIndex);
            if ((columnModel != null) && (columnModel.getSubcolumnModels() != null)) {
                SubcolumnModel model = columnModel.getSubcolumnModels().get(secondIndex);
                if (listener != null) {
                    listener.valueSelected(model);
                }
                SubcolumnSelectEvent event = new SubcolumnSelectEvent();
                event.setShowtoast(true);
                event.setSubcolumnModel(model);
//                EventBus.getDefault().post(event);
            }

        } else {
            SubcolumnSelectEvent event = new SubcolumnSelectEvent();
            event.setShowtoast(false);
            event.setSubcolumnModel(null);
            EventBus.getDefault().post(event);
        }
    }

    /**
     * Description: 检查事件是否位于柱状图的某个子柱上
     *
     * @return
     */
    private SelectedModel checkEvent(MotionEvent event) {
        SelectedModel selected = null;
        for (ColumnModel columnModel : mDataModel.getColumnModels()) {
            for (SubcolumnModel subcolumnModel : columnModel.getSubcolumnModels()) {
                RectF rect = subcolumnModel.getRect();
                if (rect.contains((int) event.getX(), (int) event.getY())) {
                    selected = new SelectedModel(subcolumnModel.getFirstIndex(), subcolumnModel.getSecondIndex());
                    break;
                }
            }
        }
        return selected;
    }

    private ValueSelectedListener listener;

    public void setListener(ValueSelectedListener listener) {
        this.listener = listener;
    }

    public interface ValueSelectedListener {
        public void valueSelected(SubcolumnModel subcolumnModel);

        public void valueDown(boolean target);
    }
}
