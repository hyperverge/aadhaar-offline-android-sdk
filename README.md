## HyperVerge Aadhaar Offline Android SDK 


## OVERVIEW 
HyperVerge Aadhaar Offline Android SDK helps FinTech companies to onboard customers by using the Aadhaar XML provided by UIDAI. The onboarding process is completly paperless and the SDK takes care of downloading the Aadhaar XML document from UIDAI and verifying the authencity of the customer. 


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
    - [Tutorial customization](#tutorial-customization)
    - [Example for adding customizations to the KYC flow](#example-for-adding-customizations-to-the-kyc-flow)
    - [Customization table](#customization-table)
    - [HVAadhaarOfflineError](#hvaadhaarofflineerror)
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
  implementation('co.hyperverge:offlinekyc:1.0.4@aar', {
    transitive = true
    exclude group: 'androidx.appcompat', module: 'appcompat'
  })
}
```

Please sync your project to download the dependecies. 


### 2. Initialising the SDK

1. To initialize the SDK, add the following line to your app before calling any of the SDK functionalities. For example, you can add it to the `onCreate` method of the application class.

```
@Override
public void onCreate() {
  super.onCreate();
  HVAadhaarOfflineSDK.init("your_app_id_here", "your_app_key_here");
}
```

2. Initiate Aadhaar Offline KYC flow

```
    // 1. set properties
    HVAadhaarOfflineConfig hvAadhaarOfflineConfig = new HVAadhaarOfflineConfig().builder().build();

    // 2. Handle completion callback
    AadhaarOfflineStartCallback completionCallback = new AadhaarOfflineStartCallback() {
      @Override
      public void aadhaarOfflineOnSuccess(JSONObject jsonObject) {
        // Handle the success event with your logic
      }

      @Override
      public void aadhaarOfflineOnError(HVAadhaarOfflineError hvAadhaarOfflineError) {
        // Handle the error event with your logic
      }
    };
 
 HVAadhaarOfflineManager.getInstance()
 .start(context, hvAadhaarOfflineConfig, completionCallback, null);
```
Where,

- context: is the context of the current Activity being displayed.
- hvAadhaarOfflineConfig: Object of type ```HVAadhaarOfflineConfig```. This provides properties to customise the KYC flow. Here's a full list of customisations.
- completetionCallback: Object of type ```AadhaarOfflineStartCallback``` interface. Provides callback methods to handle success and error flows. The ```aadhaarOfflineOnSuccess``` provides a JSONObject with results and the ```aadhaarOfflineOnError``` returns an object of type ```HVAadhaarOfflineError``` with error codes and error messages. List of error codes can be found [here](#hvaadhaarofflineerror).
- eventsCallback: Object of type ```AadhaarOfflineEventsCallback``` interface. Provides callback methods for various user interactions within the SDK. It has been set to `null` in the sample code


## SDK CUSTOMIZATIONS

You can customize the Aadhaar Offline requests using the ```AadhaarOfflineConfig```  object.

### Toolbar customization

Show ```Toolbar``` and toolbar title
	
```
hvAadhaarOfflineConfig.setShouldShowUploadToolBar(true);
hvAadhaarOfflineConfig.setUploadToolbarTitle("Complete your KYC");
```

### Face match customization

Make a face match call to verify the photo in the Aadhaar XML and the picture of the user

```
hvAadhaarOfflineConfig.setShouldMakeFaceMatchCall(true);
hvAadhaarOfflineConfig.setSelfieImageUri("/storage/emulated/0/DCIM/Camera/20141219_133139.jpg");
```

> Pro Tip : To increase the accuracy of face match results, please use the [HyperSnap SDK](https://github.com/hyperverge/capture-android-sdk) which takes care of optimising images with respect to resolutions, aspect ratio, image rotation and other parameters.

### Tutorial customization

Add your own URLs to show tutorials to users to help them understand the KYC flow

```
hvAadhaarOfflineConfig.setShowTutorial(true);
hvAadhaarOfflineConfig.setTutorialUrl("https://www.hyperverge.co");
```

 
### Example for adding customizations to the KYC flow

```
public class YourActivity extends AppCompatActivity implements AadhaarOfflineStartCallback {  
  
  @Override  
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    
    HVAadhaarOfflineConfig hvAadhaarOfflineConfig = new HVAadhaarOfflineConfig();
hvAadhaarOfflineConfig.setSelfieImageUri("<imageUri from capture SDK>")
    
  }
```

### Customization table

| Config | Type | Description | 
| ------ | ------ | ------ |
| shouldMakeFaceMatchCall | boolean | When set to true, face match is checked with the image provided |
| selfieImageUri | String |  Works with the faceMatch config. Uploads the image file to check for face match|
| shouldShowToolBar | boolean | Toolbar is shown when this is set to true |
| uploadToolbarTitle | String | Sets a custom Toolbar title |
| tutorialUrl | String | Set a custom tutorial webpage. Defaults to HyperVerge Tutorials if no value is provided |
| showTutorial | boolean | Tutorials are shown when set|


### HVAadhaarOfflineError

| Code | Description | Explanation | Action |
| ------ | ------ | ------ | ------ |
| 2 | Internal SDK Error | Occurs when an unexpected error has happened with the HyperSnapSDK. | Notify HyperVerge |
| 3 | Operation Cancelled By User | When the user closes KYC flow. | Try again |
| 4 | Permissions not granted by the user | When user denies runtime permissions | In the settings app, give permission and try again. |
| 12 | Network Error | Occurs when the internet is either non-existant or very patchy. | Check internet and try again. If Internet is proper, contact HyperVerge. |



## Contact Us
If you are interested in integrating this SDK, please send us a mail at [contact@hyperverge.co](mailto:contact@hyperverge.co) explaining your use case.  
Learn more about HyperVerge [here](http://hyperverge.co/).
