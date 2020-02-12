package uni.master.trips.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import uni.master.trips.R;
import uni.master.trips.models.SiteItemModel;

public class SiteAdapter extends ArrayAdapter<SiteItemModel> implements View.OnClickListener {
    private ArrayList<SiteItemModel> siteSet;
    Context context;

    private static class ViewHolder {
        TextView title;
        TextView description;
    }

    public SiteAdapter(ArrayList<SiteItemModel> data, Context context) {
        super(context, R.layout.site_row_item, data);
        this.siteSet = data;
        this.context=context;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
