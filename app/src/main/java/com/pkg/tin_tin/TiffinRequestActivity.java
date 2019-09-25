package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.google.firebase.firestore.FieldValue.delete;

public class TiffinRequestActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private RecyclerView recycler_view;
    private FirestoreRecyclerAdapter<TiffinRequestModel,TiffinRequestViewHolder> adapter;
    private Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_request);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        done = findViewById(R.id.menudatacard_done);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        layoutAuth();
        fatchdata();

    }

    private void fatchdata() {
        Query q  = db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail());
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot qs = task.getResult();
                List<DocumentSnapshot> ds = qs.getDocuments();
                supId(ds.get(0).getId());
            }

            private void supId(String id) {
                Query query =db.collection("SupplierUsers").document(id).collection("TiffinOrder");

                FirestoreRecyclerOptions<TiffinRequestModel> options = new FirestoreRecyclerOptions.Builder<TiffinRequestModel>()
                        .setQuery(query, TiffinRequestModel.class)
                        .build();

                adapter = new FirestoreRecyclerAdapter<TiffinRequestModel, TiffinRequestViewHolder>(options) {
                    @NonNull
                    @Override
                    public TiffinRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tiffinrequest, parent, false);
                        return new TiffinRequestViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull TiffinRequestViewHolder dataViewHolder, int i, @NonNull final TiffinRequestModel data) {
                       dataViewHolder.setMenu(data.getMenu());
                        dataViewHolder.setMobileNo(data.getMobileNO());
                        dataViewHolder.setCost(data.getCost());
                        dataViewHolder.setType(data.getType());
                        dataViewHolder.setCustomerName(data.getCustomerName());
                        dataViewHolder.setAddress(data.getAddress());
                        dataViewHolder.done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                remove(data);
                            }
                        });
                    }


                };
                recycler_view.setAdapter(adapter);
                adapter.startListening();
            }
        });
    }

    private void remove(final TiffinRequestModel data) {
        db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot qs  = task.getResult();
                        List<DocumentSnapshot> ds =qs.getDocuments();
                        suID(ds.get(0).getId(),data);
                    }

                    private void suID(final String id, TiffinRequestModel data) {
                        db.collection("SupplierUsers").document(id).collection("TiffinOrder").whereEqualTo("MobileNO",data.getMobileNO()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot qs =task.getResult();
                                        List<DocumentSnapshot> ds= qs.getDocuments();
                                        tiffinId(ds.get(0).getId());

                                    }

                                    private void tiffinId(String tid) {
                                        db.collection("SupplierUsers").document(id).collection("TiffinOrder").document(tid).delete().addOnCompleteListener(
                                                new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(),"Order Succesffuly placed",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                    }
                                });
                    }
                }
        );
    }
    private void layoutAuth() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.recyclervire_tiffinrequest);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
    }
}
