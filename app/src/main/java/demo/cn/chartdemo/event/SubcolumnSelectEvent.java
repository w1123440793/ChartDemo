package demo.cn.chartdemo.event;

import demo.cn.chartdemo.model.SubcolumnModel;

/**
 * Description:子柱状图选中、取消选中发送此event
 */
public class SubcolumnSelectEvent implements IEvent{
    //TODO 之后根据具体需求发送对应的事件类型，暂时使用布尔类型实现toast的显示隐藏
    public static final int Show = 1;
    public static final int Hide = 2;
    public static final int Ignore = 3;
    private SubcolumnModel subcolumnModel;
    private boolean showtoast;

    public SubcolumnModel getSubcolumnModel() {
        return subcolumnModel;
    }

    public void setSubcolumnModel(SubcolumnModel subcolumnModel) {
        this.subcolumnModel = subcolumnModel;
    }

    public boolean isShowtoast() {
        return showtoast;
    }

    public void setShowtoast(boolean showtoast) {
        this.showtoast = showtoast;
    }
}
