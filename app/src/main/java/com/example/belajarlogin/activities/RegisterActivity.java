package com.example.belajarlogin.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.example.belajarlogin.interfaces.RetrofitAPIRegister;
import com.example.belajarlogin.utils.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    public String message = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("");

        //set navigation icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //binding variabel
        EditText etNama = findViewById(R.id.editTextRegisterNama);
        EditText etEmail = findViewById(R.id.editTextRegisterEmail);
        EditText etPassword = findViewById(R.id.editTextRegisterPassword);
        EditText etConfirm = findViewById(R.id.editTextRegisterConfirmPassword);
        Button btnDaftar = findViewById(R.id.buttonDaftar);
        Button btntoLogin = findViewById(R.id.buttontoLogin);

        // menjalankan fungsi ketika button di klik
        btntoLogin.setOnClickListener(view -> {// fungsi menuju ke activity register
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent); // jalankan activity ketika sudah di inialisasi
        });

        //menjalankan fungsi button register ketika di klik
        btnDaftar.setOnClickListener(view -> {
            String Nama = etNama.getText().toString();
            String Email = etEmail.getText().toString();
            String Password = etPassword.getText().toString();
            String ConfPass = etConfirm.getText().toString();

            if (Nama.isEmpty() || Email.isEmpty()
                    || Password.isEmpty() || ConfPass.isEmpty()){
                Toast.makeText(getApplicationContext(),
                        "Data harus diisi semua" , Toast.LENGTH_SHORT).show();
            }

            else if(!Password.equals(ConfPass)) Toast.makeText(getApplicationContext(),
                    "Password tidak sesuai", Toast.LENGTH_SHORT).show();

            else {
//                new SendRegisterData().execute(Nama, Email, Password);
                  sendRegister(Nama , Email, Password);
//                fastRegister(Nama, Email, Password);
//                retrofitPostDataRegister(Nama, Email, Password);
            }
        });

    }

    public static class getdata {
        static String message = null;
    }

    //fungsi async task
       private class SendRegisterData extends AsyncTask<String, Void, String> {
//          private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Mohon tunggu...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) { // titik tiga itu adalah array
                //buat varibel untuk tampung data array per index
                String nama = strings[0];
                String email = strings[1];
                String password = strings[2];

                //pastikan API support GET jika ingin menggunakan metode GET
                String responseJson = Network.getJson(Apis.API_REGISTER +
                        "?" + "nama=" + nama + "&" + "email=" + email + "&" + "password=" + password
                );

                Log.d("Status respons json" , "responJson" + responseJson);

                return responseJson;
            }

            @Override
            protected void onPostExecute(String s) { //getdata data ini adalah parameter yang berisikan class getdata
                if(progressDialog.isShowing()) progressDialog.dismiss();

                String status = null;
                String message = null;

                //kenalin class lain di class sendRegisterData
                //tujuan untuk tampung data biar bisa dipakai disemua class
                getdata data = new getdata();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    status = jsonObject.getString("status");
                    data.message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("test isi" , "isi message " + data.message);

                if(status.equals("success")){
                    showDialog();
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Log.d("STATUS_LOGIN", "login success " );
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Log.d("STATUS_LOGIN", "login failed ");
                }
            }
        }

    private void dialog(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //fungsi library volley
    private void sendRegister(String nama , String email, String password){
        dialog();

        //Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        // Request a string response from the provided URL.
        String url = Apis.API_REGISTER;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        message = jsonObject.getString("message");

                        Log.d("cek status", "isi response  : " + response);
                        Log.d("cek status", "isi status : " + status);

                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (status.equals("success")) {
                            showDialog();
                            Log.d("STATUS_LOGIN", "login success ");
                        } else {
                            Log.d("STATUS_LOGIN", "login failed ");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Params.JSON_NAMA, nama);
                params.put(Params.JSON_EMAIL, email);
                params.put(Params.JSON_PASSWORD, password);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //fungsi library fast
    private void fastRegister(String nama , String email, String password){
        dialog();
        AndroidNetworking.post(Apis.API_REGISTER)
                .addBodyParameter(Params.JSON_NAMA, nama)
                .addBodyParameter(Params.JSON_EMAIL, email)
                .addBodyParameter(Params.JSON_PASSWORD , password)
                .setPriority(Priority.MEDIUM)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String status = null;
                        String message = null;

                        try {
                            status = jsonObject.getString("status");
                            message = jsonObject.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        if(status.equals("success")){
                            Log.d("STATUS_LOGIN" , "login success");
                        } else {
                            Log.d("STATUS_LOGIN" , "login failed");
                        }

                        Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), (CharSequence) anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrofitPostDataRegister(String nama , String email, String password){
        dialog();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Apis.API_RETROFIT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPIRegister retrofitAPIRegister = retrofit.create(RetrofitAPIRegister.class);

        Call<String> call = retrofitAPIRegister.STRING_CALL(nama, email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String status = null;
                String message = null;

                if(response.isSuccessful() && response.body() != null){
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        status = jsonObject.getString("status");
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT).show();

                    if(status.equals("success")){
                        Log.d("STATUS LOGIN", "login success");
                    } else {
                        Log.d("STATUS_LOGIN", "login error");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", "onFailure" + t.getMessage());
            }
        });
    }


    // ketika navigation sudah diset, kita buat function nya
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDialog(){

        // set title dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set pesan dari dialog
        alertDialogBuilder.setTitle("Peringatan!");
        alertDialogBuilder.setMessage("Apakah yakin data anda sudah benar ?");

        // set tombol ketika yes
        alertDialogBuilder.setPositiveButton("yes", (dialog, i) -> {
            Toast.makeText(getApplicationContext(), message
                    , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
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
}