package uni.master.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SiteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_details);
        // set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get the site name from the intent
        Intent intent = getIntent();
        String siteName = intent.getStringExtra("site_name");

        TextView nameTextView = findViewById(R.id.site_name);
        nameTextView.setText(siteName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // action bar buttons
        // TODO check session
        MenuInflater inflater = getMenuInflater();
        if (true) {
            inflater.inflate(R.menu.toolbar_logged_in, menu);
        } else {
            inflater.inflate(R.menu.toolbar_logged_out, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // switch between available action bar buttons
        switch (item.getItemId()) {
            case R.id.login:
                startActivity(new Intent(SiteDetailsActivity.this, LoginActivity.class));
                break;
            case R.id.logout:
//                startActivity(toolbarIntent);
                break;
            case R.id.register:
                startActivity(new Intent(SiteDetailsActivity.this, RegisterActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(SiteDetailsActivity.this, MySitesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
