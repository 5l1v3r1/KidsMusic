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
import com.labo.kaji.fragmentanimations.MoveAnimation;

import pl.droidsonroids.gif.GifImageView;

public class FragmentCubeAnimation extends Fragment {
    private int DURATION = 400;
    private int gifRawData = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_content, container, false);
        GifImageView gifImageView = (GifImageView)rootLayout.findViewById(R.id.myGifView);
        int rawDrawable = getArguments().getInt("drawable");
        gifImageView.setImageResource(rawDrawable);
        YoYo.with(Techniques.ZoomIn) //
                .duration(3000)
                .repeat(0)
                .playOn(rootLayout.findViewById(R.id.myGifView));
        return rootLayout;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.UP, enter, DURATION);
    }
}
