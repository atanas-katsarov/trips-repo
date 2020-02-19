package uni.master.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Objects;

import uni.master.trips.adapters.MySiteAdapter;
import uni.master.trips.entities.Site;

public class MySitesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private ListView sitesListView;
    private List<Site> siteOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sites);
        // set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the Authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        sitesListView = findViewById(R.id.my_sites_list);
        siteOptions = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        String email = "";
        if(b != null)
            email = b.getString("email");

        db = FirebaseFirestore.getInstance();
        db.collection("Sites").whereEqualTo("userEmail", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot siteSnapshot : Objects.requireNonNull(task.getResult())) {
                        siteOptions.add(siteSnapshot.toObject(Site.class));
                    }
                    siteOptions.add(new Site("Test site", "Description", "bulgaria", "email", 2));
                    // set adapter to the listView
                    final MySiteAdapter siteAdapter = new MySiteAdapter(siteOptions, getApplicationContext());
                    sitesListView.setAdapter(siteAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't load fragment_categories", Toast.LENGTH_LONG).show();
                }
            }
        });

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
        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MySitesActivity.this, MainActivity.class));
                break;
            case R.id.add_new_site:
                startActivity(new Intent(MySitesActivity.this, CreateEditSiteActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
