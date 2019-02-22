package com.adyun.pskin;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adyun.pskin.fragment.MusicFragment;
import com.adyun.pskin.fragment.RadioFragment;
import com.adyun.pskin.fragment.VideoFragment;
import com.adyun.skin.core.SkinManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MusicFragment());
        fragments.add(new VideoFragment());
        fragments.add(new RadioFragment());

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.music));
        titles.add(getString(R.string.video));
        titles.add(getString(R.string.radio));

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(
                getSupportFragmentManager(),fragments,titles);

        viewPager.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // 重新进入的时候调用一遍
        SkinManager.getInstance().updateSkin(this);


    }

    /**
     *  进入换肤
     * @param view
     */
    public void skinSelect(View view) {
        startActivity(new Intent(this,SkinActivity.class));
    }
}
