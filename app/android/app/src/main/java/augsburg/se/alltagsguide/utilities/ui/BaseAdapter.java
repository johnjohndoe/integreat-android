package augsburg.se.alltagsguide.utilities.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import augsburg.se.alltagsguide.utilities.Newer;
import roboguice.RoboGuice;


public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, Item extends Newer<Item>> extends RecyclerView.Adapter<VH> {

    @NonNull private List<Item> mItems;
    @NonNull protected Context mContext;

    protected BaseAdapter(@NonNull List<Item> items, @NonNull Context context) {
        super();
        RoboGuice.injectMembers(context, this);
        mContext = context;
        mItems = new ArrayList<>();
        setItems(items);
    }


    private void applyAndAnimateRemovals(@NonNull List<Item> newModels) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final Item model = mItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(@NonNull List<Item> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Item model = newModels.get(i);
            int indexOf = mItems.indexOf(model);
            if (indexOf != -1) {
                Item other = mItems.get(indexOf);
                if (model.getTimestamp() > other.getTimestamp()) {
                    /* Update might have changed the model, so put new object and notify adapter about the new item*/
                    mItems.set(indexOf, model);
                    notifyItemChanged(indexOf);
                }
            } else {
                addItem(binarySearch(model), model);
            }
        }
    }

    private int binarySearch(Item value) {
        int low = 0;
        int high = mItems.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Item midVal = mItems.get(mid);
            int cmp = midVal.compareTo(value);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return low;
    }

    private void applyAndAnimateMovedItems(@NonNull List<Item> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Item model = newModels.get(toPosition);
            final int fromPosition = mItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @NonNull
    public Item removeItem(int position) {
        final Item model = mItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Item model) {
        mItems.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Item model = mItems.remove(fromPosition);
        mItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    public Item get(int index) {
        return mItems.get(index);
    }

    public void setItems(@NonNull List<Item> items) {
        Collections.sort(items);
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

}