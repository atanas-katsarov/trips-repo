package uni.master.trips.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uni.master.trips.R;
import uni.master.trips.models.SiteItemModel;

public class MySiteAdapter extends ArrayAdapter<SiteItemModel> {

    private List<SiteItemModel> mySiteSet;

    public MySiteAdapter(List<SiteItemModel> sitesData, Context context) {
        super(context, R.layout.row_item_my_site, sitesData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
