package vienan.app.expandableswipelistview.adapter

import android.content.Context
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.util.ArrayMap
import android.view.ViewGroup

/**
 * 页面描述：MultiTypeAdapter
 *
 * Created by ditclear on 2017/10/30.
 */
class MultiTypeAdapter(context: Context, list: ObservableArrayList<Any>, val multiViewTyper: MultiViewTyper) : BindingViewAdapter<Any>(context, list) {

    protected var mCollectionViewType: MutableList<Int> = mutableListOf()

    private val mItemTypeToLayoutMap = ArrayMap<Int, Int>()

    init {
        list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Any>>() {
            override fun onItemRangeMoved(sender: ObservableList<Any>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeRemoved(sender: ObservableList<Any>?, positionStart: Int, itemCount: Int) {
                for (i in positionStart + itemCount - 1 downTo positionStart) mCollectionViewType.removeAt(i)
                if (sender?.isNotEmpty() == true) {
                    notifyItemRangeRemoved(positionStart, itemCount)
                } else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeChanged(sender: ObservableList<Any>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(sender: ObservableList<Any>?, positionStart: Int, itemCount: Int) {
                sender?.run {
                    (positionStart until positionStart + itemCount).forEach {
                        mCollectionViewType.add(it, multiViewTyper.getViewType(this[it]))
                    }
                    notifyItemRangeInserted(positionStart, itemCount)
                }
            }

            override fun onChanged(sender: ObservableList<Any>?) {
                notifyDataSetChanged()
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
        return BindingViewHolder(
                DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, getLayoutRes(viewType), parent, false))
    }

    fun addViewTypeToLayoutMap(viewType: Int?, layoutRes: Int?) {
        mItemTypeToLayoutMap.put(viewType, layoutRes)
    }

    override fun getItemViewType(position: Int): Int = mCollectionViewType[position]

    interface MultiViewTyper {
        fun getViewType(item: Any): Int
    }

    @LayoutRes
    protected fun getLayoutRes(viewType: Int): Int = mItemTypeToLayoutMap[viewType]
            ?: throw Resources.NotFoundException("$viewType 对应的布局不存在")
}