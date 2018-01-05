package vienan.app.expandableswipelistview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

import java.util.ArrayList;
import java.util.List;

import vienan.app.expandableswipelistview.databinding.ActivityMainBinding;
import vienan.app.expandableswipelistview.databinding.ChildStatusItemBinding;
import vienan.app.expandableswipelistview.model.LineItem;
import vienan.app.expandableswipelistview.recyleradapter.BaseViewAdapter;
import vienan.app.expandableswipelistview.recyleradapter.BindingViewHolder;
import vienan.app.expandableswipelistview.recyleradapter.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mMainBinding;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_TITLE = 2;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        mMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        MultiTypeAdapter adapter=new MultiTypeAdapter(this);
        adapter.addViewTypeToLayoutMap(VIEW_TYPE_TITLE, R.layout.group_status_item);
        adapter.addViewTypeToLayoutMap(VIEW_TYPE_ITEM, R.layout.child_status_item);
        mMainBinding.setAdapter(adapter);

        mMainBinding.recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        adapter.addAll(fakeData(), new MultiTypeAdapter.CustomMultiViewTyper() {

            @Override
            public int getViewType(Object item, int pos) {
                if (item instanceof LineItem){
                    if (((LineItem) item).isTitle()){
                        return VIEW_TYPE_TITLE;
                    }else {
                        return VIEW_TYPE_ITEM;
                    }
                }
                throw new RuntimeException("unExcepted item type");
            }
        });

        //设置点击事件
        adapter.setPresenter(new ItemPresenter());
        //设置额外操作
        adapter.setDecorator(new ItemDecoration());
    }

    /**
     *
     * @return 测试数据集
     */
    List<LineItem > fakeData(){
        List<LineItem > items=new ArrayList<>();
        int d=1;
        for (int i = 0; i < 15; i++) {
            LineItem lineItem;

            if (i==0||i==4||i==9){

                lineItem=new LineItem("9月"+d+"日",true);
                d++;
            }else {
                lineItem=new LineItem("Item  "+i,false);
            }
            items.add(lineItem);
        }
        return items;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置菜单按钮的点击事件
     */
    public class ItemPresenter implements BaseViewAdapter.Presenter{

        /**
         * 参考 dataBinding的用法，下同
         * @param item
         */
        public void onStarClick(LineItem item){
            Toast.makeText(mContext, "star", Toast.LENGTH_SHORT).show();
        }

        public void onDeleteClick(LineItem item){
            Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 详情见 {@link vienan.app.expandableswipelistview.recyleradapter.BaseViewAdapter#onBindViewHolder(BindingViewHolder, int)}
     */
    public class ItemDecoration implements BaseViewAdapter.Decorator{

        @Override
        public void decorator(BindingViewHolder holder, final int position, int viewType) {
            if (viewType==VIEW_TYPE_ITEM){
                ChildStatusItemBinding binding= (ChildStatusItemBinding) holder.getBinding();
                binding.swipLayout.addListener(new SwipeDragLayout.SwipeListener() {
                    @Override
                    public void onUpdate(SwipeDragLayout layout, float v, float offset) {
                        Log.d("offset", "onUpdate() called with offset = [" + offset + "]");
                    }

                    @Override
                    public void onOpened(SwipeDragLayout layout) {
                    }

                    @Override
                    public void onClosed(SwipeDragLayout layout) {

                    }
                });
            }
        }
    }


}
