package com.wwzh.az.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.wwzh.az.bean.ZebraPrintBean;
import com.zebra.sdk.btleComm.BluetoothLeConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.util.JSUtil;

/**
 *  斑马打印机 - 低功耗蓝牙打印（BLE）
 *  打印机状态：0：未连接、1：连接成功、2：打印成功、3：连接失败、4：打印失败
 */
public class ZebraPrinterBLE extends StandardFeature {
    private Connection printerConnection;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    ZebraPrinter printer;
    ZebraPrintBean zebraPrintBean;
    IWebview pWebview1;
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
        jsonArray = new JSONArray();

        // 参数赋值
        zebraPrintBean = com.alibaba.fastjson.JSONArray.parseObject(json, ZebraPrintBean.class);

        // 断开连接
        if(printerConnection != null) {
            disconnect();
        }

        printerConnection = new BluetoothLeConnection(zebraPrintBean.getBluetoothMac(), this.mApplicationContext);

        try {
            printerConnection.open();
            jsonArray.put("1");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
        } catch (ConnectionException e) {
            jsonArray.put("3");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
            disconnect();
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
        jsonArray = new JSONArray();

        // 参数赋值
        zebraPrintBean = com.alibaba.fastjson.JSONArray.parseObject(json, ZebraPrintBean.class);

        byte[] b = Base64.decode(zebraPrintBean.getData().split(",")[1], Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

        // 重新连接打印机
        if(!printerConnection.isConnected() && (zebraPrintBean.getBluetoothMac() != null) || zebraPrintBean.getBluetoothMac() != "") {
            printerConnection = new BluetoothLeConnection(zebraPrintBean.getBluetoothMac(), this.mApplicationContext);

            try {
                printerConnection.open();
                jsonArray.put("1");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
            } catch (ConnectionException e) {
                jsonArray.put("3");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
                disconnect();
            }
        }

        // 判断打印机是否连接
        if(printerConnection != null && printerConnection.isConnected()) {
            // 获取打印机实例
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
            } catch (ConnectionException e) {
                jsonArray.put("3");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
                disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                jsonArray.put("3");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
                disconnect();
            }

            // 打印
            try {
                // 左边距 上边距 图片宽度 图片高度
                printer.printImage(new ZebraImageAndroid(bitmap), 70, 50, 1100, 1100, false);
                jsonArray.put("2");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
            } catch (ConnectionException e) {
                jsonArray.put("4");
                JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
            }
        } else {
            jsonArray.put("3");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
        }
    }

    /**
     * 取消连接
     */
    public void disconnect() {
        try {
            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (ConnectionException ignored) {}
    }
}
