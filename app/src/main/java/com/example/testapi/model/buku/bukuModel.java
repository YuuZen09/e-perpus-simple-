package com.example.testapi.model.buku;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class bukuModel implements Parcelable {

    private String id_buku;

    private String judul;

    private String penulis;

    private String cover;

    private String tahun;

    private String penerbit;
    private String sinopsis;
    private String pdfPath;




    public bukuModel(String id, String judul, String penulis, String cover, String sinopsis, String tahun, String penerbit){
        this.id_buku = id;
        this.judul = judul;
        this.penulis = penulis;
        this.cover = cover;
        this.sinopsis = sinopsis;
        this.tahun = tahun;
        this.penerbit = penerbit;
    }


    public static final Creator<bukuModel> CREATOR = new Creator<bukuModel>() {
        @Override
        public bukuModel createFromParcel(Parcel in) {
            return new bukuModel(in);
        }

        @Override
        public bukuModel[] newArray(int size) {
            return new bukuModel[size];
        }
    };

    public bukuModel(String id, String judul, String penulis, String cover) {
        this.id_buku = id;
        this.judul = judul;
        this.penulis = penulis;
        this.cover = cover;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getId_buku() {
        return id_buku;
    }

    public void setId_buku(String id_buku) {
        this.id_buku = id_buku;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getId() {
        return id_buku;
    }

    public void setId(String id) {
        this.id_buku = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    @Override
    public String toString(){
        return judul;
    }

    protected bukuModel(Parcel in) {
        id_buku = in.readString();
        judul = in.readString();
        penulis = in.readString();
        cover = in.readString();
        tahun = in.readString();
        penerbit = in.readString();
        sinopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id_buku);
        dest.writeString(judul);
        dest.writeString(penulis);
        dest.writeString(cover);
        dest.writeString(tahun);
        dest.writeString(penerbit);
        dest.writeString(sinopsis);
    }
    
}
