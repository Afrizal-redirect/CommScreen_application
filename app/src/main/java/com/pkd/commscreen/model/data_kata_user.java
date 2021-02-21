package com.pkd.commscreen.model;

public class data_kata_user {
    private String namaGambar;

    private String key;

    private String gambar;

    private String sound;

    private String soundUrl;


    public data_kata_user(String namaGambar, String key, String sound, String soundUrl, String gambar) {
        this.namaGambar = namaGambar;
        this.key = key;
        this.sound = sound;
        this.soundUrl = soundUrl;
        this.gambar = gambar;
    }

    public data_kata_user(String namaGambar, String sound, String soundUrl) {
        this.namaGambar = namaGambar;

        this.sound = sound;
        this.soundUrl = soundUrl;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNamaGambar() {
        return namaGambar;
    }

    public void setNamaGambar(String namaGambar) {
        this.namaGambar = namaGambar;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public data_kata_user(){
    }


}

