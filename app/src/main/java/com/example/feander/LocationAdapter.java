package com.example.feander;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LocationAdapter extends FirestoreRecyclerAdapter<LocationModel, LocationAdapter.LocationHolder> {

    public LocationAdapter(@NonNull FirestoreRecyclerOptions<LocationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationHolder holder, int position, @NonNull LocationModel model) {
        holder.list_name.setText(model.getName());
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_location_single, parent, false);
        return new LocationHolder(view);
    }

    class LocationHolder extends RecyclerView.ViewHolder{
        TextView list_name;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);

        }
    }
}
