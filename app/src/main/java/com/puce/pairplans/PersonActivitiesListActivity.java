package com.puce.pairplans;

import android.os.Bundle;
import android.util.Log;
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
        retrofit = new RetrofitAdapter().getAdapter();
        api = retrofit.create(APIActivities.class);

        String person = getIntent().getStringExtra("person_id");
        getActivities(api, person);
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
}
