package com.example.ksbpluginreact;

import android.app.Application;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends ReactActivity {

    @Override
    protected String getMainComponentName() {
        return null;
    }

    @Override
    protected boolean getUseDeveloperSupport() {
        return false;
    }

    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(new KSBPluginPackage());
    }
}
