package com.example.belajarlogin.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.belajarlogin.R;
import com.example.belajarlogin.fragments.Tab2Fragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomListAdapter extends BaseAdapter {

    private final Object Tab2Fragment;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater = null;

    public CustomListAdapter(Tab2Fragment a, ArrayList<HashMap<String, String>> d) {
        Tab2Fragment = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.grid_item, null); //This should be your row layout

        TextView txtTitle = vi.findViewById(R.id.textViewTitle);
        TextView txtPrice = vi.findViewById(R.id.textViewPrice);
//        TextView txtRating = vi.findViewById(R.id.textViewRating);

        HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        if(data != null) {

            String price = "$ " + (String) data.get("price");
            txtTitle.setText((String) data.get("title"));
            txtPrice.setText(price);
            Picasso.get().load((String) data.get("image")).into((ImageView) vi.findViewById(R.id.imageReview));
//            txtRating.setText((String) data.get("rate"));
//            Log.d("cek rating" , "status : " + txtRating);
        }
        return vi;
    }
}
