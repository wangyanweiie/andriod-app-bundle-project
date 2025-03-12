package com.wwzh.az.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.wwzh.az.bean.JiaboPrinterBean;
import com.wwzh.az.service.JiaboPrinterService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.util.JSUtil;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 *  佳博打印机 - 打印
 */
public class JiaboPrinter extends StandardFeature {
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public JiaboPrinterService.PrinterBinder printerBinder = null;

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
     * 执行打印操作
     * @param pWebview
     * @param array
     */
    public void sentMsgPrint(IWebview pWebview, JSONArray array) throws InterruptedException {
        String json = array.optString(1);

        // 参数赋值
        JiaboPrinterBean jiaboPrinterBean = com.alibaba.fastjson.JSONArray.parseObject(json, JiaboPrinterBean.class);
        JiaboPrinterService.jiaboPrinterBean = jiaboPrinterBean;

        // 绑定服务，执行打印操作
        mApplicationContext.startService(new Intent(pWebview.getActivity().getApplication(), JiaboPrinterService.class));
        Intent intent = new Intent(pWebview.getActivity().getApplication(), JiaboPrinterService.class);
        pWebview.getActivity().getApplication().bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            printerBinder = (JiaboPrinterService.PrinterBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            printerBinder.getService().newPrinterData();
        }
    };
}
