package uni.master.trips.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uni.master.trips.R;
import uni.master.trips.entities.Site;

public class MySiteAdapter extends ArrayAdapter<Site> implements View.OnClickListener {

    private List<Site> mySiteSet;
    private Context context;

    public MySiteAdapter(List<Site> sitesData, Context context) {
        super(context, R.layout.row_item_my_site, sitesData);
        this.mySiteSet = sitesData;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Site site = getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate (R.layout.row_item_my_site, parent, false);
        }
        TextView siteTitle = convertView.findViewById(R.id.my_site_title);
        siteTitle.setText(getItem(position).getName());
        Button deleteBtn = convertView.findViewById(R.id.delete_site_btn);
        deleteBtn.setOnClickListener(this);
        return convertView;
    }
}
