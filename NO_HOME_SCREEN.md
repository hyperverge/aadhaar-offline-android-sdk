
### Prerequisites before integrating OKYC flow without home screen

1. Should have initialised the SDK as described [here](https://github.com/hyperverge/aadhaar-offline-android-sdk/blob/v1.2.5/README.md#2-initialising-the-sdk)
2. As you won't use HyperVerge's home screen, it is required for you to add ```action``` and ```category``` tags for your activity in the ```AndroidManifest.xml``` file that will start the OKYC flow and not any other Activity

Example - 
```
<activity android:name=".MainActivity">
    <intent-filter>
      <action android:name="co.hyperverge.test.okyc.REDIRECTION" />
      <!-- action name can be any constant string of your choice which will be used to redirect from a browser back to your Activity -->

      <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```
  
### Starting the Offline KYC flow without home screen

In your Activity or Fragment (ideally, on CTA clicked) add the below code snippets to start the offline KYC flow from the SDK.
  
  **1. Create ```CustomTabsCallback``` to override ChromeCustomTab methods. Overriding methods is optional.**
  
```
  CustomTabsCallback customTabsCallback = new CustomTabsCallback() {

  };
```
  **2. Create ```AadhaarOfflineFileDownloadCallback``` to get callbacks from the SDK for success or failures**
      
```
  AadhaarOfflineFileDownloadCallback aadhaarOfflineFileDownloadCallback = new AadhaarOfflineFileDownloadCallback() {

    @Override
    public void onDownloadSuccess(File file) {
      // This callback is called when the file is downloaded successfully. You can save this and use it later
    }

    @Override
    public void onDownloadError(HVAadhaarOfflineError hvAadhaarOfflineError) {
      // Handle this error callback 
    }
  };
```
  
  **3. Create a String object and assign the exact action value that was assigned to the activity previously in the ```AndroidManifest.xml``` file**
  
  Do note that this string must be exactly the same as you defined in the AndroidManifest.xml file. If the string is different the redirection after the download finish will not happen and the users will be stuck on the browser screen

```
  final String INTENT_ACTION = "co.hyperverge.test.okyc.REDIRECTION";
```

  **4. Call ```HVAadhaarOfflineManager.startChromeService``` to start the flow and add the above variables as arguments**

```
  HVAadhaarOfflineManager.startWebService(this, customTabsCallback, aadhaarOfflineFileDownloadCallback, INTENT_ACTION);
```

> All the arguments are compulsory and should not be null. ```this``` is the Activity context.

After the completion of the download, the SDK automatically redirects back the user to the Activity that is assigned with the INTENT_ACTION string.

When the download is completed, ```startActivity``` is called with intent extras - ```isRedirected``` set to true and ```file``` set to downloaded Aadhaar file.

  **5. Retrieve the extra intent values and update the UI as shown below in ```onCreate``` method of the activity**

```
  if (getIntent().getBooleanExtra("isRedirected", false)) {
    
    File aadhaarFile = getIntent().getSerializableExtra("file");
    
    /*
      Update UI accordingly here. For instance, you can show a different Fragment or open a new Intent from here
    */
    
  }
```
> **Note**
> 1. In scenarios where an Activity `A` initiates the KYC flow and an Activity `B` is being used to handle redirection, Activity `A` gets the file from the callback and the same file can be retrieved through intent extra on Activty `B` as well. 
> 2. In scenarios where the same Activity `A` is being used as both to initiate the KYC flow and to handle redirection, the Activity `A` is recreated on redirection in which case it is recommended to use the file from intent extras in the `onCreate` method of the activity


  **6. Unbind the chrome service from your Activity ```onDestroy``` method**

This will ensure to stop memory leaks and crashes on your app

```
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HVAadhaarOfflineManager.stopWebService();
    }
```
