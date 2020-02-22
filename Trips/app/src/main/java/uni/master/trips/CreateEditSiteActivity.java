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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import uni.master.trips.adapters.ListSpinnerAdapter;
import uni.master.trips.entities.Category;
import uni.master.trips.entities.Site;

public class CreateEditSiteActivity extends AppCompatActivity {


    private String previousSiteDocId;
    private Integer prevCategoryId;
    private String prevCountry;
    private Spinner categorySpinner;
    private Spinner countrySpinner;
    private EditText nameInput;
    private EditText descriptionInput;
    private List<Category> categoryOptions;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private int positionInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_site);

        // get the Authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        categorySpinner = findViewById(R.id.category_input);
        countrySpinner = findViewById(R.id.country_input);
        nameInput = findViewById(R.id.site_name_input);
        descriptionInput = findViewById(R.id.site_desc_input);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String name = bundle.getString("name");
            String description = bundle.getString("description");
            if (name != null) {
                nameInput.setText(name);
            }
            if (description != null) {
                descriptionInput.setText(description);
            }

            int id = bundle.getInt("id");
            positionInList = bundle.getInt("position");

            db.collection("Sites").whereEqualTo("id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot siteSnapshot : Objects.requireNonNull(task.getResult())) {
                            previousSiteDocId = siteSnapshot.getId();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Can't load site from DB", Toast.LENGTH_LONG).show();
                    }
                }
            });
            prevCategoryId = bundle.getInt("category");
            prevCountry = bundle.getString("country");
        }

        // list with options
        categoryOptions = new ArrayList<>();
        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Category previousCategory = null;
                    for (QueryDocumentSnapshot categorySnapshot : Objects.requireNonNull(task.getResult())) {
                        Category categoryToAdd = categorySnapshot.toObject(Category.class);
                        categoryOptions.add(categoryToAdd);
                        if (prevCategoryId != null && categoryToAdd.getId() == prevCategoryId) {
                            previousCategory = categoryToAdd;
                        }
                    }
                    // set adapter to the listView
                    final ListSpinnerAdapter categoriesAdapter = new ListSpinnerAdapter(categoryOptions, getApplicationContext());
                    categoriesAdapter.setDropDownViewResource(R.layout.row_item_spinner);
                    categorySpinner.setAdapter(categoriesAdapter);
                    if (previousCategory != null) {
                        categorySpinner.setSelection(categoriesAdapter.getPosition(previousCategory));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't load fragment_categories", Toast.LENGTH_LONG).show();
                }
            }
        });
        List<String> countryOptions = new ArrayList<>();
        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("en", country);
            countryOptions.add(locale.getDisplayCountry());
        }
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row_item_spinner, countryOptions);
        countrySpinner.setAdapter(countryAdapter);
        if (prevCountry != null) {
            countrySpinner.setSelection(countryAdapter.getPosition(prevCountry));
        }

        Button btnSave = findViewById(R.id.btn_create_edit);
        if (bundle == null) {
            btnSave.setText("Create");
        } else {
            btnSave.setText("Edit");
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Category categorySelected = (Category) categorySpinner.getSelectedItem();
                if (previousSiteDocId == null) {
                    Site site = new Site(nameInput.getText().toString(), descriptionInput.getText().toString(), countrySpinner.getSelectedItem().toString(), firebaseAuth.getCurrentUser().getEmail(), categorySelected.getId());
                    db.collection("Sites").add(site).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Intent backIntent = new Intent();
                            backIntent.putExtra("position", positionInList);
                            backIntent.putExtra("name", nameInput.getText().toString());
                            backIntent.putExtra("description", descriptionInput.getText().toString());
                            backIntent.putExtra("countryName", countrySpinner.getSelectedItem().toString());
                            backIntent.putExtra("categoryId", categorySelected.getId());
                            setResult(RESULT_OK, backIntent);
                            Toast.makeText(getApplicationContext(), "Site created", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Couldn't create the new site", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Map<Object, String> mapStr = new HashMap<>();
                    Map<Object, Integer> mapInt = new HashMap<>();
                    mapStr.put("name", nameInput.getText().toString());
                    mapStr.put("description", descriptionInput.getText().toString());
                    mapStr.put("countryName", countrySpinner.getSelectedItem().toString());
                    mapInt.put("categoryId", categorySelected.getId());
                    db.collection("Sites")
                            .document(previousSiteDocId)
                            .set(mapInt, SetOptions.merge());
                    db.collection("Sites")
                            .document(previousSiteDocId)
                            .set(mapStr, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent backIntent = new Intent();
                                    backIntent.putExtra("position", positionInList);
                                    backIntent.putExtra("name", nameInput.getText().toString());
                                    backIntent.putExtra("description", descriptionInput.getText().toString());
                                    backIntent.putExtra("countryName", countrySpinner.getSelectedItem().toString());
                                    backIntent.putExtra("categoryId", categorySelected.getId());
                                    setResult(RESULT_OK, backIntent);
                                    Toast.makeText(getApplicationContext(), "Site edited successfully ", Toast.LENGTH_LONG).show();
                                    finish();
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Couldn't edit the site", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}
