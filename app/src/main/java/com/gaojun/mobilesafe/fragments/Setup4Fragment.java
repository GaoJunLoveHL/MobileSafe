package com.gaojun.mobilesafe.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gaojun.mobilesafe.R;
import com.gaojun.mobilesafe.activities.LostFindActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setup4Fragment extends Fragment {
    private View baseView;
    private Button btn_setup_complete;
    private SharedPreferences sp;
    private Context context;

    public Setup4Fragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        baseView = inflater.inflate(R.layout.fragment_setup4, container, false);
        btn_setup_complete = (Button) baseView.findViewById(R.id.btn_setup_complete);
        btn_setup_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("configed",true);
                editor.commit();
                Intent intent = new Intent(getActivity(), LostFindActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return baseView;
    }


}
