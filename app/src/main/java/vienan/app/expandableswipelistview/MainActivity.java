package vienan.app.expandableswipelistview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

import vienan.app.expandableswipelistview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mMainBinding.setPresenter(this);
        mMainBinding.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mMainBinding.swipLayout.getSwipeDirection()== SwipeDragLayout.DIRECTION_LEFT){
                    mMainBinding.swipLayout.setSwipeDirection(SwipeDragLayout.DIRECTION_RIGHT);
                    mMainBinding.tvTitle.setText("右滑");
                    mMainBinding.menuLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);
                }else {
                    mMainBinding.swipLayout.setBackgroundColor(Color.parseColor("#FF6347"));
                    mMainBinding.swipLayout.setSwipeDirection(SwipeDragLayout.DIRECTION_LEFT);
                    mMainBinding.menuLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
                    mMainBinding.tvTitle.setText("左滑");
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_btn: {
                navigateTo(DemoListActivity.class);
                break;
            }
            case R.id.recycler_btn: {
                navigateTo(RecyclerActivity.class);
                break;
            }
            case R.id.iv_type: {
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.trash: {
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.star: {
                Toast.makeText(this, "star", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    void navigateTo(Class c) {
        startActivity(new Intent(this, c));
    }
}
