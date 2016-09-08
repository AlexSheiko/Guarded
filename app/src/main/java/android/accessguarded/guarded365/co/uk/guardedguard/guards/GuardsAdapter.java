package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.guardreview.ReviewActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

class GuardsAdapter extends RecyclerView.Adapter<GuardsAdapter.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;
    private final Context mContext;
    private List<LineItem> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    GuardsAdapter(Context context) {
        mDataset = new ArrayList<>();
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GuardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.guard_list_item, parent, false);
        }
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final LineItem item = mDataset.get(position);
        final Guard guard = item.guard;
        holder.mNameTextView.setText(guard.getFullName());

        if (!item.isHeader) {
            // Display avatar and tasks
            holder.mTasksTextView.setText(guard.getTaskCount(mContext));
            holder.mInitialsTextView.setText(guard.getInitials());
            Glide.with(mContext).load(guard.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.mPhotoImageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.mPhotoImageView.setImageDrawable(circularBitmapDrawable);
                }
            });

            // Listen for click events
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open guard details
                    Intent intent = new Intent(mContext, ReviewActivity.class);
                    intent.putExtra("guard", guard);
                    intent.putExtra("positionInAdapter", position);
                    Pair<View, String> p1 = Pair.create((View) holder.mInitialsTextView, "initials");
                    Pair<View, String> p2 = Pair.create((View) holder.mPhotoImageView, "photo");
                    Pair<View, String> p3 = Pair.create((View) holder.mNameTextView, "name");
                    Pair<View, String> p4 = Pair.create((View) holder.mTasksTextView, "tasks");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((GuardsActivity) mContext, p1, p2, p3, p4);
                    ((GuardsActivity) mContext).startActivityForResult(intent, 1, options.toBundle());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    void add(LineItem lineItem) {
        mDataset.add(lineItem);
        notifyItemInserted(mDataset.size());
    }

    void removeItemAt(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    LineItem getItem(int position) {
        return mDataset.get(position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mNameTextView;
        TextView mTasksTextView;
        TextView mInitialsTextView;
        ImageView mPhotoImageView;

        ViewHolder(View v) {
            super(v);
            mNameTextView = (TextView) v.findViewById(R.id.nameTextView);
            mInitialsTextView = (TextView) v.findViewById(R.id.initialsTextView);
            mTasksTextView = (TextView) v.findViewById(R.id.tasksTextView);
            mPhotoImageView = (ImageView) v.findViewById(R.id.photoImageView);
        }
    }

    public static class LineItem {

        public int sectionManager;

        public int sectionFirstPosition;

        public boolean isHeader;

        public Guard guard;

        public LineItem(Guard guard, boolean isHeader, int sectionManager,
                        int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.guard = guard;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
        }
    }
}
