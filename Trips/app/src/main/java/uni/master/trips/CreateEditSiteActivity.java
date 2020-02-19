package uni.master.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uni.master.trips.adapters.ListSpinnerAdapter;
import uni.master.trips.entities.Category;
import uni.master.trips.entities.Site;

public class CreateEditSiteActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner countrySpinner;
    private EditText nameInput;
    private EditText descriptionInput;
    private List<Category> categoryOptions;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_site);

        categorySpinner = findViewById(R.id.category_input);
        countrySpinner = findViewById(R.id.country_input);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nameInput = findViewById(R.id.site_name_input);
            descriptionInput = findViewById(R.id.site_desc_input);
        }

        // get the Authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        // list with options
        categoryOptions = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot categorySnapshot : Objects.requireNonNull(task.getResult())) {
                        categoryOptions.add(categorySnapshot.toObject(Category.class));
                    }
                    // set adapter to the listView
                    final ListSpinnerAdapter categoriesAdapter = new ListSpinnerAdapter(categoryOptions, getApplicationContext());
                    categoriesAdapter.setDropDownViewResource(R.layout.row_item_spinner);

                    categorySpinner.setAdapter(categoriesAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't load fragment_categories", Toast.LENGTH_LONG).show();
                }
            }
        });
        // TODO: Get countries from API
        List<String> countryOptions = new ArrayList<>();
        countryOptions.add("Bulgaria");
        countryOptions.add("Jamaica");
        countryOptions.add("Japan");

        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row_item_spinner ,countryOptions);
        countrySpinner.setAdapter(countryAdapter);

        Button btn = findViewById(R.id.btn_create_edit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category categorySelected = (Category) categorySpinner.getSelectedItem();
                Site site = new Site("Saved from Java", "Desc", "Jamaica", "aaaa@aaa.com", 2);

//                Site site = new Site(nameInput.getText().toString(), descriptionInput.getText().toString(), countrySpinner.getSelectedItem().toString(), firebaseAuth.getCurrentUser().getEmail(), categorySelected.getId());
                db.collection("Sites").add(site).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                Intent categoryPage = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                Toast.makeText(getApplicationContext(), "Site created", Toast.LENGTH_LONG).show();
//                startActivity(categoryPage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Couldn't create the new site", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
