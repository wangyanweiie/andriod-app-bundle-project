package com.wwzh.az.print;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.brightek.thermallibrary.CString;
import com.brightek.thermallibrary.ImageCommand;
import com.brightek.thermallibrary.ThermalService;
import com.wwzh.az.bean.WeihuangPrintBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import java.util.HashSet;
import java.util.Set;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.util.JSUtil;

/**
 *  炜煌打印机
 *  打印机状态：0：未连接、1：连接成功、2：打印成功、3：连接失败、4：打印失败
 */
public class WeihuangPrinter extends StandardFeature {
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    IWebview pWebview1;
    WeihuangPrintBean weihuangPrintBean;
    ThermalService thermalService;
    JSONArray jsonArray;

    Integer isConnBt = 0;

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

        // 断开连接
        if(isConnBt == 1) {
            thermalService.btClose();
        }

        // 参数赋值
        weihuangPrintBean = com.alibaba.fastjson.JSONArray.parseObject(json, WeihuangPrintBean.class);

        // 连接蓝牙打印机
        thermalService = new ThermalService(handler, pWebview1.getActivity().getApplication());
        thermalService.btConn(weihuangPrintBean.getBluetoothMac());
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
        // 参数赋值
        weihuangPrintBean = com.alibaba.fastjson.JSONArray.parseObject(json, WeihuangPrintBean.class);

        // 判断打印机是否连接
        if(isConnBt == 1) {
            byte[] b = Base64.decode(weihuangPrintBean.getData().split(",")[1], Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            // 打印
            thermalService.btSendData(ImageCommand.Print_1D76(bitmap, 400, 470));
            jsonArray = new JSONArray();
            jsonArray.put("2");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
        } else if(weihuangPrintBean.getBluetoothMac() != null && weihuangPrintBean.getBluetoothMac() != "") {
            // 连接蓝牙打印机
            thermalService = new ThermalService(handler, pWebview1.getActivity().getApplication());
            thermalService.btConn(weihuangPrintBean.getBluetoothMac());
        } else {
            jsonArray = new JSONArray();
            jsonArray.put("3");
            JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case CString.bt_Connected:
                    isConnBt = 1;

                    jsonArray = new JSONArray();
                    jsonArray.put("1");
                    JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
                    break;
                case CString.bt_DisConnected:
                    isConnBt = 0;

                    if(null != weihuangPrintBean) {
                        // 连接蓝牙打印机
                        thermalService.btConn(weihuangPrintBean.getBluetoothMac());
                    }

                    jsonArray = new JSONArray();
                    jsonArray.put("0");
                    break;
                case CString.bt_ConnectError:
                    isConnBt = 3;

                    jsonArray = new JSONArray();
                    jsonArray.put("3");
                    JSUtil.execCallback(pWebview1, CallBackID, jsonArray, JSUtil.OK, false);
                    break;
                default:
                    break;
            }
        }
    };
}
