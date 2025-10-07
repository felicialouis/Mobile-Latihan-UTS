package edu.uph.m23si3.latihanauthentication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uph.m23si3.latihanauthentication.Adapter.StudentAdapter;
import edu.uph.m23si3.latihanauthentication.model.Student;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Student> studentArrayList;
    StudentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        studentArrayList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentArrayList);
        listView.setAdapter(adapter);

        db.collection("students")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    studentArrayList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Student student = doc.toObject(Student.class);
                        studentArrayList.add(student);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal ambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }
}