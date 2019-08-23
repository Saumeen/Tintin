package com.pkg.tin_tin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recycler_view;
    private FirebaseUser firebaseUser;
    protected String TAG = "HomeActivity";
    private  FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient googleApiClient;
    private FirebaseFirestore db;
    private TextView name,mobileno,addres;
    private ArrayList<MenuData> menuDataArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private HomeMenuRecyclerViewAdapter adapter;
   
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        name = findViewById(R.id.home_card_name);
        mobileno = findViewById(R.id.home_card_mobile);
        addres = findViewById(R.id.home_card_address);
       // swipeRefreshLayout = findViewById(R.id.swipeRefereshlayout);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth1) {
                if (firebaseAuth1.getCurrentUser() == null) {
                    //Toast.makeText(MainActivity.this,"in Listner "+ firebaseAuth1.getCurrentUser().toString(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        };
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        layoutauthentication();
        setData();
       // Refresh();
    }

    private void Refresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("Data---","Referesh");
                    menuDataArrayList.clear();
                    setData();
            }
        });
    }


    private void setData() {
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
                    loadData(list.get(0).getId());
                    loadMenuData(list.get(0).getId());
                }
            }
        });
    }

    private void loadData(String id) {

        db.collection("SupplierUsers").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    name.setText("Name : "+documentSnapshot.getString("Name"));
                    addres.setText("Address : "+documentSnapshot.getString("Adddress"));
                    mobileno.setText("Mobile No : "+documentSnapshot.getString("MobileNo" +
                            "")
                    );

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void layoutauthentication() {

        menuDataArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view = findViewById(R.id.home_recyclerview);
        recycler_view.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

    }

//    private void loadDataFromFirebase() {
//        getUser();
//
//    }

//    private void getUser() {
//        db.collection("users").whereEqualTo("Email",firebaseUser.getEmail())
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    QuerySnapshot qs = task.getResult();
//                    List<DocumentSnapshot> list = qs.getDocuments();
//                    //               loadMenuData(list.get(0).getId());
//                    loadMenuData(list.get(0).getId());
//                }
//            }
//        });
//    }

    private void loadMenuData(String id) {
        db.collection("SupplierUsers").document(id).collection("Menu")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                recycler_view.setAdapter(null);
               // adapter=null;
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                    MenuData menuData = new MenuData( documentSnapshot.getString("Menu"),
                                    documentSnapshot.getString("Cost"),  documentSnapshot.getString("Type")
                                    ,documentSnapshot.getString("Quantity"));
                            menuDataArrayList.add(menuData);
                            //adapter = new MenuFirebaseAdapter(EditDataActivity.this,menuDataArrayList);
                    Log.d("Log---", documentSnapshot.getString("Menu") + "");
                }
                HomeMenuRecyclerViewAdapter adapter = new HomeMenuRecyclerViewAdapter(HomeActivity.this, menuDataArrayList);
                adapter.notifyDataSetChanged();
                recycler_view.setAdapter(adapter);
            }
        });


//        final Query query  = db.collection("SupplierUsers").document(id).collection("Menu");
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
//
//
//                            MenuData menuData = new MenuData( documentSnapshot.getString("Menu"),
//                                    documentSnapshot.getString("Cost"),  documentSnapshot.getString("Type")
//                                    ,documentSnapshot.getString("Quantity"));
//                            menuDataArrayList.add(menuData);
//                            //adapter = new MenuFirebaseAdapter(EditDataActivity.this,menuDataArrayList);
//                            adapter = new HomeMenuRecyclerViewAdapter(HomeActivity.this, menuDataArrayList);
//
//                            recycler_view.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),"Fail to Load", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    @Override
    protected void onStart(){
        super.onStart();
       // setData();
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            addMenu();
        }
        else if(id==R.id.nav_tiffinadd){
             addTiffin();               
        } else if (id == R.id.nav_edit) {
            editData();

        } else if (id == R.id.nav_feedback) {
            feedBack();

        } else if (id == R.id.nav_share) {

        } else if(id == R.id.nav_logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Are you sure ?").setTitle("Logout");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    firebaseAuth.signOut();
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    googleSignInClient= GoogleSignIn.getClient(HomeActivity.this,gso);
                    googleSignInClient.signOut().addOnCompleteListener(HomeActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
           AlertDialog dialog = builder.create();
            dialog.show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void editData() {
        Intent intent = new Intent(HomeActivity.this,EditDataActivity.class);
        startActivity(intent);
    }

    private void addTiffin() {
        Intent intent = new Intent(HomeActivity.this,TiffinMenuActivity.class);
        startActivity(intent);
    }

    public void feedBack(){
        Intent intent = new Intent(HomeActivity.this,FeedbackActivity.class);
        startActivity(intent);
    }

    public void addMenu(){
        Intent intent = new Intent(HomeActivity.this, HomeMenuActivity.class);
        startActivity(intent);
    }
}
