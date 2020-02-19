package uni.master.trips;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uni.master.trips.adapters.SiteAdapter;
import uni.master.trips.entities.Site;

public class SitesByTypeFragment extends Fragment {

    private static final String ID_CATEGORY = "id";

    private Integer idCategory;

    private OnSiteByTypeInteractionListener mListener;

    private FirebaseFirestore db;
    private List<Site> siteOptions;
    private ListView sitesListView;

    public SitesByTypeFragment() {
        // Required empty public constructor
    }

    public static SitesByTypeFragment newInstance(Integer id) {
        SitesByTypeFragment fragment = new SitesByTypeFragment();
        Bundle args = new Bundle();
        args.putInt(ID_CATEGORY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCategory = getArguments().getInt(ID_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sites_by_type, container, false);

        sitesListView = view.findViewById(R.id.sites_list);
        siteOptions = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("Sites").whereEqualTo("categoryId", idCategory).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot siteSnapshot : Objects.requireNonNull(task.getResult())) {
                        siteOptions.add(siteSnapshot.toObject(Site.class));
                    }
                    // set adapter to the listView
                    final SiteAdapter siteAdapter = new SiteAdapter(siteOptions, getActivity().getApplicationContext());
                    sitesListView.setAdapter(siteAdapter);
                    sitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // get the Item name
                            Site siteItem = (Site) parent.getItemAtPosition(position);
                            // set title of action bar
                            ((MainActivity) getActivity()).getSupportActionBar().setTitle(siteItem.getName());
                            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_container, SiteDetailsFragment.newInstance(siteItem.getId())).commit();
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Couldn't load the site for the selected category", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSiteByTypeInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSiteByTypeInteractionListener) {
            mListener = (OnSiteByTypeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSiteByTypeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSiteByTypeInteractionListener {
        void onSiteByTypeInteraction();
    }
}
