package com.rex.plugin;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tapapp.rex.PermActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;

public class AndroidCheckDoNotDisturbPlugin extends CordovaPlugin  {

    private CallbackContext callback = null;
    static CordovaWebView cordovaWebView;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        cordovaWebView = webView;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        callback = callbackContext;

        if("checkDoNotDisturb".equals(action)){
           final PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, checkDoNotDisturb()) ;
           callbackContext.sendPluginResult(pluginResult);
        } else if("openDoNotDisturbWindow".equals(action)){
            openDoNotDisturbWindow();
        } else if("showDoNotDisturbPermission".equals(action)){
            openDoNotDisturbWindow();
        }
       return true;
    }

    private boolean showDoNotDisturbPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean checkDoNotDisturb() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager n = (NotificationManager) cordova.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            return n.isNotificationPolicyAccessGranted();
        }
        return true;
    }

    public void openDoNotDisturbWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager n = (NotificationManager) cordova.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (!n.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                cordova.setActivityResultCallback(this);
                cordova.getActivity().startActivityForResult(intent, 11);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            Runnable jsLoader = new Runnable() {
                public void run() {
                    cordovaWebView.loadUrl("javascript:document.dispatchEvent( new CustomEvent('onPermissionChenged'))" );
                }
            };

            try {
                Method post = cordovaWebView.getClass().getMethod("post", Runnable.class);
                post.invoke(cordovaWebView, jsLoader);
            } catch (Exception e) {
                ((Activity) (cordovaWebView.getContext())).runOnUiThread(jsLoader);
            }
        }
    }

}
