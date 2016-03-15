package demo.cn.chartdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;
import demo.cn.chartdemo.R;
import demo.cn.chartdemo.Utils.ClickFilter;
import demo.cn.chartdemo.event.IEvent;
import demo.cn.chartdemo.event.SubcolumnSelectEvent;
import demo.cn.chartdemo.model.ColumnDataModel;
import demo.cn.chartdemo.model.ColumnModel;
import demo.cn.chartdemo.model.SubcolumnModel;
import demo.cn.chartdemo.view.ColumnChartView;
import demo.cn.chartdemo.view.FormView;
import demo.cn.chartdemo.view.LineChartView;
import demo.cn.chartdemo.view.PieChartView;
import demo.cn.chartdemo.view.PopupLayout;

public class MainActivity extends AppCompatActivity {

    private static final int TYPE_LINE=0;
    private static final int TYPE_PIE=1;
    private static final int TYPE_COLUMN=2;
    private static final int TYPE_COLUMN_H=3;

    private int type=-1;
    private PieChartView pieChart;
    private PopupLayout toast;
    private ColumnChartView columnChartView,hColumnChartView;
    private FormView formView;

    private LineChartView lineChartView;
    private Button pieBtn,lineBtn,columBtn,hColumBtn,formBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        type=TYPE_LINE;
        pieBtn = (Button)findViewById(R.id.btn_pie);
        lineBtn = (Button)findViewById(R.id.btn_line);
        columBtn = (Button)findViewById(R.id.btn_column);
        hColumBtn= (Button) findViewById(R.id.btn_column_h);
        formBtn= (Button) findViewById(R.id.btn_form);

        lineChartView = (LineChartView)findViewById(R.id.chat_line);
        pieChart= (PieChartView) findViewById(R.id.piechart);
        toast= (PopupLayout) findViewById(R.id.toast);
        columnChartView = (ColumnChartView) findViewById(R.id.columnchart);
        hColumnChartView= (ColumnChartView) findViewById(R.id.columncharth);
        formView= (FormView) findViewById(R.id.form);


        pieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=TYPE_PIE;
                pieChart.setVisibility(View.VISIBLE);
                columnChartView.setVisibility(View.GONE);
                lineChartView.setVisibility(View.GONE);
                hColumnChartView.setVisibility(View.GONE);
                toast.setVisibility(View.GONE);
                formView.setVisibility(View.GONE);
                pieChart.reset();
            }
        });

        lineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=TYPE_LINE;
                pieChart.setVisibility(View.GONE);
                columnChartView.setVisibility(View.GONE);
                lineChartView.setVisibility(View.VISIBLE);
                toast.setVisibility(View.GONE);
                hColumnChartView.setVisibility(View.GONE);
                formView.setVisibility(View.GONE);
                lineChartView.reset();
            }
        });

        columBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TYPE_COLUMN;
                pieChart.setVisibility(View.GONE);
                columnChartView.setVisibility(View.VISIBLE);
                lineChartView.setVisibility(View.GONE);
                hColumnChartView.setVisibility(View.GONE);
                columnChartView.reset();
                toast.setVisibility(View.GONE);
                formView.setVisibility(View.GONE);
            }
        });

        hColumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TYPE_COLUMN_H;
                pieChart.setVisibility(View.GONE);
                columnChartView.setVisibility(View.GONE);
                lineChartView.setVisibility(View.GONE);
                hColumnChartView.setVisibility(View.VISIBLE);
                hColumnChartView.reset();
                toast.setVisibility(View.GONE);
                formView.setVisibility(View.GONE);
            }
        });

        formBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.setVisibility(View.GONE);
                columnChartView.setVisibility(View.GONE);
                lineChartView.setVisibility(View.GONE);
                hColumnChartView.setVisibility(View.GONE);
                toast.setVisibility(View.GONE);
                formView.setVisibility(View.VISIBLE);
            }
        });

        columnChartView.setListener(new ColumnChartView.OnColumnClickListener() {
            @Override
            public void onClick() {
                toast.setVisibility(View.VISIBLE);
            }
        });

        lineChartView.setListener(new LineChartView.onClickDotListener() {
            @Override
            public void onClick(int count) {
                if (count != -1) {
                    toast.setVisibility(View.VISIBLE);
                    toast.setTxt("第" + count + "个");
                } else {
                    toast.setVisibility(View.GONE);
                }
            }
        });

        pieChart.setListener(new PieChartView.OnClickPieListener() {
            @Override
            public void onClick() {
                toast.setVisibility(View.VISIBLE);
            }
        });

        hColumnChartView.setListener(new ColumnChartView.OnColumnClickListener() {
            @Override
            public void onClick() {
                toast.setVisibility(View.VISIBLE);
            }
        });

        EventBus.getDefault().register(this);

        generateDummyData();
        initColumnAction();
        toast.setVisibility(View.GONE);
    }

    private void initColumnAction(){
        toast.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickFilter.filter())
                    return;
                switch (type){
                    case TYPE_LINE:
                        lineChartView.step(-1);
                        break;
                    case TYPE_PIE:
                        pieChart.clickView(true);
                        break;
                    case TYPE_COLUMN:
                        columnChartView.step(-1);
                        break;
                    case TYPE_COLUMN_H:
                        hColumnChartView.step(-1);
                        break;
                }
            }
        });

        toast.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickFilter.filter())
                    return;
                switch (type){
                    case TYPE_LINE:
                        lineChartView.step(1);
                        break;
                    case TYPE_PIE:
                        pieChart.clickView(false);
                        break;
                    case TYPE_COLUMN:
                        columnChartView.step(1);
                        break;
                    case TYPE_COLUMN_H:
                        hColumnChartView.step(1);
                }
            }
        });
    }

    public void  onEvent(IEvent event){
        //TODO 事件处理需要再细化
        if (event instanceof SubcolumnSelectEvent){
            SubcolumnSelectEvent ev = (SubcolumnSelectEvent) event;
            if (ev.isShowtoast()){
            }else {
                columnChartView.reset();
            }
        }
    }


    private void generateDummyData(){
        int columnCount = 50;
        int subcolumnCount = 1;
        ColumnDataModel columnDataModel = new ColumnDataModel();
        List<ColumnModel> columnModels = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < columnCount; i++){
            ColumnModel columnModel = new ColumnModel();
            columnModel.setName(2000 + i + "");
            columnModel.setTextcolor(Color.RED);
            columnModel.setTextsize(20);
            List<SubcolumnModel> subcolumnModels = new ArrayList<>();
            for (int j = 0; j < subcolumnCount; j++){
                SubcolumnModel subcolumnModel = new SubcolumnModel();
                subcolumnModel.setFirstIndex(i);
                subcolumnModel.setSecondIndex(j);
                subcolumnModel.setOriginColor(Color.GREEN);
                subcolumnModel.setUnSelectedColor(Color.GRAY);
                subcolumnModel.setColor(Color.BLUE);
                //subcolumnModel.setValue(100);
                subcolumnModel.setValue(random.nextInt(20) * 20 + 30f );
                subcolumnModels.add(subcolumnModel);
            }
            columnModel.setSubcolumnModels(subcolumnModels);
            columnModels.add(columnModel);
        }
        columnDataModel.setColumnModels(columnModels);
        columnDataModel.setHorizontal(false);
        columnChartView.setmColumnData(columnDataModel);
        columnDataModel.setHorizontal(true);
        hColumnChartView.setmColumnData(columnDataModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
