package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.login.LoginActivity;
import android.accessguarded.guarded365.co.uk.guardedguard.login.User;
import android.accessguarded.guarded365.co.uk.guardedguard.review.ReviewService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class GuardsActivity extends AppCompatActivity {

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
        inflater.inflate(R.menu.guards, menu);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.guarded365.co.uk/api/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ReviewService service = retrofit.create(ReviewService.class);
        Call<User> call = service.listGuards(getUserId());
    }
}
