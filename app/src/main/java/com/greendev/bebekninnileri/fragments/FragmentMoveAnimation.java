package com.greendev.bebekninnileri.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.greendev.bebekninnileri.R;
import com.labo.kaji.fragmentanimations.CubeAnimation;

import pl.droidsonroids.gif.GifImageView;

public class FragmentMoveAnimation extends Fragment {
    private int DURATION = 400;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_content, container, false);
        GifImageView gifImageView = (GifImageView)rootLayout.findViewById(R.id.myGifView);
        int rawDrawable = getArguments().getInt("drawable");
        gifImageView.setImageResource(rawDrawable);
        YoYo.with(Techniques.ZoomInUp) //
                .duration(3000)
                .repeat(0)
                .playOn(rootLayout.findViewById(R.id.myGifView));
        return rootLayout;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, DURATION);
    }
}
