# TimeLine
android时光轴(ExpandableListView)，包含child的滑动效果

----------

[App Demo ](http://www.wandoujia.com/apps/vienan.app.cardgallery/binding "app")

[相应的blog](http://vienan.github.io/blog/expandable_swipe_listview/ "blog")

>效果图

###普通时间轴

![](http://i.imgur.com/Bl8scDR.jpg)



###可滑动删除的时间轴
![](http://i.imgur.com/ip0ZZy6.jpg)


> 因为很少有`ExpandableListview`的`child`滑动的例子加上做app时的需要，也就有了这个库，简单来说就是将`child`的布局换为`swipelayout`，然后在`adpter`中设置`click`事件。主要的代码在上边。
> 
> 效果可以下载app看一下,有用的话不妨`star`一下

