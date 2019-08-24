package com.pkg.tin_tin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private EditText selectDate;
    private int mYear, mMonth, mDay;
    private TextInputEditText name, mobileno,houseno,landmark;
    private Button submit;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private String TAG = "DetailsActivity";
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private DocumentReference documentReference;
    private Map<String,Object> maplist = new HashMap<>();
    private String dataname,datahouseno,datalandmark,datacity,datamobilno,databirthday;
    private AutoCompleteTextView cityname;
    String[] cdata={"Godhra","Nadiad","Vadodara","Ahmedabad","Gandhinagar","Surat","Anand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name = findViewById(R.id.name);
        houseno = findViewById(R.id.houseflatno);
        landmark=findViewById(R.id.landmark);
        cityname = findViewById(R.id.cityname);
        mobileno = findViewById(R.id.mobile);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cdata);
        cityname.setThreshold(1);
        cityname.setAdapter(adapter);





        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(DetailsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                if (firebaseAuth1.getCurrentUser() == null) {
                    Intent intent = new Intent(DetailsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
        firebaseUser = firebaseAuth.getCurrentUser();
        db =FirebaseFirestore.getInstance();
//        selectDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dateSelection();
//            }
//        });
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(name)){
                    name.setError("Please Enter Your name");
                }
                if(isEmpty(houseno)){
                    houseno.setError("please Enter House/Flat NO");
                }

                if(isEmpty(landmark)){
                    landmark.setError("please Landmark Address");
                }
                if(isvalid(cityname)){
                    cityname.setError("please Enter City");
                }
                if(isEmpty(mobileno)){
                    mobileno.setError("Please Enter Mobile No");
                }
                else{
                db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot qs = task.getResult();
                                    List<DocumentSnapshot> list = qs.getDocuments();
                                    updateData(list.get(0).getId());
                                }
                            }
                        });
                Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } } });
    }

    private boolean isvalid(AutoCompleteTextView cityname) {
       String input = cityname.getText().toString().trim();
       return input.length() == 0;
    }


    public static boolean isEmpty(TextInputEditText textInputEditText) {
        String input = textInputEditText.getText().toString().trim();
        return input.length() == 0;
    }


//    public void dateSelection() {
//        final Calendar c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        selectDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//    }


    public void updateData(String id){
        dataname = name.getText().toString();
        datahouseno = houseno.getText().toString();
        datalandmark = landmark.getText().toString();
        datacity = cityname.getText().toString();
        datamobilno = mobileno.getText().toString();

        maplist.put("Name",dataname);
        maplist.put("HouseFlatNo",datahouseno);
        maplist.put("Landmark",datalandmark);
        maplist.put("City",datacity);
        maplist.put("MobileNo",datamobilno);

        documentReference = db.collection("SupplierUsers").document(id);
        documentReference.update(maplist).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}
