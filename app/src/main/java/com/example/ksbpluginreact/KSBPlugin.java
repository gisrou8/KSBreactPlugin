package com.example.ksbpluginreact;

import com.brother.ptouch.sdk.NetPrinter;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class KSBPlugin extends ReactContextBaseJavaModule{

    private String modelName = "QL-820NWB";; // the print model name.
    private NetPrinter[] mNetPrinter; // array of storing Printer informations.
    private SearchThread searchPrinter;
    private Printer printer;
    WritableMap printerinfo;
    boolean searchEnd;
    ReactApplicationContext reactContext;

    public KSBPlugin(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    public void scanNetPrinters(final Promise promise){
        searchPrinter = new SearchThread();
        searchPrinter.start();
    }

    private void sendEvent(ReactApplicationContext reactContext,
                           String eventName,
                           WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public String getName() {
        return "KSBPlugin";
    }


    private class SearchThread extends Thread {

        /* search for the printer for 10 times until printer has been found. */
        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                // search for net printer.
                if (netPrinterList(i)) {
                    sendEvent(reactContext, "printerinfo", printerinfo);
                    //msgDialog.close();
                    break;
                }
            }
            //msgDialog.close();
        }
    }

    /**
     * search the net printer and adds the searched printer information into the
     * printerList
     */
    private boolean netPrinterList(int times) {

        searchEnd = false;
        printerinfo = Arguments.createMap();

        try {
            // get net printers of the particular model
            Printer myPrinter = new Printer();
            PrinterInfo info = myPrinter.getPrinterInfo();
            myPrinter.setPrinterInfo(info);

            mNetPrinter = myPrinter.getNetPrinters(modelName);
            final int netPrinterCount = mNetPrinter.length;

            // when find printers,set the printers' information to the list.
            if (netPrinterCount > 0) {
                searchEnd = true;

                String dispBuff[] = new String[netPrinterCount];
                for (int i = 0; i < netPrinterCount; i++) {
//                    dispBuff[i] = mNetPrinter[i].modelName + "\n\n"
//                            + mNetPrinter[i].ipAddress + "\n"
//                            + mNetPrinter[i].macAddress + "\n"
//                            + mNetPrinter[i].serNo + "\n"
//                            + mNetPrinter[i].nodeName;
                    printerinfo.putString("ModelName", mNetPrinter[i].modelName);
                    printerinfo.putString("ipAdress",  mNetPrinter[i].ipAddress);
                    printerinfo.putString("macAdress",  mNetPrinter[i].macAddress);
                    printerinfo.putString("serNo", mNetPrinter[i].serNo);
                    printerinfo.putString("nodeName",  mNetPrinter[i].nodeName);
                    System.out.println(printerinfo);
                }
            } else if (netPrinterCount == 0
                    && times == (10 - 1)) { // when no printer
                // is found
//                String dispBuff[] = new String[1];
//                dispBuff[0] = "No Net Printer Found";
//                mItems.add(dispBuff[0]);
                printerinfo.putString("no Printer", "No printer found");
                searchEnd = true;
            }

            if (searchEnd) {
                // list the result of searching for net printer
//                this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final ArrayAdapter<String> fileList = new ArrayAdapter<String>(
//                                Activity_NetPrinterList.this,
//                                android.R.layout.test_list_item, mItems);
//                        Activity_NetPrinterList.this.setListAdapter(fileList);
//                    }
//                });
            }
        } catch (Exception e) {
        }

        return searchEnd;
    }
}
