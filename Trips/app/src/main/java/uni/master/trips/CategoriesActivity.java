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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import uni.master.trips.entities.Category;

//TODO: Take the global variables above onCreate()
public class CategoriesActivity extends AppCompatActivity {

    FirebaseFirestore db;
    List<String> categoryOptions;
    ListView categoriesListView;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);

        // set action bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the Authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        // get the list view
        categoriesListView = findViewById(R.id.categories_list);
        final int listItemId = android.R.layout.simple_list_item_1;
        // list with options
        // TODO use custom class instead of String
        categoryOptions = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot categorySnapshot : task.getResult()) {
                                categoryOptions.add(categorySnapshot.toObject(Category.class).getName());
                            }
                            // set adapter to the listView
                            final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(CategoriesActivity.this, listItemId, categoryOptions);
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
                        } else {
                            Toast.makeText(CategoriesActivity.this, "Couldn't load categories", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // action bar buttons
        // TODO get current session and check if user is logged in
        MenuInflater inflater = getMenuInflater();
        if (firebaseAuth.getCurrentUser() != null) {
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
