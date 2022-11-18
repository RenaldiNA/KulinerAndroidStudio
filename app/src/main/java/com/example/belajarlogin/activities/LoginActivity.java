package com.example.belajarlogin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.belajarlogin.enums.SharedPre;
import com.example.belajarlogin.interfaces.RetrofitAPILogin;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity<Private> extends AppCompatActivity {
    private EditText etEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // cek sudah dapat data dari sharedpreferences atau belum
        isLogin();

        //binding variabel
        etEmail = findViewById(R.id.editTextEmail);
        EditText etPassword = findViewById(R.id.editTextPassword);
        Button btnLogin = findViewById(R.id.buttonLogin);
        Button btnRegister = findViewById(R.id.buttonregister);

        // menjalankan fungsi ketika button di klik
        btnRegister.setOnClickListener(view -> {
            // fungsi menuju ke activity register
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent); // jalankan activity ketika sudah di inialisasi
        });

        // menjalankan fungsi button login ketika di klik
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                showSnackbar(view , "Email/Password harus diisi");
                // panggil fungsi snackbar dan beri value parameter
            } else {
                    sendLogin(email, password);
//                  retrofitPostDataLogin(email, password);
//                  fastLogin(email, password);
//                  new SendLoginData().execute(email, password);
            }
        });
    }

    public void isLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPre.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String dataEmail = sharedPreferences.getString(SharedPre.SP_EMAIL, "");//is default value
        Log.d("cek data login email " , "isi data " + dataEmail);

//         jika data email dapat akses ke main dipersilahkan
        if(!dataEmail.isEmpty()) {
            Intent goMain = new Intent(this , MainActivity.class);
            startActivity(goMain);
        }

    }

    //fungsi snackbar dan terdapat parameter
    public void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
         .setBackgroundTint(Color.parseColor("#FF6200EE"))
         .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
         .setAction("Oke", view1 -> {
         }).setActionTextColor(Color.parseColor("#FFFFFFFF"));
        snackbar.show();
    }

    //buat class getdata untuk menampung nilai status dan message
