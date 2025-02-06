package com.zebra.emdk_barcode;

import android.content.Context;
import android.util.Log;

import com.symbol.emdk.EMDKBase;
import com.symbol.emdk.EMDKException;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerInfo;

import java.util.ArrayList;
import java.util.List;

public class ZebraEMDKBarcode implements EMDKManager.EMDKListener {
    private final String logTAG =  this.getClass().getSimpleName();

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;


    private List<ScannerInfo> deviceList = null;

    public void createInstance(Context applicationContext) {
        EMDKResults results = EMDKManager.getEMDKManager(applicationContext, this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            throw new RuntimeException("EMDKManager object request failed!");
        }
        deviceList = new ArrayList<ScannerInfo>();
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        Log.e(logTAG, "open success!");
        this.emdkManager = emdkManager;

        try {
            emdkManager.getInstanceAsync(EMDKManager.FEATURE_TYPE.BARCODE, (statusData, emdkBase) -> {
                if(statusData.getResult() == EMDKResults.STATUS_CODE.SUCCESS) {
                    Log.e(logTAG, "SCANNER MANAGER INITIALIZED");
                    barcodeManager = (BarcodeManager) emdkBase;


                    deviceList = barcodeManager.getSupportedDevicesInfo();

                    Log.i(logTAG, "INIZIALIZZATO size dev " + deviceList.size());
                    deviceList.forEach(scannerInfo -> {
                        Log.i(logTAG, "SCANNER INFO: " + scannerInfo.getFriendlyName());
                    });
                    scanner = barcodeManager.getDevice(deviceList.get(0));
                }
            });
        } catch (EMDKException e) {
            throw new RuntimeException(e);
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
