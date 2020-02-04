package uni.master.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);

        // set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the list view
        ListView categoriesListView = findViewById(R.id.categories_list);
        // list with options
        // TODO get data from Firebase
        // TODO use custom class instead of String
        List<String> categoryOptions = new ArrayList<>();
        categoryOptions.add("Lakes");
        categoryOptions.add("Parks");
        // set adapter to the listView
        final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryOptions);
        categoriesListView.setAdapter(categoriesAdapter);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the Item name
                String categoryItem = (String) parent.getItemAtPosition(position);
                // TODO filter sites by the selected category
                Intent intent = new Intent(CategoriesActivity.this, SitesByTypeActivity.class);
                intent.putExtra("title", categoryItem);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // action bar buttons
        // TODO get current session and check if user is logged in
        MenuInflater inflater = getMenuInflater();
        if (false) {
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
                startActivity(new Intent(CategoriesActivity.this, LoginActivity.class));
                break;
            case R.id.logout:
                // TODO logout user
//                startActivity(toolbarIntent);
                break;
            case R.id.register:
                startActivity(new Intent(CategoriesActivity.this, RegisterActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(CategoriesActivity.this, MySitesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
