package com.example.belajarlogin.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.belajarlogin.R;
import com.example.belajarlogin.activities.MySimpleAdapter;
import com.example.belajarlogin.enums.Apis;
//import com.example.belajarlogin.activities.Adapter;
//import com.example.belajarlogin.activities.MySimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Tab2Fragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class Tab2Fragment extends Fragment {
    private ProgressDialog progressDialog;
    GridView gridView;
    ImageView img;
    ArrayList<HashMap<String, String>> arrayList;
    public String image = "";
    public String title = "";
    public String price = "";
    public String rate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        gridView = view.findViewById(R.id.grid_view);
        img = view.findViewById(R.id.imageReview);

        arrayList = new ArrayList<>();
        setReview();

        return view;
    }

    private void dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //fungsi volley
    private void setReview(){
        dialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Apis.FAKE_API;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,
                response -> {
                    try {
                        //get data json array
                        JSONArray jsonArray = new JSONArray(response);

                        //looping data
                        for (int i = 0; i < jsonArray.length(); i ++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            JSONObject dataRating = object.getJSONObject("rating");

                            title = object.getString("title");
                            price = object.getString("price");
                            image = object.getString("image");
                            rate = dataRating.getString("rate");

                            // tmp hash map for single allData
                            HashMap<String, String> allData = new HashMap<>();

                            // adding each child node to HashMap key => value
                            allData.put("title", title);
                            allData.put("price", price);
                            allData.put("image" , image);
                            allData.put("rate" , rate);

                            // adding contact to array list
                            arrayList.add(allData);

                            //progress dialog
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Log.d("cek status", "isi namaKuliner  : " + title);
                            Log.d("cek status", "isi ket  : " + price);
                            Log.d("cek status", "isi image  : " + image);
                            Log.d("cek status", "isi rating  : " + rate);
                            Log.d("cek status", "isi array  : " + arrayList);

                        }

                        MySimpleAdapter adapter = new MySimpleAdapter(getContext(), arrayList,
                                R.layout.grid_item, new String[] {}, new int[] {});
                        gridView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}