package com.wwzh.az.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 佳博 - 打印连接
 */
public class JiaboPrinterBean implements Serializable {
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

    // 打印信息
    private List<PrinterInfo> data = new ArrayList<>();
    public List<PrinterInfo> getData() { return data; }
    public void setData(List<PrinterInfo> data) { this.data = data; }

    // 1.居中且大字体，2.居中，3.左对齐，4.图片
    public static class PrinterInfo implements Serializable {
        private int type;
        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }

        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        private String value;
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }




}
