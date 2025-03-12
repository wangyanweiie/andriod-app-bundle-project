package com.wwzh.az.bean;

import java.io.Serializable;

/**
 * 霍尼韦尔 - 打印连接
 */
public class HoneywellPrintBean implements Serializable {
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

    // 霍尼韦尔打印数据
    private String data;
    public String getData() { return data; }
    public void setData(String data) {
        this.data = data;
    }

    // 图片 x 轴起始位置
    private Integer row;
    public Integer getRow() {
        return row;
    }
    public void setRow(Integer row) {
        this.row = row;
    }

    // 图片 y 轴起始位置
    private Integer col;
    public Integer getCol() {
        return col;
    }
    public void setCol(Integer col) {
        this.col = col;
    }

    // 图片宽度
    private Integer targetWeight;
    public Integer getTargetWeight() {
        return targetWeight;
    }
    public void setTargetWeight(Integer targetWeight) {
        this.targetWeight = targetWeight;
    }

    // 图片高度
    private Integer targetHeight;
    public Integer getTargetHeight() {
        return targetHeight;
    }
    public void setTargetHeight(Integer targetHeight) {
        this.targetHeight = targetHeight;
    }
}
