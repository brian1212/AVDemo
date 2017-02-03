package brian.com.avdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import brian.com.avdemo.R;
import brian.com.avdemo.adapter.ViewPagerAdapter;
import brian.com.avdemo.fragment.FavoriteFragment;
import brian.com.avdemo.fragment.HomeFragment;
import brian.com.avdemo.fragment.InfoFragment;
import brian.com.avdemo.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int count = 0;
    private AdView avBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        avBanner = (AdView) findViewById(R.id.av_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        avBanner.loadAd(adRequest);
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.home_selector,
                R.drawable.fav_selector,
                R.drawable.search_selector,
                R.drawable.info_selector
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.setSelectedTabIndicatorHeight(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment());
        adapter.addFrag(new FavoriteFragment());
        adapter.addFrag(new SearchFragment());
        adapter.addFrag(new InfoFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        if (viewPager.getCurrentItem() == 0 && count == 1) {
            EventBus.getDefault().post("back");
            count = 0;
        } else {
            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, getString(R.string.lbl_press_one_more_time_to_exit), Toast.LENGTH_SHORT).show();
            displayToast();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 5000);
        }

    }

    @Subscribe
    public void onEvent(String event) {
        if (event.equals("drawer")) {
            count = 1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        if (avBanner != null) {
            avBanner.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (avBanner != null) {
            avBanner.resume();
        }
    }

    @Override
    protected void onStop() {
        if (avBanner != null) {
            avBanner.destroy();
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void displayToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_view, (ViewGroup) findViewById(R.id.toastParent));
//        ImageView image = (ImageView) layout.findViewById(R.id.image);
//        image.setImageResource(R.drawable.ic_launcher);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(getString(R.string.lbl_press_one_more_time_to_exit));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
