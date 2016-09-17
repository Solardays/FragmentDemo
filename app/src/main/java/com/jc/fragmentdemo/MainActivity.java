package com.jc.fragmentdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jc.fragmentdemo.base.BaseActivity;
import com.jc.fragmentdemo.dao.MainIView;
import com.jc.fragmentdemo.util.ScreenUtils;
import com.jc.fragmentdemo.view.fragment.FourFragment;
import com.jc.fragmentdemo.view.fragment.OneFragment;
import com.jc.fragmentdemo.view.fragment.ThreeFragment;
import com.jc.fragmentdemo.view.fragment.TwoFragment;

public class MainActivity extends BaseActivity implements MainIView {
    private DrawerLayout mDrawerLayout;
    private ImageView ivIcon;
    private RadioGroup rgBottom;

    private String[] fragmentTags={"fragment1Tag","fragment2Tag","fragment3Tag","fragment4Tag"};
    private FragmentManager fm;
    private int currentTagIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();

        initDragLayout();
        initView();
        initFragment(savedInstanceState);

    }

    private void initDragLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
    }

    private void initView() {
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //END即gravity.right 从右向左显示   START即left  从左向右弹出显示
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);//关闭抽屉
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);//打开抽屉
                }
            }
        });
        rgBottom = (RadioGroup) findViewById(R.id.rg_bottom);
        setBottom();
        rgBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("MainActivity",checkedId+"");
                switchFragment(checkedId);
            }

        });
    }

    private void setBottom() {
        Drawable drawable0 = getResources().getDrawable(R.drawable.bottom_radiobtn0_color);
        int pix = ScreenUtils.dip2px(this,25);
        drawable0.setBounds(0,0,pix,pix);
        ((RadioButton)rgBottom.getChildAt(0)).setCompoundDrawables(null,drawable0,null,null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.bottom_radiobtn1_color);
        drawable1.setBounds(0,0,pix,pix);
        ((RadioButton)rgBottom.getChildAt(1)).setCompoundDrawables(null,drawable1,null,null);
        Drawable drawable2 = getResources().getDrawable(R.drawable.bottom_radiobtn2_color);
        drawable2.setBounds(0,0,pix,pix);
        ((RadioButton)rgBottom.getChildAt(2)).setCompoundDrawables(null,drawable2,null,null);
        Drawable drawable3 = getResources().getDrawable(R.drawable.bottom_radiobtn3_color);
        drawable3.setBounds(0,0,pix,pix);
        ((RadioButton)rgBottom.getChildAt(3)).setCompoundDrawables(null,drawable3,null,null);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            Fragment fragment = new OneFragment();
            fm.beginTransaction()
                    .replace(R.id.container, fragment, fragmentTags[currentTagIndex]).commit();
        }
    }

    @Override
    public void switchFragment(int checkedId) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment currentFragment = fm.findFragmentByTag(fragmentTags[currentTagIndex]);
        if(currentFragment!=null){
            ft.hide(currentFragment);
        }

        switch (checkedId) {
            case R.id.cb1:
                showFragment(ft,0,OneFragment.class);
                break;
            case R.id.cb2:
                showFragment(ft,1,TwoFragment.class);
                break;
            case R.id.cb3:
                showFragment(ft,2,ThreeFragment.class);
                break;
            case R.id.cb4:
                showFragment(ft,3,FourFragment.class);
                break;
            default:
                break;
        }
        ft.commit();
    }

    /**显示fragment*/
    private void showFragment(FragmentTransaction ft, int index, Class<? extends Fragment> clazz) {
        Fragment fragment = fm.findFragmentByTag(fragmentTags[index]);
        if (fragment == null) {
            try {
                fragment = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            ft.add(R.id.container, fragment, fragmentTags[index]);
        } else {
            ft.show(fragment);
        }
        currentTagIndex = index;
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < rgBottom.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) rgBottom.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }


}
