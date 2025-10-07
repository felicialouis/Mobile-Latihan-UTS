package edu.uph.m23si3.latihanauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uph.m23si3.latihanauthentication.api.ApiResponse;
import edu.uph.m23si3.latihanauthentication.api.ApiService;
import edu.uph.m23si3.latihanauthentication.model.Kota;
import edu.uph.m23si3.latihanauthentication.model.Provinsi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword, edtNama, edtStudentID;
    Spinner spnProvinsi, spnKota;
    Button btnRegister;
    TextView txvLogin;
    List<Provinsi> provinsiList = new ArrayList<>();
    List<String> namaProvinsi = new ArrayList<>();
    List<Kota> kotaList = new ArrayList<>();
    List<String> namaKota = new ArrayList<>();
    ArrayAdapter<String> adapterProvinsi, adapterKota;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtNama = findViewById(R.id.edtNama);
        edtStudentID = findViewById(R.id.edtStudentID);

        spnProvinsi = findViewById(R.id.spnProvinsi);
        adapterProvinsi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaProvinsi);
        adapterProvinsi.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnProvinsi.setAdapter(adapterProvinsi);

        spnKota = findViewById(R.id.spnKota);
        adapterKota = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, namaKota);
        adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnKota.setAdapter(adapterKota);

        // init retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://wilayah.id")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getProvinsi().enqueue(new Callback<ApiResponse<Provinsi>>() {
            @Override
            public void onResponse(Call<ApiResponse<Provinsi>> call, Response<ApiResponse<Provinsi>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    provinsiList = response.body().getData();
                    namaProvinsi.clear();
                    for (Provinsi p : provinsiList) {
                        if (p.getName() != null) {
                            Log.d("Provinsi", p.getName());
                            namaProvinsi.add(p.getName());
                        }
                    }

                    adapterProvinsi.notifyDataSetChanged();

                    spnProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Provinsi selectedProvinsi = provinsiList.get(position);
                            Log.d("Provinsi", selectedProvinsi.getCode() + " - " + selectedProvinsi.getName());

                            apiService.getKota(selectedProvinsi.getCode()).enqueue(new Callback<ApiResponse<Kota>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<Kota>> call, Response<ApiResponse<Kota>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        kotaList = response.body().getData();
                                        namaKota.clear();
                                        for (Kota k : kotaList) {
                                            if (k.getName() != null) {
                                                Log.d("Kota", k.getName());
                                                namaKota.add(k.getName());
                                            }
                                        }

                                        adapterKota.notifyDataSetChanged();

                                        spnKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                Kota selectedKota = kotaList.get(position);
                                                Log.d("Kota", selectedKota.getCode() + " - " + selectedKota.getName());
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<Kota>> call, Throwable t) {
                                    Toast.makeText(RegisterActivity.this, "Gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Provinsi>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String nama = edtNama.getText().toString().trim();
                String studentID = edtStudentID.getText().toString().trim();

                int selectedProvinsiPosition = spnProvinsi.getSelectedItemPosition();
                String provinsi = namaProvinsi.get(selectedProvinsiPosition);

                int selectedKotaPosition = spnKota.getSelectedItemPosition();
                String kota = namaKota.get(selectedKotaPosition);

                registerStudent(email, nama, studentID, provinsi, kota);
                register(email, password);
            }
        });

        txvLogin = findViewById(R.id.txvLogin);
        txvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
    }

    public void register(String email, String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "signInWithCustomToken:success");
                    toMain();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Register", "signInWithCustomToken:failure", task.getException());
                }
            }
        });
    }

    public void registerStudent(String email, String nama, String studentID, String provinsi, String kota) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first, middle, and last name
        Map<String, Object> student = new HashMap<>();
        student.put("Email", email);
        student.put("Nama", nama);
        student.put("StudentID", studentID);
        student.put("Provinsi", provinsi);
        student.put("Kota", kota);

        // Add a new document with a generated ID
        db.collection("students")
                .add(student)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Student", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Student", "Error adding document", e);
                    }
                });

    }

    public void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void toMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
