package android.accessguarded.guarded365.co.uk.guardedguard.guardreview;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.guards.Guard;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Guard guard = (Guard) getIntent().getSerializableExtra("guard");
        populateDetails(guard);
    }

    private void populateDetails(Guard guard) {
        // Init views
        final TextView initialsTextView = (TextView) findViewById(R.id.initialsTextView);
        final ImageView photoImageView = (ImageView) findViewById(R.id.photoImageView);

        // Display data
        initialsTextView.setText(guard.getInitials());
        Glide.with(this).load(guard.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                photoImageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}