package mx.androssapps.crud;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import mx.androssapps.crud.modelo.Persona;

public class MainActivity extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtTelefono;
    Button btnAgregar, btnEliminar, btnModificar;
    String nombre = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference personaRef = db.collection("agenda");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNombre = this.findViewById(R.id.edtNombre);
        edtApellido= this.findViewById(R.id.edtApellido);
        edtTelefono = this.findViewById(R.id.edtTelefono);

        btnAgregar = (Button) this.findViewById(R.id.btnAgregar);
        btnEliminar = (Button) this.findViewById(R.id.btnEliminar);
        btnModificar = (Button) this.findViewById(R.id.btnModificar);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nombrePersona = edtNombre.getText().toString();
                final String apellidoPersona = edtApellido.getText().toString();
                final String telefonoPersona = edtTelefono.getText().toString();

                DocumentReference personaRef = db.collection("agenda").document();

                Map<String, Object> nuevaPersona = new HashMap<>();
                nuevaPersona.put("nombre",nombrePersona);
                nuevaPersona.put("apellido",apellidoPersona);
                nuevaPersona.put("telefono",telefonoPersona);
                nuevaPersona.put("idPersona", personaRef.getId());

                db.collection("agenda").document(personaRef.getId()).set(nuevaPersona)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "¡" + nombrePersona+ " ha sido registrado",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "No se pudo realizar la operación.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarPersona();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                buscarPersona();
                final String apellidoPersona = edtApellido.getText().toString();
                final String telefonoPersona = edtTelefono.getText().toString();

                Map<String, Object> editarPersona = new HashMap<>();
                editarPersona.put("apellido", apellidoPersona);
                editarPersona.put("telefono", telefonoPersona);

                db.collection("agenda").document(nombre).update(editarPersona)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "¡Ha actualizado",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "No se pudo realizar la operación.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        
            }


        });
    }

    public void buscarPersona(){
        Query query = personaRef.whereEqualTo("nombre", edtNombre.getText());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Persona usuario = document.toObject(Persona.class);
                        edtApellido.setText(usuario.getApellido());
                        edtTelefono.setText(usuario.getTelefono());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Error al recuperar el perfil", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void borrarPersona(){
        CollectionReference resenasRef = db.collection("agenda");
        Query queryEliminar = resenasRef.whereEqualTo("nombre", edtNombre.getText());

        queryEliminar.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        String id = document.getId();
                        db.collection("agenda").document(id).delete();
                    }
                }
            }
        });
    }



}
