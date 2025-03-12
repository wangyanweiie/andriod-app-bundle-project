package com.wwzh.az.bean;

import java.io.Serializable;

/**
 * 斑马 - 打印连接
 */
public class ZebraPrintBean implements Serializable {
    // 蓝牙名称
    private String bluetoothName;
    public String getBluetoothName() {
        return bluetoothName;
    }
    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    // 蓝牙 mac 地址
    private String bluetoothMac;
    public String getBluetoothMac() {
        return bluetoothMac;
    }
    public void setBluetoothMac(String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }

    // 打印数据
    private String data;
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
