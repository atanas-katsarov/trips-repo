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

public class CategoryAdapter extends ArrayAdapter<Category> {
    private List<Category> categorySet;
    private Context context;

    public CategoryAdapter(List<Category> categorySet, Context context) {
        super(context, R.layout.row_item_category, categorySet);
        this.categorySet = categorySet;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
//            LayoutInflater inflater = ((MainActivity) getContext()).getLayoutInflater();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate (R.layout.row_item_category, parent, false);
        }
        TextView titleView = convertView.findViewById(R.id.category_row_title);
//        TextView descView = convertView.findViewById(R.id.site_row_desc);
        titleView.setText(getItem(position).getName());
//        descView.setText(getItem(position).getDescription());
        return convertView;
    }
}
