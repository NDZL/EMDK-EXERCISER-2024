package com.zebra.emdk_barcode;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.symbol.emdk.EMDKBase;
import com.symbol.emdk.EMDKException;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ZebraEMDKBarcode implements EMDKManager.EMDKListener {
    private final String logTAG =  this.getClass().getSimpleName();

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;

    public String scannerList = "N/A";

    private List<ScannerInfo> deviceList = null;

    TextView localTV = null;

    public void createInstance(Context applicationContext, TextView _tv) {
        EMDKResults results = EMDKManager.getEMDKManager(applicationContext, this);
        localTV = _tv;
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            throw new RuntimeException("EMDKManager object request failed!");
        }
        deviceList = new ArrayList<ScannerInfo>();
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        Log.i(logTAG, "EMDK onOpened success!");
        this.emdkManager = emdkManager;

        StringBuilder sb = new StringBuilder();

        try {
            emdkManager.getInstanceAsync(EMDKManager.FEATURE_TYPE.BARCODE, (statusData, emdkBase) -> {
                if(statusData.getResult() == EMDKResults.STATUS_CODE.SUCCESS) {
                    Log.i(logTAG, "emdkManager INITIALIZED");
                    sb.append("emdkManager INITIALIZED\n");
                    barcodeManager = (BarcodeManager) emdkBase;


                    deviceList = barcodeManager.getSupportedDevicesInfo();

                    Log.i(logTAG, "barcodeManager size dev " + deviceList.size());
                    deviceList.forEach(scannerInfo -> {
                        Log.i(logTAG, "SCANNER INFO: " + scannerInfo.getFriendlyName());
                        sb.append(scannerInfo.getFriendlyName()+"\n");
                    });
                    scanner = barcodeManager.getDevice(deviceList.get(0));

                    scannerList = sb.toString();
                    //run in main thread
                    localTV.post(() -> {
                        localTV.setText(scannerList);
                    });

                }
            });
        } catch (EMDKException e) {
            Log.e(logTAG, "emdkManager error " + e.getMessage());
            //throw new RuntimeException(e);
        }



    }


    @Override
    public void onClosed() {
        // Release all the resources
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

}
