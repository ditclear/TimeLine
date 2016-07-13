package vienan.app.expandableswipelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

import java.util.ArrayList;
import java.util.List;

import vienan.app.expandableswipelistview.model.ChildEntity;
import vienan.app.expandableswipelistview.model.GroupEntity;

/**
 * Created by vienan on 2015/9/17.
 */
public class MyAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater = null;
    private List<GroupEntity> groupList;
    private Context context;

    List<SwipeDragLayout> mLayouts = new ArrayList<>();

    /**
     * 构造方法
     *
     * @param context
     * @param group_list
     */
    public MyAdapter(Context context, List<GroupEntity> group_list) {
        this.context = context;
        this.groupList = group_list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * 返回一级Item总数
     */
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupList.size();
    }

    /**
     * 返回二级Item总数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupList.get(groupPosition).getChildEntities() == null) {
            return 0;
        } else {
            return groupList.get(groupPosition).getChildEntities().size();
        }
    }

    /**
     * 获取一级Item内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groupList.get(groupPosition);
    }

    /**
     * 获取二级Item内容
     */
    @Override
    public ChildEntity getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getChildEntities().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        GroupViewHolder holder = new GroupViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_status_item, null);
        }
        holder.groupName = (TextView) convertView
                .findViewById(R.id.one_status_name);

        holder.groupName.setText(groupList.get(groupPosition).getGroupName());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;
        ChildEntity entity = getChild(groupPosition,
                childPosition);
        if (convertView != null) {
            viewHolder = (ChildViewHolder) convertView.getTag();
        } else {
            viewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.child_status_item, null);
            viewHolder.childTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.swipeLayout = (SwipeDragLayout) convertView.findViewById(R.id.sample);
            viewHolder.iv_star = (ImageView) convertView.findViewById(R.id.star);
            viewHolder.iv_trash = (ImageView) convertView.findViewById(R.id.trash);
            convertView.setTag(viewHolder);
        }
        if (entity != null) {
            render(viewHolder, entity);
        }

        return convertView;
    }

    private void render(final ChildViewHolder viewHolder, final ChildEntity entity) {
        viewHolder.swipeLayout.addListener(new SwipeDragLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeDragLayout layout) {

            }

            @Override
            public void onStartClose(SwipeDragLayout layout) {
            }

            @Override
            public void onUpdate(SwipeDragLayout layout, float offset) {

            }

            @Override
            public void onOpened(SwipeDragLayout layout) {
                toast("onOpened");
            }

            @Override
            public void onClosed(SwipeDragLayout layout) {
                toast("onClosed");
            }
        });

        viewHolder.childTitle.setText(entity.getChildTitle());

        viewHolder.iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("click star");
                //do something
            }
        });
        viewHolder.iv_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    toast("click trash");
                //do something
            }
        });
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return false;
    }

    static class GroupViewHolder {
        TextView groupName;
    }

    static class ChildViewHolder {
        public SwipeDragLayout swipeLayout;
        public ImageView iv_star, iv_trash;
        public TextView childTitle;

    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }
}
