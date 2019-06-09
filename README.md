# Android: Cordova Embedded
This repository contains a [newly generated Cordova project](https://cordova.apache.org/docs/en/latest/guide/cli/index.html#create-the-app) that has been embedded into an [Android](https://www.android.com/) application.

I created this repository as a personal go-to refrence for the proper practice in embedding Cordova into Android. [For a few years](https://github.com/apache/cordova-docs/pull/498) the Cordova documentation has provided incorrect information on how to embed Cordova into an Android application.

This repository contains a working copy of the [un-merged Pull Request](https://github.com/apache/cordova-docs/pull/904) updating the documentation. Below I'll provide step-by-step instructions on how to replicate this project.

## Prerequisites
Before continuing, make sure you have [installed](https://cordova.apache.org/docs/en/latest/guide/cli/index.html#installing-the-cordova-cli) the Cordova CLI, [Java SE JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html), [Android Studio](https://developer.android.com/studio/index.html), and the [Android SDK Platform Tools](https://developer.android.com/studio/releases/platform-tools.html).

> **Note** that this project is currently configured to require at least API version 21 of the Android SDK. You can adjust this in `build.gradle (Module: app)`. You will also need to replace the `ConstraintComponent` found within the `/res/layout/activity_main.xml` file.

You should also have a good understanding of how the Cordova framework is structured/works as well as how to navigate Android Studio.

## Using this project
Using this project is as straight-forward as cloning the repository and opening it using Android Studio. As stated above it is configured to require a minimum Android SDK API version of 21.

## Creating your own project
In order to embed Cordova into an Android application, you will need to utalize the Cordova CLI to create a new project and download all project dependencies and plugins.

Next we will be taking the files generated and adding them to an empty Android project. The same method would apply if you wanted to import Cordova into an already existing application.

Another method for embedding Cordova is refrenced in [this comment](https://github.com/apache/cordova-docs/pull/904#issuecomment-438371037) on the PR refrenced above. I will **not** be covering this method. However, it may be a more favored approach if you only wish to use the Cordova framework, and not extend/implement your own features using it.

### Generate a new Cordova project
[Generate](https://cordova.apache.org/docs/en/latest/guide/cli/index.html#create-the-app) a new Cordova project through the Cordova CLI:
```bash
cordova create ...
```

### Add plugins and the Android platform
Add any Cordova plugins you would like to take advantage of via the CLI:
```bash
cordova plugin add cordova-plugin-battery-status
```

After, add Android as one of your project's platforms to generate the required project files that will be imported into Android Studio:
```bash
cordova platform add android
```

Finally, ensure all project-wide configuration is transfered to your Android configuration files using the prepare command:
```bash
cordova prepare android
```

### Generating an Android application
Open Android Studio and generate a new, **Empty Activity** project. For the sake of consistency, name this project the same thing you named your Cordova project.

Now you have both of the projects required to embed Cordova into an Android application. The next section will consist of you jumping from one project to another, copying files. Open both of the project's root directories side-by-side in your file explorer to make it easier.

### Copying over project files
**1.** Go back to your Cordova project and enter the `CORDOVA_PROJECT_ROOT/platforms/android/CordovaLib/src/` directory. You should see a directory labeled `org` that contains all of the core Cordova framework implementation in Java. Copy this directory to your clipboard.

**2.** Next, focus on your (Android) Studio project. You are going to navigate to the `STUDIO_PROJECT_ROOT/app/src/main/java/` directory. Paste the copied `org` directory into this one. It should now be side-by-side your own applications `MainActivity` implementation.

**3.** Focus back on your Cordova project, navigating to the `CORDOVA_PROJECT_ROOT/platforms/android/app/src/main/java/` directory. Again, you are going to want to copy the `org` directory to your clipboard. This directory contains any plugins you added to your Cordova project.

**4.** Head back over to your Studio project and paste this directory into the same, `STUDIO_PROJECT_ROOT/app/src/main/java/` used before. This should put your plugins alongside the remainder of the Cordova framework.

**5.** In your Cordova project, enter the `CORDOVA_PROJECT_ROOT/platforms/android/app/src/main/` directory and copy the `assets` directory. This directory contains all the Cordova JavaScript libraries required to create the web portion of an application. In addition, it will be where you store your own HTML, CSS, JavaScript, and images.

**6.** Paste the `/assets` directory in your Studio project, under the `STUDIO_PROJECT_ROOT/app/src/main/` directory.

**7.** Now copy the `xml` directory from your Cordova project's `CORDOVA_PROJECT_ROOT/platforms/android/app/src/main/res/` directory. This contains configuration used by Cordova when rendering and initializing the framework.

**8.** Lastly, you will want to paste it in your Studio's `STUDIO_PROJECT_ROOT/app/src/main/res/` directory.

### Editing your application's source
You're done with the Cordova project, but I'd keep it around for when you'd like to implement new plugins in the future (explained below).

Open your Studio project in Android Studio.

**1. Adjusting activity_main.xml** 

We will start by editing your blank activity you should have generated when you made the project. You can find this at `/res/layout/activity_main.xml`. At the bottom of the Design view, hit the `Text` button to switch to raw XML.
By default it will contain a `ConstraintLayout` container and a `TextView` component displaying a **Hello World** message. Replace its contents with the following (assuming you have choosen to use Android SDK >= 21):
```XML
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <org.apache.cordova.engine.SystemWebView
        android:id="@+id/webView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</android.support.constraint.ConstraintLayout>
```

This will replace the `TextView` with a `SystemWebView` component identified as `webView`. This will be our view responsible for rendering our web-based UI.

**2. Reimplementing the MainActivity class**

Open your `MainActivity` class, it should resemble the following as generated by Android Studio:
```Java
package ...;

import ...;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

**2.1** We'll start by changing the class our activity extends:
```Java
public class MainActivity extends CordovaActivity { ... }
```
This will allow it to access methods offered by Cordova.

**2.2** Next we'll expand our `onCreate` method to initialize the `CordovaActivity` and load the application's root URL:
```Java
@Override
public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main); // Set content view to main activity as done in any Android app

    super.init();

    loadUrl(launchUrl); // Preload launch URL found from /res/xml/config.xml -> <content src...>
}
```
Notice that the method is also being changed from `protected` to `public`.

**2.3** Now we are going to add a new method to our class responsible for initializing the `CordovaWebView` which will manage all events, plugins, and the `CordovaWebViewEngine`. It will also *glue* together a `SystemWebViewEngine` implementation that captures events from the `WebView`.
```Java
@Override
protected CordovaWebView makeWebView() {
    SystemWebView appView = findViewById(R.id.webView);
    return new CordovaWebViewImpl(new SystemWebViewEngine(appView));
}
```

**2.4** After, add an empty `createViews` method which will ensure our `activity_main.xml` is used for rendering the UI.
```Java
@Override
protected void createViews() { }
```

**2.5** Putting it all together, your `MainActivity` class should now resemble the following:
```Java
public class MainActivity extends CordovaActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set content view to main activity as done in any Android app

        super.init();

        loadUrl(launchUrl); // Preload launch URL found from /res/xml/config.xml -> <content src...>
    }

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
```

This class now (1) Sets the inital view of the application to our main layout found at `/res/layout/activity_main.xml`. It will (2) bind a `SystemWebViewEngine` to the `SystemWebView` component found on the layout. This Engine is then (3) binded to a `CordovaWebView` implementation which will allow our application to communicate with the underlying device.

** 3. Configuring versioning (optional)**
The project on this repository is configured so that you define the MAJOR.MINOR.PATCH numbers once and they are applied throughout the application. In addition, a version code will automatically be generated.

Start by opening the `build.gradle (Module: app)` file, located under **Gradle Scripts**.

Under `apply plugin: 'com.android.application'` add the following variable definitions:
```
def verMajor = 1
def verMinor = 1
def verPatch = 0
```
You will be updating these three values everytime you push an update of your application.

Next, we will adjust our `defaultConfig` object to contain the following values:
```
android {
...
  defaultConfig {
        ...
        versionCode verMajor * 10000 + verMinor * 100 + verPatch
        versionName "${verMajor}.${verMinor}.${verPatch}"
    }
...
}
```

Finally, we are going to pass these values along as resource values within the `release` build type:
```
buildTypes {
  release {
      ...
      defaultConfig.resValue "string", "app_ver_name", "${defaultConfig.versionName}"
      defaultConfig.resValue "integer", "app_ver_code", "${defaultConfig.versionCode}"
  }
}
```

All that remains now is to update the `android:versionName`, `android:versionCode`, and `version` found in the `AndroidManifest.xml` and Cordova `config.xml` to use the newly created resource values.

## Finishing up
You should now have a fully working Android application with the Cordova framework embedded into it. You can build and run your application on an emulator or Android device. If you followed along with this tutorial, you should see the standard Cordova **Device Ready** screen. If you have cloned the repository, you should see the same screen and a sample usage of the battery-status plugin.

