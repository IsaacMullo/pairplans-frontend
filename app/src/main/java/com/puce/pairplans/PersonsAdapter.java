package com.puce.pairplans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonsAdapter extends ArrayAdapter<Person> {
    List<Person> PersonsList;
    AppCompatActivity appCompatActivity;

    PersonsAdapter(AppCompatActivity context, List<Person> PersonsList) {
        super(context, R.layout.person_template, PersonsList);
        this.appCompatActivity = context;
        this.PersonsList = PersonsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.person_template, null);

        TextView textView1 = item.findViewById(R.id.personTemplateTV);
        textView1.setText(PersonsList.get(position).getName());

        return item;
    }
}

