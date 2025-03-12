package com.wwzh.az.print;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.wwzh.az.bean.HoneywellPrintBean;
import com.wwzh.az.connection.Connection_Bluetooth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import honeywell.connection.ConnectionBase;
import honeywell.printer.DocumentFP;
import honeywell.printer.DocumentFP_Android;
import honeywell.printer.ParametersFP;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.util.JSUtil;

/**
 *  霍尼韦尔打印机
 *  打印机状态：0：未连接、1：连接成功、2：打印成功、3：连接失败、4：打印失败
 */
public class HoneywellPrinter extends StandardFeature {
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectionBase mConn;
    private DocumentFP mDocumentFpl;
    private ParametersFP mParams;
    private DocumentFP_Android androidDocument;

    IWebview pWebview1;
    HoneywellPrintBean honeywellPrintBean;
    JSONArray jsonArray;

    String json = "";
    String CallBackID = "";

    /**
     * 获取蓝牙列表
     * @param pWebview
     * @param array
     */
    public void getBluetoothList(IWebview pWebview, JSONArray array) throws InterruptedException, JSONException {
        String CallBackID = array.optString(0);

        JSONArray jsonArray = new JSONArray();
        Set<BluetoothDevice> bondedDevices = new HashSet<>();

        bluetoothAdapter.startDiscovery();
        bondedDevices = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device : bondedDevices) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bluetoothName", device.getName());
            jsonObject.put("bluetoothMac", device.getAddress());

            jsonArray.put(jsonObject);
        }

        JSUtil.execCallback(pWebview, CallBackID, jsonArray, JSUtil.OK, false);

    }

    /**
     * 连接蓝牙
     * @param pWebview
     * @param array
     */
    public void connBluetooth(IWebview pWebview, JSONArray array) throws InterruptedException {
        json = array.optString(1);
        CallBackID = array.optString(0);
        pWebview1 = pWebview;

        Log.i(TAG, String.valueOf(json));

        // 参数赋值
        honeywellPrintBean = com.alibaba.fastjson.JSONArray.parseObject(json, HoneywellPrintBean.class);

        // 连接蓝牙
        if (mConn == null) {
            try {
                mConn = Connection_Bluetooth.createClient(honeywellPrintBean.getBluetoothMac());

                if (!mConn.getIsOpen()) {
                    mConn.open();
                }
            } catch (Exception e) {
                mConn = null;

                // 3.连接失败
                jsonArray = new JSONArray();
                jsonArray.put("3");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);

                Log.e(TAG,"Connect exception");
            }
        }

        if (mConn.getIsOpen()) {
            // 1.连接成功
            jsonArray = new JSONArray();
            jsonArray.put("1");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
        }
    }


    /**
     * 执行打印操作
     * @param pWebview
     * @param array
     */
    public void sentMsgPrint(IWebview pWebview, JSONArray array) throws InterruptedException {
        json = array.optString(1);
        CallBackID = array.optString(0);
        pWebview1 = pWebview;

        Log.i(TAG, String.valueOf(json));

        // 判断是否连接蓝牙
        try {
            if (!mConn.getIsOpen()) {
                mConn.open();
            }

            preparePrint();
        } catch (Exception e) {
            // 4.打印失败
            jsonArray = new JSONArray();
            jsonArray.put("4");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);

            Log.e(TAG,"Connect exception");
            Log.e(TAG,"Connect exception");
        }
    }

    /**
     * 打印准备
     */
    public void preparePrint() {
        // 参数赋值
        honeywellPrintBean = com.alibaba.fastjson.JSONArray.parseObject(json, HoneywellPrintBean.class);

        if (mParams == null) {
            Log.i(TAG, "preparePrint:create new ParametersFP for Printing ");
            mParams = new ParametersFP();
        }

        if (mDocumentFpl == null) {
            Log.i(TAG, "preparePrint: create new DocumentFP for Printing");
            mDocumentFpl = new DocumentFP();
        }

        if (androidDocument == null) {
            Log.i(TAG, "preparePrint: create new DocumentFP_Android for Printing");
            androidDocument = new DocumentFP_Android();
        }

        try {
            byte[] decodedBytes = Base64.decode(honeywellPrintBean.getData().split(",")[1], Base64.DEFAULT);
            Bitmap pngDecoded = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            androidDocument = new DocumentFP_Android();

            mConn.clearWriteBuffer();
            mConn.write("CLL\r\n");

            androidDocument.writeImageBitmap(pngDecoded, honeywellPrintBean.getRow(), honeywellPrintBean.getCol(), honeywellPrintBean.getTargetWeight(), honeywellPrintBean.getTargetHeight(), mParams);

            // 开始打印图片
            startPrintImage();
        } catch (Exception e) {
            // 4.打印失败
            jsonArray = new JSONArray();
            jsonArray.put("4");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);

            throw new RuntimeException(e);
        }
    }

    private void startPrintImage() {
        Log.i(TAG, "startPrint");

        androidDocument.printDocument();
        byte[] documentData = androidDocument.getDocumentData();

        try {
            mConn.write(documentData);
            Thread.sleep(500);
            androidDocument = null;
        } catch (InterruptedException e) {
            // 4.打印失败
            jsonArray = new JSONArray();
            jsonArray.put("4");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
        }

        // 2.打印成功
        jsonArray = new JSONArray();
        jsonArray.put("2");
        JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
    }
}
