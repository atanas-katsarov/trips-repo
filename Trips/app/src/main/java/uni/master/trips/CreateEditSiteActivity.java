package uni.master.trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uni.master.trips.adapters.CategoryAdapter;
import uni.master.trips.entities.Category;

public class CreateEditSiteActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner countrySpinner;
    private List<Category> categoryOptions;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_site);

        categorySpinner = findViewById(R.id.category_input);
        countrySpinner = findViewById(R.id.country_input);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            EditText nameInput = findViewById(R.id.site_name_input);
            EditText descriptionInput = findViewById(R.id.site_desc_input);
        }

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
                    final CategoryAdapter categoriesAdapter = new CategoryAdapter(categoryOptions, getApplicationContext());
                    categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    categorySpinner.setAdapter(categoriesAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't load fragment_categories", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
