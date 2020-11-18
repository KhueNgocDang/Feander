package com.example.feander.Location;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.feander.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> implements Filterable {
    private Context context;
    private List<LocationModel> locationModelList;
    private List<LocationModel> locationModelListFull;
    private OnLocationListener onLocationListener;
    private Activity callingActivity;

    public LocationAdapter(Context context, List<LocationModel> locationModelList, OnLocationListener onLocationListener) {
        this.context = context;
        this.locationModelList = locationModelList;
        this.locationModelListFull = locationModelList;
        this.onLocationListener = onLocationListener;
    }

    public void clearAdapter() {
        int size = this.locationModelList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                locationModelList.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
        return new LocationHolder(view, onLocationListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        LocationModel model = locationModelList.get(position);
        holder.list_name.setText(model.getName());
        holder.location.setText(model.getLocation());
        holder.distance.setText((int) model.getDistance() + "m");
        holder.status.setText(getStatus(model));
        Glide.with(holder.img.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(model.getImage())
                .into(holder.img);
        /*if (!callingActivity.isDestroyed()) {

        }*/
    }

    @Override
    public int getItemCount() {
        return locationModelList.size();
    }

    public void setCallingActivity(Activity callingActivity) {
        this.callingActivity = callingActivity;
    }

    class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView list_name;
        TextView location;
        TextView distance;
        TextView status;
        ImageView img;
        OnLocationListener onLocationListener;

        public LocationHolder(@NonNull View itemView, OnLocationListener onLocationListener) {
            super(itemView);
            list_name = itemView.findViewById(R.id.location_name);
            location = itemView.findViewById(R.id.location_address);
            distance = itemView.findViewById(R.id.location_distance);
            img = itemView.findViewById(R.id.location_image);
            status = itemView.findViewById(R.id.location_status);
            this.onLocationListener = onLocationListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLocationListener.onLocationClick(getAdapterPosition());
        }
    }


    public Filter getTeaShopFilter() {
        return TeaShop_Filter;
    }

    private Filter TeaShop_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = locationModelListFull;
            } else {
                String filterPattern = Normalizer.normalize(constraint.toString().toLowerCase(), Normalizer.Form.NFD);
                List<LocationModel> filteredList = new ArrayList<>();
                for (LocationModel item : locationModelListFull) {
                    String tea_shop = Normalizer.normalize(item.isSeller().toLowerCase().trim(), Normalizer.Form.NFD);
                    if (tea_shop.contains(filterPattern)) {
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
            if (locationModelList.isEmpty()) {
                CharSequence text = "Không có kết quả phù hợp";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    };

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
            } else {
                String filterPattern = Normalizer.normalize(constraint.toString().toLowerCase(), Normalizer.Form.NFD);
                List<LocationModel> filteredList = new ArrayList<>();
                for (LocationModel item : locationModelListFull) {
                    String name = Normalizer.normalize(item.getName().toLowerCase().trim(), Normalizer.Form.NFD);
                    String location = Normalizer.normalize(item.getLocation().toLowerCase().trim(), Normalizer.Form.NFD);
                    if (name.contains(filterPattern) || location.contains(filterPattern)) {
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
            if (locationModelList.isEmpty()) {
                CharSequence text = "Không có kết quả phù hợp";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    };

    public interface OnLocationListener {
        void onLocationClick(int position);
    }

    public String getStatus(LocationModel mParam1){
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY)*100;
        String status;
        if(mParam1.getStart_hour()!=mParam1.getEnd_hour()){
            if(mParam1.getStart_hour()<=hour&&hour<=mParam1.getEnd_hour())
            {
                status = "Đang mở: "+" Từ: "+mParam1.getStart_hour()+" cho tới: "+ mParam1.getEnd_hour();
            }
            //if (mParam1.getStart_hour()>hour||hour>mParam1.getEnd_hour())
            else
                {
                    status = "Đang đóng "+" Từ: "+mParam1.getStart_hour()+" cho tới: "+ mParam1.getEnd_hour();
                }
        }
        else {status="Luôn mở";}

        return status;
    }
}
