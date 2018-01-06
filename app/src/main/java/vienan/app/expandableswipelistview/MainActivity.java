package vienan.app.expandableswipelistview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.storage.LocalStorage;
import com.google.android.libraries.remixer.ui.RemixerInitialization;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

import vienan.app.expandableswipelistview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mMainBinding.setPresenter(this);
        RemixerFragment.newInstance().attachToFab(this,mMainBinding.fabBtn);

        //debug ui
        RemixerInitialization.initRemixer(getApplication());
        Remixer.getInstance().setSynchronizationMechanism(new LocalStorage(getApplicationContext()));
        RemixerBinder.bind(this);


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

    @RangeVariableMethod(minValue = 1F, maxValue = 9F, initialValue = 2F)
    public void 设置最小距离(Float offsetRatio) {
        mMainBinding.swipLayout.setNeedOffset(offsetRatio/10);
    }

    @BooleanVariableMethod(initialValue = true)
    public void 开启IOS效果(Boolean ios) {
        mMainBinding.swipLayout.setIos(ios);
    }

    @BooleanVariableMethod(initialValue = true)
    public void 切换滑动方向(Boolean left) {
        mMainBinding.swipLayout.setSwipeDirection(left?SwipeDragLayout.DIRECTION_LEFT:SwipeDragLayout.DIRECTION_RIGHT);
        mMainBinding.menuLayout.setLayoutDirection(left?LinearLayout.LAYOUT_DIRECTION_LTR:LinearLayout.LAYOUT_DIRECTION_RTL);
    }

    @BooleanVariableMethod(initialValue = true)
    public void 是否可滑动(Boolean enable) {
        mMainBinding.swipLayout.setSwipeEnable(enable);
    }


}
