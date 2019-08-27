package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.FitWindowsFrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestOrderActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recycler_view;
    private ArrayList<RequestOrderModel> requestOrderModelArrayList;
    private BottomAppBar toolbar;
    private TextView totalreq;
    private ArrayList<String> customername=new ArrayList<>();
    private Map<String,Object> datamap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_order);
        totalreq = findViewById(R.id.totalrequest);
        toolbar=findViewById(R.id.toolbar_requestorder);
        datamap=new HashMap<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab_requestorder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"All done",Toast.LENGTH_LONG).show();
                setFlagFalse();
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        layoutAuth();
        loadRequest();

    }

    private void setFlagFalse() {
        Log.d("In----",customername+"");
        for(String name:customername){
            db.collection("CustomerUsers").whereEqualTo("Name",name).get().addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot qs =task.getResult();
                            List<DocumentSnapshot> ls = qs.getDocuments();
                            setFlag(ls.get(0).getId());
                        }

                        private void setFlag(String id) {
                            datamap.put("isOrdered",false);
                            db.collection("CustomerUsers").document(id).update(datamap).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    }
                            );
                            db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        QuerySnapshot qs = task.getResult();
                                        List<DocumentSnapshot> list = qs.getDocuments();
                                        removeOrder(list.get(0).getId());
                                    }
                                }

                                private void removeOrder(final String id) {
                                    db.collection("SupplierUsers").document(id).collection("RequestOrder").get()
                                            .addOnCompleteListener(
                                                    new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                QuerySnapshot qs= task.getResult();
                                                                List<DocumentSnapshot> ds= qs.getDocuments();
                                                                removelist(ds);
                                                        }

                                                        private void removelist(List<DocumentSnapshot> ds) {
                                                            for(DocumentSnapshot documentSnapshot:ds){
                                                                db.collection("SupplierUsers").document(id).collection("RequestOrder").document(documentSnapshot.getId()).delete()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                            );
                                }
                            });

                        }
                    }
            );
        }
        Toast.makeText(getApplicationContext(),"Complate",Toast.LENGTH_LONG).show();
    }

    private void loadRequest() {
        db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
                    selectorder(list.get(0).getId());
                }
            }
        });
    }

    private void selectorder(final String id) {
        db.collection("SupplierUsers").document(id).collection("RequestOrder").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs = task.getResult();
                        int total = qs.getDocuments().size();
                        totalreq.setText("Total Request : "+total);
                        for(DocumentSnapshot ds :qs.getDocuments()){
                            fatchOrderData(ds.getId(),id);
                        }
                    }
                }
        );
    }

    private void fatchOrderData(String dsid,String id) {
        db.collection("SupplierUsers").document(id).collection("RequestOrder").document(dsid).get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    RequestOrderModel requestOrderModel = new RequestOrderModel(
                                            documentSnapshot.getString("CustName"),
                                            documentSnapshot.getString("CustPhoneNo"),
                                            documentSnapshot.getString("Type")
                                    );
                                    customername.add(documentSnapshot.getString("CustName"));
                                    requestOrderModelArrayList.add(requestOrderModel);
                                    RequestOrderViewAdapter adapter = new RequestOrderViewAdapter(requestOrderModelArrayList);
                                    recycler_view.setAdapter(adapter);
                            }
                        }
                );
    }

    private void layoutAuth() {
        requestOrderModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.recyclerview_requestorder);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
    }

}
