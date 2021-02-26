package com.greendev.bebekninnileri.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.greendev.bebekninnileri.R;
import com.labo.kaji.fragmentanimations.CubeAnimation;

/**
 * Created by root on 03.03.2018.
 */

public class FragmentControlMain extends Fragment {

    private int DURATION = 400;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_control, container, false);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.UP, enter, DURATION);
    }
}
