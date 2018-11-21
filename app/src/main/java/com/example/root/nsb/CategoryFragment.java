package com.example.root.nsb;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


public class CategoryFragment extends Fragment {
    GridView gridView;
    CategoryGridViewAdapter categoryGridViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryGridViewAdapter=new CategoryGridViewAdapter(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_category, container, false);
        gridView = rootView.findViewById(R.id.gridView);
        gridView.setAdapter(categoryGridViewAdapter);
        return rootView;
    }
}
