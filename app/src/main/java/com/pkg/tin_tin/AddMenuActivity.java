package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class AddMenuActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    protected String TAG = "AddMenuActivity";
    private  FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    private FirebaseFirestore db;
    private TextInputEditText tiffinmenu,homemenu,Quantity,cost;
    private Switch sw;
    private Button submit;
    private Map<String,Object> datamap;
    private RadioGroup radioGroup;
    private RadioButton lunch,dinner;
    private String lunchdinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        tiffinmenu = findViewById(R.id.tiffinmenu);
        homemenu = findViewById(R.id.homemenu);
        sw = findViewById(R.id.onoff);
        Quantity = findViewById(R.id.Quantity);
        cost = findViewById(R.id.cost);
        submit = findViewById(R.id.submit);
        datamap = new HashMap<>();
        radioGroup = findViewById(R.id.radiogroup);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);


        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            Intent intent=new Intent(AddMenuActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        authStateListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                if(firebaseAuth1.getCurrentUser()==null){
                    Intent intent=new Intent(AddMenuActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
                Intent intent = new Intent(AddMenuActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sw.isChecked()){
                    Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_LONG).show();
                    tiffinmenu.setEnabled(true);
                }
                else{
                    Toast.makeText(getApplicationContext(),"off",Toast.LENGTH_LONG).show();
                    tiffinmenu.setEnabled(false);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.lunch:
                        lunchdinner = "Lunch";
                        break;
                    case R.id.dinner:
                        lunchdinner = "Dinner";
                        break;
                    default:
                        break;
                }
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
       String homemenudata = homemenu.getText().toString();
       String tiffinmenudata = tiffinmenu.getText().toString();
        String Quantiydata = Quantity.getText().toString();
        String costdata = cost.getText().toString();

        datamap.put("Home menu",homemenudata);
        datamap.put("Tiffin Menu",tiffinmenudata);
        datamap.put("Cost",costdata);
        datamap.put("Quantity",Quantiydata);
        datamap.put("Type",lunchdinner);
        datamap.put("Type1","Tiffin");

        db.collection("users").document(id).collection("menu").add(datamap)
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
