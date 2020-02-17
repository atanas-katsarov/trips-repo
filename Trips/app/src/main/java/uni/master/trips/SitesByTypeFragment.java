package uni.master.trips;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import uni.master.trips.adapters.SiteAdapter;
import uni.master.trips.models.SiteItemModel;

public class SitesByTypeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnSiteByTypeInteractionListener mListener;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private List<SiteItemModel> siteOptions;
    private ListView sitesListView;

    public SitesByTypeFragment() {
        // Required empty public constructor
    }

    public static SitesByTypeFragment newInstance(String param1, String param2) {
        SitesByTypeFragment fragment = new SitesByTypeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sites_by_type, container, false);

        sitesListView = view.findViewById(R.id.sites_list);
        siteOptions = new ArrayList<>();
//
//        // get the Authentication object
//        firebaseAuth = FirebaseAuth.getInstance();
//        // list with options
//        // TODO get data from Firebase
//        db = FirebaseFirestore.getInstance();
//        db.collection("Sites");
//        db.collection("Sites").get();
//        db.collection("Sites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot sitesSnapshot : Objects.requireNonNull(task.getResult())) {
//                        siteOptions.add(new SiteItemModel(sitesSnapshot.toObject(Site.class).getName(),sitesSnapshot.toObject(Site.class).getDescription()));
//                    }
//                } else {
//                    Toast.makeText(getActivity().getApplicationContext(), "Couldn't load fragment_site_by_type", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        siteOptions.add(new SiteItemModel("Site 1","Some description"));
        siteOptions.add(new SiteItemModel("Site 2","Some description 2"));
        siteOptions.add(new SiteItemModel("Site 3","Some description 3"));
        SiteAdapter sitesAdapter = new SiteAdapter(siteOptions,getActivity().getApplicationContext());
//                new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, siteOptions);
        sitesListView.setAdapter(sitesAdapter);
        sitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the Item name
                SiteItemModel siteItem = (SiteItemModel) parent.getItemAtPosition(position);
                // TODO get site details
//                Intent intent = new Intent(MainActivity.this, SiteDetailsFragment.class);
//                intent.putExtra("site_name", siteItem);
//                startActivity(intent);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SiteDetailsFragment()).commit();

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
