package com.puce.pairplans;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button AddPersonButton, GuideButtonP;
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
        GuideButtonP = findViewById(R.id.guideButtonP);

        PersonsListLV = (ListView) findViewById(R.id.personsListLV);
        PersonsAdapter adapter = new PersonsAdapter(this, PersonsList);

        PersonsListLV.setAdapter(adapter);
        retrofit = new RetrofitAdapter().getAdapter();
        api = retrofit.create(APIPersons.class);
        getPersons(api);




            AddPersonButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    showAddDialog();
                };
            });

        GuideButtonP.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showGuideDialog();
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

    public void addPerson(final APIPersons api, String name) {
        Person person = new Person();
        person.setName(name);

        Call<Void> call = api.addPersonI(person);
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
                    Toast.makeText(PersonsListActivity.this, "Se agregó correctamente :D", Toast.LENGTH_SHORT).show();
                    getPersons(api);
                } else {
                    Toast.makeText(PersonsListActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG", "Error al agregar "+t);
            }
        });
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_person);

        EditText inputEditText = dialog.findViewById(R.id.inputEditText);
        Button submitButton = dialog.findViewById(R.id.sendButtonDA);
        Button cancelButtonP = dialog.findViewById(R.id.cancelButtonDA);

        cancelButtonP.setOnClickListener(v -> {
            inputEditText.setText("");
            dialog.dismiss();
        });

        submitButton.setOnClickListener(v -> {
            String input = inputEditText.getText().toString();
            if (!input.isEmpty()) {
                addPerson(api, input);
                dialog.dismiss();
            } else {
                Toast.makeText(PersonsListActivity.this, "Ingresa el nombre/apodo por favor", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void showGuideDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_guide_p);
        dialog.show();
    }


}