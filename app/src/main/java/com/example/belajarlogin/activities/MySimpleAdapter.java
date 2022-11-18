package com.example.belajarlogin.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.belajarlogin.R;
import com.example.belajarlogin.fragments.Tab2Fragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter implements Filterable {
    private Context mContext;
    public LayoutInflater inflater = null;

    public MySimpleAdapter(Context context,
                           List<? extends Map<String, ?>> data, int resource, String[] from,
                           int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.grid_item, null);

        TextView txtTitle = vi.findViewById(R.id.textViewTitle);
        TextView txtPrice = vi.findViewById(R.id.textViewPrice);
        TextView txtRating = vi.findViewById(R.id.textViewRating);

        HashMap<String, String> data = (HashMap<String, String>) getItem(position);


        txtTitle.setText((String) data.get("title"));
        txtPrice.setText((String) data.get("price"));
        Picasso.get().load((String) data.get("image")).into((ImageView) vi.findViewById(R.id.imageReview));

        return vi;
    }

}
