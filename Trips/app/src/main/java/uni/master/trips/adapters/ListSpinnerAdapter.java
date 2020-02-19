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
import uni.master.trips.entities.Category;

public class ListSpinnerAdapter extends ArrayAdapter<Category> {

    private List<Category> myCategorySet;
    private Context context;

    public ListSpinnerAdapter(List<Category> sitesData, Context context) {
        super(context, R.layout.row_item_spinner, sitesData);
        this.myCategorySet = sitesData;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate (R.layout.row_item_spinner, parent, false);
        }
        Category category = getItem(position);
        if (category != null) {
            TextView siteTitle = convertView.findViewById(R.id.item_title);
            siteTitle.setText(category.getName());
        }
        return convertView;
    }
}
