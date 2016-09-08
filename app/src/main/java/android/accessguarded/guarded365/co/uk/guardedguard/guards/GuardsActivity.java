package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.guardreview.ReviewService;
import android.accessguarded.guarded365.co.uk.guardedguard.login.LoginActivity;
import android.accessguarded.guarded365.co.uk.guardedguard.util.Site;
import android.accessguarded.guarded365.co.uk.guardedguard.util.Sites;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.accessguarded.guarded365.co.uk.guardedguard.R.menu.guards;
import static android.accessguarded.guarded365.co.uk.guardedguard.guards.GuardsAdapter.LineItem;

public class GuardsActivity extends AppCompatActivity {

    private GuardsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guards);
        removeActionBarShadow();

        // Make sure that user is logged in
        if (isLoggedIn()) {
            // Load feed
            displayGuards();
        } else {
            // Log in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(guards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int position = data.getIntExtra("positionInAdapter", -1);
            mAdapter.removeItemAt(position);
        }
    }

    public boolean isLoggedIn() {
        return getUserId() != 0;
    }

    private void logout() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().clear().apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private int getUserId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("userId", 0);
    }

    private void displayGuards() {
        prepareRecyclerView();
        loadGuards();
    }

    private void prepareRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItem(position).isHeader) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GuardsAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(-1)) {
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

    private void loadGuards() {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.guarded365.co.uk/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReviewService service = retrofit.create(ReviewService.class);
        Call<Sites> call = service.listGuards(getUserId());
        call.enqueue(new Callback<Sites>() {
            @Override
            public void onResponse(Call<Sites> call, Response<Sites> response) {
                Sites sites = response.body();
                for (Site site : sites.getList()) {
                    // Add list headers
                    mAdapter.add(new LineItem(new Guard(site.getName()), true, 0, 0));
                    // Add guards for each header
                    for (Guard guard : site.getGuards()) {
                        mAdapter.add(new LineItem(guard, false, 0, 0));
                    }
                }
                // TODO: Remove after preview
                mAdapter.add(new LineItem(new Guard("Second section"), true, 0, 0));
                mAdapter.add(new LineItem(new Guard("Derek", "Smitherton"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Nathaniel", "Specrakovich"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Tracey", "Holdingforth"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Robert", "Day"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Randy", ""), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Derek", "Smitherton"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Third section"), true, 0, 0));
                mAdapter.add(new LineItem(new Guard("Nathaniel", "Specrakovich"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Tracey", "Holdingforth"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Fourth section"), true, 0, 0));
                mAdapter.add(new LineItem(new Guard("Derek", "Smitherton"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Nathaniel", "Specrakovich"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Tracey", "Holdingforth"), false, 0, 0));
                mAdapter.add(new LineItem(new Guard("Fifth section"), true, 0, 0));
                mAdapter.add(new LineItem(new Guard("Robert", "Day"), false, 0, 0));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Sites> call, Throwable t) {
                findViewById(R.id.errorTextView).setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
