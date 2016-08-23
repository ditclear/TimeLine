# TimeLine
android时光轴(ExpandableListView)，包含child的滑动效果

同时，在这里也推荐我的另一个库[MVPTimeLine](https://github.com/vienan/MVPTimeLine)

>使用listView做的android 时间轴，项目使用了MVP架构，想要了解MVP的朋友也可以star和fork下,代码少很容易学习,包含了下拉刷新操作

![](https://github.com/vienan/TimeLine/blob/master/screenshot/screenshot.gif)

###自定义属性

| attr        | type          | default | meaning |
| ------------- |:-------------:| -----:|:-------------:|
| need_offset    | float | 0.2f |  最小需要滑动的距离|
| click_to_open     | boolean      |   false | 点击展开|
| click_to_close| boolean      |    false |点击关闭|

> 因为很少有`ExpandableListview`的`child`滑动的例子加上做app时的需要，也就有了这个库，简单来说就是将`child`的布局换为[SwipeDragLayout](https://github.com/vienan/TimeLine/blob/master/swipelayout/src/main/java/com/ditclear/swipelayout/SwipeDragLayout.java)，然后在`adpter`中设置`click`事件。主要的代码在上边。
> 
> 效果可以下载app看一下,有用的话不妨`star`一下

