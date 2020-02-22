package uni.master.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private RecyclerView sitesRecyclerView;
    private MySiteAdapter sitesAdapter;
    private RecyclerView.LayoutManager sitesLayoutManager;
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

        sitesRecyclerView = findViewById(R.id.my_sites_recycler_view);
        sitesRecyclerView.setHasFixedSize(true);
        sitesLayoutManager = new LinearLayoutManager(this);
        sitesRecyclerView.setLayoutManager(sitesLayoutManager);

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
                    // set adapter to the listView
                    sitesAdapter = new MySiteAdapter(siteOptions, getApplicationContext());
                    sitesAdapter.setOnItemClickListener(new MySiteAdapter.OnItemClickListener() {
                        @Override
                        public void onEditClick(int position) {
                            Intent intent = new Intent(getApplicationContext(), CreateEditSiteActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", siteOptions.get(position).getId());
                            bundle.putInt("position", position);
                            bundle.putInt("category", siteOptions.get(position).getCategoryId());
                            bundle.putString("name", siteOptions.get(position).getName());
                            bundle.putString("description", siteOptions.get(position).getDescription());
                            bundle.putString("country", siteOptions.get(position).getCountryName());
                            intent.putExtras(bundle);
                            startActivityForResult(intent,2);
                        }

                        @Override
                        public void onDeleteClick(final int position) {
                            db.collection("Sites")
                                    .whereEqualTo("id", siteOptions.get(position).getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                String siteIdDelete = null;
                                                for (QueryDocumentSnapshot siteSnapshot : Objects.requireNonNull(task.getResult())) {
                                                    siteIdDelete = siteSnapshot.getId();
                                                }
                                                if (siteIdDelete != null) {
                                                    db.collection("Sites").document(siteIdDelete).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getApplicationContext(), "Site deleted successfully ", Toast.LENGTH_LONG).show();
                                                                    siteOptions.remove(position);
                                                                    sitesAdapter.notifyItemRemoved(position);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getApplicationContext(), "Couldn't delete the site", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                        }
                    });
                    sitesRecyclerView.setAdapter(sitesAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't load MySites", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == RESULT_OK) {
                int categoryId = data.getIntExtra("categoryId", -1);
                String name = data.getStringExtra("name");
                String description = data.getStringExtra("description");
                String country = data.getStringExtra("countryName");
                if(requestCode == 1) {
                    int id = data.getIntExtra("site_id", -1);
                    String email = data.getStringExtra("email");
                    siteOptions.add(siteOptions.size(),new Site(id,name, description, country, email, categoryId));
                    sitesAdapter.notifyItemInserted(siteOptions.size());
                } else if (requestCode == 2) {
                    int position = data.getIntExtra("position", -1);
                    siteOptions.get(position).setName(name);
                    siteOptions.get(position).setDescription(description);
                    siteOptions.get(position).setCountryName(country);
                    siteOptions.get(position).setCategoryId(categoryId);
                    sitesAdapter.notifyItemChanged(position);
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to add the changes", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // action bar buttons
        getMenuInflater().inflate(R.menu.toolbar_profile, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_site).getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sitesAdapter.getFilter().filter(newText);
                return false;
            }
        });
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
                startActivityForResult(new Intent(MySitesActivity.this, CreateEditSiteActivity.class), 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
