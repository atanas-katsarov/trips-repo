package uni.master.trips;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import uni.master.trips.entities.Site;

public class SiteDetailsFragment extends Fragment {

    private static final String ID_SITE = "id";
    private Integer idSite;
    private FirebaseFirestore db;
    private OnDetailsInteractionListener mListener;
    private TextView title;
    private TextView desc;
    private TextView country;
    private ImageView flag;
    private ImageView siteImg;

    public SiteDetailsFragment() {
        // Required empty public constructor
    }

    public static SiteDetailsFragment newInstance(Integer id) {
        SiteDetailsFragment fragment = new SiteDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_SITE, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idSite = getArguments().getInt(ID_SITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_site_details, container, false);

        title = view.findViewById(R.id.title_details);
        desc = view.findViewById(R.id.desc_details);
        country = view.findViewById(R.id.country_details);
        flag = view.findViewById(R.id.flag_details);
        siteImg = view.findViewById(R.id.site_image);

        db = FirebaseFirestore.getInstance();
        db.collection("Sites").whereEqualTo("id", idSite).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot siteSnapshot : Objects.requireNonNull(task.getResult())) {
                        final Site site = siteSnapshot.toObject(Site.class);
                        @SuppressLint("StaticFieldLeak")
                        AsyncTask asyncTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("https://restcountries.eu/rest/v2/name/" + site.getCountryName()).build();
                                Response response = null;

                                try {
                                    response = client.newCall(request).execute();
                                    return response.body().string();
                                } catch (Exception e) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Couldn't load the country flag", Toast.LENGTH_LONG).show();
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                Activity activity = getActivity();
                                String url = o.toString();
                                String flagPath = url.substring(url.lastIndexOf("https"), url.lastIndexOf("\",\"regionalBlocs"));
                                flag.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                GlideToVectorYou.justLoadImage(activity, Uri.parse(flagPath), flag);
                            }
                        }.execute();
                        title.setText(site.getName());
                        desc.setText(site.getDescription());
                        country.setText(site.getCountryName());

                        Picasso.get()
                                .load("https://www.komar.de/en/media/catalog/product/cache/5/image/9df78eab33525d08d6e5fb8d27136e95/S/H/SHX9-008_1568286487.jpg")
//                              .resize(250,250)
                                .into(siteImg);


                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Couldn't load site details", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDetailsInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsInteractionListener) {
            mListener = (OnDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDetailsInteractionListener {
        void onDetailsInteraction();
    }
}
