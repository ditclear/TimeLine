package vienan.app.expandableswipelistview;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

import java.util.ArrayList;
import java.util.List;

import vienan.app.expandableswipelistview.adapter.BindingViewAdapter;
import vienan.app.expandableswipelistview.adapter.BindingViewHolder;
import vienan.app.expandableswipelistview.adapter.SingleTypeAdapter;
import vienan.app.expandableswipelistview.databinding.ChildStatusItemBinding;
import vienan.app.expandableswipelistview.databinding.RecyclerActivityBinding;
import vienan.app.expandableswipelistview.model.LineItem;

/**
 * 页面描述：RecyclerActivity
 *
 * Created by ditclear on 2018/1/5.
 */

public class RecyclerActivity extends AppCompatActivity
        implements BindingViewAdapter.ItemDecorator,
        BindingViewAdapter.ItemClickPresenter<LineItem>, SwipeRefreshLayout.OnRefreshListener {


    RecyclerActivityBinding mBinding;
    private SingleTypeAdapter<LineItem> adapter;
    ObservableArrayList<LineItem> mDatas=new ObservableArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.recycler_activity);
        getSupportActionBar().setTitle("RecyclerView");
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new SingleTypeAdapter<LineItem>(this,
                R.layout.child_status_item,mDatas);

        adapter.setItemDecorator(this);
        adapter.setItemPresenter(this);
        mBinding.recyclerView.setAdapter(adapter);
        mDatas.addAll(fakeData());

        mBinding.refreshLayout.setOnRefreshListener(this);
    }

    /**
     * @return 测试数据集
     */
    List<LineItem> fakeData() {
        List<LineItem> items = new ArrayList<>();
        int d = 1;
        for (int i = 0; i <= 30; i++) {
            LineItem lineItem;
            if (i % 2 == 0) {
                lineItem = new LineItem("item"+i, true);
            } else {
                lineItem = new LineItem("item"+i, false);
            }
            items.add(lineItem);
        }
        return items;
    }

    @Override
    public void decorator(BindingViewHolder holder, final int position, int viewType) {
        final ChildStatusItemBinding binding = (ChildStatusItemBinding) holder.getBinding();
        final LineItem lineItem=mDatas.get(position);
        if (mDatas.get(position).isLeft()) {
            binding.swipLayout.setSwipeDirection(SwipeDragLayout.DIRECTION_LEFT);
            binding.menuLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
        } else {
            binding.swipLayout.setSwipeDirection(SwipeDragLayout.DIRECTION_RIGHT);
            binding.menuLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);
        }

        binding.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("delete   "+lineItem.getContent());
                mDatas.remove(lineItem);
            }
        });
        binding.swipLayout.setBackgroundColor(Color.parseColor("#FF6347"));
        binding.swipLayout.addListener(new SwipeDragLayout.SwipeListener() {
            @Override
            public void onUpdate(SwipeDragLayout layout, float offsetRatio, float offset) {
                //do sth.
            }

            @Override
            public void onOpened(SwipeDragLayout layout) {
                //do sth..
            }

            @Override
            public void onClosed(SwipeDragLayout layout) {
                //do sth..
            }
        });
    }

    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View v, LineItem item) {
        switch (v.getId()){
            case R.id.iv_type:
                toast("edit");
                break;
            case R.id.tv_title:
                toast(item.getContent());
                break;
            case R.id.star:
                toast("star");
                break;

        }
    }

    @Override
    public void onRefresh() {
        mBinding.refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.refreshLayout.setRefreshing(false);
            }
        },2000);
    }
}