//    public class getdata {
//        String status = null;
//        String message = null;
//    }

    //munculin progressDialog / loading
    private void dialog(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //fungsi library volley
    private void sendLogin(String email, String password){
        dialog();
        //Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        // Request a string response from the provided URL.
        String url = Apis.API_LOGIN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        
                        JSONObject data = jsonObject.getJSONObject("data");
                        String nama = data.getString("nama");
                        String foto = data.getString("foto");

                        Log.d("cek status", "isi response  : " + response);
                        Log.d("cek status", "isi status : " + status);
                        Log.d("cek status", "isi data  : " + data);
                        Log.d("cek status", "isi nama  : " + nama);
                        Log.d("cek status", "isi foto  : " + foto);

                        if(progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if (status.equals("success")) {
                            Log.d("STATUS_LOGIN", "login success ");

                            SharedPreferences.Editor editor = getSharedPreferences(SharedPre.MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString(SharedPre.SP_NAMA, nama);
                            editor.putString(SharedPre.SP_EMAIL, email);
                            editor.putString(SharedPre.SP_FOTO, foto);
                            editor.commit();

                            //jika sukses ke halaman main
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);

//                          intent.putExtra("email" , email);
//                          intent.putExtra("nama" , nama);
//                          intent.putExtra("foto" , foto);

                        } else {
                            Log.d("STATUS_LOGIN", "login failed ");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Params.JSON_EMAIL, email);
                params.put(Params.JSON_PASSWORD, password);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    //fungsi library retrofit
    private void retrofitPostDataLogin(String email, String password){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Apis.API_RETROFIT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPILogin retrofitAPILogin = retrofit.create(RetrofitAPILogin.class);

        Call<String> call = retrofitAPILogin.STRING_CALL(email, password);
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

    //fungsi library fast
    private void fastLogin(String email, String password){
        dialog();

        //initialize dari library fast
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post(Apis.API_LOGIN)
                .addBodyParameter(Params.JSON_EMAIL, email)
                .addBodyParameter(Params.JSON_PASSWORD, password)
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

//    // async task login
//    private class SendLoginData extends AsyncTask<String, Void, getdata> {
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Mohon tunggu...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected getdata doInBackground(String... strings) { // titik tiga itu adalah array
//            //buat varibel untuk tampung data array per index
//            String email = strings[0];
//            String password = strings[1];
//
//            //buat variabel untuk tampung data api nya, berupa 1.URL 2.KEY 3.VALUE
//            String responseJson = postJson("http://192.3.168.178/flutter/login.php" ,
//                    "email", "password",
//                    email, password);
//
//            Log.d("Status respons json" , "responJson" + responseJson);
//
//            getdata data = new getdata(); //
//
//            try {
//                JSONObject jsonObject = new JSONObject(responseJson);
//                data.status = jsonObject.getString("status");
//                data.message = jsonObject.getString("message");
//
//                Log.d("cek message", "doInBackground: " + data.message);
//                Log.d("cek status", "doInBackground: " + data.status);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(getdata data) { //getdata data ini adalah parameter yang berisikan class getdata
//            if(progressDialog.isShowing()) progressDialog.dismiss();
//
//            if(data.status.equals("success")){
//                Toast.makeText(getApplicationContext(), data.message , Toast.LENGTH_SHORT).show();
//                Log.d("STATUS_LOGIN", "login success " + data.message);
//            } else {
//                Toast.makeText(getApplicationContext(), data.message , Toast.LENGTH_SHORT).show();
//                Log.d("STATUS_LOGIN", "login failed ");
//            }
//        }
//    }

//    public static class getData {
//        String status = null;
//        String message = null;
//    }

    // cara menggunakan response json langsung
//        private class SendLoginData extends AsyncTask<String, Void, Request<String>> {
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Mohon tunggu...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected Request<String> doInBackground(String... strings) { // titik tiga itu adalah array
//            //buat varibel untuk tampung data array per index
//            String email = strings[0];
//            String password = strings[1];
//
////            getData data = new getData();
////            final VolleyCallback callback = null;
//
//            // Instantiate the RequestQueue.
//            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//            // Request a string response from the provided URL.
//            String url = "http://192.3.168.178/flutter/login.php" + "?" + "email=" + email + "&" + "password=" + password;
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                status = jsonObject.getString("status");
//                                message = jsonObject.getString("message");
//
//                                Log.d("cek status", "isi response  : " + response);
//                                Log.d("cek status", "isi status : " + status);
//
//                                if (status.equals("success")) {
//                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                    Log.d("STATUS_LOGIN", "login success ");
//                                } else {
//                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                    Log.d("STATUS_LOGIN", "login failed ");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            });
//            // Add the request to the RequestQueue.
//            return queue.add(stringRequest);
//
////          return data;
//        }
//
//
//
//            @Override
//        protected void onPostExecute(Request<String> data) { //getdata data ini adalah parameter yang berisikan class getdata
//            if(progressDialog.isShowing()) progressDialog.dismiss();
//
//            Log.d("test", "cek data " + data);
////                Log.d("test", "cek status onpost " + status);
//
////                if (status.equals("success")) {
////                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
////                    Log.d("STATUS_LOGIN", "login success ");
////                } else {
////                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
////                    Log.d("STATUS_LOGIN", "login failed ");
////                }
//
//        }
//
//    }

    //cara menggunakan split
//    private class SendLoginData extends AsyncTask<String, Void, String> {
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Mohon tunggu...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) { // titik tiga itu adalah array
//            //buat varibel untuk tampung data array per index
//            String email = strings[0];
//            String password = strings[1];
//
//            //buat variabel untuk tampung data api nya, berupa 1.URL 2.KEY 3.VALUE
//            String responseJson = postJson("http://192.3.168.178/flutter/login.php" ,
//                    "email", "password",
//                    email, password);
//
//            Log.d("Status respons json" , "responJson" + responseJson);
//
//            String status = null;
//            try {
//                JSONObject jsonObject = new JSONObject(responseJson);
//                status = jsonObject.getString("status") + "," + jsonObject.getString("message");
//
//                Log.d("cek status", "doInBackground: " + status);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return status;
//        }
//
//        @Override
//        protected void onPostExecute(String s) { //getdata data ini adalah parameter yang berisikan class getdata
//            if(progressDialog.isShowing()) progressDialog.dismiss();
//
//            String[] data = s.split(",");
//            String dataStatus = data[0];
//            String dataMessage = data[1];
//
//            if(s.equals(dataStatus)){
//                Toast.makeText(getApplicationContext(), dataMessage, Toast.LENGTH_SHORT).show();
//                Log.d("STATUS_LOGIN", "login success " + dataMessage);
//            } else {
//                Toast.makeText(getApplicationContext(), dataMessage, Toast.LENGTH_SHORT).show();
//                Log.d("STATUS_LOGIN", "login failed ");
//            }
//        }
//    }


    //cara dengan mereturn jsonobject nya langsung
//    private class SendLoginData extends AsyncTask<String, Void, JSONObject> {
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute(){
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Mohon tunggu...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... strings) { // titik tiga itu adalah array
//            //buat varibel untuk tampung data array per index
//            String email = strings[0];
//            String password = strings[1];
//
//            //buat variabel untuk tampung data api nya, berupa 1.URL 2.KEY 3.VALUE
//            String responseJson = postJson("http://192.3.168.178/flutter/login.php" ,
//                    "email", "password",
//                    email, password);
//
//            Log.d("Status respons json" , "responJson" + responseJson);
//
//            JSONObject jsonObject = null;
//
//            Log.d("Status respons json" , "status" + jsonObject);
//            try {
//                jsonObject = new JSONObject(responseJson);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonObject;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) { //getdata data ini adalah parameter yang berisikan class getdata
//            if(progressDialog.isShowing()) progressDialog.dismiss();
//
//            String status = null;
//            String message = null;
//
//            try {
//                status = jsonObject.getString("status");
//                message = jsonObject.getString("message");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            if(status.equals("success")){
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                Log.d("STATUS_LOGIN", "login success " + message);
//            } else {
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                Log.d("STATUS_LOGIN", "login failed ");
//            }
//        }
//    }
}