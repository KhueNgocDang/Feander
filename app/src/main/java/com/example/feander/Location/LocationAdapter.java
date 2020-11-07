package com.example.feander.Location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feander.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> implements Filterable {
    private Context context;
    private List<LocationModel> locationModelList;
    private List<LocationModel> locationModelListFull;
    private OnLocationListener onLocationListener;

    public LocationAdapter(Context context, List<LocationModel> locationModelList,OnLocationListener onLocationListener) {
        this.context = context;
        this.locationModelList = locationModelList;
        this.locationModelListFull = locationModelList;
        this.onLocationListener = onLocationListener;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
        return new LocationHolder(view, onLocationListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        LocationModel model = locationModelList.get(position);
        holder.list_name.setText(model.getName());
        holder.location.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        return locationModelList.size();
    }

    class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView list_name;
        TextView location;
        OnLocationListener onLocationListener;

        public LocationHolder(@NonNull View itemView, OnLocationListener onLocationListener) {
            super(itemView);
            list_name = itemView.findViewById(R.id.location_name);
            location = itemView.findViewById(R.id.location_address);
            this.onLocationListener = onLocationListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLocationListener.onLocationClick(getAdapterPosition());
        }
    }

    @Override
    public Filter getFilter() {
        return location_Filter;
    }
    private Filter location_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = locationModelListFull;
            }
            else {
                String filterPattern = constraint.toString().toLowerCase();
                List<LocationModel> filteredList = new ArrayList<>();
                for (LocationModel item : locationModelListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                results.values = filteredList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            locationModelList = (List<LocationModel>) results.values;
            notifyDataSetChanged();
        }
    };

    public interface OnLocationListener{
        void onLocationClick(int position);
    }
}
