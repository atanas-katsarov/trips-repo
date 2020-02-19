package uni.master.trips.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uni.master.trips.R;
import uni.master.trips.entities.Site;

public class SiteAdapter extends ArrayAdapter<Site> implements View.OnClickListener {
    private List<Site> siteSet;
    private Context context;

//    private static class ViewHolder {
//        TextView title;
//        TextView description;
//    }

    public SiteAdapter(List<Site> data, Context context) {
        super(context, R.layout.row_item_site, data);
        this.siteSet = data;
        this.context=context;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate (R.layout.row_item_site, parent, false);
        }
        TextView titleView = convertView.findViewById(R.id.site_row_title);
        TextView descView = convertView.findViewById(R.id.site_row_desc);
        titleView.setText(getItem(position).getName());
        descView.setText(getItem(position).getDescription());
        return convertView;
    }
}
