package android.accessguarded.guarded365.co.uk.guardedguard.guardreview;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.guards.Guard;
import android.accessguarded.guarded365.co.uk.guardedguard.util.ReviewResponse;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        removeActionBarShadow();

        populateDetails(getGuard());
    }

    private Guard getGuard() {
        return (Guard) getIntent().getSerializableExtra("guard");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Display animation when returning to the home screen
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateDetails(Guard guard) {
        // Init views
        final TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        final TextView initialsTextView = (TextView) findViewById(R.id.initialsTextView);
        final TextView siteTextView = (TextView) findViewById(R.id.siteTextView);
        final TextView tasksTextView = (TextView) findViewById(R.id.tasksTextView);
        final TextView incidentsTextView = (TextView) findViewById(R.id.incidentsTextView);
        final ImageView photoImageView = (ImageView) findViewById(R.id.photoImageView);

        // Display data
        nameTextView.setText(guard.getFullName().replaceAll("\n", " "));
        initialsTextView.setText(guard.getInitials());
        siteTextView.setText(guard.getSiteName());
        tasksTextView.setText(guard.getTaskCount(this));
        incidentsTextView.setText(guard.getIncidentCount(this));
        Glide.with(this).load(guard.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                photoImageView.setImageDrawable(circularBitmapDrawable);
            }
        });

        // Elevate action bar on scroll
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (!scrollView.canScrollVertically(-1)) {
                    removeActionBarShadow();
                } else {
                    displayActionBarShadow();
                }
            }
        });
    }

    private void removeActionBarShadow() {
        getSupportActionBar().setElevation(0);
    }

    private void displayActionBarShadow() {
        Resources resources = this.getResources();
        float elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, resources.getDisplayMetrics());
        getSupportActionBar().setElevation(elevation);
    }

    public void onClickSubmit(View view) {
        // Collect form data
        int uniformRating = (int) ((RatingBar) findViewById(R.id.uniformRatingBar)).getRating();
        int attendanceRating = (int) ((RatingBar) findViewById(R.id.attendanceRatingBar)).getRating();
        int attitudeRating = (int) ((RatingBar) findViewById(R.id.attitudeRatingBar)).getRating();
        int incidentRating = (int) ((RatingBar) findViewById(R.id.incidentRatingBar)).getRating();
        int performanceRating = (int) ((RatingBar) findViewById(R.id.performanceRatingBar)).getRating();
        String comment = ((EditText) findViewById(R.id.commentEditText)).getText().toString();

        // Compose review object
        Guard guard = getGuard();
        Review review = new Review(guard.getId(), guard.getSiteId(), getUserId(),
                uniformRating, attendanceRating, attitudeRating, incidentRating, performanceRating,
                comment);

        submitReview(review);
    }

    private void submitReview(Review review) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.submitButton).setEnabled(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.guarded365.co.uk/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReviewService service = retrofit.create(ReviewService.class);
        Call<ReviewResponse> call = service.submitReview(review);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponse reviewResponse = response.body();
                if (reviewResponse == null || reviewResponse.isSuccessful()) {
                    // Go back to a list of guards
                    Toast.makeText(ReviewActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                    getIntent().putExtra("positionInAdapter", getIntent().getIntExtra("positionInAdapter", -1));
                    setResult(RESULT_OK, getIntent());
                    supportFinishAfterTransition();
                } else {
                    // Display an error message
                    Toast.makeText(ReviewActivity.this, reviewResponse.getMessage() + "", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.submitButton).setEnabled(true);
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, R.string.error_review_network, Toast.LENGTH_LONG).show();
                findViewById(R.id.submitButton).setEnabled(true);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });
    }

    private int getUserId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("userId", 0);
    }
}