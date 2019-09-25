package com.pkg.tin_tin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditDataActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ArrayList<MenuDataModel> menuDataModelArrayList;
    private RecyclerView recycler_view;
    private EditMenuRecyclerViewAdapter adapter;
    private FirestoreRecyclerAdapter<MenuDataModel,EditMenuRecyclerViewHolder> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        authentication();

        layoutauthentication();
        loadDataFromFirebase();
    }

    private void layoutauthentication() {

        menuDataModelArrayList = new ArrayList<>();
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
        Query query = db.collection("SupplierUsers").document(id).collection("Menu");
        FirestoreRecyclerOptions<MenuDataModel> options = new FirestoreRecyclerOptions.Builder<MenuDataModel>()
                .setQuery(query, MenuDataModel.class)
                .build();

        adapter1 = new FirestoreRecyclerAdapter<MenuDataModel, EditMenuRecyclerViewHolder>(options) {
            @NonNull
            @Override
            public EditMenuRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_menu_card, parent, false);
                return new EditMenuRecyclerViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EditMenuRecyclerViewHolder dataViewHolder, final int i, @NonNull final MenuDataModel data) {
                dataViewHolder.setMenu(data.getMenu());
                dataViewHolder.setCost(data.getCost());
                dataViewHolder.setType(data.getType());
                dataViewHolder.setQuantity(data.getQuantity());
                dataViewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(data);

                    }
                });
            }


        };
        recycler_view.setAdapter(adapter1);
        adapter1.startListening();
    }

    private void remove(final MenuDataModel data) {
        Log.d("In", firebaseUser.getEmail());
        db.collection("SupplierUsers").whereEqualTo("Email", firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                     if (task.isSuccessful()) {
                                                         QuerySnapshot qs = task.getResult();
                                                         List<DocumentSnapshot> list = qs.getDocuments();
                                                         removeData(list.get(0).getId());
                                                     }
                                                 }

                                                 private void removeData(String id) {

                                                     final CollectionReference collectionReference = db.collection("SupplierUsers").document(id).collection("Menu");
                                                     Query query = collectionReference.whereEqualTo("Menu", data.getMenu())
                                                             .whereEqualTo("Cost", data.getCost());
                                                     Log.d("In--", query.get().toString());
                                                     query.get()
                                                             .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                     if (task.isSuccessful()) {
                                                                         Log.d("In--", "ONcomplate");
                                                                         for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                                             Log.d("In--", "Before delete");
                                                                             try {
                                                                                 collectionReference.document(documentSnapshot.getId()).delete();
                                                                             } catch (Exception e) {
                                                                                 Toast.makeText(getApplicationContext(), "Delete Successfully", Toast.LENGTH_LONG).show();
                                                                             }
                                                                         }

                                                                     }
                                                                 }
                                                             }).addOnFailureListener(new OnFailureListener() {
                                                         @Override
                                                         public void onFailure(@NonNull Exception e) {
                                                             Toast.makeText(getApplicationContext(), "Failer----", Toast.LENGTH_SHORT).show();
                                                         }
                                                     });
                                                 }

                                             }

        );
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
