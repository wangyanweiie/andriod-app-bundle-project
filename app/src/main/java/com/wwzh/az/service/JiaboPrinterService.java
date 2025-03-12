package com.wwzh.az.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.wwzh.az.bean.JiaboPrinterBean;
import com.wwzh.az.util.PhotoCompress;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import static com.gprinter.command.EscCommand.ENABLE.OFF;
import static com.gprinter.command.EscCommand.ENABLE.ON;
import static com.gprinter.command.EscCommand.JUSTIFICATION.CENTER;
import static com.gprinter.command.EscCommand.JUSTIFICATION.LEFT;

/**
 * @author colin
 * 2020-08-12
 * 蓝牙打印机打印服务
 **/
public class JiaboPrinterService extends Service {
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final String PRINT_LINE = "------------------------------------------------\n";
    public static final short PRINT_POSITION_1 = 17 * 3;
    public static final short PRINT_POSITION_CODE_LEFT = 17 * 3 - 8;
    public static final short PRINT_POSITION_2 = 23 * 3;
    public static final short PRINT_POSITION_3 = 32 * 3;
    public static final short PRINT_UNIT = 43;
    private static final int REQUEST_PRINT_LABEL = 0xfd;
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;
    private static final int MAX_PRINTER_CNT = 1;

    private PrinterServiceConnection conn = null;
    private Boolean turnOffToOn = false;

    public static JiaboPrinterBean jiaboPrinterBean;
    public GpService mGpService = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PrinterBinder();
    }

    public class PrinterBinder extends Binder {
        public JiaboPrinterService getService() {
            return JiaboPrinterService.this;
        }
    }

    /**
     *  onStartCommand（） 方法为服务 Service 的生命周期：创建服务后调用 - 服务的入口
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 注册sdk服务
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_DEVICE_REAL_STATUS);
        filter.addAction(GpCom.ACTION_RECEIPT_RESPONSE);
//        registerReceiver(mBroadcastReceiver, filter);  // 注册广播
//        registerBroadcast();
        connectGpService();
        return START_STICKY;
    }

    /**
     *  注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(printerRealStatusBroadcastReceiver, filter);
    }

    /**
     *  广播 - 接收打印机状态
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GpCom.ACTION_DEVICE_REAL_STATUS.equals(action)) {
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);

                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);

                    if (status == GpCom.STATE_NO_ERR) {
                        Log.e("1","打印机正常");
                    } else {
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            Log.e("2","打印机脱机");
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            Log.e("3","打印机缺纸");
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            Log.e("4","打印机开盖");
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            Log.e("5","打印机出错");
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            Log.e("6","查询超时");
                        }
                    }
                }
            }
        }
    };

    /**
     *  广播 - 监听打印机连接情况
     */
    private BroadcastReceiver printerRealStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);

                if (type == GpDevice.STATE_CONNECTING) {
                    Log.e("6","连接中");
                } else if (type == GpDevice.STATE_NONE) {
                    Log.e("6","无状态的连接");
                    try {
                        connectDevice(jiaboPrinterBean.getBluetoothMac());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    Log.e("6","可用的打印连接");
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    Log.e("6","不可用的打印连接");
                } else if (type == GpDevice.STATE_CONNECTED) {
                    Log.e("6","已经连接上");
                } else if (type == GpDevice.STATE_LISTEN) {
                    Log.e("6","正在监听");
                }
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        if (turnOffToOn) {
                            try {
                                connectDevice(jiaboPrinterBean.getBluetoothMac());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            turnOffToOn = false;
                        }
                        break;
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_TURNING_ON:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                    default:
                        Log.e("6","请打开设备蓝牙开关");
                        turnOffToOn = true;
                        break;
                }
            }
        }
    };

    /**
     * 连接服务
     */
    public void connectGpService() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 连接打印服务
     */
    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            // 初始化端口
            // 检查端口状态
            try {
                connectDevice(jiaboPrinterBean.getBluetoothMac());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpService = null;
        }
    }

    /**
     *  连接蓝牙设备
     * @throws RemoteException
     */
    private void connectDevice(String blueMac) throws RemoteException {
        int rel = 0;
        rel = mGpService.openPort(1, PortParameters.BLUETOOTH, blueMac, 0);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];

        if (r != GpCom.ERROR_CODE.SUCCESS) {
            if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                Log.e("6","连接成功");
            } else {
                Log.e("6","连接失败");
            }
        } else {
            Log.e("6","蓝牙连接成功");
            newPrinterData();
        }
    }

    /**
     *  打印数据
     */
    public void newPrinterData() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 0);

        List<JiaboPrinterBean.PrinterInfo> data = jiaboPrinterBean.getData();

        for (JiaboPrinterBean.PrinterInfo printerInfo : data) {
            int type = printerInfo.getType();
            String name = printerInfo.getName();
            String value = printerInfo.getValue();

            String result = (name != null && !name.equals("")) ?  name + "：" + value : value;

            if (type == 1) {
                esc.addPrintAndFeedLines((byte) 1);
                printerText(esc, CENTER, OFF, ON, ON, OFF, result, 2);
            }

            if (type == 2) {
                esc.addPrintAndFeedLines((byte) 1);
                printerText(esc, CENTER, OFF, OFF, OFF, OFF, result, 1);
            }

            if (type == 3) {
                esc.addPrintAndFeedLines((byte) 1);
                printerText(esc, LEFT, OFF, OFF, OFF, OFF, result, 1);
            }

            if (type == 4) {
                // 签字信息
                if (value != null && value != "") {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;

                    if (value != null) {
                        Bitmap[] bitmap = {null};
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bitmap[0] = getImageFromNetByUrl("http://192.168.3.41:9000/hongren/1636015513114_b39f8d25e4dd408685d1838307e688ce.jpg");
                            }
                        }).start();

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Bitmap bitmap1 = PhotoCompress.compressImage(bitmap[0]);
                        esc.addSelectJustification(CENTER);
                        esc.addRastBitImage(bitmap1, bitmap1.getWidth(), 0);
                    }

                    esc.addPrintAndFeedLines((byte) 1);
                }
            }
        }

        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand(); // 发送数据

        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);

        int rs;

        try {
            rs = mGpService.sendEscCommand(1, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];

            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Log.e("1","打印机连接不上");
            }
        } catch (RemoteException e) {}

    }

    private void printerText(EscCommand esc, EscCommand.JUSTIFICATION justification, EscCommand.ENABLE emphasized, EscCommand.ENABLE doubleHeight, EscCommand.ENABLE doubleWidth, EscCommand.ENABLE underline, String content, int feedLines) {
        esc.addSelectJustification(justification);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, emphasized, doubleHeight, doubleWidth, underline);
        esc.addText(content);
        esc.addPrintAndFeedLines((byte) feedLines);
    }

    /**
     * 根据图片地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static Bitmap  getImageFromNetByUrl(String strUrl){
        Bitmap bitmap = null;

        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            bitmap = BitmapFactory.decodeStream(inStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver);
//        unregisterReceiver(printerRealStatusBroadcastReceiver);

//        if (conn != null) {
//            unbindService(conn);
//        }

//        if (mGpService != null) {
//            try {
////                mGpService.closePort(1);
////            } catch (RemoteException e) {
//            }
//        }
    }
}
