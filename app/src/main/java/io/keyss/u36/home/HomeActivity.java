package io.keyss.u36.home;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.keyss.base.view.BaseActivity;
import io.keyss.u36.R;
import io.keyss.u36.databinding.ActivityHomeBinding;
import io.keyss.u36.home.fragment.RecordFragment;
import io.keyss.u36.home.fragment.SettingsFragment;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initLayout(@Nullable Bundle savedInstanceState) {
        setTitle(R.string.title_record);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecordFragment());
        fragments.add(new SettingsFragment());
        binding.vpHomeAct.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        binding.bnvHomeAct.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.id1:
                    binding.vpHomeAct.setCurrentItem(0);
                    break;
                case R.id.id2:
                    binding.vpHomeAct.setCurrentItem(1);
                    break;
            }
            return false;
        });

        binding.vpHomeAct.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.bnvHomeAct.getMenu().getItem(position).setChecked(true);
                setActionBarTitle(position);
            }
        });

        // 方便调试
        //binding.vpHomeAct.setCurrentItem(1);

        new RxPermissions(mContext)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                        Toast.makeText(mContext, "权限正常", Toast.LENGTH_SHORT).show();
                    } else {
                        // At least one permission is denied
                        Toast.makeText(mContext, "缺少权限将无法正常使用本软件", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setActionBarTitle(int position) {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(position == 0 ? R.string.title_record : R.string.title_settings);
        }
    }
}
