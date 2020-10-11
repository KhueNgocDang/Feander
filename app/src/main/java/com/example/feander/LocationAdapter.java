package com.example.feander;

import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.util.List;

public class LocationAdapter extends FirestoreRecyclerAdapter<LocationModel, LocationAdapter.LocationHolder> {
    private OnItemClickListener listener;
    private Geocoder geocoder ;

    public LocationAdapter(@NonNull FirestoreRecyclerOptions<LocationModel> options) {
        super(options);
    }

    @Override
    //Getting info from database
    protected void onBindViewHolder(@NonNull LocationHolder holder, int position, @NonNull LocationModel model) {

        holder.list_name.setText(model.getName());
        holder.location.setText(model.getLocation());

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
        TextView location;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            location = itemView.findViewById(R.id.location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener != null)
                    {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
