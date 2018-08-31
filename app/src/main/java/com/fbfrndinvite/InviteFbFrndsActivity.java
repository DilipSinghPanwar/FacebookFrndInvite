package com.fbfrndinvite;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InviteFbFrndsActivity extends AppCompatActivity {

    private static final String TAG = InviteFbFrndsActivity.class.getSimpleName();
    String appLinkUrl, previewImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_invitefbfrnds);
        printKeyHash(this);
        findViewById(R.id.btnInviteFbFrnds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteFriends();
            }
        });
    }

    private void inviteFriends() {
        appLinkUrl = "https://fb.me/493857950766025";
        previewImageUrl = "http://2.bp.blogspot.com/-99shOruuadw/VQsG2T233sI/AAAAAAAAEi0/noFTxUBh_rg/s1600/appscripts.png";
        AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(appLinkUrl)
                .setPreviewImageUrl(previewImageUrl)
                .build();
        AppInviteDialog.show(InviteFbFrndsActivity.this, content);
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
