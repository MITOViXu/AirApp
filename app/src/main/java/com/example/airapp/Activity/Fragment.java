package com.example.airapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.airapp.Fragment.GrapFragment;
import com.example.airapp.Fragment.MainDashBoardFragment;
import com.example.airapp.Fragment.MapFragment;
import com.example.airapp.R;
import com.example.airapp.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Fragment extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        Bundle bundle = getIntent().getExtras();
                        String accessToken = bundle.getString("access_token");
                        Bundle bundle2 = getIntent().getExtras();
                        String username = bundle2.getString("user_name");
                        MainDashBoardFragment mainDashBoardFragment = MainDashBoardFragment.newInstance(accessToken, username);
                        getSupportFragmentManager().beginTransaction().replace(R.id.maindahboradFragment, mainDashBoardFragment).commit();
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_1).setChecked(true);
                        break;
                    case 1:
                        Bundle bundle3 = getIntent().getExtras();
                        String accessToken3 = bundle3.getString("access_token");
                        MapFragment mapFragment = MapFragment.newInstance(accessToken3);
                        getSupportFragmentManager().beginTransaction().replace(R.id.mapFragment, mapFragment).commit();
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                        break;
                    case 2:
                        Bundle bundle1 = getIntent().getExtras();
                        String accessToken1 = bundle1.getString("access_token");
                        GrapFragment grapFragment = GrapFragment.newInstance(accessToken1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.grapFragment, grapFragment).commit();
                        bottomNavigationView.getMenu().findItem(R.id.menu_tab_3).setChecked(true);
                        break;
                }
//                // Lấy access_token từ Intent gửi từ SignIn
//                Bundle bundle = getIntent().getExtras();
//                String accessToken = bundle.getString("access_token");
//
//                // Tạo Intent để chuyển sang MainDashBoardFragment
//                Intent intent = new Intent(Fragment.this, MainDashBoardFragment.class);
//
//                // Đính kèm access_token vào Bundle và set Bundle cho Intent
//                Bundle mainDashboardBundle = new Bundle();
//                mainDashboardBundle.putString("access_token", accessToken);
//                intent.putExtras(mainDashboardBundle);
//
//                // Start MainDashBoardFragment
//                startActivity(intent);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_tab_1) {
                    viewPager.setCurrentItem(0);
                } else if (item.getItemId() == R.id.menu_tab_2) {
                    viewPager.setCurrentItem(1);
                } else if (item.getItemId() == R.id.menu_tab_3) {
                    viewPager.setCurrentItem(2);
                }
                return true;
            }
        });
    }
}