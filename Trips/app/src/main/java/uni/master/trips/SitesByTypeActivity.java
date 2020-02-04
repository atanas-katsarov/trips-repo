package uni.master.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class SitesByTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sites_by_type);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (categoryName == null) {
            categoryName = "Types";
        }
        getSupportActionBar().setTitle(categoryName);

        ListView sitesListView = findViewById(R.id.sites_list);
        List<String> siteOptions = new ArrayList<>();
        siteOptions.add("Site 1");
        siteOptions.add("Site 2");
        final ArrayAdapter<String> sitesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, siteOptions);
        sitesListView.setAdapter(sitesAdapter);
        sitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String siteItem = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(SitesByTypeActivity.this, SiteDetailsActivity.class);
                intent.putExtra("site_name", siteItem);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        switch (item.getItemId()) {
            case R.id.login:
                startActivity(new Intent(SitesByTypeActivity.this, LoginActivity.class));
                break;
            case R.id.logout:
//                startActivity(toolbarIntent);
                break;
            case R.id.register:
                startActivity(new Intent(SitesByTypeActivity.this, RegisterActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(SitesByTypeActivity.this, MySitesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
