package com.ditclear.swipelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
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
    public final static int DIRECTION_LEFT = 1;
    public final static int DIRECTION_RIGHT = 2;
    private View contentView;
    private View menuView;
    private ViewDragHelper mDragHelper;
    private Point originPos = new Point();
    private boolean isOpen, ios, swipeEnable;
    private boolean clickToClose=true;
    private int swipeDirection = DIRECTION_LEFT;
    private float offsetRatio;
    private float needOffset = 0.2f;
    private SwipeListener mListener;
    //防止多指拖动,当第一个被拖动时，置为true,其它的则不触发拖动
    private static boolean isTouching;

    public SwipeDragLayout(Context context) {
        this(context, null);
    }

    public SwipeDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwipeDragLayout);
        //最小滑动距离
        needOffset = array.getFloat(R.styleable.SwipeDragLayout_need_offset, 0.2f);
        //是否有回弹效果
        ios = array.getBoolean(R.styleable.SwipeDragLayout_ios, false);
        //是否可滑动
        swipeEnable = array.getBoolean(R.styleable.SwipeDragLayout_swipe_enable, true);
        //滑动方向，默认左滑
        swipeDirection = array.getInt(R.styleable.SwipeDragLayout_swipe_direction, DIRECTION_LEFT);
        init();
        array.recycle();
    }

    public static SwipeDragLayout getCacheView() {
        return mCacheView;
    }

    //初始化dragHelper，对拖动的view进行操作
    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //捕获contentView
                return child == contentView;
            }
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //滑动距离,如果启动IOS效果，则可滑动4/3倍菜单宽度的距离
                if (swipeDirection == DIRECTION_LEFT) {
                    //左滑
                    final int leftBound = getPaddingLeft() - (ios ? menuView.getWidth() * 4 / 3
                            : menuView.getWidth());
                    final int rightBound = getWidth() - child.getWidth();
                    return Math.min(Math.max(left, leftBound), rightBound);
                } else {
                    final int leftBound = 0;
                    final int rightBound = (ios ? menuView.getWidth() * 4 / 3 : menuView.getWidth());
                    return Math.min(Math.max(left,leftBound),rightBound);
                }
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return contentView == child ? menuView.getWidth() : 0;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                //fix issue 6 List滚动和Item左右滑动有冲突
                // {@link https://github.com/ditclear/SwipeLayout/issues/6}
                getParent().requestDisallowInterceptTouchEvent(true);
                return super.clampViewPositionVertical(child, top, dy);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                final int childWidth = menuView.getWidth();
                //offsetRatio can callback here
                if (swipeDirection == DIRECTION_LEFT) {
                    offsetRatio = -(float) (left - getPaddingLeft()) / childWidth;
                    menuView.setTranslationX(Math.max(-menuView.getWidth(), left));
                } else {
                    offsetRatio = ((float) left) / childWidth;
                    menuView.setTranslationX(Math.min(menuView.getWidth(), left));
                }
                if (mListener != null) {
                    mListener.onUpdate(SwipeDragLayout.this, offsetRatio, left);
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                //松手时，判断打开还是关闭
                if (swipeDirection == DIRECTION_LEFT) {
                    autoLeft();
                } else {
                    autoRight();
                }
            }
        });

    }

    private void autoRight() {
        if (isOpen) {
            if (offsetRatio != 1 && offsetRatio > (1 - needOffset)) {
                open();
            } else if (offsetRatio == 1) {
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
            if (offsetRatio != 0 && offsetRatio < needOffset) {
                close();
            } else if (offsetRatio == 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                open();
                if (mListener != null) {
                    mListener.onOpened(SwipeDragLayout.this);
                }
            }
        }
    }

    public int getSwipeDirection() {
        return swipeDirection;
    }

    public void setSwipeDirection(int swipeDirection) {
        if (this.swipeDirection != swipeDirection) {
            this.swipeDirection = swipeDirection;
            requestLayout();
        }
    }

    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    public void setNeedOffset(float needOffset) {
        this.needOffset = needOffset;
    }

    private void autoLeft() {
        if (isOpen()) {
            if (offsetRatio != 1 && offsetRatio > (1 - needOffset)) {
                open();
            } else if (offsetRatio == 1) {
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
            if (offsetRatio != 0 && offsetRatio < needOffset) {
                close();
            } else if (offsetRatio == 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                open();
                if (mListener != null) {
                    mListener.onOpened(SwipeDragLayout.this);
                }
            }
        }
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        mCacheView = SwipeDragLayout.this;
        if (swipeDirection==DIRECTION_LEFT) {
            mDragHelper.smoothSlideViewTo(contentView, originPos.x - menuView.getWidth(),
                    originPos.y);
        }else {
            mDragHelper.smoothSlideViewTo(contentView, originPos.x + menuView.getWidth(),
                    originPos.y);
        }
        isOpen = true;
        invalidate();
    }

    public void close() {
        mDragHelper.smoothSlideViewTo(contentView, originPos.x, originPos.y);
        isOpen = false;
        mCacheView=null;
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //防止多指拖拽
            case MotionEvent.ACTION_DOWN:
                if (isTouching){
                    return false;
                }else {
                    isTouching=true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isTouching){
                    isTouching=false;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCacheView != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (mCacheView != this) {
                        mCacheView.close();
                        mCacheView = null;
                        return true;
                    } else if (isOpen && mDragHelper.isViewUnder(contentView, (int) ev.getX(),
                            (int) ev.getY())) {
                        //open时，不触发contentView的点击事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                }
                break;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeEnable) {
            mDragHelper.processTouchEvent(event);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        originPos.x = contentView.getLeft();
        originPos.y = contentView.getTop();

        if (DIRECTION_LEFT == swipeDirection) {
            //左滑
            menuView.layout(contentView.getWidth(), menuView.getTop(),
                    contentView.getWidth() + menuView.getWidth(), menuView.getBottom());
        } else {
            //右滑
            menuView.layout(-menuView.getWidth(), menuView.getTop(), originPos.x
                    , menuView.getBottom());
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
        if (swipeDirection == DIRECTION_LEFT) {
            FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            menuView.setLayoutParams(params);
            if (isInEditMode()) {
                menuView.layout(contentView.getWidth(), menuView.getTop(),
                        contentView.getWidth() + menuView.getWidth(), menuView.getBottom());
            }
        } else if (isInEditMode()) {
            menuView.layout(-menuView.getWidth(), menuView.getTop(), contentView.getLeft()
                    , menuView.getBottom());
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mCacheView == this) {
            mCacheView.close();
            mCacheView = null;
        }
        if (menuView!=null&&menuView.getTranslationX()!=0){
            menuView.setTranslationX(0f);
        }
        super.onDetachedFromWindow();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //recyclerView复用时有点问题，需加以下代码确保layout完全关闭
        if (menuView!=null&&menuView.getTranslationX()!=0){
            menuView.setTranslationX(0);
        }
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
