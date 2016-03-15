package demo.cn.chartdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import demo.cn.chartdemo.R;

/**
 * Description 双向滑动表格
 */
public class FormView extends LinearLayout {

    private Context context;
    private ListView mListView;
    //方便测试，直接写的public
    public HorizontalScrollView mTouchView;
    //装入所有的HScrollView
    protected List<CHScrollView> mHScrollViews =new ArrayList<CHScrollView>();
    private String[] cols = new String[] { "标题", "数据1", "数据2", "数据3", "数据4", "数据5",
            "数据6","数据7","数据8", "数据9"};

    private ScrollAdapter mAdapter;
    public FormView(Context context) {
        super(context);
        initViews(context);
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    public void initViews(Context context){
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.layout_hlistview,this);
        List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
        Map<String, String> data = null;
        CHScrollView headerScroll = (CHScrollView) findViewById(R.id.item_scroll_title);
        //添加头滑动事件
        mHScrollViews.add(headerScroll);
        mListView = (ListView) findViewById(R.id.hlistview_scroll_list);
        for(int i = 0; i < 20; i++) {
            data = new HashMap<String, String>();
            data.put("标题", "行" + i);
            for (int j = 1; j <= cols.length; j++) {
                data.put("数据" + j, "数据" + j + "_" + i );
            }

            datas.add(data);
        }
        mAdapter = new ScrollAdapter(context, datas, R.layout.common_item_hlistview//R.layout.item
                , cols
                , new int[] { R.id.item_titlev
                , R.id.item_datav1
                , R.id.item_datav2
                , R.id.item_datav3
                , R.id.item_datav4
                , R.id.item_datav5
                , R.id.item_datav6
                , R.id.item_datav7
                , R.id.item_datav8
                , R.id.item_datav9});
        mListView.setAdapter(mAdapter);
    }

    public void setDate(){
        TextView textView=new TextView(context);
    }

    public void addHViews(final CHScrollView hScrollView) {
        if(!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CHScrollView scrollView = mHScrollViews.get(size - 1);
//            final int scrollX = scrollView.getScrollX();
//            //第一次满屏后，向下滑动，有一条数据在开始时未加入
//            if(scrollX != 0) {
//                mListView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //当listView刷新完成之后，把该条移动到最终位置
//                        hScrollView.scrollTo(scrollX, 0);
//                    }
//                });
//            }
            scrollView.AddOnScrollChangedListener(new CHScrollView.OnScrollChangedListener() {
                @Override
                public void onScrollChanged(int l, int t, int oldl, int oldt) {
                    FormView.this.onScrollChanged(l, t, oldl, oldt);
                }
            });
        }
        mHScrollViews.add(hScrollView);
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt){
        for(CHScrollView scrollView : mHScrollViews) {
            //防止重复滑动
            if(mTouchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }

    class MyScrollAdapter extends BaseAdapter{

        private List<String> list;
        private Context context;
        public MyScrollAdapter(Context context,List<String> list){
            this.list=list;
            this.context=context;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHodler viewHodler;
            if (convertView==null){
                ArrayList<TextView> textlist=new ArrayList<>();
                convertView=LayoutInflater.from(context).inflate(R.layout.common_item_hlistview,null);
                addHViews((CHScrollView) convertView.findViewById(R.id.item_chscroll_scroll));
                viewHodler=new ViewHodler();
                for (int i = 0; i < list.size(); i++) {
                    TextView text=new TextView(context);
                    text.setGravity(Gravity.CENTER);
                    textlist.add(text);
                }
                viewHodler.setList(textlist);

                convertView.setTag(viewHodler);
            }else {
                viewHodler= (ViewHodler) convertView.getTag();
            }
            for (int i = 0; i < viewHodler.list.size(); i++) {
                viewHodler.list.get(i).setText(list.get(i));
            }
            return convertView;
        }

        class ViewHodler{
            public List<TextView> list;

            public ViewHodler(){
                list=new ArrayList<>();
            }

            public void setList(List<TextView> list) {
                list.clear();
                this.list.addAll(list);
            }
        }
    }

    class ScrollAdapter extends SimpleAdapter {

        private List<? extends Map<String, ?>> datas;
        private int res;
        private String[] from;
        private int[] to;
        private Context context;
        public ScrollAdapter(Context context,
                             List<? extends Map<String, ?>> data, int resource,
                             String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.datas = data;
            this.res = resource;
            this.from = from;
            this.to = to;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                v = LayoutInflater.from(context).inflate(res, null);
                //第一次初始化的时候装进来
                addHViews((CHScrollView) v.findViewById(R.id.item_chscroll_scroll));
                View[] views = new View[to.length];
                //单元格点击事件
                for(int i = 0; i < to.length; i++) {
                    View tv = v.findViewById(to[i]);
                    tv.setOnClickListener(clickListener);
                    views[i] = tv;
                }
                //每行点击事件
				/*for(int i = 0 ; i < from.length; i++) {
					View tv = v.findViewById(row_hlistview[i]);
				}*/
                //
                v.setTag(views);
            }
            View[] holders = (View[]) v.getTag();
            int len = holders.length;
            for(int i = 0 ; i < len; i++) {
                ((TextView)holders[i]).setText(this.datas.get(position).get(from[i]).toString());
            }
            return v;
        }
    }

    //测试点击的事件
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
            Toast.makeText(context, "点击了:" + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
        }
    };
}
