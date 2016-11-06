package vienan.app.expandableswipelistview;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vienan.app.expandableswipelistview.model.ChildEntity;
import vienan.app.expandableswipelistview.model.GroupEntity;

public class RecyclerActivity extends Activity {
    private ExpandableListView expandableListView;
    private List<GroupEntity> lists;
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        lists = initList();
        adapter = new ListAdapter(this, lists);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return adapter.menuIsOpen();
            }
        });
        expandableListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState!=SCROLL_STATE_IDLE){
                    if (adapter.menuIsOpen()){
                        adapter.closeMenu();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null); // 去掉默认带的箭头
        expandableListView.setSelection(0);// 设置默认选中项
        // 遍历所有group,将所有项设置成默认展开
        int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        adapter.setOnSwipeChildClickListener(new ListAdapter.OnSwipeChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                toast(childTimeArray[groupPosition][childPosition]);

                return true;
            }
        });
    }
    //测试数据 8.23 one year
    String[] groupArray = new String[]{"9月1日", "9月2日", "9月3日"};
    String[][] childTimeArray = new String[][]{
            {"测试数据1", "测试数据2", "测试数据3"},
            {"测试数据4"}, {"测试数据5", "测试数据6","测试数据7", "测试数据8","测试数据9",
            "测试数据10","测试数据11", "测试数据12"}};

    private List<GroupEntity> initList() {

        List<GroupEntity> groupList;

        groupList = new ArrayList<GroupEntity>();
        for (int i = 0; i < groupArray.length; i++) {
            GroupEntity groupEntity = new GroupEntity(groupArray[i]);
            List<ChildEntity> childList = new ArrayList<ChildEntity>();
            for (int j = 0; j < childTimeArray[i].length; j++) {
                ChildEntity childStatusEntity = new ChildEntity(childTimeArray[i][j]);
                childList.add(childStatusEntity);
            }
            groupEntity.setChildEntities(childList);
            groupList.add(groupEntity);
        }
        return groupList;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
