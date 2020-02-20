package uni.master.trips.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import uni.master.trips.CreateEditSiteActivity;
import uni.master.trips.R;
import uni.master.trips.entities.Site;

public class MySiteAdapter extends ArrayAdapter<Site> implements View.OnClickListener {

    private List<Site> mySiteSet;
    private Context context;
    private FirebaseFirestore db;

    public MySiteAdapter(List<Site> sitesData, Context context) {
        super(context, R.layout.row_item_my_site, sitesData);
        this.mySiteSet = sitesData;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Site site = getItem(position);

        switch (v.getId()) {
            case R.id.delete_site_btn:
                db.collection("Sites")
                        .whereEqualTo("id", site.getId())
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
                                                        Toast.makeText(getContext(), "Site deleted successfully ", Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Couldn't delete the site", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
                break;
            case R.id.edit_site_btn:
                Intent intent = new Intent(getContext(), CreateEditSiteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", site.getId());
                bundle.putInt("category", site.getCategoryId());
                bundle.putString("name", site.getName());
                bundle.putString("description", site.getDescription());
                bundle.putString("country", site.getCountryName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                break;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_my_site, parent, false);
        }
        TextView siteTitle = convertView.findViewById(R.id.my_site_title);
        siteTitle.setText(getItem(position).getName());

        ImageButton deleteBtn = convertView.findViewById(R.id.delete_site_btn);
        deleteBtn.setOnClickListener(this);
        deleteBtn.setTag(position);

        ImageButton editBtn = convertView.findViewById(R.id.edit_site_btn);
        editBtn.setOnClickListener(this);
        editBtn.setTag(position);
        return convertView;
    }
}
