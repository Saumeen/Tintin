package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiffinMenuActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private TextInputEditText menu,cost;
    private Button addmore,submit;
    private RadioGroup radioGroup;
    private Map<String,Object> dataMap;
    private String halffull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_menu);
        menu = findViewById(R.id.tiffinmenu);
        cost = findViewById(R.id.tiffincost);
        submit= findViewById(R.id.tiffin_submit);
        addmore = findViewById(R.id.tiffin_addmore);
        radioGroup = findViewById(R.id.radiogroup);
        dataMap = new HashMap<>();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(TiffinMenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.fulltiffin:
                        halffull = "Full Tiffin";
                        break;
                    case R.id.minitiffin:
                        halffull = "Mini Tiffin";
                        break;
                    default:
                        break;
                }

            }
        });

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setText("");
                cost.setText("");
                addmore.setEnabled(false);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
                addmore.setEnabled(true);
                Toast.makeText(getApplicationContext(),"submit",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addMenu(){

        db.collection("users").whereEqualTo("Email",firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
                    addData(list.get(0).getId());
                }
            }
        });
    }

    public void addData(String id){
        String menudata = menu.getText().toString();
        String costdata = cost.getText().toString();

        dataMap.put("Menu",menudata);
        dataMap.put("Cost",costdata);
        dataMap.put("Type",halffull);

        db.collection("users").document(id).collection("TiffinMenu").add(dataMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"added succcessful",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"not successful",Toast.LENGTH_LONG).show();
            }
        });

    }


}
