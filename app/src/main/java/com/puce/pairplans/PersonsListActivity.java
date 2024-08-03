package com.puce.pairplans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonsListActivity extends AppCompatActivity {
    Button AddPersonButton;
    List<Person> PersonsList = new ArrayList<>();
    ListView PersonsListLV;
    Retrofit retrofit;
    APIPersons api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_persons_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AddPersonButton = findViewById(R.id.addPersonButton);
        PersonsListLV = (ListView) findViewById(R.id.personsListLV);
        PersonsAdapter adapter = new PersonsAdapter(this, PersonsList);

        PersonsListLV.setAdapter(adapter);
        retrofit = new RetrofitAdapter().getAdapter();
        api = retrofit.create(APIPersons.class);
        getPersons(api);


        PersonsListLV.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterview, View view, int i, long l){
                Person selectedPerson = PersonsList.get(i);
                Intent intent = new Intent(PersonsListActivity.this, PersonActivitiesListActivity.class);
                intent.putExtra("person_id", selectedPerson.getId());
                startActivity(intent);
            };
        });

    }


    private void getPersons(APIPersons api) {
        PersonsList.clear();
        Call<List<Person>> call = api.getPersonsI();

        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PersonsList = new ArrayList<>(response.body());
                    PersonsAdapter adapter = new PersonsAdapter(PersonsListActivity.this, PersonsList);
                    PersonsListLV.setAdapter(adapter);
                } else {

                    Log.e("API Error", "Response not successful or is empty");
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Log.e("TAG","Error ",t);
            }
        });
    }

}