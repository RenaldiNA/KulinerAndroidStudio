package com.example.belajarlogin.fragments;

import static com.android.volley.toolbox.Volley.newRequestQueue;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.belajarlogin.R;
//import com.example.belajarlogin.activities.Contact;
import com.example.belajarlogin.activities.KulinerActivity;
//import com.example.belajarlogin.activities.NumbersView;
//import com.example.belajarlogin.activities.NumbersViewAdapter;
import com.example.belajarlogin.activities.RegisterActivity;
import com.example.belajarlogin.enums.Apis;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Tab1Fragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class Tab1Fragment extends Fragment{
    private ProgressDialog progressDialog;
    ListView listView;
    SearchView searchView;
    View contentView;
//    NumbersViewAdapter numbersArrayAdapter;
//    final ArrayList<NumbersView> arrayList1 = new ArrayList<NumbersView>();
    ArrayList<HashMap<String, String>> arrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    SimpleAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        contentView =  inflater.inflate(R.layout.fragment_tab1, container, false);

        //binding variabel didalam onCreateView
        listView = contentView.findViewById(R.id.listViewLayout);
        swipeRefreshLayout = contentView.findViewById(R.id.refreshLayout);
        searchView = contentView.findViewById(R.id.searchViewText);

        arrayList = new ArrayList<>();
        setKuliner();

        searchView.setActivated(true);

        // fungsi agar tidak perlu klik icon kaca pembesar
        // dan harus di set sebelum setOnQueryTextListener
        searchView.onActionViewExpanded();

        //fungsi untuk menandakan bahwa ada di fragment ini dan pakai this
//        searchView.setOnQueryTextListener(this);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //refresh listview from arraylist
                        arrayList.clear();

                        // This method performs the actual data-refresh operation.
                        setKuliner();

                        // The method calls setRefreshing(false) when it's finished.
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return contentView;
    }

    private void dialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //fungsi volley
    private void setKuliner(){
        dialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Apis.API_KULINER;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,
                response -> {
                    try {
                        //get data json
                        JSONObject jsonObject = new JSONObject(response);

                        //get data json array
                        JSONArray data = jsonObject.getJSONArray("data");

                        //looping data
                        for (int i = 0; i < data.length(); i ++){
                            JSONObject object = data.getJSONObject(i);
                            String namaKuliner = object.getString("nama");
                            String ket = object.getString("keterangan");

                            // tmp hash map for single allData
                            HashMap<String, String> allData = new HashMap<>();

//                            arrayList1.add(new NumbersView(namaKuliner , ket));

                            // adding each child node to HashMap key => value
                            allData.put("nama", namaKuliner);
                            allData.put("keterangan", ket);

                            // adding contact to array list
                            arrayList.add(allData);

                            //progress dialog
                            if(progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                        adapter = new SimpleAdapter(getActivity(),
                                arrayList,
                                R.layout.list_item,
                                new String[] {"nama" , "keterangan"} , new int[]{R.id.textViewNamaKuliner , R.id.textViewKeterangan});
                       listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    numbersArrayAdapter = new NumbersViewAdapter(getContext(), arrayList1);
//                    listView.setAdapter(numbersArrayAdapter);
//
                }, error -> Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}