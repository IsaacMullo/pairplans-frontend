package com.puce.pairplans;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonActivitiesListActivity extends AppCompatActivity {
    List<Activity> ActivitiesList = new ArrayList<>();
    RecyclerView ActivitiesListRV;
    Button AddActivitiesBTN;
    Retrofit retrofit;
    APIActivities api;
    ActivitiesAdapter ActivitiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_person_activities_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivitiesListRV = findViewById(R.id.activitiesListRV);
        ActivitiesListRV.setLayoutManager(new GridLayoutManager(this, 1));
        ActivitiesAdapter = new ActivitiesAdapter(this, ActivitiesList);
        ActivitiesListRV.setAdapter(ActivitiesAdapter);
        AddActivitiesBTN = findViewById(R.id.addActivitiesBTN);
        retrofit = new RetrofitAdapter().getAdapter();
        api = retrofit.create(APIActivities.class);

        String person = getIntent().getStringExtra("person_id");
        getActivities(api, person);

        AddActivitiesBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showAddActivityDialog(person);
            };
        });

    }

    private void getActivities(APIActivities api, String person) {
        ActivitiesList.clear();
        Call<List<Activity>> call = api.getActivitiesI(person);

        call.enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ActivitiesList = new ArrayList<>(response.body());
                    ActivitiesAdapter = new ActivitiesAdapter(PersonActivitiesListActivity.this, ActivitiesList);
                    ActivitiesListRV.setAdapter(ActivitiesAdapter);
                } else {
                    Log.e("API Error", "Response not successful or is empty");
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                Log.e("TAG", "Error ", t);
            }
        });
    }

    public void addActivity(final APIActivities api,String personId, String activityD) {
        Activity activity = new Activity();

        activity.setActivity(activityD);
        activity.setPerson(personId);

        Call<Void> call = api.addActivtyI(activity);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("TAG", "Codigo "+response.code());
                Log.d("TAG", "Body "+response.body());
                Log.d("TAG", "Error Body "+response.errorBody());
                Log.d("TAG", "Mensaje "+response.message());
                Log.d("TAG", "RAW "+response.raw());
                Log.d("TAG", "Headers "+response.headers());

                if (response.isSuccessful()) {
                    Toast.makeText(PersonActivitiesListActivity.this, "Se agregó correctamente :D", Toast.LENGTH_SHORT).show();
                    getActivities(api, personId);
                } else {
                    Toast.makeText(PersonActivitiesListActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG", "Error al agregar "+t);
            }
        });
    }

    private void showAddActivityDialog(String person_id) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_activityp);

        EditText inputEditTextA = dialog.findViewById(R.id.inputEditTextA);
        Button submitButtonA = dialog.findViewById(R.id.sendButtonDAA);
        Button cancelButtonA = dialog.findViewById(R.id.cancelButtonDAA);

        cancelButtonA.setOnClickListener(v -> {
            inputEditTextA.setText("");
            dialog.dismiss();
        });

        submitButtonA.setOnClickListener(v -> {
            String input = inputEditTextA.getText().toString();
            if (!input.isEmpty()) {
                addActivity(api, person_id, input);
                dialog.dismiss();
            } else {
                Toast.makeText(PersonActivitiesListActivity.this, "Ingresa la actividad por favor", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
