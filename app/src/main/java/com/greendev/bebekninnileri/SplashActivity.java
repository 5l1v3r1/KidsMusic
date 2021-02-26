package com.greendev.bebekninnileri;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        YoYo.with(Techniques.ZoomIn) //
                .duration(3000)
                .repeat(0)
                .playOn(findViewById(R.id.splashImageButton));
        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        //checkMermission();
                    }
                }, 5000);

    }
/*
    private void checkMermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()){
                    checkMermission();
                } else if (report.areAllPermissionsGranted()){
                    downloadDB();
                } else {
                    checkMermission();
                }

            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }




    interface fileDownloadService {
        @Streaming
        @GET
        Download.Builder download(@Url String url);
    }

    private void retroDownloadStart(){

        downloadProgress = new ProgressDialog(this);
        downloadProgress.setMax(100);
        downloadProgress.setMessage("Please Wait for downloading!");
        downloadProgress.setTitle("Its downloading....");
        downloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadProgress.setCancelable(false);
        downloadProgress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DATABASE_URL)
                .addCallAdapterFactory(DownloadCallAdapterFactory.create())
                .build();

        final fileDownloadService service = retrofit.create(fileDownloadService.class);
        service.download("database/encrypted.db")
                .progress(new ProgressListener() {
                    @Override
                    public void onProgress(Download download, long bytesRead, long totalBytesRead, long contentLength) {
                        int progress = (int) (totalBytesRead * 100f / contentLength);
                        Log.e("Progress", progress+"");
                        downloadProgress.setProgress(progress);

                    }
                })
                .to(new File(Constant.DATABASE_PATH, Constant.DATABSE_NAME))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            downloadProgress.cancel();
                            downloadProgress.dismiss();
                            downloadDB();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
*/

}
