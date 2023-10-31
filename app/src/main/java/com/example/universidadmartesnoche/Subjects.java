package com.example.universidadmartesnoche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Subjects extends AppCompatActivity {
    EditText jetCodigo, jetnombre, jetCreditos;
    CheckBox jcbactivo;
    Button jbtbuscar,jbtadicionar,jbtmodificar,jbtanular,jbteliminar;
    String codigo,nombreMateria,credito,coleccion="Materias", clave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        jetCodigo = findViewById(R.id.etCodigo);
        jetnombre = findViewById(R.id.etnombre);
        jetCreditos = findViewById(R.id.etCreditos);
        jcbactivo = findViewById(R.id.cbactivo);
        jbtbuscar = findViewById(R.id.btbuscar);
        jbtadicionar = findViewById(R.id.btadicionar);
        jbtmodificar = findViewById(R.id.btmodificar);
        jbtanular = findViewById(R.id.btanular);
        jbteliminar = findViewById(R.id.bteliminar);
        jetCodigo.requestFocus();
    }//Fin oncreate

    public void ConsultarMateria(View view){
        Consultar_materia();
    }//Fin metodo Consultar

    public void AdicionarMateria(View view){
        //Pasar la informacion de los objetos a variables
        codigo=jetCodigo.getText().toString();
        nombreMateria=jetnombre.getText().toString();
        credito=jetCreditos.getText().toString();
        //Validar que se ingreso toda la informacion
        if (!codigo.isEmpty() && !nombreMateria.isEmpty() && !credito.isEmpty()){
            // Create a new student with a first and last name
            Map<String, Object> materia = new HashMap<>();
            materia.put("Codigo", codigo);
            materia.put("NombreMateria", nombreMateria);
            materia.put("Credito", credito);
            materia.put("Activo", "Si");
            // Add a new document with a generated ID
            db.collection(coleccion)
                    .add(materia)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(Subjects.this, "Materia guardado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(Subjects.this, "Materia no hallado", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetCodigo.requestFocus();
        }
    }//Fin metodo Adicionar

    public void ModificarMateria(View view){
        //Pasar la informacion de los objetos a variables
        codigo=jetCodigo.getText().toString();
        nombreMateria=jetnombre.getText().toString();
        credito=jetCreditos.getText().toString();
        //Validar que se ingreso toda la informacion
        if (!codigo.isEmpty() && !nombreMateria.isEmpty() && !credito.isEmpty()){
            // Modify a new student with a first and last name
            Map<String, Object> materia = new HashMap<>();
            materia.put("Codigo", codigo);
            materia.put("NombreMateria", nombreMateria);
            materia.put("Credito", credito);
            if (jcbactivo.isChecked())
                materia.put("Activo","Si");
            else
                materia.put("Activo","No");
            db.collection(coleccion).document(clave)
                    .set(materia)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Subjects.this,"Documento actualizado ...",Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Subjects.this,"Error actualizando documento...",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetCodigo.requestFocus();
        }
    }//Fin metodo modificar

    public void EliminarMateria(View view){
        db.collection(coleccion).document(clave)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Limpiar_campos();
                        Toast.makeText(Subjects.this,"Documento eliminado ...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Subjects.this,"Error eliminando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//fin metodo eliminar

    public void AnularMateria(View view){
        Map<String, Object> materia = new HashMap<>();
        materia.put("Codigo", codigo);
        materia.put("NombreMateria", nombreMateria);
        materia.put("Credito", credito);
        materia.put("Activo","No");
        db.collection(coleccion).document(clave)
                .set(materia)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Subjects.this,"Documento anulado ...",Toast.LENGTH_SHORT).show();
                        Limpiar_campos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Subjects.this,"Error anulando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//Fin metodo Anular

    public void Volver(View view){

        Intent intent = new Intent(this, options.class);
        startActivity(intent);
    }

    private void Consultar_materia(){
        codigo=jetCodigo.getText().toString();
        if (!codigo.isEmpty()){
            db.collection(coleccion)
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave=document.getId();
                                        jetnombre.setText(document.getString("NombreMateria"));
                                        jetCreditos.setText(document.getString("Credito"));
                                        if (document.getString("Activo").equals("Si"))
                                            jcbactivo.setChecked(true);
                                        else
                                            jcbactivo.setChecked(false);
                                    }
                                    jcbactivo.setEnabled(true);
                                    jbtmodificar.setEnabled(true);
                                    jbtanular.setEnabled(true);
                                    jbteliminar.setEnabled(true);
                                }else{
                                    Toast.makeText(Subjects.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                                    jbtadicionar.setEnabled(true);
                                }
                                jetCodigo.setEnabled(false);
                                jetnombre.setEnabled(true);
                                jetCreditos.setEnabled(true);
                                jetnombre.requestFocus();
                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(Subjects.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Carnet es requerido", Toast.LENGTH_SHORT).show();
            jetCodigo.requestFocus();
        }
    }//Fin metodo Consultar_documento

    private void Limpiar_campos(){
        jetCodigo.setText("");
        jetnombre.setText("");
        jetCreditos.setText("");
        jcbactivo.setChecked(false);
        jetCodigo.setEnabled(true);
        jetCodigo.requestFocus();
        jetnombre.setEnabled(false);
        jetCreditos.setEnabled(false);
        jcbactivo.setEnabled(false);
        jbtmodificar.setEnabled(false);
        jbteliminar.setEnabled(false);
        jbtanular.setEnabled(false);
        jbtadicionar.setEnabled(false);
    }//fin metodo Limpiar_campos
}