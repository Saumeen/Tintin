package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiffinMenuActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private TextInputEditText cost;
    private Button addmore,submit;
    private RadioGroup radioGroup;
    private Map<String,Object> dataMap;
    private String halffull;
    private ListView listView;
    private MultiAutoCompleteTextView menu;

    private ArrayList<HashMap<String,String>> menulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_menu);

        cost = findViewById(R.id.tiffincost);
        submit= findViewById(R.id.tiffin_submit);
        addmore = findViewById(R.id.tiffin_addmore);
        radioGroup = findViewById(R.id.radiogroup);
        dataMap = new HashMap<>();

        String menudata[] ={"Dal","Bhat","PauBhaji","PaniPuri","Roti","Chapati","Khichdi","Kadhi","Khaman Dhokla","Dabeli","Patra","DalDhokli","Kadhi","Roti","Chapati","Kachumber","Buttermilk","Wagharelo Rotlo",
                "BhaatNaPoodla","Chivda","Nimki","Namkeen Shakarpara","Basundi","Shrikhand","FadaNiLapsi","DoodhPaak","Bhakri",
                "Kobi","Karela","Bhindi","Rajma","Achar","ChanaDal","Aloo","Paneer","Nan","Dosa","Idli","PauBhaji","Thepla","Paratha",
                "Aloo Paratha","DoodhiThepla","MethiThepla","BajraNoRotlo","Chinese","Dal Fry","Biriyani","ChholePuri","Dhokla",
                "Rasgulla","Ladoos","MoongDal","Dudhi","Guvar","Methi Bhaji","PhoolKobi","Choli","Tindola","SevTameta"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menudata);
        menu = findViewById(R.id.tiffinmenu);

        menu.setThreshold(1);
        menu.setAdapter(adapter);
        menu.setTokenizer(new SpaceTokenizer());
        menu.setTextColor(Color.BLACK);

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

        db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail())
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

        db.collection("SupplierUsers").document(id).collection("Menu").add(dataMap)
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
