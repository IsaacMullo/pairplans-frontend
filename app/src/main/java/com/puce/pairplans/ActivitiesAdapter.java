package com.puce.pairplans;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>{
    Context context;
    List<Activity> ActivitiesList;
    AppCompatActivity appCompatActivity;
    APIActivities api;

    public ActivitiesAdapter(Context context, List<Activity> ActivitiesList) {
        this.context = context.getApplicationContext();
        this.appCompatActivity = (AppCompatActivity) context;
        this.ActivitiesList = ActivitiesList;
        RetrofitAdapter retrofitAdapter = new RetrofitAdapter();
        this.api = retrofitAdapter.getAdapter().create(APIActivities.class);
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_template, null, false);
        return new ActivitiesAdapter.ActivityViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = ActivitiesList.get(position);
        holder.ActivityNameTV.setText(ActivitiesList.get(position).getActivity());

        holder.ActivityTemplateDelete.setOnClickListener(v ->{
            showDeleteActivityDialog(position);
        });

        holder.ActivityTemplateEdit.setOnClickListener(v ->{
            showEditActivityDialog(position);
        });

    }

    @Override
    public int getItemCount() {
        return ActivitiesList.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        TextView ActivityNameTV;
        Button ActivityTemplateDelete, ActivityTemplateEdit;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            ActivityNameTV = itemView.findViewById(R.id.activityNameTV);
            ActivityTemplateDelete = itemView.findViewById(R.id.activityTemplateDelete);
            ActivityTemplateEdit = itemView.findViewById(R.id.activityTemplateEdit);
        }
    }

    private void removeActivity(String id, int position) {
        Call<Void> call = api.removeActivityI(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ActivitiesList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar producto", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Fallo al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editActivity(String id, String activityD, int position) {
        Activity activity =  ActivitiesList.get(position);
        activity.setActivity(activityD);

        Call<Void> call = api.updateActivityI(id, activity);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ActivitiesList.get(position).setActivity(activityD);
                    Toast.makeText(context, "Actividad editada correctamente", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "No se editó.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });


    }

    private void showDeleteActivityDialog(int position) {
        Dialog dialog = new Dialog(appCompatActivity);
        Activity activity = ActivitiesList.get(position);
        dialog.setContentView(R.layout.dialog_delete_activityp);
        Button CancelButtonDDA = dialog.findViewById(R.id.cancelButtonDDA);
        Button ConfirmButtonDDA = dialog.findViewById(R.id.confirmButtonDDA);

        CancelButtonDDA.setOnClickListener(v -> {
            dialog.dismiss();

        });

        ConfirmButtonDDA.setOnClickListener(v -> {
            removeActivity(activity.getId(), position);
            dialog.dismiss();
        });

        dialog.show();

    }

    private void showEditActivityDialog(int position) {
        Dialog dialog = new Dialog(appCompatActivity);
        dialog.setContentView(R.layout.dialog_edit_activityp);
        Activity activity = ActivitiesList.get(position);

        EditText InputEditTextDEA = dialog.findViewById(R.id.inputEditTextDEA);
        Button CancelButtonDEA = dialog.findViewById(R.id.cancelButtonDEA);
        Button EditButtonDEA = dialog.findViewById(R.id.editButtonDEA);

        CancelButtonDEA.setOnClickListener(v -> {

            dialog.dismiss();

        });

        EditButtonDEA.setOnClickListener(v -> {
            String inputEA = InputEditTextDEA.getText().toString();
            if (!inputEA.isEmpty()) {
                editActivity(activity.getId(), inputEA, position);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Ingresa la actividad por favor", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}
