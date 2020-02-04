package uni.master.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MySitesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sites);
        // set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // action bar buttons
        getMenuInflater().inflate(R.menu.toolbar_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // switch between available action bar buttons
//        Intent toolbarIntent;
        switch (item.getItemId()) {
            case R.id.logout:
                // TODO logout user
//                toolbarIntent = new Intent(MySitesActivity.this, LoginActivity.class);
//                startActivity(toolbarIntent);
                break;
            case R.id.add_new_site:
//                toolbarIntent = new Intent(MySitesActivity.this, LoginActivity.class);
//                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
