package vienan.app.expandableswipelistview.recyleradapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Super simple single-type adapter using data-binding.
 */
public class SingleTypeAdapter<T> extends BaseViewAdapter<T> {

    protected int mLayoutRes;

    public SingleTypeAdapter(Context context) {
        this(context, 0);
    }

    public SingleTypeAdapter(Context context, int layoutRes) {
        super(context);
        mCollection = new ArrayList<>();
        mLayoutRes = layoutRes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingViewHolder(
                DataBindingUtil.inflate(mLayoutInflater, getLayoutRes(), parent, false));
    }

    @Override
    public int getItemCount() {
        return mCollection.size();
    }

    public void add(T viewModel) {
        mCollection.add(viewModel);
        notifyItemInserted(mCollection.size());
    }

    public void add(int position, T viewModel) {
        mCollection.add(position, viewModel);
        notifyDataSetChanged();
    }

    public void delete(T item) {
        if (mCollection.indexOf(item) != -1) {
            mCollection.remove(item);
            notifyItemRemoved(mCollection.indexOf(item));

        }
    }

    public void set(List<T> viewModels) {
        mCollection.clear();
        addAll(viewModels);
    }

    public void addAll(List<T> viewModels) {
        mCollection.addAll(viewModels);
        notifyDataSetChanged();
    }

    public T getItemByPos(int pos) {
        return mCollection.get(pos);
    }

    @LayoutRes
    protected int getLayoutRes() {
        return mLayoutRes;
    }


    public interface Presenter<T> extends BaseViewAdapter.Presenter {
        void onItemClick(T t);
    }
}