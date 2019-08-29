package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class HomeMenuActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    protected String TAG = "HomeMenuActivity";
    private  FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    private FirebaseFirestore db;
    private TextInputEditText tiffinmenu,Quantity,cost;
    private MultiAutoCompleteTextView homemenu;
    private Switch sw;
    private Button submit;
    private Map<String,Object> datamap;
    private RadioGroup radioGroup;
    private RadioButton lunch,dinner;
    private String lunchdinner;
    private Map<String,Object> searchdata=new HashMap<>();
    private String supid;
    private Map<String,Object> docdata=new HashMap<>();
    private Map<String,Object> nesteddata=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        homemenu = findViewById(R.id.homemenu);

        Quantity = findViewById(R.id.Quantity);
        cost = findViewById(R.id.cost);
        submit = findViewById(R.id.submit);
        datamap = new HashMap<>();
        radioGroup = findViewById(R.id.radiogroup);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);


        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            Intent intent=new Intent(HomeMenuActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        authStateListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                if(firebaseAuth1.getCurrentUser()==null){
                    Intent intent=new Intent(HomeMenuActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        String menudata[] ={"Dal","Bhat","PauBhaji","PaniPuri","Roti","Chapati","Khichdi","Kadhi","Khaman Dhokla","Dabeli","Patra","DalDhokli","Kadhi","Roti","Chapati","Kachumber","Buttermilk","Wagharelo Rotlo",
                "BhaatNaPoodla","Chivda","Nimki","Namkeen Shakarpara","Basundi","Shrikhand","FadaNiLapsi","DoodhPaak","Bhakri",
                "Kobi","Karela","Bhindi","Rajma","Achar","ChanaDal","Aloo","Paneer","Nan","Dosa","Idli","PauBhaji","Thepla","Paratha",
                "Aloo Paratha","DoodhiThepla","MethiThepla","BajraNoRotlo","Chinese","Dal Fry","Biriyani","ChholePuri","Dhokla",
                "Rasgulla","Ladoos","MoongDal","Dudhi","Guvar","Methi Bhaji","PhoolKobi","Choli","Tindola","SevTameta"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menudata);
        homemenu = findViewById(R.id.homemenu);
        homemenu.setThreshold(1);
        homemenu.setAdapter(adapter);
        homemenu.setTokenizer(new SpaceTokenizer());
        homemenu.setTextColor(Color.BLACK);


        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
                Intent intent = new Intent(HomeMenuActivity.this,HomeActivity.class);
                startActivity(intent);
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

    public void addData(final String id){
       String homemenudata = homemenu.getText().toString().toLowerCase();
        String Quantiydata = Quantity.getText().toString();
        String costdata = cost.getText().toString();

        datamap.put("Menu",homemenudata);
        datamap.put("Cost",costdata);
        datamap.put("Quantity",Quantiydata);
        datamap.put("Type",lunchdinner);

        db.collection("SupplierUsers").document(id).collection("Menu").add(datamap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                db.collection("SupplierUsers").document(id).get().addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot ds= task.getResult();

                                supid= ds.getString("Name");
                                nesteddata.put("SupplierId",supid);
                                nesteddata.put("MenuId",documentReference);

                                StringTokenizer st = new StringTokenizer(homemenu.getText().toString()," ");
                                while(st.hasMoreTokens()){
                                    docdata.put(st.nextToken().toLowerCase(), FieldValue.arrayUnion(nesteddata));

                                    db.collection("SearchListData").document("data").update(docdata).addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(), "Searchlist created", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                    );
                                }
                            }
                        }
                );
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
