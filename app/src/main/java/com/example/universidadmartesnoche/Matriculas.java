package com.example.universidadmartesnoche;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Matriculas extends AppCompatActivity {

    EditText jetCodigoMatricula, jetcodMateria, jetCodigoEstudiante;
    TextView jtvcarrera, jtvnommateria, jtvnombreEstudiante, jtvFecha, jtvCreditos;
    CheckBox jcbactivo;
    Button jbtadicionar, jbtanular, jbtBuscarMatricula, jbtBuscarEstudiante, jbtBuscarMateria, jbtLimpiar, jbtVolver, jbtFecha;
    String nomEstudiante, nomMateria, carrera, codMateria, codMatricula, codEstudiante, coleccion="Matriculas", clave, fecha;
    private int year, mes, dia;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matriculas);
        jetCodigoMatricula=findViewById(R.id.etcodigoMatriculas);
        jtvFecha=findViewById(R.id.etfecha);
        jetCodigoEstudiante=findViewById(R.id.etcarnet);
        jtvnombreEstudiante=findViewById(R.id.tvnombreEstudiante);
        jetcodMateria=findViewById(R.id.etcodmateria);
        jtvnommateria=findViewById(R.id.tvnommateria);
        jtvCreditos =findViewById(R.id.tvcreditos);
        jtvcarrera=findViewById(R.id.tvcarrera);
        jcbactivo=findViewById(R.id.cbactivo);
        jbtadicionar=findViewById(R.id.btadicionar);
        jbtanular=findViewById(R.id.btanular);
        jbtBuscarMatricula=findViewById(R.id.btbuscarmat);
        jbtBuscarEstudiante=findViewById(R.id.btbuscarcar);
        jbtBuscarMateria=findViewById(R.id.btbuscarmateria);
        jbtLimpiar=findViewById(R.id.btlimpiar);
        jbtVolver=findViewById(R.id.btlimpiar);
        jbtFecha=findViewById(R.id.btFecha);

        jbtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(Matriculas.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        jtvFecha.setText(year + "/" + (month) + "/" + dayOfMonth);
                    }
                }, year, mes, dia);
                datePicker.show();
                jetCodigoEstudiante.requestFocus();
            }
        });
    }

    public void AgregarMatricula(View view){
        //Pasar la informacion de los objetos a variables
        carrera=jtvcarrera.getText().toString();
        fecha=jtvFecha.getText().toString();
        codMatricula=jetCodigoMatricula.getText().toString();
        codMateria=jetcodMateria.getText().toString();
        codEstudiante=jetCodigoEstudiante.getText().toString();
        //Validar que se ingreso toda la informacion
        if (!codMatricula.isEmpty() && !codMateria.isEmpty() && !codEstudiante.isEmpty() ){
            // Create a new student with a first and last name
            Map<String, Object> matricula = new HashMap<>();
            matricula.put("CodigoMatricula", codMatricula);
            matricula.put("Fecha", fecha);
            matricula.put("Carnet", codEstudiante);
            matricula.put("Carrera", carrera);
            matricula.put("Codigo", codMateria);
            matricula.put("Activo", "Si");

            jetCodigoMatricula.setEnabled(false);
            jbtadicionar.setEnabled(true);
// Add a new document with a generated ID
            db.collection(coleccion)
                    .add(matricula)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(Matriculas.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(Matriculas.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(Matriculas.this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetCodigoMatricula.requestFocus();
        }
    }

    public void ConsultarMatricula(View view){
        Consultar_matricula();
    }

    private void Consultar_matricula(){
        codMatricula=jetCodigoMatricula.getText().toString();
        if (!codMatricula.isEmpty()){
            db.collection("Matriculas")
                    .whereEqualTo("CodigoMatricula",codMatricula)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave=document.getId();
                                        jtvFecha.setText(document.getString("Fecha"));
                                        jetCodigoEstudiante.setText(document.getString("Carnet"));
                                        //jtvnombreEstudiante.setText(document.getString("Nombre"));
                                        Consultar_documento_estudiante();
                                        jetcodMateria.setText(document.getString("Codigo"));
                                        Consultar_codigo_materia();
                                        if (document.getString("Activo").equals("Si"))
                                            jcbactivo.setChecked(true);
                                        else
                                            jcbactivo.setChecked(false);
                                    }
                                    jcbactivo.setEnabled(true);
                                    jetCodigoEstudiante.setEnabled(false);
                                    jtvFecha.setEnabled(false);
                                    jetcodMateria.setEnabled(false);
                                    jbtBuscarEstudiante.setEnabled(true);
                                    jbtBuscarMateria.setEnabled(true);
                                    jbtLimpiar.setEnabled(true);
                                    jbtanular.setEnabled(true);
                                } else {
                                    Toast.makeText(Matriculas.this, "Matricula no encontrada", Toast.LENGTH_SHORT).show();
                                    jetCodigoEstudiante.setEnabled(true);
                                    jetcodMateria.setEnabled(true);
                                    jetCodigoEstudiante.setText("");
                                    jtvnombreEstudiante.setText("");
                                    jtvcarrera.setText("");
                                    jetcodMateria.setText("");
                                    jtvCreditos.setText("");
                                    jtvnommateria.setText("");
                                    jbtBuscarEstudiante.setEnabled(true);
                                    jbtBuscarMateria.setEnabled(true);
                                    jbtadicionar.setEnabled(true);
                                    jbtanular.setEnabled(false);
                                    jetCodigoEstudiante.requestFocus();
                                }
                            }
                        }
                    });
        }else{
            Toast.makeText(Matriculas.this, "Codigo de matricula es requerido", Toast.LENGTH_SHORT).show();
            jetCodigoMatricula.requestFocus();
        }
    }//Fin metodo Consultar_documento

    public void AnularMateria(View view) {
        Map<String, Object> matricula = new HashMap<>();
        matricula.put("CodigoMatricula", codMatricula);
        matricula.put("Fecha", fecha);
        matricula.put("Carnet", codEstudiante);
        matricula.put("Carrera", carrera);
        matricula.put("Codigo", codMateria);
        matricula.put("Activo", "No");
        db.collection("Matriculas").document(clave)
                .set(matricula)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Matriculas.this,"Documento anulado ...",Toast.LENGTH_SHORT).show();
                        Limpiar_campos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Matriculas.this,"Error anulando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Consultar_estudiante(View view) {
        Consultar_documento_estudiante();
    }
    private void Consultar_documento_estudiante(){

        codEstudiante=jetCodigoEstudiante.getText().toString();
        if (!codEstudiante.isEmpty()){
            db.collection("Estudiante")
                    .whereEqualTo("Carnet",codEstudiante)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave=document.getId();
                                        jtvnombreEstudiante.setText(document.getString("Nombre"));
                                        jtvcarrera.setText(document.getString("Carrera"));
                                    }
                                } else {
                                    Toast.makeText(Matriculas.this, "Estudiante no hallado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(Matriculas.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(Matriculas.this, "Carnet es requerido", Toast.LENGTH_SHORT).show();
            jetCodigoEstudiante.setText("");
            jetCodigoEstudiante.requestFocus();
        }
    }//Fin metodo Consultar_documento_estudiante

    public void Consultar_materia(View view) {
        Consultar_codigo_materia();
    }
    private void Consultar_codigo_materia(){

        codMateria=jetcodMateria.getText().toString();
        if (!codMateria.isEmpty()){
            db.collection("Materias")
                    .whereEqualTo("Codigo",codMateria)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave=document.getId();
                                        jtvnommateria.setText(document.getString("NombreMateria"));
                                        jtvCreditos.setText(document.getString("Credito"));
                                    }
                                } else {
                                    Toast.makeText(Matriculas.this, "Materia no encotrada", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(Matriculas.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(Matriculas.this, "Codigo de materia requerido es requerido", Toast.LENGTH_SHORT).show();
            jetcodMateria.setText("");
            jetcodMateria.requestFocus();
        }
    }//Fin metodo Consultar_documento_estudiante

    public void Volver(View view){

        Intent intent = new Intent(Matriculas.this, options.class);
        startActivity(intent);
    }

    public void Limpiar(View view) {
        Limpiar_campos();
    }
    private void Limpiar_campos(){
        jetCodigoMatricula.setText("");
        jtvFecha.setText("");
        jetCodigoEstudiante.setText("");
        jtvnombreEstudiante.setText("");
        jtvcarrera.setText("");
        jetcodMateria.setText("");
        jtvCreditos.setText("");
        jtvnommateria.setText("");
        jcbactivo.setChecked(false);
        jetCodigoMatricula.setEnabled(true);
        jetCodigoMatricula.requestFocus();
        jetCodigoEstudiante.setEnabled(false);
        jetcodMateria.setEnabled(false);
        jcbactivo.setEnabled(false);
        jbtadicionar.setEnabled(false);
        jbtanular.setEnabled(false);
        jbtBuscarMatricula.setEnabled(true);
        jbtBuscarEstudiante.setEnabled(false);
        jbtBuscarMateria.setEnabled(false);
        jbtLimpiar.setEnabled(false);

    }//fin metodo Limpiar_campos
}