package pceti.a8c1.cetitoolapp.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pceti.a8c1.cetitoolapp.Fragments.Horario.AgregarHorarioFragment;
import pceti.a8c1.cetitoolapp.Fragments.Horario.MostrarHorarioFragment;
import pceti.a8c1.cetitoolapp.R;

public class HorariosFragment extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horarios, container, false);

        View contenedor=(View)container.getParent();
        appBar = (AppBarLayout)contenedor.findViewById(R.id.appbar);
        tabs=new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        //appBar.addView(tabs);

        viewPager = (ViewPager)view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public  class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        String[] tituloTabs={"Mostrar","Agregar"};

        @Override
        public Fragment getItem(int position){

            switch (position){
                case 0:return new MostrarHorarioFragment();
                case 1:return new AgregarHorarioFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];
        }
    }
}
