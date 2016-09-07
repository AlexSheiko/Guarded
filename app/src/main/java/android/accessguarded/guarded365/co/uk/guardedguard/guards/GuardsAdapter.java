package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Open guard details
                    Toast.makeText(mContext, "Coming soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final LineItem item = mDataset.get(position);
        Guard guard = item.guard;
        holder.mNameTextView.setText(guard.getFirstName() + " " + guard.getLastName());

        if (!item.isHeader) {
            // Display avatar and tasks
            holder.mTasksTextView.setText(
                    String.format(mContext.getResources().getQuantityString(
                            R.plurals.tasks_count_label, guard.getTaskCount()), guard.getTaskCount()));
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

    public void add(LineItem lineItem) {
        mDataset.add(lineItem);
        notifyItemInserted(mDataset.size());
    }

    public LineItem getItem(int position) {
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
