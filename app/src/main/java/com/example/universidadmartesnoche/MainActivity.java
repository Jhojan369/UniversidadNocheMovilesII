package com.example.universidadmartesnoche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText jetEmail, jetPassword;
    Button jbtIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jetEmail = findViewById(R.id.etEmail);
        jetPassword = findViewById(R.id.etPassword);
        jbtIngresar = findViewById(R.id.btIngresar);
    }

    public void Ingresar(View view){
        String email = jetEmail.getText().toString();
        String pass = jetPassword.getText().toString();
        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Los campos son obligatorios.", Toast.LENGTH_SHORT).show();
        } else if (email.equals("cesde123") && pass.equals("1234")) {
            Intent next = new Intent(this, options.class);
            startActivity(next);
            jetEmail.setText("");
            jetPassword.setText("");
        }else{
            Toast.makeText(this, "Correo o contraseña incorrecta.", Toast.LENGTH_SHORT).show();
            jetEmail.setText("");
            jetPassword.setText("");
        }
    }
}