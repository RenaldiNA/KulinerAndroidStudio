package com.example.belajarlogin.activities;

import static com.example.belajarlogin.enums.SharedPre.MY_PREFS_NAME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belajarlogin.R;
import com.example.belajarlogin.TabLayoutAdapter;
import com.example.belajarlogin.enums.SharedPre;
import com.example.belajarlogin.fragments.Tab1Fragment;
import com.example.belajarlogin.fragments.Tab2Fragment;
import com.example.belajarlogin.fragments.Tab3Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.belajarlogin.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    TabLayoutAdapter tabLayoutAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    private String[] titles = new String[]{"Kuliner", "Review", "Gallery"};


    private FusedLocationProviderClient fusedLocationProviderClient;
    private EditText latitude, longtitude;
    private final static int REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        get data dari login activity
//        Intent intent = getIntent();

//        deklarasi variabel baru untuk menampung data yang berhasil di get
//        String email = intent.getStringExtra("email");
//        String nama = intent.getStringExtra("nama");
//        String foto = intent.getStringExtra("foto");

        //binding variabel tablayout dan viewpager
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

//        latitude = findViewById(R.id.editTextLatitude);
//        longtitude = findViewById(R.id.editTextLongtitude);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        //initialize adapter
        tabLayoutAdapter = new TabLayoutAdapter(this);
        viewPager.setAdapter(tabLayoutAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles[position])).attach();
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // floating button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getLocation();
                Intent goKuliner = new Intent(getApplicationContext(), KulinerActivity.class);
                startActivity(goKuliner);
            }
        });


        //deklarasi variabel baru untuk menampung data yang berhasil di get
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String perfEmail = prefs.getString(SharedPre.SP_EMAIL, "");// is the default value.
        String perfNama = prefs.getString(SharedPre.SP_NAMA, "");// is the default value.
        String perfFoto = prefs.getString(SharedPre.SP_FOTO, "");// is the default value.

        Log.d("cek nama", "data main nama " + perfNama);
        Log.d("cek email", "data main email " + perfEmail);
        Log.d("cek foto", "data main foto " + perfFoto);

        // ------------- NAV DRAWER ------------ //
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // color hamburger icon
        toggle.getDrawerArrowDrawable().setColor(
                getResources().getColor(R.color.white));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Toast.makeText(getApplicationContext(),
                            "Menu Nav Home selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    logOut();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //set data didalam navigation view
        View header = navigationView.getHeaderView(0);
        new DownloadImageFromInternet(header.findViewById(R.id.profile_image)).execute(perfFoto);

        TextView textViewNama = header.findViewById(R.id.textViewNama);
        textViewNama.setText(perfNama);

        TextView textViewEmail = header.findViewById(R.id.textViewEmail);
        textViewEmail.setText(perfEmail);

    }

    //fungsi klik ketika logout
    private void logOut() {
        SharedPreferences rm = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = rm.edit();
        String perfEmail= rm.getString(SharedPre.SP_EMAIL, "No Email Defined");

        //jika email tidak kosong
        if(!perfEmail.isEmpty()){
            editor.clear();
            editor.apply();
            sendToLogin();
        }
    }

    // fungsi menuju ke login
    private void sendToLogin() {
        //handel ketika klik tombol back akan langsung keluar ke home
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    //load image berupa url menggunakan async task
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        CircleImageView circleImageView;
        public DownloadImageFromInternet(ImageView image) {
            this.circleImageView= (CircleImageView) image;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            circleImageView.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //ketika nagivasi nya muncul , ketika klik back maka akan menutup kembali
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    public class TabLayoutAdapter extends FragmentPagerAdapter {
//        Context mContext;
//        int mTotalTabs;
//
//        public TabLayoutAdapter(Context context, FragmentManager fragmentManager, int totalTabs) {
//            super(fragmentManager);
//            mContext = context;
//            mTotalTabs = totalTabs;
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position){
//            Log.d("Cek posisi" , position + " ");
//            switch (position){
//                case 0:
//                    return new Tab1Fragment();
//                case 1:
//                    return new Tab2Fragment();
//                case 3:
//                    return new Tab3Fragment();
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public int getCount(){
//            return mTotalTabs;
//        }
//    }

}