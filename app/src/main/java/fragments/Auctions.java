package fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hecticus.ofertaloca.testapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento de la sección Auctions.
 */

public class Auctions extends Fragment {

    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;

    public Auctions() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_ofertaloca_pager, container, false);

        if (savedInstanceState == null) {
            insertarTabs(container);

            // Setear adaptador al viewpager.
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            setupViewPager(viewPager);
            pestanas.setupWithViewPager(viewPager);
        }

        return view;
    }



    private void insertarTabs(ViewGroup container) {
        View padre = (View) container.getParent();
        appBar = (AppBarLayout) padre.findViewById(R.id.appbar);
        pestanas = new TabLayout(getActivity());
        pestanas.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        pestanas.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        pestanas.setTabMode(TabLayout.MODE_FIXED);
        pestanas.setTabGravity(TabLayout.GRAVITY_FILL);

        appBar.addView(pestanas);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment(new AllAuctions(), getString(R.string.tab1));
        adapter.addFragment(new OpenAuctions(), getString(R.string.tab2));
        adapter.addFragment(new ComingAuctions(), getString(R.string.tab3));
        /*
        adapter.addFragment(new WinnersAuctions(), getString(R.string.tab4));
        */
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(pestanas);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
