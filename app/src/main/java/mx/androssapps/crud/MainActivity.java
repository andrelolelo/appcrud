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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MainActivity extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtTelefono;
    Button btnAgregar, btnEliminar, btnModificar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

            }
        });
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
