package vienan.app.expandableswipelistview.recyleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import vienan.app.expandableswipelistview.BR;
/**
 * Base Data Binding RecyclerView Adapter.
 */
public abstract class BaseViewAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {

  protected final LayoutInflater mLayoutInflater;

  protected List<T> mCollection;
  protected Presenter mPresenter;
  protected Decorator mDecorator;

  public interface Presenter {

  }

  public interface Decorator {
    void decorator(BindingViewHolder holder, int position, int viewType);
  }

  public BaseViewAdapter(Context context) {
    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public void onBindViewHolder(BindingViewHolder holder, int position) {
    final Object item = mCollection.get(position);
    holder.getBinding().setVariable(BR.item, item);
    holder.getBinding().setVariable(BR.presenter, getPresenter());
    holder.getBinding().executePendingBindings();
    if (mDecorator != null) {
      mDecorator.decorator(holder, position, getItemViewType(position));
    }
  }

  @Override
  public int getItemCount() {
    return mCollection.size();
  }

  public void remove(int position) {
    mCollection.remove(position);
    notifyItemRemoved(position);
  }

  public void remove(T item) {
    int position = mCollection.indexOf(item);
    mCollection.remove(item);
    notifyItemRemoved(position);
  }

  public List<T> getData() {
    return mCollection;
  }

  public void notifyItemChanged(T item) {
    notifyItemChanged(mCollection.indexOf(item));
  }

  public void clear() {
    mCollection.clear();
    notifyDataSetChanged();
  }

  public void setDecorator(Decorator decorator) {
    mDecorator = decorator;
  }

  public void setPresenter(Presenter presenter) {
    mPresenter = presenter;
  }

  protected Presenter getPresenter() {
    return mPresenter;
  }
}
