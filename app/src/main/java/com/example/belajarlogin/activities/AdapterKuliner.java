//package com.example.belajarlogin.activities;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.example.belajarlogin.R;
//import com.example.belajarlogin.fragments.Tab2Fragment;
//
//import java.util.ArrayList;
//
//public class AdapterKuliner extends ArrayAdapter<ModelKuliner> {
//    public AdapterKuliner(@NonNull Context context, ArrayList<ModelKuliner> modelKulinerArrayList) {
//        super(context, 0, modelKulinerArrayList);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        View listitemView = convertView;
//
//        if (listitemView == null) {
//            // Layout Inflater inflates each item to be displayed in GridView.
//            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
//        }
//
//        ModelKuliner item = getItem(position);
//        ImageView img = listitemView.findViewById(R.id.imageReview);
//        TextView etTitle= listitemView.findViewById(R.id.textViewTitle);
//        TextView etPrice = listitemView.findViewById(R.id.textViewPrice);
//
//        img.setImageResource(item.getImage());
//        etTitle.setText(item.getTitle());
//        etPrice.setText(item.getPrice());
//
//        Glide.with(getContext())
//                .load(item)
//                .into(img);
//
//        return listitemView;
//    }
//}
