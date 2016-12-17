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

    public static SwipeDragLayout getmCacheView() {
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
                if (releasedChild == contentView) {
                    if (isOpen()) {
                        if (offset != 1 && offset > (1 - needOffset)) {
                            open();
                        } else if (offset == 1) {
                            if (clickToClose) {
                                close();
                            }
                        } else {
                            close();

                        }
                    } else {
                        if (offset != 0 && offset < needOffset) {
                            close();
                        } else if (offset == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            open();
                            Log.d("Released and isOpen", "" + isOpen);
                            if (mListener != null) {
                                mListener.onOpened(SwipeDragLayout.this);
                            }
                        }
                    }
                    invalidate();
                }
            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //滑动距离
                final int leftBound = getPaddingLeft() - (ios ? menuView.getWidth() * 3 / 2 : menuView.getWidth());
                final int rightBound = getWidth() - child.getWidth();
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
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
                if (mListener!=null){
                    mListener.onUpdate(SwipeDragLayout.this,offset);
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
        mDragHelper.settleCapturedViewAt(originPos.x - menuView.getWidth(), originPos.y);
        isOpen = true;
    }

    public void smoothOpen(boolean smooth) {
        mCacheView = SwipeDragLayout.this;
        if (smooth) {
            mDragHelper.smoothSlideViewTo(contentView, originPos.x - menuView.getWidth(), originPos.y);
        } else {
            contentView.layout(originPos.x - menuView.getWidth(), originPos.y, menuView.getLeft(), menuView.getBottom());
        }
    }

    private void smoothClose(boolean smooth) {
        if (smooth) {
            mDragHelper.smoothSlideViewTo(contentView, getPaddingLeft(), getPaddingTop());
            postInvalidate();
        } else {
            contentView.layout(originPos.x, originPos.y, menuView.getRight(), menuView.getBottom());
        }
        isOpen = false;
        mCacheView = null;

    }



    public void close() {
        mDragHelper.settleCapturedViewAt(originPos.x, originPos.y);
        isOpen = false;
        mCacheView = null;
        if (mListener != null) {
            mListener.onClosed(SwipeDragLayout.this);
        }
    }

    public boolean isOpenStatus() {
        View surfaceView = contentView;
        if (surfaceView == null) {
            return false;
        }
        int surfaceLeft = surfaceView.getLeft();
        int surfaceTop = surfaceView.getTop();
        if (surfaceLeft == getPaddingLeft() && surfaceTop == getPaddingTop()) return false;
        int mDragDistance = menuView.getWidth();
        if (surfaceLeft == (getPaddingLeft() - mDragDistance) || surfaceLeft == (getPaddingLeft() + mDragDistance)
                || surfaceTop == (getPaddingTop() - mDragDistance) || surfaceTop == (getPaddingTop() + mDragDistance))
            return true;

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCacheView != null) {
                    if (mCacheView != this) {
                        mCacheView.smoothClose(true);
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
        contentView = getChildAt(1);
        menuView = getChildAt(0);
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        menuView.setLayoutParams(params);
        //重写OnClickListener会导致关闭失效
        if (contentView!=null){
            contentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickToClose&&isOpen()){
                        smoothClose(true);
                        return;
                    }
                    if (mListener!=null){
                        mListener.onClick(SwipeDragLayout.this);
                    }

                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mCacheView == this) {
            mCacheView.smoothClose(false);
            mCacheView = null;
        }
        super.onDetachedFromWindow();

    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    public void addListener(SwipeListener listener) {
        mListener = listener;
    }

    //滑动监听
    public interface SwipeListener {

        /**
         * 拖动中，可根据offset 进行其他动画
         * @param layout
         * @param offset 偏移量
         */
        void onUpdate(SwipeDragLayout layout, float offset);

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

        /**
         * 点击内容layout {@link #onFinishInflate()}
         * @param layout
         */
        void onClick(SwipeDragLayout layout);
    }

}
