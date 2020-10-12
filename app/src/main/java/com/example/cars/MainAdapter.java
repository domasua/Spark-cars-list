package com.example.cars;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cars.model.CarsList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements Filterable {

    //Initialize variable
    private List<CarsList> dataArrayList;
    // Search
    private List<CarsList> dataArrayListAll;
    private Activity activity;

    //Create constructor
    public MainAdapter(Activity activity, List<CarsList> dataArrayList){
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        // Search new array
        this.dataArrayListAll = new ArrayList<>(dataArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Initialize Cars list
        CarsList data = dataArrayList.get(position);

        // Set image on image view
        Glide.with(activity).load(data.getModel().getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        // Set name on text view
        holder.textViewModel.setText("Model: " + data.getModel().getTitle());

        // Set plate number in text view
        holder.textViewPlateNumber.setText("Plate: " + data.getPlateNumber());

        //set battery
        holder.textViewBattery.setText("Battery: " + data.getBatteryPercentage() + "%");
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        // filter methode run in background
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            // new array list for filtered list CarList
            List<CarsList> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(dataArrayListAll);
            }
            else {
                for (CarsList cars : dataArrayListAll){
                    if (cars.getModel().getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(cars);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }
        // runs on a UI
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataArrayList.clear();
            dataArrayList.addAll((Collection<? extends CarsList>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize variables
        ImageView imageView;
        TextView textViewModel;
        TextView textViewPlateNumber;
        TextView textViewBattery;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign variable
            imageView = itemView.findViewById(R.id.image_view);
            textViewModel = itemView.findViewById(R.id.text_view_model);
            textViewPlateNumber = itemView.findViewById(R.id.text_view_plate);
            textViewBattery = itemView.findViewById(R.id.text_View_battery);
        }
    }
}
