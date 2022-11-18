package com.example.belajarlogin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.belajarlogin.R;
import com.example.belajarlogin.enums.Apis;
import com.example.belajarlogin.enums.Params;
import com.example.belajarlogin.interfaces.RetrofitAPIKuliner;
import com.example.belajarlogin.interfaces.RetrofitAPIRegister;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class KulinerActivity extends AppCompatActivity {
    private EditText etNamaKuliner , etKeterangan , etKontributor, etLatitude, etLongtitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button btnSimpan , btnMap;
    private ProgressDialog progressDialog;
    public String Datamessage = null;
    private String dataAksi = "simpan";

    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuliner);
        getSupportActionBar().setTitle("My Kuliner");

        //set navigation icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //binding variabel
        etNamaKuliner = findViewById(R.id.editTextNamaKuliner);
        etKeterangan  = findViewById(R.id.editTextKeterangan);
        etKontributor = findViewById(R.id.editTextKontributor);
        etLatitude    = findViewById(R.id.editTextLatitude);
        etLongtitude  = findViewById(R.id.editTextLongtitude);
        btnSimpan     = findViewById(R.id.buttonSimpan);
        btnMap        = findViewById(R.id.buttonMap);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(KulinerActivity.this);

        getLocation();

        btnMap.setOnClickListener(view -> {
            Intent goMap = new Intent(Intent.ACTION_VIEW);
            goMap.setData(Uri.parse("geo:"+etLatitude+","+etLongtitude));
            Intent choose = Intent.createChooser(goMap, "Launch Maps");
            startActivity(choose);
        });

        //set button simpan
        btnSimpan.setOnClickListener(view -> {
            String nmKuliner = etNamaKuliner.getText().toString();
            String keterangan = etKeterangan.getText().toString();
            String kontributor = etKontributor.getText().toString();
            String lat = etLatitude.getText().toString();
            String longtitude = etLongtitude.getText().toString();
            String status = String.valueOf(1);

            if(nmKuliner.isEmpty() || keterangan.isEmpty() || kontributor.isEmpty()
                    || lat.isEmpty() || longtitude.isEmpty()){
                Toast.makeText(getApplicationContext(),
                        "Data harus diisi semua" , Toast.LENGTH_SHORT).show();
            } else {
                setKuliner(nmKuliner, keterangan, kontributor, lat, longtitude, status);
//                fastKuliner(nmKuliner, keterangan, kontributor, lat, longtitude);
//                retrofitPostKuliner(nmKuliner, keterangan, kontributor, lat, longtitude);
            }
        });
    }

    // ketika navigation sudah diset, kita buat function nya
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void dialog(){
        progressDialog = new ProgressDialog(KulinerActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Izin lokasi tidak di aktifkan!", Toast.LENGTH_SHORT).show();
            }else{
                getLocation();
            }
        }
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // get Permission

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);

        }else {
            // get Location
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null) {
                        String lat = String.valueOf(location.getLatitude());
                        etLatitude.setText(lat);

                        String lot = String.valueOf(location.getLongitude());
                        etLongtitude.setText(lot);

                        Log.d("Cek latitude" , "isi " + lat);
                        Log.d("Cek longtitude" , "isi " + lot);

                    }else{
                        Toast.makeText(getApplicationContext(), "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showDialog(){

        // set title dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set pesan dari dialog
        alertDialogBuilder.setTitle("Peringatan!");
        alertDialogBuilder.setMessage("Apakah yakin data anda sudah benar ?");

        // set tombol ketika yes
        alertDialogBuilder.setPositiveButton("yes", (dialog, i) -> {
            Toast.makeText(getApplicationContext(), Datamessage
                    , Toast.LENGTH_SHORT).show();

            //intent membuka activity baru dan akan menumpuk activity semakin banyak
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);

            //ketika button yes di klik , akan mengakhiri proses
            // dan lebih clean menggunakan finish dibanding intent
            finish();
        });

        // set tombol ketika klik no
        alertDialogBuilder.setNegativeButton("No", (dialog, i) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        // change color text
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF6200EE"));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF6200EE"));
    }

    //fungsi volley
    private void setKuliner(String nama, String keterangan, String kontributor, String lat, String lon, String status){
        dialog();
        RequestQueue queue = Volley.newRequestQueue(KulinerActivity.this);
        String url = Apis.API_KULINER;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url ,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String Datastatus = jsonObject.getString("status");
                        Datamessage = jsonObject.getString("message");

                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (Datastatus.equals("success")) {
                            showDialog();
                            Log.d("STATUS_INSERT", "insert success ");
                        } else {
                            Toast.makeText(getApplicationContext(), "insert failed", Toast.LENGTH_SHORT).show();
                            Log.d("STATUS_INSERT", "insert failed ");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Params.JSON_AKSI, dataAksi);
                params.put(Params.JSON_NAMA, nama);
                params.put(Params.JSON_KETERANGAN, keterangan);
                params.put(Params.JSON_KONTRIBUTOR, kontributor);
                params.put(Params.JSON_LAT, lat);
                params.put(Params.JSON_LON, lon);
                params.put(Params.JSON_STATUS, status);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //fungsi fast
    private void fastKuliner(String nama, String keterangan, String kontributor, String lat, String lon){
        dialog();
        AndroidNetworking.post(Apis.API_KULINER)
                .addBodyParameter(Params.JSON_AKSI, dataAksi)
                .addBodyParameter(Params.JSON_NAMA, nama)
                .addBodyParameter(Params.JSON_KETERANGAN, keterangan)
                .addBodyParameter(Params.JSON_KONTRIBUTOR , kontributor)
                .addBodyParameter(Params.JSON_LAT, lat)
                .addBodyParameter(Params.JSON_LON , lon)
                .setPriority(Priority.MEDIUM)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String status = null;

                        try {
                            status = jsonObject.getString("status");
                            Datamessage = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        if(status.equals("success")){
                            showDialog();
                            Log.d("STATUS_INSERT" , "Insert success");
                        } else {
                            Log.d("STATUS_INSERT" , "Insert failed");
                            Toast.makeText(getApplicationContext(), "Insert Failed" ,Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), (CharSequence) anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //fungsi retrofit
    private void retrofitPostKuliner(String nama, String keterangan, String kontributor, String lat, String lon){
        dialog();
//        String simpan = "simpan";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Apis.API_KULINER_RETROFIT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPIKuliner retrofitAPIKuliner = retrofit.create(RetrofitAPIKuliner.class);

        Call<String> call = retrofitAPIKuliner.STRING_CALL(dataAksi, nama, keterangan, kontributor, lat, lon);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = null;

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        status = jsonObject.getString("status");
                        Datamessage = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if(status.equals("success")){
                        showDialog();
                        Log.d("STATUS_INSERT", "Insert success");
                    } else {
                        Toast.makeText(getApplicationContext(), "Insert Failed" , Toast.LENGTH_SHORT).show();
                        Log.d("STATUS_INSERT", "Insert failed");
                    }
                }
//            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", "onFailure" + t.getMessage());
            }
        });
    }
}