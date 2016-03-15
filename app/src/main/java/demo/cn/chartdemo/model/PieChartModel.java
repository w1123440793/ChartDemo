package demo.cn.chartdemo.model;

/**
 * Description 饼图model
 */
public class PieChartModel extends BaseModel {

    private float startAngel;
    private float sectorAngel;
    private String percent;

    public float getStartAngel() {
        return startAngel;
    }

    public void setStartAngel(float startAngel) {
        this.startAngel = startAngel;
    }

    public float getSectorAngel() {
        return sectorAngel;
    }

    public void setSectorAngel(float sectorAngel) {
        this.sectorAngel = sectorAngel;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
