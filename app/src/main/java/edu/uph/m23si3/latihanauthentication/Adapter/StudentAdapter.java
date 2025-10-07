package edu.uph.m23si3.latihanauthentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.uph.m23si3.latihanauthentication.R;
import edu.uph.m23si3.latihanauthentication.model.Student;

public class StudentAdapter extends ArrayAdapter<Student> {

    public StudentAdapter(@NonNull Context context, @NonNull List<Student> students) {
        super(context, 0, students);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_layout, parent, false);
        }

        Student student = getItem(position);

        TextView txvNama = convertView.findViewById(R.id.txvNama);
        TextView txvStudentID = convertView.findViewById(R.id.txvStudentID);
        TextView txvProvinsi = convertView.findViewById(R.id.txvProvinsi);
        TextView txvKota = convertView.findViewById(R.id.txvKota);

        txvNama.setText("Nama: " + student.getNama());
        txvStudentID.setText("Student ID: " + student.getStudentID());
        txvProvinsi.setText("Provinsi: " + student.getProvinsi());
        txvKota.setText("Kota: " + student.getKota());

        return convertView;
    }
}
