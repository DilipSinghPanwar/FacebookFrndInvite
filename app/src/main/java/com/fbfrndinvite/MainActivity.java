package com.fbfrndinvite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bolts.AppLinks;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    String appLinkUrl, previewImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        printKeyHash(this);
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,InviteFbFrndsActivity.class));
            }
        });

        FacebookSdk.sdkInitialize(this);
        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("ActivityName", "App Link Target URL: " + targetUrl.toString());
        } else {
            AppLinkData.fetchDeferredAppLinkData(
                    this,
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            //process applink data
                        }
                    });
        }
    }

    public static String printKeyHash(Activity context) {
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();
            //Retriving package info
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.e(TAG, "printKeyHash: >>" + context.getApplicationContext().getPackageName());
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e(TAG, "printKeyHash: >>" + key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
}
