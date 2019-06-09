/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package net.jesselockard.android_cordova_embedded;

import android.os.Bundle;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * Main activity responsible for initializing Cordova WebView and Engine
 */
public class MainActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set content view to main activity as done in any Android app

        super.init();

        loadUrl(launchUrl); // Preload launch URL found from /res/xml/config.xml -> <content src...>
    }

    /**
     * Creates a new {@link org.apache.cordova.engine.SystemWebViewEngine SystemWebViewEngine} linked to our {@link org.apache.cordova.engine.SystemWebView SystemWebView} component found on our main activity layout.
     * Also initializes a new {@link org.apache.cordova.CordovaWebView CordovaWebView} responsible for managing our applications plugins, events, and the {@link org.apache.cordova.CordovaWebViewEngine CordovaWebViewEngine}.
     */
    @Override
    protected CordovaWebView makeWebView() {
        SystemWebView appView = findViewById(R.id.webView);
        return new CordovaWebViewImpl(new SystemWebViewEngine(appView));
    }

    /**
     * Intentionally left blank so that the activity_hud and webView are used.
     */
    @Override
    protected void createViews() { }
}
