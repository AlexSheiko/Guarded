package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.login.LoginActivity;
import android.accessguarded.guarded365.co.uk.guardedguard.review.ReviewService;
import android.accessguarded.guarded365.co.uk.guardedguard.util.Site;
import android.accessguarded.guarded365.co.uk.guardedguard.util.Sites;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.accessguarded.guarded365.co.uk.guardedguard.R.menu.guards;

public class GuardsActivity extends AppCompatActivity {

    private GuardsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guards);

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
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GuardsAdapter();
        recyclerView.setAdapter(mAdapter);
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
                mAdapter.add(new Guard("Robert"));
                mAdapter.add(new Guard("Bill"));
                mAdapter.add(new Guard("Tony"));
                mAdapter.add(new Guard("Tracey"));
                for (Site site : sites.getList()) {
                    for (Guard guard : site.getGuards()) {
                        mAdapter.add(guard);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Sites> call, Throwable t) {
                Toast.makeText(GuardsActivity.this, R.string.error_guards_network, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
