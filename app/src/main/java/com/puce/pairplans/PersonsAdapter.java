package com.puce.pairplans;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonsAdapter extends ArrayAdapter<Person> {
    List<Person> PersonsList;
    AppCompatActivity appCompatActivity;
    Context context;
    APIPersons api;

    PersonsAdapter(AppCompatActivity context, List<Person> PersonsList) {
        super(context, R.layout.person_template, PersonsList);
        this.appCompatActivity = context;
        this.context = context.getApplicationContext();
        this.PersonsList = PersonsList;
        RetrofitAdapter retrofitAdapter = new RetrofitAdapter();
        this.api = retrofitAdapter.getAdapter().create(APIPersons.class);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.person_template, parent, false);

        TextView textView1 = item.findViewById(R.id.personTemplateTV);
        Button PersonTemplateDelete = item.findViewById(R.id.personTemplateDelete);
        Button PersonTemplateEdit = item.findViewById(R.id.personTemplateEdit);


        Person person = PersonsList.get(position);
        textView1.setText(person.getName());

        item.setOnClickListener(v -> {
            Intent intent = new Intent(appCompatActivity, PersonActivitiesListActivity.class);
            intent.putExtra("person_id", person.getId());
            appCompatActivity.startActivity(intent);
        });


        PersonTemplateDelete.setOnClickListener(v -> {
            showDeleteDialog(position);
        });

        PersonTemplateEdit.setOnClickListener(v -> {
            showEditDialog(position);
        });

        return item;
    }


    private void removePerson(String id, int position) {
        Call<Void> call = api.removePersonI(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    PersonsList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Persona eliminada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar persona", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Fallo al conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editPerson(String id, String name, int position) {
        Person person = new Person();
        person.setName(name);


        Call<Void> call = api.updatePersonI(id, person);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    PersonsList.get(position).setName(name);
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


    private void showDeleteDialog(int position) {
        Dialog dialog = new Dialog(appCompatActivity);
        Person person = PersonsList.get(position);
        dialog.setContentView(R.layout.dialog_delete_person);
        Button CancelButtonDD = dialog.findViewById(R.id.cancelButtonDD);
        Button ConfirmButtonDD = dialog.findViewById(R.id.confirmButtonDD);

        CancelButtonDD.setOnClickListener(v -> {
            dialog.dismiss();

        });

        ConfirmButtonDD.setOnClickListener(v -> {
            removePerson(person.getId(), position);
            dialog.dismiss();

        });

        dialog.show();

    }



    private void showEditDialog(int position) {
        Dialog dialog = new Dialog(appCompatActivity);
        dialog.setContentView(R.layout.dialog_edit_person);
        Person person = PersonsList.get(position);

        EditText InputEditTextDE = dialog.findViewById(R.id.inputEditTextDE);
        Button CancelButtonDE = dialog.findViewById(R.id.cancelButtonDE);
        Button ConfirmButtonDE = dialog.findViewById(R.id.confirmButtonDE);

        CancelButtonDE.setOnClickListener(v -> {

            dialog.dismiss();

        });

        ConfirmButtonDE.setOnClickListener(v -> {
            String inputE = InputEditTextDE.getText().toString();
            if (!inputE.isEmpty()) {
                editPerson(person.getId(), inputE, position);
                Toast.makeText(context, "Nombre editado correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Ingresa el nuevo nombre por favor", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}

