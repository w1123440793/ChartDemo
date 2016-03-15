package demo.cn.chartdemo.model;

/**
 * Created by Albert on 2016/2/2.
 * Mail : lbh@jusfoun.com
 * TODO :
 * Description:记录选中模块
 */
public class SelectedModel {
    private int firstIndex,secondIndex;

    public SelectedModel(int firstIndex, int secondIndex) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(int secondIndex) {
        this.secondIndex = secondIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SelectedModel){
            SelectedModel model = (SelectedModel) o;
            return ((this.getFirstIndex() == model.getFirstIndex()) && (this.getSecondIndex() == model.getSecondIndex()));
        }
        return super.equals(o);
    }
}
