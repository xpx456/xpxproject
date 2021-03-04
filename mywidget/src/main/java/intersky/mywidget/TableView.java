package intersky.mywidget;

import android.content.Context;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by xpx on 2017/2/14.
 */
//SwipeRefreshLayout.OnRefreshListener,MySwipeRefreshLayout.OnLoadMoreListener
public class TableView extends RelativeLayout implements MySwipeRefreshLayout.OnRefreshListener, MySwipeRefreshLayout.OnLoadMoreListener {

    private View mTable;
    private RelativeLayout tableBase;
    private MyScrollView tableGridev;
    private MyHScrollView tableGrideh;
    private MyScrollView tableList;
    private MyHScrollView tableHorizontalList;
    private LinearLayout head;
    private LinearLayout left;
    private LinearLayout content;
    private GrideData mGrideData;
    private TableCountPager tableCountPager;
    private Context mContext;
    private DoClickListener mDoClickListener;
    private PullUpDownListner mPullUpDownListener;
    public MySwipeRefreshLayout swipeRefreshLayout;
    public DisplayMetrics metric;

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        this.mContext = context;
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTable = mInflater.inflate(R.layout.table, null);
        tableBase = mTable.findViewById(R.id.table_base);
        tableGrideh = mTable.findViewById(R.id.contenth);
        tableGridev = mTable.findViewById(R.id.contentv);
        tableList = mTable.findViewById(R.id.gride_left);
        tableHorizontalList = mTable.findViewById(R.id.table_head);
        tableGrideh.setScrollViewListener(gridHScollLitener);
        tableGridev.setScrollViewListener(gridvScollLitener);
        tableList.setScrollViewListener(leftScollLitener);
        tableHorizontalList.setScrollViewListener(headHScollLitener);
        left = mTable.findViewById(R.id.left);
        head = mTable.findViewById(R.id.head);
        content = mTable.findViewById(R.id.content);
        swipeRefreshLayout = mTable.findViewById(R.id.gank_swipe_refresh_layout);
        swipeRefreshLayout.setLineView(tableGridev,content);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        tableHorizontalList.setHorizontalScrollBarEnabled(true);
        this.addView(mTable);
    }

    public void init(GrideData mGrideData) {
        this.mGrideData = mGrideData;
        this.tableCountPager = new TableCountPager(mGrideData.dataKeys.size());
        left.removeAllViews();
        content.removeAllViews();
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View textitem = mInflater.inflate(R.layout.grid_text_item_head, null);
        TextView tv = (TextView) textitem.findViewById(R.id.gride_text);
        tv.setWidth(mGrideData.tabkeBase.mWidth);
        textitem.setBackgroundResource(R.drawable.grid_cell_shape);
        tv.setText(mGrideData.tabkeBase.mTitle);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        TextPaint p = tv.getPaint();
        p.setFakeBoldText(true);
        tv.setMaxWidth(mGrideData.tabkeBase.mWidth);
        tableBase.addView(textitem);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                (int) (mGrideData.tabkeBase.mWidth+10*metric.density), LayoutParams.MATCH_PARENT);
        params1.addRule(RelativeLayout.BELOW,R.id.table_base);
        tableList.setLayoutParams(params1);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,(int) (50*metric.density));
        params2.addRule(RelativeLayout.RIGHT_OF,R.id.table_base);
        tableHorizontalList.setLayoutParams(params2);
        addHead();
        initView();
    }

    public ScrollViewListener leftScollLitener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
            tableGridev.scrollTo(tableGridev.getScrollX(),y);
        }
    };

    public ScrollViewListener gridvScollLitener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
            tableList.scrollTo(tableList.getScrollX(),y);
        }
    };

    public ScrollViewHListener gridHScollLitener = new ScrollViewHListener() {

        @Override
        public void onScrollChanged(HorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
            tableHorizontalList.scrollTo(x,tableHorizontalList.getScrollY());
        }
    };

    public ScrollViewHListener headHScollLitener = new ScrollViewHListener() {

        @Override
        public void onScrollChanged(HorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
            tableGrideh.scrollTo(x,tableGrideh.getScrollY());
        }
    };


    public TableAdapter.OnItemClickListener gridClickListener = new TableAdapter.OnItemClickListener()
    {

        @Override
        public void onClick(int position,TextView view) {
            int cloum = position%mGrideData.tableHead.size()+1;
            int line = position/mGrideData.tableHead.size();
            if(mDoClickListener != null)
            mDoClickListener.doClickListener(mGrideData.tableCloums.get(cloum),mGrideData.tableGrid.get(position),line,view);
        }

        @Override
        public void onLongClick(int position,TextView view) {

        }
    };

    public TableAdapter.OnItemClickListener leftClickListener = new TableAdapter.OnItemClickListener()
    {

        @Override
        public void onClick(int position,TextView view) {
            int line = position;
            if(mDoClickListener != null)
            mDoClickListener.doClickListener(mGrideData.tableCloums.get(0),mGrideData.tableLeft.get(position),line,view);
        }

        @Override
        public void onLongClick(int position,TextView view) {

        }
    };

    @Override
    public void onRefresh() {
        if(mPullUpDownListener != null)
        {
            mPullUpDownListener.doPullDown();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        if (tableCountPager.checkShowmore()) {
            for (int i = tableCountPager.showindex; i < tableCountPager.showcount; i++) {
                addleftView(this.mGrideData.tableLeft.get(i),i);
                addContextView(mGrideData,i);
                tableCountPager.showindex = tableCountPager.showcount;
            }
            swipeRefreshLayout.setLoading(false);
        }
        else
        {
            if(mPullUpDownListener != null)
            {
                mPullUpDownListener.doPullUp();
            }
        }

    }


    public interface DoClickListener
    {
        void doClickListener(TableCloumArts mTableCloumArts, TableItem mTableItem, int line, TextView view);

    }

    public interface PullUpDownListner
    {
        void doPullUp();
        void doPullDown();
    }

    public void setDoClickListener(DoClickListener mDoClickListener)
    {
        this.mDoClickListener = mDoClickListener;
    }

    public void setPullUpDownListner(PullUpDownListner mPullUpDownListener)
    {
        this.mPullUpDownListener = mPullUpDownListener;
    }

    public void initView() {

        for (int i = this.tableCountPager.showindex; i < this.tableCountPager.showcount; i++) {
            addleftView(this.mGrideData.tableLeft.get(i),i);
            addContextView(mGrideData,i);
            tableCountPager.showindex++;
        }
    }

    public void addHead() {
        for(int i = 0 ; i < mGrideData.tableHead.size() ; i++)
        {
            addItemView(head,1,mGrideData.tableHead.get(i),i+1,0,mGrideData.tableCloumArtsHashMap.get(mGrideData.tableHead.get(i).filde));
        }
    }

    public void addleftView(TableItem tableItem,int position)
    {

        addItemView(left,position%2,tableItem,0,position+1,mGrideData.tableCloumArtsHashMap.get(tableItem.filde));
    }

    public void addContextView(GrideData grideData,int position)
    {
        LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(R.layout.grid_content_item, null);
        LinearLayout layout = v.findViewById(R.id.gride_line);
        for(int i = position*grideData.tableHead.size(); i <  (position+1)*grideData.tableHead.size() ; i++)
        {
            addItemView(layout, position%2, grideData.tableGrid.get(i),i%grideData.tableHead.size()+1,position+1,mGrideData.tableCloumArtsHashMap.get(grideData.tableGrid.get(i).filde));
        }
        content.addView(v);
    }

    public void addItemView(LinearLayout linearLayout,int bg,TableItem data,int x,int y,TableCloumArts tableCloumArts)
    {
        if(tableCloumArts.isGrideVisiable)
        {
            LayoutInflater mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = mInflater.inflate(R.layout.grid_text_item, null);
            TextView mTextView;
            LinearLayout content;
            mTextView = v.findViewById(R.id.gride_text);
            mTextView.setWidth(data.mWidth);
            content = v.findViewById(R.id.content);
            if (bg == 0) {
                content.setBackgroundResource(R.drawable.grid_cell_shape2);
            } else {
                content.setBackgroundResource(R.drawable.grid_cell_shape);
            }
            mTextView.setText(data.mTitle);
            mTextView.setTag(new Pos(x,y));
            mTextView.setOnClickListener(onClickListener);
            linearLayout.addView(v);

        }

    }

    public class Pos{

        public int x;
        public int y;

        public Pos(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Pos pos = (Pos) v.getTag();
            TableCloumArts tableCloumArts = mGrideData.tableCloums.get(pos.x);
            TableItem tableItem = null;
            if(pos.x == 0 && pos.y == 0)
            {
                tableItem = mGrideData.tabkeBase;
            }
            else if(pos.x == 0 && pos.y != 0)
            {
                tableItem = mGrideData.tableLeft.get(pos.y-1);
            }
            else if(pos.x != 0 && pos.y == 0)
            {
                tableItem = mGrideData.tableHead.get(pos.x-1);
            }
            else
            {
                tableItem = mGrideData.tableGrid.get(mGrideData.tableHead.size()*(pos.y-1)+pos.x-1);
            }

            if(mDoClickListener != null)
            {
                mDoClickListener.doClickListener(tableCloumArts,tableItem,pos.y, (TextView) v);
            }
        }
    };


    public void updata() {
        tableCountPager.updataTotal(mGrideData.tableLeft.size());
    }
}




