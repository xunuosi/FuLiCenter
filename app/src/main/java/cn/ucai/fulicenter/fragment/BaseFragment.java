package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ucai.fulicenter.R;

public abstract class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView();
        initData();
        setListener();
        // Inflate the layout for this fragment
        return null;
    }

    protected abstract void setListener();

    protected abstract void initData();

    protected abstract void initView();
}
