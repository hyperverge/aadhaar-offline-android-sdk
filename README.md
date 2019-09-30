## HyperVerge Aadhar Offline Android SDK 


## OVERVIEW 
HyperVerge Aadhar Offline Android SDK helps FinTech companies to onboard customers by using the Aadhar XML provided by UIDAI. The onboarding process is completly paperless and the SDK takes care of downloading the Aadhar XML document from UIDAI and verifying the authencity of the customer. 


## PREREQUISITES

Before integrating the SDK please contact your HyperVerge POC to request your organisation specific appId and appKey strings and S3 read credentials.

## SDK COMPATIBILITY

The SDK supports all Android flavours above 19 (Android 4.4 Kitkat)  and upto Android 29 (Android 10 Q)

## TABLE OF CONTENTS
- [OVERVIEW](#overview)
- [PREREQUISITES](#prerequisites)
- [SDK COMPATIBILITY](#sdk-compatibility)
- [INTEGRATION STEPS](#integration-steps)
    1. [Adding the SDK to your project](#1-adding-the-sdk-to-your-project)
    2. [Initialising the SDK](#2-initialising-the-sdk)
- [SDK CUSTOMIZATIONS](#sdk-customizations)
    - [Toolbar customization](#toolbar-customization)
    - [Face match customization](#face-match-customization)
    - [Example for adding customizations to the KYC flow](#example-for-adding-customizations-to-the-kyc-flow)
    - [Customization table](#customization-table)
- [CONTACT US](#contact-us)

## INTEGRATION STEPS

### 1. Adding the SDK to your project

1. In your `project_name/build.gradle` file add the following lines 
	
```
allprojects {
  repositories {
    maven {
      url "s3://hvsdk/android/releases"
      credentials(AwsCredentials) {
        accessKey “your_aws_access_key”
        secretKey “your_aws_secret_key”
      }
    }
  }
}
```

2. In your ```app/build.gradle``` add the ```offlinekyc``` SDK as a dependency

```
dependencies {
  implementation('co.hyperverge:offlinekyc:1.0.1@aar', {
    transitive = true
    exclude group: 'androidx.appcompat', module: 'appcompat'
    exclude group: 'androidx.legacy', module: 'legacy-support-v4'
    exclude group: 'androidx.navigation', module: 'navigation-ui'
  })
}
```

Please sync your project to download the dependecies. 

### 2. Initialising the SDK

1. Initialise the SDK in your ```Application``` class and make sure the class is added in your ```AndroidManifest.xml```

```
@Override
public void onCreate() {
  super.onCreate();
  HyperVergeOfflineKYCSDK.init("your_app_id_here", "your_app_key_here");
}
```

Add the Application class in AndroidManifest.xml

```
<application
  android:name="your_application_class_name"
>
```

> Note: It is advisable to not have the secret hard coded in the app. It should be fetched from your backend server.

2. Initiate Aadhar Offline KYC flow with default customisations

```
public class YourActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    AadharOfflineKycManager.getInstance().start(activity_context, this);

  }
}
```

3. Handle the success and error events

```
public class YourActivity extends AppCompatActivity implements AadharOfflineStartCallback {
  @Override
  public void aadharOfflineOnSuccess(SuccessDTO successDTO) {
    // Handle the success event with your logic
  }

  @Override  
  public void aadharOfflineOnError(ErrorDTO errorDTO) {
    // Handle the error event with your logic
  }
}
```

## SDK CUSTOMIZATIONS

You can customize the Aadhar Offline requests using the ```AadharOfflineConfig```  object.

### Toolbar customization

Show ```Toolbar``` and toolbar title
	
```
AadharOfflineConfig aadharOfflineConfig =  
    AadharOfflineConfig.builder()
        .shouldShowToolBar(true)
        .uploadToolbarTitle("Complete your KYC")
        .build()
```

### Face match customization

Make a face match call to verify the photo in the Aadhar XML and the picture of the user

```
AadharOfflineConfig aadharOfflineConfig =  
    AadharOfflineConfig.builder()  
        .faceMatch(true)
        .selfieImageFile(file)
        .build()
```

> Pro Tip : To increase the accuracy of face match results, please use the [HyperSnap SDK](https://github.com/hyperverge/capture-android-sdk) which takes care of optimising images with respect to resolutions, aspect ratio, image rotation and other parameters.
 
### Example for adding customizations to the KYC flow

```
public class YourActivity extends AppCompatActivity implements AadharOfflineStartCallback {  
  
  @Override  
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    
    AadharOfflineConfig aadharOfflineConfig = AadharOfflineConfig.builder()
            .shouldShowToolBar(true)
            .uploadToolbarTitle("Complete your KYC")
            .faceMatch(true)
            .selfieImageFile(selfieImageFile)
            .build();
    
    AadharOfflineKycManager.getInstance().start(this, aadharOfflineConfig, this);  
  
  }
```

### Customization table

| Config | Type | Description | 
| ------ | ------ | ------ |
| faceMatch | boolean | When set to true, face match is checked with the image provided |
| selfieImageFile | File |  Works with the faceMatch config. Uploads the image file to check for face match|
| shouldShowToolBar | boolean | Toolbar is shown when this is set to true |
| uploadToolbarTitle | String | Sets a custom Toolbar title |

## Contact Us
If you are interested in integrating this SDK, please do send us a mail at [contact@hyperverge.co](mailto:contact@hyperverge.co) explaining your use case. We will give you the `aws_access_key` & `aws_secret_pass` so that you can try it out. Learn more about HyperVerge [here](http://hyperverge.co/).
