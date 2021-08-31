package co.hyperverge.aadhaarofflinesdkclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;

import co.hyperverge.offlinekyc.HVAadhaarOfflineSDK;
import co.hyperverge.offlinekyc.aadhaar.HVAadhaarOfflineConfig;
import co.hyperverge.offlinekyc.aadhaar.HVAadhaarOfflineError;
import co.hyperverge.offlinekyc.aadhaar.HVAadhaarOfflineManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String KYC_API = "https://hv-aadhaar-xml.hyperverge.co/v2.1/readAadhaarXml";

    private HVAadhaarOfflineManager.AadhaarOfflineStartCallback aadhaarOfflineStartCallback =
            new HVAadhaarOfflineManager.AadhaarOfflineStartCallback() {
                @Override
                public void aadhaarOfflineOnSuccess(JSONObject jsonObject) {
                    Log.d(TAG, jsonObject.toString());
                }

                @Override
                public void aadhaarOfflineOnError(HVAadhaarOfflineError hvAadhaarOfflineError) {
                    Log.d(TAG, hvAadhaarOfflineError.toString());
                }
            };

    private HVAadhaarOfflineManager.AadhaarOfflineEventsCallback aadhaarOfflineEventsCallback =
            new HVAadhaarOfflineManager.AadhaarOfflineEventsCallback() {
                @Override
                public void onDownloadClick() {
                    Log.d(TAG, "onDownloadClick");
                }

                @Override
                public void onDownloadFinish() {
                    Log.d(TAG, "onDownloadClick");
                }

                @Override
                public void onFileAttach() {
                    Log.d(TAG, "onFileAttach");
                }

                @Override
                public void onShareCodeEntered() {
                    Log.d(TAG, "onShareCodeEntered");
                }

                @Override
                public void onShareCodeCleared() {
                    Log.d(TAG, "onShareCodeCleared");
                }

                @Override
                public void onSubmit(File file, String shareCode) {
                    Log.d(TAG, "onSubmit");
                    Log.d(TAG, file.getPath());
                    Log.d(TAG, shareCode);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO : Add your appId and appKey
        HVAadhaarOfflineSDK.init("", "");

        Button button = findViewById(R.id.start_offline_kyc_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HVAadhaarOfflineManager.getInstance().start(MainActivity.this, buildConfig(),
                        aadhaarOfflineStartCallback, aadhaarOfflineEventsCallback);
            }
        });
    }

    private HVAadhaarOfflineConfig buildConfig() {
        return HVAadhaarOfflineConfig.builder()
                .shouldShowUploadToolBar(true)
                .uploadToolbarTitle("Complete your KYC")
                .showManualFileAttachButton(true)
                .offlineKycApi(KYC_API)
                .build();
    }
}
