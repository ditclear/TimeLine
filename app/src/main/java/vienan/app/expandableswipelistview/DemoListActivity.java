package vienan.app.expandableswipelistview;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

import java.util.ArrayList;
import java.util.List;

import vienan.app.expandableswipelistview.databinding.ListActivityBinding;

/**
 * 页面描述：DemoListActivity
 *
 * Created by ditclear on 2018/1/5.
 */

public class DemoListActivity extends AppCompatActivity {

    ListActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.list_activity);
        getSupportActionBar().setTitle("ListView");
        final List<String> mList = fakeData();
        mBinding.list.setAdapter(
                new ArrayAdapter<String>(this, R.layout.child_status_item, R.id.tv_title,
                        mList) {
                    @NonNull
                    @Override
                    public View getView(final int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
                        MyViewHolder myViewHolder;
                        if (convertView != null) {
                            myViewHolder = (MyViewHolder) convertView.getTag();
                        } else {
                            convertView = LayoutInflater.from(getContext()).inflate(
                                    R.layout.child_status_item, null);
                            myViewHolder = new MyViewHolder(convertView);
                            convertView.setTag(myViewHolder);
                        }


                        render(position, myViewHolder);
                        return convertView;
                    }

                    private void render(final int position, final MyViewHolder myViewHolder) {
                        if (position % 2 == 0) {
                            myViewHolder.layout.setSwipeDirection(SwipeDragLayout.DIRECTION_LEFT);
                            myViewHolder.mTextView.setText("swipe left ");
                            myViewHolder.menuLayout.setLayoutDirection(
                                    LinearLayout.LAYOUT_DIRECTION_LTR);
                            myViewHolder.layout.setBackgroundColor(Color.parseColor("#FF6347"));

                        } else {
                            myViewHolder.menuLayout.setLayoutDirection(
                                    LinearLayout.LAYOUT_DIRECTION_RTL);
                            myViewHolder.layout.setSwipeDirection(SwipeDragLayout.DIRECTION_RIGHT);
                            myViewHolder.layout.setBackgroundColor(Color.parseColor("#FF6347"));
                            myViewHolder.mTextView.setText("swipe right ");

                        }
                        myViewHolder.mTextView.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(v.getContext(),
                                                myViewHolder.mTextView.getText(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        myViewHolder.mImageView.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(v.getContext(), "edit",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                        myViewHolder.deleteIv.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myViewHolder.layout.close();
                                        mList.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(v.getContext(), "delete",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        myViewHolder.starIv.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(v.getContext(), "star",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                });
    }


    /**
     * @return 测试数据集
     */
    List<String> fakeData() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String lineItem = "Item  " + i;

            items.add(lineItem);
        }
        return items;
    }

    static class MyViewHolder {

        public TextView mTextView;
        public ImageView mImageView;
        public ImageView deleteIv;
        public ImageView starIv;
        public SwipeDragLayout layout;
        public LinearLayout menuLayout;

        public MyViewHolder(View itemView) {
            mTextView = itemView.findViewById(R.id.tv_title);
            mImageView = itemView.findViewById(R.id.iv_type);
            deleteIv = itemView.findViewById(R.id.trash);
            starIv = itemView.findViewById(R.id.star);
            layout = (SwipeDragLayout) itemView;
            menuLayout = (LinearLayout) itemView.findViewById(R.id.menu_layout);
        }

    }

}
