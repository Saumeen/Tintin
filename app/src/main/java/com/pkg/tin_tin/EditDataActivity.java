package com.pkg.tin_tin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditDataActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ArrayList<MenuData> menuDataArrayList;
    private RecyclerView recycler_view;
    private EditMenuRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        authentication();

        layoutauthentication();
        loadDataFromFirebase();
    }

    private void layoutauthentication() {

        menuDataArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.edit_recyclerview);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

    }

    private void loadDataFromFirebase() {
        getUser();

    }

    private void getUser() {
        db.collection("SupplierUsers").whereEqualTo("Email",firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
     //               loadMenuData(list.get(0).getId());
                    loadMenuData(list.get(0).getId());
                }
            }
        });
    }

    private void loadMenuData(String id) {
        final Query query  = db.collection("SupplierUsers").document(id).collection("Menu");
//
//        final FirestoreRecyclerOptions<MenuData> options = new FirestoreRecyclerOptions.Builder<MenuData>().setQuery(query,MenuData.class).build();
//
//        Log.d("IN--",options.getSnapshots().toArray().length+"");
//        firebaseAdapter = new MenuFirebaseAdapter(options);
//        recycler_view.setAdapter(firebaseAdapter);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {


                            MenuData menuData = new MenuData( documentSnapshot.getString("Menu"),
                                     documentSnapshot.getString("Cost"),  documentSnapshot.getString("Type")
                                    ,documentSnapshot.getString("Quantity"));
                            menuDataArrayList.add(menuData);
                            //adapter = new MenuFirebaseAdapter(EditDataActivity.this,menuDataArrayList);
                            adapter = new EditMenuRecyclerViewAdapter(EditDataActivity.this, menuDataArrayList);
                            adapter.notifyDataSetChanged();
                            recycler_view.setAdapter(adapter);
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Load", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void authentication() {

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(EditDataActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

}
