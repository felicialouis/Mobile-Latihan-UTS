package edu.uph.m23si3.latihanauthentication.model;

public class Student {
    String Nama, StudentID, Provinsi, Kota;

    public Student(){}

    public Student(String nama, String studentID, String provinsi, String kota) {
        Nama = nama;
        StudentID = studentID;
        Provinsi = provinsi;
        Kota = kota;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getProvinsi() {
        return Provinsi;
    }

    public void setProvinsi(String provinsi) {
        Provinsi = provinsi;
    }

    public String getKota() {
        return Kota;
    }

    public void setKota(String kota) {
        Kota = kota;
    }
}
