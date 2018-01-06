# TimeLine
> 关于本项目

最初的本意是做一个TimeLine时间轴，到后来逐渐成为了一个侧滑的自定义控件。也很感谢大家的支持，所以趁着年初有空闲，所以重构了当前项目。以后也会逐渐完善和维护本项目并提供maven依赖，再次感谢！

[demo.apk](apk/timeline.apk)

#### 截图

![](screenshot/feature.gif)					![](screenshot/modify.gif)

![](screenshot/recyclerview.gif)					![](screenshot/listview.gif)





### Feature

- [SwipeDragLayout](https://github.com/vienan/TimeLine/blob/master/swipelayout/src/main/java/com/ditclear/swipelayout/SwipeDragLayout.java)使用ViewDragHelper来进行滑动操作，代码少，易理解，核心代码不过150行
- 使用了保留一个静态类的方法来确保只有一个展开，并在`onDetachedFromWindow`方法中进行关闭操作
- 提供了多种自定义属性，见下表
- sample使用了DataBinding和kotlin 进行了多类型的绑定，对于了解和使用DataBinding大有益处，添加多种Type更是十分简单,再也不用extends RecyclerView.Adapter了，可以参考[BindingListAdapter](https://github.com/ditclear/BindingListAdapter)


#### 自定义属性

| attr            |  type   | default |     meaning      |
| --------------- | :-----: | ------: | :--------------: |
| need_offset     |  float  |    0.2f |    最小需要滑动的距离     |
| ios             | boolean |   false |     拖动越界回弹效果     |
| swipe_enable    | boolean |    true |       开启滑动       |
| swipe_direction |   int   |       1 | 滑动方向,默认1是左滑，2为右滑 |


#### 回调监听

```java
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
```

### License

[MIT](LICENSE.txt)

