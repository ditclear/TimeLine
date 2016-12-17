# TimeLine
android时光轴(RecycleView 多种type实现)，包含child的滑动效果`SwipeDragLayout`,2.0版本sample项目使用了Databinding 来操作数据

### Feature

- SwipeDragLayout使用ViewDragHelper来进行滑动操作，代码少，易理解，核心代码不过150行
- 使用了保留一个静态类的方法解决复用问题，并在`onDetachedFromWindow`方法中进行关闭操作
- 轻松实现IOS QQ消息列表的回弹效果(有多轻松:见SwipeDragLayout 第100行)
- sample使用了DataBinding和RecyclerView 进行了多类型的绑定，对于了解和使用DataBinding大有益处，添加多种Type更是十分简单,再也不用extends RecyclerView.Adapter了



[demo](https://github.com/vienan/TimeLine/tree/master/apk)



![](https://github.com/vienan/TimeLine/blob/master/screenshot/screenshot.gif)

###自定义属性

| attr           |  type   | default |  meaning  |
| -------------- | :-----: | ------: | :-------: |
| need_offset    |  float  |    0.2f | 最小需要滑动的距离 |
| ios            | boolean |   false | 拖动越界回弹效果  |
| click_to_close | boolean |   false |   点击关闭    |

### 回调监听

```java
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
```

### Thanks To

[DataBindingAdapter](https://github.com/markzhai/DataBindingAdapter)



> Ps:
>
> 
>
> 效果可以下载app看一下,有用的话不妨`star`一下

