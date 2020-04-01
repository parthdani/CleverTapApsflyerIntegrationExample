package com.atul.ctwithsource;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.SyncListener;

import org.json.JSONObject;

import java.util.Map;

public class MyApplication extends Application {

    private static final String AF_DEV_KEY = "njfds4AjDWYwtyUATSqmhd";
    SyncListener listener2;
    AppsFlyerConversionListener conversionListener;

    @Override
    public void onCreate() {

        ActivityLifecycleCallback.register(this);
        SyncListener listener = new SyncListener() {
            @Override
            public void profileDataUpdated(JSONObject updates) {

            }

            @Override
            public void profileDidInitialize(String CleverTapID) {
                String attributionID = CleverTapAPI.getDefaultInstance(getApplicationContext()).getCleverTapAttributionIdentifier();
                AppsFlyerLib.getInstance().setCustomerUserId(attributionID);
                //AppsFlyerLib.getInstance().waitForCustomerUserId(true);

                AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, getApplicationContext());
                AppsFlyerLib.getInstance().startTracking(MyApplication.this);
                //AppsFlyerLib.getInstance().setCustomerIdAndTrack(attributionID, MyApplication.this);
            }
        };

        CleverTapAPI.getDefaultInstance(MyApplication.this).setSyncListener(listener);

        super.onCreate();

        AppsFlyerLib.getInstance().setDebugLog(true);

        conversionListener = new AppsFlyerConversionListener() {


            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.e("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.e("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.e("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {

                Log.e("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

    }

}
