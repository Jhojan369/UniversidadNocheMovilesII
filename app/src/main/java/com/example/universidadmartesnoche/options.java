package com.example.universidadmartesnoche;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class options extends AppCompatActivity {

    Button jbtMaterias, jbtEstudiantes;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        jbtEstudiantes = findViewById(R.id.btEstudiantes);
        jbtMaterias = findViewById(R.id.btMaterias);
    }

    public void IrEstudiantes(View view){
        Intent intent = new Intent(this, Student.class);
        startActivity(intent);
    }

    public void IrMaterias(View view){
        Intent intent = new Intent(this, Subjects.class);
        startActivity(intent);
    }

    public void Salir(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}