package com.ditclear.swipelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ditclear on 16/7/12. 可滑动的layout extends FrameLayout
 */
public class SwipeDragLayout extends FrameLayout {

    private final String TAG=getClass().getSimpleName();
    private static SwipeDragLayout mCacheView;
    private View contentView;
    private View menuView;
    private ViewDragHelper mDragHelper;
    private Point originPos = new Point();
    private boolean isOpen, ios, clickToClose;
    private float offset;
    private float needOffset = 0.2f;
    private SwipeListener mListener;

    public SwipeDragLayout(Context context) {
        this(context, null);
    }

    public SwipeDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwipeDragLayout);
        needOffset = array.getFloat(R.styleable.SwipeDragLayout_need_offset, 0.2f);
        //是否有回弹效果
        ios = array.getBoolean(R.styleable.SwipeDragLayout_ios, false);
        clickToClose = array.getBoolean(R.styleable.SwipeDragLayout_click_to_close, false);
        init();
        array.recycle();
    }

    public static SwipeDragLayout getCacheView() {
        return mCacheView;
    }

    //初始化dragHelper，对拖动的view进行操作
    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == contentView;
            }
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (isOpen()) {
                        if (offset != 1 && offset > (1 - needOffset)) {
                            open();
                        } else if (offset == 1) {
                            if (clickToClose) {
                                close();
                                if (mListener != null) {
                                    mListener.onClosed(SwipeDragLayout.this);
                                }
                            }
                        } else {
                            close();
                            if (mListener != null) {
                                mListener.onClosed(SwipeDragLayout.this);
                            }
                        }
                } else {
                        if (offset != 0 && offset < needOffset) {
                            close();
                        } else if (offset == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            open();
                            if (mListener != null) {
                                mListener.onOpened(SwipeDragLayout.this);
                            }
                        }
                    }

            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //滑动距离,如果启动效果，则可滑动3/2倍菜单宽度的距离
                final int leftBound = getPaddingLeft() - (ios ? menuView.getWidth() * 4 / 3 : menuView.getWidth());
                final int rightBound = getWidth() - child.getWidth();
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return contentView == child ? menuView.getWidth() : 0;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                final int childWidth = menuView.getWidth();
                offset = -(float) (left - getPaddingLeft()) / childWidth;
                //offset can callback here
                Log.d("left", "onViewPositionChanged() called with: left = [" + left + "]");
                menuView.setTranslationX(Math.max(-menuView.getWidth(),left));
                Log.d(TAG, "onViewPositionChanged: "+mDragHelper.getMinVelocity());
                if (mListener!=null){
                    mListener.onUpdate(SwipeDragLayout.this,offset,left);
                }
            }


        });

    }

    public void setClickToClose(boolean clickToClose) {
        this.clickToClose = clickToClose;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        mCacheView = SwipeDragLayout.this;
        mDragHelper.smoothSlideViewTo(contentView, originPos.x - menuView.getWidth(), originPos.y);
        isOpen = true;
        invalidate();
    }

    private void close() {
        mDragHelper.smoothSlideViewTo(contentView, originPos.x, originPos.y);
        isOpen = false;
        mCacheView = null;
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCacheView != null) {
                    if (mCacheView != this) {
                        mCacheView.close();
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

        }
        return mDragHelper.shouldInterceptTouchEvent(ev);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mDragHelper.processTouchEvent(event);

        return true;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        originPos.x = contentView.getLeft();
        originPos.y = contentView.getTop();

        menuView.layout(contentView.getWidth(), menuView.getTop(),
                contentView.getWidth() + menuView.getWidth(), menuView.getBottom());

    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()!=2){
            throw new UnsupportedOperationException("子View暂只支持两个");
        }
        contentView = getChildAt(0);
        menuView = getChildAt(1);
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        menuView.setLayoutParams(params);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mCacheView == this) {
            mCacheView = null;
        }
        super.onDetachedFromWindow();

    }



    public void addListener(SwipeListener listener) {
        mListener = listener;
    }

    //滑动监听
    public interface SwipeListener {

        /**
         * 拖动中，可根据offset 进行其他动画
         * @param layout
         * @param offsetRatio  偏移相对于menu宽度的比例
         * @param offset 偏移量px
         */
        void onUpdate(SwipeDragLayout layout, float offsetRatio, float offset);

        /**
         * 展开完成
         * @param layout
         */
        void onOpened(SwipeDragLayout layout);

        /**
         * 关闭完成
         * @param layout
         */
        void onClosed(SwipeDragLayout layout);
    }

}
