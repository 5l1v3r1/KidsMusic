package com.greendev.bebekninnileri;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.greendev.bebekninnileri.adapters.MusicListAdapter;
import com.greendev.bebekninnileri.configs.Constant;
import com.greendev.bebekninnileri.fragments.FragmentCubeAnimation;
import com.greendev.bebekninnileri.fragments.FragmentFlipAnimation;
import com.greendev.bebekninnileri.fragments.FragmentMoveAnimation;
import com.greendev.bebekninnileri.fragments.FragmentPushAnimation;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.pixplicity.easyprefs.library.Prefs;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import tyrantgit.explosionfield.ExplosionField;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener{
    private AdvanceDrawerLayout drawer;
    private MediaPlayer mediaPlayer =new MediaPlayer();

    public MusicListAdapter musicListAdapter;
    private int exitAppCount = 0;
    Toolbar toolbar;

    NotificationManagerCompat notificationManager;
    Intent notifyIntent;

    private ArrayList<String> mediaNameList = new ArrayList<>();
    private ArrayList<Integer> mediaRawList = new ArrayList<>();
    private int mediaPosition = 0;
    private boolean autoPlay = true;
    public boolean isMediaPlay = false;

    @BindView(R.id.playButtonID) ImageButton playMediaButton;
    @BindView(R.id.autoButtonID) ImageButton autoPlayMediaButton;
    @BindView(R.id.lvMediaPlayer) ListView surahListView;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvLastTime)
    TextView tvLastTime;
    @BindView(R.id.progressBar)
    DiscreteSeekBar progressBar;
    public ExplosionField mExplosionField;
    private Fragment fragment = null;
    private FragmentManager fm = null;
    public FragmentTransaction fragmentTransaction = null;
    private int gifPosition = 0;
    private ArrayList<Integer> imageArray = new ArrayList<>();
    private InterstitialAd mInterstitialAd;
    public AdView bannerDefault;
    private int fullPageCount = 0;
    public  @BindView(R.id.viewKonfetti) KonfettiView konfettiView;

    private BillingClient mBillingClient;
    @BindView(R.id.txtBuyApps) TextView tvBuy;
    @BindView(R.id.btnBuyApps) ImageButton btnBuy;
    @BindView(R.id.lytBuyApps)
    LinearLayout lytBuy;

    private ArrayList<String> mediaDescription = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = NotificationManagerCompat.from(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerView.getId()==R.id.nav_view_notification){
                    //applaySettings();
                }
            }
        };

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.drawer_icon);
        toolbar.setNavigationIcon(R.drawable.drawer_icon);
        toolbar.setNavigationOnClickListener(clickToolbar);
        drawer.addDrawerListener(toggle);
        ButterKnife.bind(this);

        drawer.useCustomBehavior(Gravity.START);
        drawer.useCustomBehavior(Gravity.END);
        drawer.openDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        surahListView.setOnItemClickListener(clickMediaList);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        init();
        fm = getSupportFragmentManager();
        addImageMyArray();
        prepareFirstFragment();

        playMyAnime();
        prepareMaterialProgress();

        openBannerADS();
}

    private void playMyAnime(){
        konfettiView.build()
                .addColors(Color.YELLOW, Color.RED, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(2f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(4000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 12f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(300, 6000L);
    }

    private void openBannerADS(){

        bilingStates();
        if (Prefs.getBoolean("buy", false) != true) {

            MobileAds.initialize(this, getString(R.string.bannerID));
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.fullPageAds));
            bannerDefault = findViewById(R.id.bannerDefault);
            AdRequest adRequestdefault = new AdRequest.Builder().build();
            bannerDefault.loadAd(adRequestdefault);
        } else {
            isBuyContent();
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

    @OnClick(R.id.btnCube)
    public void clickCube(){

        fragment = new FragmentCubeAnimation();
        Bundle bundle = new Bundle();
        bundle.putInt("drawable", takeImageFromLst());
        fragment.setArguments(bundle);
        changeFragment();
        playMyAnime2();
    }

    @OnClick(R.id.btnFlip)
    public void clickFlip(){

        fragment = new FragmentFlipAnimation();
        Bundle bundle = new Bundle();
        bundle.putInt("drawable", takeImageFromLst());
        fragment.setArguments(bundle);
        changeFragment();
        playMyAnime2();
    }

    @OnClick(R.id.btnMove)
    public void clickMove(){

        fragment = new FragmentMoveAnimation();
        Bundle bundle = new Bundle();
        bundle.putInt("drawable", takeImageFromLst());
        fragment.setArguments(bundle);
        changeFragment();
        playMyAnime2();
    }

    @OnClick(R.id.btnPush)
    public void clickPush(){

        fragment = new FragmentPushAnimation();
        Bundle bundle = new Bundle();
        bundle.putInt("drawable", takeImageFromLst());
        fragment.setArguments(bundle);
        changeFragment();
        playMyAnime2();
    }

    private void prepareFirstFragment(){
        fragment = new FragmentMoveAnimation();
        Bundle bundle = new Bundle();
        bundle.putInt("drawable", takeImageFromLst());
        fragment.setArguments(bundle);
        changeFragment();
    }

    private int takeImageFromLst(){
        gifPosition++;
        int image = 0;
        if (!(gifPosition<imageArray.size())) {
            gifPosition = 0;
        }
        image = imageArray.get(gifPosition);
        return image;
    }

    private void changeFragment(){
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fragment);

        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();

    }
    private void init(){

        mExplosionField = ExplosionField.attach2Window(this);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        prepareMediaList();

        notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      /*  notifyIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notifyIntent.setAction(Intent.ACTION_MAIN);
        */
        autoPlay = Prefs.getBoolean("autoPlay", true);
        if (autoPlay){
            autoPlayMediaButton.setImageResource(R.mipmap.repeat_enabled_r);
        } else {
            autoPlayMediaButton.setImageResource(R.mipmap.repeat_disable_r);
        }

        stopMediPlayer();
    }

    private void prepareMediaList(){
        mediaNameList.clear();
        mediaRawList.clear();
        mediaDescription.clear();

        musicListAdapter = new MusicListAdapter(getApplicationContext(), mediaNameList);
        surahListView.setAdapter(musicListAdapter);
        musicListAdapter.notifyDataSetChanged();
        for(String name: getResources().getStringArray(R.array.baslik)) {
            mediaNameList.add(name.trim());
        }

        for(String name: getResources().getStringArray(R.array.detay)) {
            mediaDescription.add(name.trim());
        }

        for (int i =1; i<=22; i++){
            int rawFile = getResources().getIdentifier("@raw/m"+i, "raw", getPackageName());
            mediaRawList.add(rawFile);

        }

    }


    AdapterView.OnItemClickListener clickMediaList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            closeDrawers();
            mediaPosition = position;
            startMediaPlayer();
        }
    };

    private void startMediaPlayer(){
        updateNotification();
        playMyAnime();


        playMediaButton.setImageResource(R.mipmap.pause_r);
        surahListView.requestFocusFromTouch(); surahListView.setSelection(mediaPosition); surahListView.requestFocus();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer =null;
        }



        mediaPlayer = MediaPlayer.create(this, mediaRawList.get(mediaPosition));
        mediaPlayer.start();


        toolbar.setTitle(mediaNameList.get(mediaPosition));
        long minute = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration());
        long second = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration());
        second = second%60;
        tvLastTime.setText(minute+":"+second);
        progressBar.setMax((int) TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration()));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (autoPlay){
                    playNextMedia();

                } else {
                    stopMediPlayer();
                }
            }
        });
    }

    private void playNextMedia() {
        stopMediPlayer();
        fullPageCount++;
        if (fullPageCount>=3){
            fullPageCount = 0;
            showFullAds();
        }
        mediaPosition++;
        if (mediaPosition < mediaNameList.size()){
            startMediaPlayer();
        } else {
            mediaPosition = 0;
            startMediaPlayer();
        }

        fragment = new FragmentPushAnimation();
        Bundle bundle = new Bundle();
        bundle.putInt("drawable", takeImageFromLst());
        fragment.setArguments(bundle);
        changeFragment();
    }

    @OnClick(R.id.playButtonID)
    public void clickPlayMedia(ImageButton imageButton){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            playMediaButton.setImageResource(R.mipmap.play_r);
        } else if (mediaPlayer != null){
            mediaPlayer.start();
            playMediaButton.setImageResource(R.mipmap.pause_r);
        } else {
            startMediaPlayer();
        }

        mExplosionField.explode(imageButton);
        clearAnime(imageButton);
    }
    @OnClick(R.id.backButtonID)
    public void clickBackButton(ImageButton imageButton){
        stopMediPlayer();
        mediaPosition--;
        if (mediaPosition >= 0){
            startMediaPlayer();
        } else {

            showMessage(getResources().getString(R.string.first_mp3), MDToast.TYPE_INFO);
            stopMediPlayer();
            mediaPosition = 0;
            openDrawers();
        }
        mExplosionField.explode(imageButton);
        clearAnime(imageButton);
    }

    @OnClick(R.id.nextButtonID)
    public void clickNextButton(ImageButton imageButton){
        playNextMedia();
        mExplosionField.explode(imageButton);
        clearAnime(imageButton);
    }

    @OnClick(R.id.autoButtonID)
    public void setAutoPlay(ImageButton imageButton){
        autoPlay = Prefs.getBoolean("autoPlay", true);
        if (autoPlay){
            autoPlay = false;
            Prefs.putBoolean("autoPlay", autoPlay);
            imageButton.setImageResource(R.mipmap.repeat_disable_r);
        } else {
            autoPlay = true;
            Prefs.putBoolean("autoPlay", autoPlay);
            imageButton.setImageResource(R.mipmap.repeat_enabled_r);
        }
        mExplosionField.explode(imageButton);
        clearAnime(imageButton);
    }

    @OnClick(R.id.btnStartPiano)
    public void startPianoActivity(){
        stopMediPlayer();
        startActivity(new Intent(getApplicationContext(), PiyanoActivity.class));
        finish();
    }
    @OnClick(R.id.stopButtonID)
    public void clickStopMedia(ImageButton imageButton){
        stopMediPlayer();
        mExplosionField.explode(imageButton);
        clearAnime(imageButton);
    }

    private void stopMediPlayer(){
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer =null;
        }
        playMediaButton.setImageResource(R.mipmap.play_r);
        dismisNotification();
        isMediaPlay = false;
    }

    private void updateNotification(){
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "media_playback_channel");
        notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.holoDarkGreen))
                .setSmallIcon(R.mipmap.play_r)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentIntent(notifyPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(mediaNameList.get(mediaPosition))
                .setContentText(getString(R.string.app_name))
                .setOngoing(true)
                .setAutoCancel(false);

        notificationManager.notify(0, notificationBuilder.build());
    }


    private void dismisNotification(){
        notificationManager.cancel(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_setting:
                drawer.openDrawer(Gravity.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.settingBar)
    public void clickSetting(){
        if (drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (exitAppCount<1){
            exitAppCount+=1;

            showMessage(getResources().getString(R.string.exit_message), MDToast.TYPE_INFO);
        } else {
            super.onBackPressed();
        }
    }

    View.OnClickListener clickToolbar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!drawer.isDrawerOpen(GravityCompat.START)){
                drawer.openDrawer(GravityCompat.START);
            }
        }
    };

    @OnClick(R.id.btnShareApp)
    public void shareApp(){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String appPackageName = getPackageName();
        String text = "App: "+getResources().getText(R.string.app_name) +"\n\n"+"Google Play: https://play.google.com/store/apps/details?id=" + appPackageName;
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share App"));
    }

    @OnClick(R.id.btnRateApp)
    public void rateApp(){
        String appPackageName = getPackageName();
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @OnClick(R.id.btnMoreApp)
    public void moreApp(){
        String appPackageName = Constant.DEVELOPER_ACCOUNT;
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void showMessage(String message, int toastType){
        MDToast mdToast = MDToast.makeText(getApplicationContext(), message, MDToast.LENGTH_SHORT, toastType);
        mdToast.show();
    }

    private void closeDrawers(){
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }

        if (drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }
    }

    private void openDrawers(){
        if (!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void clearAnime(final View imageButton){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mExplosionField.clear();
                imageButton.setScaleX(1f);
                imageButton.setScaleY(1f);
                imageButton.setAlpha(1f);
            }
        }, 1500);
    }

    private void addImageMyArray(){
        imageArray.clear();
        gifPosition = 0;
        imageArray.add(R.drawable.gif_butterfly8);
        imageArray.add(R.drawable.gif_bird);
        imageArray.add(R.drawable.gif_butterfly2);
        imageArray.add(R.drawable.gif_butterfly3);
        imageArray.add(R.drawable.gif_flybird1);
        imageArray.add(R.drawable.gif_fly2);
        imageArray.add(R.drawable.gif_flybird4);
        imageArray.add(R.drawable.gif_rabit);
        imageArray.add(R.drawable.gif_crow);
        imageArray.add(R.drawable.gif_duck);
        imageArray.add(R.drawable.gif_cat);
        imageArray.add(R.drawable.gif_butterfly5);
        imageArray.add(R.drawable.gif_eagle);
        imageArray.add(R.drawable.gif_small_cat);
        imageArray.add(R.drawable.gif_butterfly7);
        imageArray.add(R.drawable.gif_flowwer);
        imageArray.add(R.drawable.gif_hunter);
        imageArray.add(R.drawable.gif_flower2);
        imageArray.add(R.drawable.gif_walk);
    }

    private void playMyAnime2(){
        konfettiView.build()
                .addColors(Color.BLUE, Color.RED, Color.CYAN)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 3f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2800L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 12f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(300, 4000L);
    }

    private void prepareMaterialProgress(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long second = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition());
                    second = second%60;
                    if (second<10){
                        tvStatus.setText(minutes+":0"+second);
                    } else {
                        tvStatus.setText(minutes+":"+second);
                    }

                    progressBar.setProgress((int)TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()));

                }
                prepareMaterialProgress();

            }
        },1000);
    }


    @OnClick({R.id.lytBuyApps, R.id.btnBuyApps, R.id.txtBuyApps, R.id.txtbyApps})
    public void buyClicked(){
        buyOneYear("one_year_time");
    }
    private void startSplash(){
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        this.finish();
    }
    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            //satın alma başarılı
            Prefs.putBoolean("buy", true);

            for (final Purchase purchase : purchases) {
                mBillingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(int responseCode, String purchaseToken) {
                        if (responseCode == BillingClient.BillingResponse.OK) {


                        }
                    }
                });
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startSplash();
                }
            }, 3000);

        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {//kullanıcı iptal etti
            Prefs.putBoolean("buy", false);

        }
    }

    private void isBuyContent(){
        boolean state = Prefs.getBoolean("buy", false);
        if (state){
            lytBuy.setVisibility(View.GONE);
            btnBuy.setVisibility(View.GONE);
            tvBuy.setVisibility(View.GONE);
        } else {
            openBannerADS();
        }
    }

    private void bilingStates(){
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    enableOrDisableButtons(true);

                } else {
                    enableOrDisableButtons(false);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

                enableOrDisableButtons(false);
            }
        });
    }

    private void enableOrDisableButtons(boolean isEnabled) {
        lytBuy.setEnabled(isEnabled);
        btnBuy.setEnabled(isEnabled);
        tvBuy.setEnabled(isEnabled);
    }

    private void buyOneYear(String skuId) {
        //haftalık,aylık,3 aylık,6 aylık ,yıllık üyelik için
        //Buradaki skuId , google playde tanımladığımız id'ler olmalı
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(skuId)
                .setType(BillingClient.SkuType.SUBS)
                .build();
        mBillingClient.launchBillingFlow(this, flowParams);
    }

    @OnClick(R.id.goToButtonID)
    public void goToButton(){

        new MaterialDialog.Builder(this)
                .title(mediaNameList.get(mediaPosition))
                .titleGravity(GravityEnum.CENTER)
                .content(mediaDescription.get(mediaPosition))
                .contentGravity(GravityEnum.CENTER)
                .contentColor(getResources().getColor(R.color.black))
                .positiveText("Kapat")
                .show();
    }
}