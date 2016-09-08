package vienan.app.expandableswipelistview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

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
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return super.getCombinedChildId(groupId, childId);
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
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;
        ChildEntity entity = getChild(groupPosition, childPosition);
        Log.d("id", "" + groupPosition + "," + childPosition);
        if (convertView != null) {
            viewHolder = (ChildViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.child_status_item, null);
            viewHolder = new ChildViewHolder();
            viewHolder.childTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.swipeLayout = (SwipeDragLayout) convertView.findViewById(R.id.sample);
            viewHolder.iv_star = (ImageView) convertView.findViewById(R.id.star);
            viewHolder.iv_trash = (ImageView) convertView.findViewById(R.id.trash);
            convertView.setTag(viewHolder);
        }
        if (entity != null) {
            render(viewHolder, entity, groupPosition, childPosition, convertView, parent);
        }

        return convertView;
    }

    private void render(ChildViewHolder viewHolder, final ChildEntity entity, final int groupPosition,
                        final int childPosition, final View convertView, final ViewGroup parent) {
        //必须是false,否则...
        if (entity.isOpen) {
            viewHolder.swipeLayout.smoothOpen(false);
        } else {
            viewHolder.swipeLayout.smoothClose(false);
        }
        viewHolder.swipeLayout.addListener(new SwipeDragLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeDragLayout layout) {
                toast("onStartOpen");
            }

            @Override
            public void onStartClose(SwipeDragLayout layout) {
                toast("onStartClose");
            }

            @Override
            public void onUpdate(SwipeDragLayout layout, float offset) {
//                toast("onUpdate");
            }

            @Override
            public void onOpened(SwipeDragLayout layout) {
                entity.isOpen = true;
                toast("onOpened");
            }

            @Override
            public void onClosed(SwipeDragLayout layout) {
                entity.isOpen = false;
                toast("onClosed");
            }

            @Override
            public void onCancel(SwipeDragLayout layout) {
                toast("onCancel");
            }

            @Override
            public void onClick(SwipeDragLayout layout) {
//                toast(entity.getChildTitle());
                if (mOnSwipeChildClickListener!=null) {
                    mOnSwipeChildClickListener.onChildClick((ExpandableListView) parent, convertView, groupPosition,
                            childPosition, getCombinedChildId(groupPosition, childPosition));
                }
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
        return true;
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
        Log.d("msg",msg);
    }

    private OnSwipeChildClickListener mOnSwipeChildClickListener;

    public void setOnSwipeChildClickListener(OnSwipeChildClickListener onSwipeChildClickListener) {
        mOnSwipeChildClickListener = onSwipeChildClickListener;
    }

    public interface OnSwipeChildClickListener {
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id);
    }
}
