package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.login.LoginActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class GuardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guards);

        // Make sure that user is logged in
        if (!isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public boolean isLoggedIn() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = prefs.getInt("userId", 0);
        return userId != 0;
    }
}
