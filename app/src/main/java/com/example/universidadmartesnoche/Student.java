package com.example.universidadmartesnoche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends AppCompatActivity{

    EditText etcarnet,etnombre,etsemestre;
    TextView etcarrera;
    Spinner spinnerMaterias;
    CheckBox cbactivo;
    Button btadicionar,btmodificar,btanular,bteliminar;
    String carnet,nombre,carrera,semestre,coleccion="Estudiante", clave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        etcarnet=findViewById(R.id.etcarnet);
        etnombre=findViewById(R.id.etnombre);
        etcarrera=findViewById(R.id.etcarrera);
        etsemestre=findViewById(R.id.etsemestre);
        cbactivo=findViewById(R.id.cbactivo);
        btadicionar=findViewById(R.id.btadicionar);
        btanular=findViewById(R.id.btanular);
        bteliminar=findViewById(R.id.bteliminar);
        btmodificar=findViewById(R.id.btmodificar);
        spinnerMaterias = findViewById(R.id.spinnerMaterias);
        etcarnet.requestFocus();
    }//fin metodo onCreate

           /* public void ListMaterias(String studentId) {
             DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Student").child(studentId);

            studentRef.child("subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> listaDeAsignaturas = new ArrayList<>();

                for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {
                    String subjectId = subjectSnapshot.getKey();
                    listaDeAsignaturas.add(subjectId);
                }
            }
                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                }
            });
    }*/

    public void ListMaterias(String studentId){
        //Recuperar datos de la colección "subject"
        db.collection("Materias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Crear una lista de materias
                            List<String> subjectList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String subjectName = document.getString("nombre"); // Reemplaza "nombre" con el campo real en tu colección
                                subjectList.add(subjectName);
                            }
                            //Mostrar la lista en una lista desplegable
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_spinner_item, subjectList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Spinner spinner = findViewById(R.id.spinnerMaterias);
                            spinner.setAdapter(adapter);
                        } else {
                            //Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void Adicionar(View view){
        //Pasar la informacion de los objetos a variables
        carnet=etcarnet.getText().toString();
        nombre=etnombre.getText().toString();
        carrera=etcarrera.getText().toString();
        //ListarMaterias(etcarrera.getText().toString());
        semestre=etsemestre.getText().toString();
        //Validar que se ingreso toda la informacion
        if (!carnet.isEmpty() && !nombre.isEmpty() && !carrera.isEmpty() && !semestre.isEmpty()){
            // Create a new student with a first and last name
            Map<String, Object> alumno = new HashMap<>();
            alumno.put("Carnet", carnet);
            alumno.put("Nombre", nombre);
            alumno.put("Carrera", carrera);
            alumno.put("Semestre", semestre);
            alumno.put("Activo", "Si");
// Add a new document with a generated ID
            db.collection(coleccion)
                    .add(alumno)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(Student.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                            //Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(Student.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(Student.this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo Adicionar

    public void Modificar(View view){
        //Pasar la informacion de los objetos a variables
        carnet=etcarnet.getText().toString();
        nombre=etnombre.getText().toString();
        carrera=etcarrera.getText().toString();
        //ListarMaterias(etcarrera.getText().toString());
        semestre=etsemestre.getText().toString();
        //Validar que se ingreso toda la informacion
        if (!carnet.isEmpty() && !nombre.isEmpty() && !carrera.isEmpty() && !semestre.isEmpty()){
            // Modify a new student with a first and last name
            Map<String, Object> alumno = new HashMap<>();
            alumno.put("Carnet", carnet);
            alumno.put("Nombre", nombre);
            alumno.put("Carrera", carrera);
            alumno.put("Semestre", semestre);
            if (cbactivo.isChecked())
                alumno.put("Activo","Si");
            else
                alumno.put("Activo","No");
            db.collection(coleccion).document(clave)
                    .set(alumno)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Student.this,"Documento actualizado ...",Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Student.this,"Error actualizando documento...",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(Student.this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo modificar

    public void Eliminar(View view){
        db.collection(coleccion).document(clave)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Limpiar_campos();
                        Toast.makeText(Student.this,"Documento eliminado ...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Student.this,"Error eliminando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//fin metodo eliminar

    public void Anular(View view){
        Map<String, Object> alumno = new HashMap<>();
        alumno.put("Carnet", carnet);
        alumno.put("Nombre", nombre);
        alumno.put("Carrera", carrera);
        alumno.put("Semestre", semestre);
        alumno.put("Activo","No");
        db.collection(coleccion).document(clave)
                .set(alumno)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Student.this,"Documento anulado ...",Toast.LENGTH_SHORT).show();
                        Limpiar_campos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Student.this,"Error anulando documento...",Toast.LENGTH_SHORT).show();
                    }
                });
    }//Fin metodo Anular

    public void Consultar(View view){
        Consultar_documento();
    }//Fin metodo Consultar

    private void Consultar_documento(){
        carnet=etcarnet.getText().toString();
        if (!carnet.isEmpty()){
            db.collection(coleccion)
                    .whereEqualTo("Carnet",carnet)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() != 0){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                                        clave=document.getId();
                                        etnombre.setText(document.getString("Nombre"));
                                        etcarrera.setText(document.getString("Carrera"));
                                        etsemestre.setText(document.getString("Semestre"));
                                        if (document.getString("Activo").equals("Si"))
                                            cbactivo.setChecked(true);
                                        else
                                            cbactivo.setChecked(false);
                                    }
                                    cbactivo.setEnabled(true);
                                    btmodificar.setEnabled(true);
                                    btanular.setEnabled(true);
                                    bteliminar.setEnabled(true);
                                }else{
                                    Toast.makeText(Student.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                                    btadicionar.setEnabled(true);
                                }
                                etcarnet.setEnabled(false);
                                etnombre.setEnabled(true);
                                etcarrera.setEnabled(true);
                                etsemestre.setEnabled(true);
                                etnombre.requestFocus();
                            } else {
                                // Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(Student.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(Student.this, "Carnet es requerido", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }
    }//Fin metodo Consultar_documento

    public void Volver(View view){

        Intent intent = new Intent(Student.this, options.class);
        startActivity(intent);
    }

    /*private void ListarMaterias(String nombreMateria){
        try {
            final List<ListSubjects> materiasList = new ArrayList<>();
            mDatabase.child("Codigo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            String codigo = ds.getKey();
                            String nombreMateria = ds.child("NombreMateria").getValue().toString();
                            materiasList.add(new ListSubjects(codigo, nombreMateria));
                        }
                        System.out.println(dataSnapshot);
                        ArrayAdapter<ListSubjects> arrayAdapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_dropdown_item_1line);
                        spinnerMaterias.setAdapter(arrayAdapter);
                        spinnerMaterias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                materiaElejida = adapterView.getItemAtPosition(i).toString();
                                etcarrera.setText("Nombre" + materiaElejida);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                }
            });
        }catch (Exception e){
            Intent intent = new Intent(this, options.class);
            startActivity(intent);
        }
    }//Fin listar materias */

    /*    private void ObtenerMaterias(String codigoMateria){
        dataSnapshot.child("Subjects").child("NombreMateria").getValue().toString();
        List<ListSubjects> subjectsList = new ArrayList<>();
        ArrayAdapter<ListSubjects> arrayAdapter = new ArrayAdapter<>(Student.this, android.R.layout.simple_dropdown_item_1line);
        if (subjectsList.size() != 0){
            for(DataSnapshot dataSnapshot : dataSnapshot.getChildren()){
                spinnerMaterias.setAdapter(arrayAdapter);
            }
        }
    }*/

    private void Limpiar_campos(){
        etcarnet.setText("");
        etnombre.setText("");
        etcarrera.setText("");
        etsemestre.setText("");
        cbactivo.setChecked(false);
        etcarnet.setEnabled(true);
        etcarnet.requestFocus();
        etnombre.setEnabled(false);
        etcarrera.setEnabled(false);
        etsemestre.setEnabled(false);
        cbactivo.setEnabled(false);
        btmodificar.setEnabled(false);
        bteliminar.setEnabled(false);
        btanular.setEnabled(false);
        btadicionar.setEnabled(false);
    }//fin metodo Limpiar_campos

}