package com.example.userlistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonAdd;
    private TextView textViewList;
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonAdd = findViewById(R.id.buttonAdd);
        textViewList = findViewById(R.id.textViewList);

        // Conectar ao Firestore
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        // Botão adicionar usuário
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                if (!name.isEmpty()) {
                    // Criar novo usuário
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);

                    usersRef.add(user)
                            .addOnSuccessListener(documentReference -> {
                                Log.d("Firestore", "Usuário adicionado: " + documentReference.getId());
                                editTextName.setText(""); // Limpar campo
                            })
                            .addOnFailureListener(e -> Log.w("Firestore", "Erro ao adicionar usuário", e));
                }
            }
        });

        // Escutar mudanças na lista de usuários
        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Firestore", "Erro ao escutar mudanças.", error);
                    return;
                }

                StringBuilder data = new StringBuilder();
                if (value != null) {
                    for (QueryDocumentSnapshot doc : value) {
                        String name = doc.getString("name");
                        data.append(name).append("\n");
                    }
                }
                textViewList.setText(data.toString());
            }
        });
    }
}
