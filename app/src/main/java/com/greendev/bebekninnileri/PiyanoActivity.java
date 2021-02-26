package com.greendev.bebekninnileri;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.cunoraz.gifview.library.GifView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pixplicity.easyprefs.library.Prefs;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class PiyanoActivity extends AppCompatActivity {

    @BindView(R.id.imgDoFirst) GifView imgDoFirst;
    @BindView(R.id.imgRe) GifView imgRe;
    @BindView(R.id.imgMI) GifView imgMI;
    @BindView(R.id.imgFA) GifView imgFA;
    @BindView(R.id.imgSOL) GifView imgSOL;
    @BindView(R.id.imgLA) GifView imgLA;
    @BindView(R.id.imgSI) GifView imgSI;
    @BindView(R.id.imgDOLast) GifView imgDOLast;
    @BindView(R.id.tvScorID) TextView tvScor;
    @BindView(R.id.viewAnimeFinish) KonfettiView viewAnimeFinish;

    @BindView(R.id.musicSpinner) MaterialSpinner musicListSpinner;
    private ArrayList<String> playMusicList = new ArrayList<>();
    private int haveToClickPosition = 0;
    private SoundPool soundPool;
    private int poolDOFirst;
    private int poolRE;
    private int poolMI;
    private int poolFA;
    private int poolSOL;
    private int poolLA;
    private int poolSI;
    private int poolDOLast;

    private Random random = new Random();
    private int scor = 0;

    private InterstitialAd mInterstitialAd;
    private AdView bannerDefault;
    private int fullPageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paio);
        ButterKnife.bind(this);
        loadSound();
        playGif();
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        addItemInSpinner();
        choseNotes(0);
        musicListSpinner.setOnItemSelectedListener(selectSpinner);
        nextClick(0);

        playMyAnime2();
        MDToast.makeText(getApplicationContext(), getResources().getString(R.string.pianoyu_cal), MDToast.LENGTH_LONG,
                MDToast.TYPE_INFO).show();
        openBannerADS();
    }

    private void openBannerADS(){
        if (Prefs.getBoolean("buy", false) != true) {
            MobileAds.initialize(this, getString(R.string.bannerID));
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.fullPageAds));
            bannerDefault = findViewById(R.id.bannerPiano);
            AdRequest adRequestdefault = new AdRequest.Builder().build();
            bannerDefault.loadAd(adRequestdefault);
        }

    }

    private void showFullAds(){
        fullPageCount = 0;
        if (Prefs.getBoolean("buy", false) != true) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
            });
        }
    }

    @OnClick({R.id.lytDOFirst, R.id.tvDoFirst, R.id.imgDoFirst, R.id.ttvDoFirst})
    public void lytDOFirst(){

        soundPool.play(poolDOFirst, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("DO");

    }

    @OnClick({R.id.lytRE, R.id.tvRE, R.id.imgRe, R.id.ttvRE})
    public void lytRE(){
        soundPool.play(poolRE, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("RE");
    }

    @OnClick({R.id.lytMI, R.id.tvMI, R.id.imgMI, R.id.ttvMI})
    public void lytMI(){
        soundPool.play(poolMI, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("MI");
    }

    @OnClick({R.id.lytFA, R.id.tvFA, R.id.imgFA, R.id.ttvFA})
    public void lytFA(){
        soundPool.play(poolFA, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("FA");
    }

    @OnClick({R.id.lytSOL, R.id.tvSOL, R.id.imgSOL, R.id.ttvSOL})
    public void lytSOL(){
        soundPool.play(poolSOL, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("SOL");
    }

    @OnClick({R.id.lytLA, R.id.tvLA, R.id.imgLA, R.id.ttvLA})
    public void lytLA(){
        soundPool.play(poolLA, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("LA");
    }

    @OnClick({R.id.lytSI, R.id.tvSI, R.id.imgSI, R.id.ttvSI})
    public void lytSI(){
        soundPool.play(poolSI, 0.99f, 0.99f, 1, 0, 0.99f);
        clickNoteMatch("SI");
    }

    @OnClick({R.id.lytDOLast, R.id.tvDOLast, R.id.imgDOLast, R.id.ttvDOLast})
    public void lytDOLast(){
        soundPool.play(poolDOLast, 0.99f, 0.99f, 1, 0, 0.99f);
    }

    private void loadSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()).build();
        } else {
            soundPool = new SoundPool(10, 3, 0);
        }

        poolDOFirst = soundPool.load(this, R.raw.c, 1);
        poolRE = soundPool.load(this, R.raw.d, 1);
        poolMI = soundPool.load(this, R.raw.e, 1);
        poolFA = soundPool.load(this, R.raw.f, 1);
        poolSOL = soundPool.load(this, R.raw.g, 1);
        poolLA = soundPool.load(this, R.raw.a, 1);
        poolSI = soundPool.load(this, R.raw.b, 1);
        poolDOLast = soundPool.load(this, R.raw.ctiz, 1);
    }


    private void playGif(){
        imgDoFirst.play();
        imgRe.play();
        imgMI.play();
        imgFA.play();
        imgSOL.play();
        imgLA.play();
        imgSI.play();
        imgDOLast.play();
    }

    private void addItemInSpinner(){
        musicListSpinner.setItems(
                "Kutu Kutu Pense",
                "Küçük Kurbağa",
                "Neşeli Ol Ki genç Kalasın",
                "Bak Postacı Geliyor",
                "Yaşasın Okulumuz",
                "Süt İçtim Dilim Yandı",
                "Tren Gelir Hoş Gelir",
                "Fış Fış Kayıkçı");
        }

    MaterialSpinner.OnItemSelectedListener selectSpinner = new MaterialSpinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            choseNotes(position);
            haveToClickPosition = 0;
            scor = 0;
            tvScor.setText("Puan\n"+scor);
            nextClick(0);
            MDToast.makeText(getApplicationContext(), getResources().getString(R.string.selected_music_piano)+" "+view.getText(), MDToast.LENGTH_LONG, MDToast.TYPE_INFO).show();
        }
    };


    private void clickNoteMatch(String note){
        if (!(haveToClickPosition<playMusicList.size())){
            return;
        }
        if (playMusicList.get(haveToClickPosition).equalsIgnoreCase(note)) {
            haveToClickPosition++;
            nextClick(haveToClickPosition);
            scor = scor+random.nextInt(3)+1;
            tvScor.setText("Puan\n"+scor);
        }

    }

    private void nextClick(int position){
        setImageInvisible();
        if (!(playMusicList.size()>position)){
            MDToast.makeText(getApplicationContext(), getResources().getString(R.string.finish_piano_msg), MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
            playMyAnime2();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showFullAds();
                }
            }, 4000);
            return;
        }
        switch (playMusicList.get(position)){
            case "DO":
                imgDoFirst.setVisibility(View.VISIBLE);
                break;

            case "RE":
                imgRe.setVisibility(View.VISIBLE);
                break;

            case "MI":
                imgMI.setVisibility(View.VISIBLE);
                break;

            case "FA":
                imgFA.setVisibility(View.VISIBLE);
                break;

            case "SOL":
                imgSOL.setVisibility(View.VISIBLE);
                break;

            case "LA":
                imgLA.setVisibility(View.VISIBLE);
                break;

            case "SI":
                imgSI.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void choseNotes(int position){
        playMusicList.clear();
        switch (position){
            case 0:
                addValueMyList(SongCreator.kutuKutuPense());
                break;

            case 1:
                addValueMyList(SongCreator.kucukKurbaga());
                break;

            case 2:
                addValueMyList(SongCreator.neseliOlKiGenc());
                break;

            case 3:
                addValueMyList(SongCreator.bakPostaciGeliyor());
                break;

            case 4:
                addValueMyList(SongCreator.yasasinOkulumuz());
                break;

            case 5:
                addValueMyList(SongCreator.sutIctimDilimYandi());
                break;

            case 6:
                addValueMyList(SongCreator.trenGelirHosGelir());
                break;

            case 7:
                addValueMyList(SongCreator.fisFisKayikci());
                break;

        }
    }
    private void addValueMyList(ArrayList<String> arrayList){
        for (int i = 0; i<arrayList.size(); i++){
            playMusicList.add(arrayList.get(i));
        }
    }
    private void setImageInvisible(){
        if (imgDoFirst.getVisibility() == View.VISIBLE) {
            imgDoFirst.setVisibility(View.INVISIBLE);
        }

        if (imgRe.getVisibility() == View.VISIBLE) {
            imgRe.setVisibility(View.INVISIBLE);
        }

        if (imgMI.getVisibility() == View.VISIBLE) {
            imgMI.setVisibility(View.INVISIBLE);
        }

        if (imgFA.getVisibility() == View.VISIBLE) {
            imgFA.setVisibility(View.INVISIBLE);
        }

        if (imgSOL.getVisibility() == View.VISIBLE) {
            imgSOL.setVisibility(View.INVISIBLE);
        }

        if (imgLA.getVisibility() == View.VISIBLE) {
            imgLA.setVisibility(View.INVISIBLE);
        }

        if (imgDoFirst.getVisibility() == View.VISIBLE) {
            imgDoFirst.setVisibility(View.INVISIBLE);
        }

        if (imgSI.getVisibility() == View.VISIBLE) {
            imgSI.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        soundPool.release();
        stopGif();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void stopGif(){
        imgDoFirst.pause();
        imgRe.pause();
        imgMI.pause();
        imgFA.pause();
        imgSOL.pause();
        imgLA.pause();
        imgSI.pause();
        imgDOLast.pause();
    }

    private void playMyAnime2(){
        viewAnimeFinish.build()
                .addColors(Color.BLUE, Color.RED, Color.CYAN)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 3f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2800L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 12f))
                .setPosition(-50f, viewAnimeFinish.getWidth() + 50f, -50f, -50f)
                .stream(300, 4000L);
    }
}
