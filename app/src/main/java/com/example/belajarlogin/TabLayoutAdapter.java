package com.example.belajarlogin;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.belajarlogin.activities.MainActivity;
import com.example.belajarlogin.fragments.Tab1Fragment;
import com.example.belajarlogin.fragments.Tab2Fragment;
import com.example.belajarlogin.fragments.Tab3Fragment;

public class TabLayoutAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Kuliner","Review","Gallery"};

    public TabLayoutAdapter(@NonNull MainActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("Cek posisi" , position + " ");

        switch (position){
            case 0:
                return new Tab1Fragment();
            case 1:
                return new Tab2Fragment();
            default:
                return new Tab3Fragment();
        }
//        return new Tab3Fragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
