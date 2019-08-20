package com.pkg.tin_tin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditDataActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ArrayList<MenuData> menuDataArrayList;
    private RecyclerView recycler_view;
    private MenuRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        authentication();

        menuDataArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.edit_recyclerview);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        getUser();

    }

    private void getUser() {
        db.collection("users").whereEqualTo("Email",firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot qs = task.getResult();
                    List<DocumentSnapshot> list = qs.getDocuments();
                    loadMenuData(list.get(0).getId());
                    loadHomeMenuData(list.get(0).getId());
                }
            }
        });
    }

    private void loadHomeMenuData(String id) {
        db.collection("users").document(id).collection("HomeMenu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            MenuData menuData = new MenuData("Menu : " + documentSnapshot.getString("Menu"),
                                    "Cost : " + documentSnapshot.getString("Cost"), "Type : " + documentSnapshot.getString("Type")
                                    ,"Quantity : "+documentSnapshot.getString("Quantity"));
                            menuDataArrayList.add(menuData);
                            adapter = new MenuRecyclerViewAdapter(EditDataActivity.this, menuDataArrayList);
                            recycler_view.setAdapter(adapter);
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Load",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadMenuData(String id) {
        db.collection("users").document(id).collection("TiffinMenu").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            MenuData menuData = new MenuData("Menu : " + documentSnapshot.getString("Menu"),
                                    "Cost : " + documentSnapshot.getString("Cost"), "Type : " + documentSnapshot.getString("Type"));
                            menuDataArrayList.add(menuData);
                            adapter = new MenuRecyclerViewAdapter(EditDataActivity.this, menuDataArrayList);
                            recycler_view.setAdapter(adapter);
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail to Load",Toast.LENGTH_LONG).show();
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
