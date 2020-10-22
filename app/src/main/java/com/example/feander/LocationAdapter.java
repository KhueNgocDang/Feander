package com.example.feander;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {
    private Context context;
    private List<LocationModel> locationModelList;

    public LocationAdapter(Context context, List<LocationModel> locationModelList) {
        this.context = context;
        this.locationModelList = locationModelList;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
        return new LocationHolder(view);
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

    class LocationHolder extends RecyclerView.ViewHolder{
        TextView list_name;
        TextView location;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.location_name);
            location = itemView.findViewById(R.id.location_address);
        }
    }
}
