package uni.master.trips;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uni.master.trips.adapters.CategoryAdapter;
import uni.master.trips.entities.Category;
import uni.master.trips.models.CategoryItemModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCategoriesInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnCategoriesInteractionListener mListener;

    private FirebaseFirestore db;
    private List<CategoryItemModel> categoryOptions;
    private ListView categoriesListView;
    private FirebaseAuth firebaseAuth;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Categories");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        // get the Authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        // get the list view
        categoriesListView = view.findViewById(R.id.categories_list);
        // list with options
        categoryOptions = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot categorySnapshot : Objects.requireNonNull(task.getResult())) {
                        categoryOptions.add(new CategoryItemModel(categorySnapshot.toObject(Category.class).getName(),"0"));
                    }
                    // set adapter to the listView
                    final CategoryAdapter categoriesAdapter = new CategoryAdapter(categoryOptions, getActivity().getApplicationContext());
                    categoriesListView.setAdapter(categoriesAdapter);
                    categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // get the Item name
                            CategoryItemModel categoryItem = (CategoryItemModel) parent.getItemAtPosition(position);
                            // set title of action bar
                            ((MainActivity) getActivity()).getSupportActionBar().setTitle(categoryItem.getCategoryName());
                            // TODO filter sites by the selected category
                            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, new SitesByTypeFragment()).commit();
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Couldn't load fragment_categories", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCategoriesInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoriesInteractionListener) {
            mListener = (OnCategoriesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCategoriesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCategoriesInteractionListener {
        void onCategoriesInteraction();
    }
}
