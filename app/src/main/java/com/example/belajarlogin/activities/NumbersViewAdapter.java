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
//import com.example.belajarlogin.R;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//public class NumbersViewAdapter extends ArrayAdapter<NumbersView> {
//    private TextView etNama , etKeterangan;
//    private ArrayAdapter<NumbersView> adapter;
////    private List<NumbersView> mContacts = null;
//    ArrayList<NumbersView> arrayList = new ArrayList<NumbersView>();
//
//
//    // invoke the suitable constructor of the ArrayAdapter class
//    public NumbersViewAdapter(@NonNull Context context, ArrayList<NumbersView> arrayList) {
//
//        // pass the context and arrayList for the super
//        // constructor of the ArrayAdapter class
//        super(context, 0, arrayList);
////        this.arrayList.addAll(mContacts);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        // convertView which is recyclable view
//        View currentItemView = convertView;
//
//        // of the recyclable view is null then inflate the custom layout for the same
//        if (currentItemView == null) {
//            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
//        }
//
//        // get the position of the view from the ArrayAdapter
//        NumbersView currentNumberPosition = getItem(position);
//
//        // then according to the position of the view assign the desired image for the same
////        ImageView numbersImage = currentItemView.findViewById(R.id.imageReview);
////        assert currentNumberPosition != null;
////        numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());
//
//        // then according to the position of the view assign the desired TextView 1 for the same
//        etNama = currentItemView.findViewById(R.id.textViewNamaKuliner);
//        etNama.setText(currentNumberPosition.getNumberInDigit());
//
//        // then according to the position of the view assign the desired TextView 2 for the same
//        etKeterangan = currentItemView.findViewById(R.id.textViewKeterangan);
//        etKeterangan.setText(currentNumberPosition.getNumbersInText());
//
//        // then return the recyclable view
//        return currentItemView;
//    }
//
////    public void filter(String charText) {
////        charText = charText.toLowerCase(Locale.getDefault());
////        arrayList.clear();
////        if (charText.length() == 0) {
////            mContacts.addAll(arrayList);
////        } else {
////            for (NumbersView wp : arrayList) {
////                if (wp.getNumberInDigit().toLowerCase(Locale.getDefault())
////                        .contains(charText)) {
////                    mContacts.add(wp);
////                }
////            }
////        }
////        notifyDataSetChanged();
////    }
//
//
////    // filter name in Search Bar
////    public void filter(String characterText) {
////        characterText = characterText.toLowerCase(Locale.getDefault());
////        mContacts.clear();
////        if (characterText.length() == 0) {
////            mContacts.addAll(arrayList);
////        } else {
////            mContacts.clear();
////            for (NumbersView contact: arrayList) {
////                if (contact.getNumberInDigit().toLowerCase(Locale.getDefault()).contains(characterText)) {
////                    mContacts.add(contact);
////                }
////            }
////        }
////        notifyDataSetChanged();
////    }
//}
