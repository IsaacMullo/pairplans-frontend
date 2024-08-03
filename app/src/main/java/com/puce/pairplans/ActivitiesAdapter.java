package com.puce.pairplans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>{
    Context context;
    List<Activity> ActivitiesList;

    public ActivitiesAdapter(Context context, List<Activity> ActivitiesList) {
        this.context = context;
        this.ActivitiesList = ActivitiesList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_template, null, false);
        return new ActivitiesAdapter.ActivityViewHolder(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.ActivityNameTV.setText(ActivitiesList.get(position).getActivity());

    }
    @Override
    public int getItemCount() {
        return ActivitiesList.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        TextView ActivityNameTV;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            ActivityNameTV = itemView.findViewById(R.id.activityNameTV);

        }
    }
}
