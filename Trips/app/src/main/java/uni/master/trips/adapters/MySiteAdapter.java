package uni.master.trips.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import uni.master.trips.R;
import uni.master.trips.entities.Site;

public class MySiteAdapter extends RecyclerView.Adapter<MySiteAdapter.MySitesViewHolder> implements Filterable {

    private List<Site> mySiteSet;
    private List<Site> mySiteSetFull;
    private Context context;
    private FirebaseFirestore db;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MySitesViewHolder extends RecyclerView.ViewHolder {
        public TextView siteTitle;
        public ImageButton deleteBtn;
        public ImageButton editBtn;

        public MySitesViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            siteTitle = itemView.findViewById(R.id.my_site_title);
            deleteBtn = itemView.findViewById(R.id.delete_site_btn);
            editBtn = itemView.findViewById(R.id.edit_site_btn);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public MySiteAdapter(List<Site> sitesData, Context context) {
        this.context = context;
        this.mySiteSet = sitesData;
        this.mySiteSetFull = new ArrayList<>(sitesData);
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MySitesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_my_site, parent, false);

        return new MySitesViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MySitesViewHolder holder, int position) {
        Site site = mySiteSet.get(position);

        holder.siteTitle.setText(site.getName());
    }

    @Override
    public int getItemCount() {
        return mySiteSet.size();
    }

    @Override
    public Filter getFilter() {
        return mySitesFilter;
    }

    private Filter mySitesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Site> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mySiteSetFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Site site : mySiteSetFull) {
                    if (site.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(site);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mySiteSet.clear();
            mySiteSet.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
