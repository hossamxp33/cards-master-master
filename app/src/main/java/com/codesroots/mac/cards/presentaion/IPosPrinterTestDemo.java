package com.codesroots.mac.cards.presentaion;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.codesroots.mac.cards.R;
import com.codesroots.mac.cards.models.Buypackge;
import com.codesroots.mac.cards.presentaion.Utils.HandlerUtils;
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel;
import com.codesroots.mac.cards.presentaion.payment.Payment;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;


public class IPosPrinterTestDemo {

    private static final String TAG = "IPosPrinterTestDemo";
    /* Demo 版本号*/
    private static final String VERSION = "V1.1.1";
    private static IPosPrinterTestDemo mAidlUtil = new IPosPrinterTestDemo();


    private Button b_barcode, b_pic, b_qcode, b_self, b_text, b_table, b_init, b_lines, b_test, b_testall;
    private Button b_erlmo, b_meituan, b_baidu, b_query, b_bytes, b_length, b_continu, b_koubei;
    private Button b_runpaper, b_motor, b_demo, b_wave, b_error, b_loop;

    /*定义打印机状态*/
    private final int PRINTER_NORMAL = 0;
    private final int PRINTER_PAPERLESS = 1;
    private final int PRINTER_THP_HIGH_TEMPERATURE = 2;
    private final int PRINTER_MOTOR_HIGH_TEMPERATURE = 3;
    private final int PRINTER_IS_BUSY = 4;
    private final int PRINTER_ERROR_UNKNOWN = 5;
    /*打印机当前状态*/
    private int printerStatus = 0;

    /*定义状态广播*/
    private final String PRINTER_NORMAL_ACTION = "com.iposprinter.iposprinterservic e.NORMAL_ACTION";
    private final String PRINTER_PAPERLESS_ACTION = "com.iposprinter.iposprinterservice.PAPERLESS_ACTION";
    private final String PRINTER_PAPEREXISTS_ACTION = "com.iposprinter.iposprinterservice.PAPEREXISTS_ACTION";
    private final String PRINTER_THP_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_HIGHTEMP_ACTION";
    private final String PRINTER_THP_NORMALTEMP_ACTION = "com.iposprinter.iposprinterservice.THP_NORMALTEMP_ACTION";
    private final String PRINTER_MOTOR_HIGHTEMP_ACTION = "com.iposprinter.iposprinterservice.MOTOR_HIGHTEMP_ACTION";
    private final String PRINTER_BUSY_ACTION = "com.iposprinter.iposprinterservice.BUSY_ACTION";
    private final String PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION = "com.iposprinter.iposprinterservice.CURRENT_TASK_PRINT_COMPLETE_ACTION";
    private final String GET_CUST_PRINTAPP_PACKAGENAME_ACTION = "android.print.action.CUST_PRINTAPP_PACKAGENAME";

    /*定义消息*/
    private final int MSG_TEST = 1;
    private final int MSG_IS_NORMAL = 2;
    private final int MSG_IS_BUSY = 3;
    private final int MSG_PAPER_LESS = 4;
    private final int MSG_PAPER_EXISTS = 5;
    private final int MSG_THP_HIGH_TEMP = 6;
    private final int MSG_THP_TEMP_NORMAL = 7;
    private final int MSG_MOTOR_HIGH_TEMP = 8;
    private final int MSG_MOTOR_HIGH_TEMP_INIT_PRINTER = 9;
    private final int MSG_CURRENT_TASK_PRINT_COMPLETE = 10;

    /*循环打印类型*/
    private final int MULTI_THREAD_LOOP_PRINT = 1;
    private final int INPUT_CONTENT_LOOP_PRINT = 2;
    private final int DEMO_LOOP_PRINT = 3;
    private final int PRINT_DRIVER_ERROR_TEST = 4;
    private final int DEFAULT_LOOP_PRINT = 0;

    //循环打印标志位
    private int loopPrintFlag = DEFAULT_LOOP_PRINT;
    private byte loopContent = 0x00;
    private int printDriverTestCount = 0;
    private Context context;


    private TextView info;
    private IPosPrinterService mIPosPrinterService;
    private IPosPrinterCallback callback = null;

    private Random random = new Random();
    private HandlerUtils.MyHandler handler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static IPosPrinterTestDemo getInstance() {
        return mAidlUtil;
    }

    /**
     * 消息处理
     */

    private BroadcastReceiver IPosPrinterStatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                Log.d(TAG, "IPosPrinterStatusListener onReceive action = null");
                return;
            }
            Log.d(TAG, "IPosPrinterStatusListener action = " + action);
            if (action.equals(PRINTER_NORMAL_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_IS_NORMAL, 0);
            } else if (action.equals(PRINTER_PAPERLESS_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_PAPER_LESS, 0);
            } else if (action.equals(PRINTER_BUSY_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_IS_BUSY, 0);
            } else if (action.equals(PRINTER_PAPEREXISTS_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_PAPER_EXISTS, 0);
            } else if (action.equals(PRINTER_THP_HIGHTEMP_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_THP_HIGH_TEMP, 0);
            } else if (action.equals(PRINTER_THP_NORMALTEMP_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_THP_TEMP_NORMAL, 0);
            } else if (action.equals(PRINTER_MOTOR_HIGHTEMP_ACTION))  //此时当前任务会继续打印，完成当前任务后，请等待2分钟以上时间，继续下一个打印任务
            {
                handler.sendEmptyMessageDelayed(MSG_MOTOR_HIGH_TEMP, 0);
            } else if (action.equals(PRINTER_CURRENT_TASK_PRINT_COMPLETE_ACTION)) {
                handler.sendEmptyMessageDelayed(MSG_CURRENT_TASK_PRINT_COMPLETE, 0);
            } else if (action.equals(GET_CUST_PRINTAPP_PACKAGENAME_ACTION)) {
                String mPackageName = intent.getPackage();
                Log.d(TAG, "*******GET_CUST_PRINTAPP_PACKAGENAME_ACTION：" + action + "*****mPackageName:" + mPackageName);

            } else {
                handler.sendEmptyMessageDelayed(MSG_TEST, 0);
            }
        }
    };


    /**
     * 绑定服务实例
     */
    private ServiceConnection connectService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
            callback = new IPosPrinterCallback.Stub() {

                @Override
                public void onRunResult(final boolean isSuccess) throws RemoteException {
                    Log.i(TAG, "result:" + isSuccess + "\n");
                }

                @Override
                public void onReturnString(final String value) throws RemoteException {
                    Log.i(TAG, "result:" + value + "\n");
                }
            };
            client = new GoogleApiClient.Builder(context).addApi(AppIndex.API).build();
            ThreadPoolManager.getInstance().executeTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        mIPosPrinterService.printerInit(callback);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });

            client.connect();
            AppIndex.AppIndexApi.start(client, getIndexApiAction());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIPosPrinterService = null;
        }
    };

    public static void writePrintDataToCacheFile(String printStr, byte[] printByteData) {
        String printDataDirPath = Environment.getExternalStorageDirectory()+ File.separator+"PrintDataCache";
        String printDataFilePath1 = printDataDirPath + File.separator+ "printdata_1.txt";
        String printDataFilePath2 = printDataDirPath + File.separator+ "printdata_2.txt";
        Log.d(TAG, "printDataDirPath:" + printDataDirPath);

        File fileDir = new File(printDataDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }


        if (fileDir.exists()) {
            Log.d(TAG, printDataDirPath + " is exists!!!!!");
        } else {
            Log.d(TAG, printDataDirPath + " is not exists!!!!!");
        }

        File printDataFile = new File(printDataFilePath1);
        if (printDataFile.exists() && printDataFile.isFile()) {
            if (printDataFile.length() > 5 * 1024 * 1024) {
                printDataFile = new File(printDataFilePath2);
                if (printDataFile.exists() && printDataFile.isFile()) {
                    if (printDataFile.length() > 5 * 1024 * 1024) {
                        return;
                    }
                } else {
                    try {
                        printDataFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            printDataFile = new File(printDataFilePath2);
            if (printDataFile.exists() && printDataFile.isFile()) {
                if (printDataFile.length() > 5 * 1024 * 1024) {
                    printDataFile = new File(printDataFilePath1);
                }
            } else {
                printDataFile = new File(printDataFilePath1);
                try {
                    printDataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if ((printStr == null) && (printByteData == null)) {
            return;
        }

        FileOutputStream fileOut = null;
        if (printStr != null) {
            BufferedWriter outw = null;
            try {
                fileOut = new FileOutputStream(printDataFile, true);
                outw = new BufferedWriter(new OutputStreamWriter(fileOut));
                outw.write(printStr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (outw != null) {
                        outw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (printByteData != null) {
            BufferedOutputStream bufOut = null;
            try {
                fileOut = new FileOutputStream(printDataFile, true);
                bufOut = new BufferedOutputStream(fileOut);
                bufOut.write(printByteData);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (fileOut != null) {
                        fileOut.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (bufOut != null) {
                        bufOut.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connectPrinterService(Context context) {
        this.context = context;
        Intent intent = new Intent();
        intent.setPackage("com.iposprinter.iposprinterservice");
        intent.setAction("com.iposprinter.iposprinterservice.IPosPrintService");
        //startService(intent);
        context.bindService(intent, connectService, Context.BIND_AUTO_CREATE);

    }




    /**
     * 获取打印机状态
     */
    public int getPrinterStatus() {

        Log.i(TAG, "***** printerStatus" + printerStatus);
        try {
            printerStatus = mIPosPrinterService.getPrinterStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "#### printerStatus" + printerStatus);
        return printerStatus;
    }
    public boolean isConnect() {
        return mIPosPrinterService != null;
    }

    /**
     * 打印机初始化
     */


    /**
     * 打印机自检
     */


    /**
     * 获取打印长度
     */


    /**
     * 打印机走纸
     */


    /**
     * 打印空白行
     */


    /**
     * 打印随机黑点
     */


    /**
     * 打印大黑块
     */

    /**
     * 打印文字
     */
    public void printText(Buypackge value, Bitmap bitmaps, Bitmap notes, MainViewModel viewmodel)  {
        if (mIPosPrinterService != null )
            if (getPrinterStatus() == PRINTER_NORMAL)
                ThreadPoolManager.getInstance().executeTask(new Runnable() {
                    @Override
                    public void run() {
                        BitmapFactory.Options options = new BitmapFactory.Options();

                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo, options);

                        try {

                                for (Integer i = 0; i < value.getPencode().size(); i++) {


                                    mIPosPrinterService.printBitmap(1, 12, bitmaps, callback);
                                    mIPosPrinterService.printBlankLines(1, 8, callback);
                                    mIPosPrinterService.PrintSpecFormatText(value.getPrice()+"\n", "ST", 48, 1, callback);
                                    mIPosPrinterService.PrintSpecFormatText("PIN\n", "ST", 48, 1, callback);

                                    mIPosPrinterService.PrintSpecFormatText(value.getPencode().get(i).getPencode()+"\n", "ST", 32, 1, callback);
                                    mIPosPrinterService.printBlankLines(1, 8, callback);
                                    mIPosPrinterService.printBitmap(1, 8, notes, callback);

                                    mIPosPrinterService.printBlankLines(1, 8, callback);
                                    mIPosPrinterService.printSpecifiedTypeText("SERIAL :" + value.getPencode().get(i).getSerial()+"\n", "ST", 32, callback);
                                    mIPosPrinterService.printBlankLines(1, 8, callback);
                                    mIPosPrinterService.printSpecifiedTypeText("EXPIRY :" + value.getPencode().get(i).getExpdate()+"\n", "ST", 32, callback);
                                    mIPosPrinterService.printSpecifiedTypeText("Merchant :" + value.getSalor()+"\n", "ST", 32, callback);

                                    mIPosPrinterService.printBlankLines(1, 8, callback);
                                    mIPosPrinterService.PrintSpecFormatText("Date & Time\n", "ST", 32,1, callback);
                                    mIPosPrinterService.printBlankLines(1, 16, callback);
                                    mIPosPrinterService.printBlankLines(1, 16, callback);
                                    mIPosPrinterService.PrintSpecFormatText(value.getTime()+"\n", "ST", 32, 1, callback);
                                    mIPosPrinterService.printSpecifiedTypeText("********************************", "ST", 24, callback);
                                    mIPosPrinterService.printBitmap(1, 2, bitmap, callback);
                                    mIPosPrinterService.printBlankLines(1, 16, callback);

                                    //   bitmapRecycle(bitmap);
                                    mIPosPrinterService.printerPerformPrint(160, callback);

                                    viewmodel.PrintOrder(value.getOpno());


                                }
                            Intent intent = new Intent(context, Payment.class);

                            intent.putExtra("myobj", value);

                            (context).startActivity(intent);
                        } catch (RemoteException e) {
                            e.printStackTrace();

                        }
                    }
                });
    }

    /**
     * 打印表格
     */


    /**
     * 打印图片
     */

    /**
     * 打印一维码
     */


    /**
     * 打印二维码
     */


    /**
     * 打印饿了么小票
     */


    /**
     * 打百度小票
     */


    /**
     * 口碑外卖
     */


    /**
     * 美团小票
     */


    /**
     * 打印大数据
     * numK: 打印数据的大小，以4k为单位，最大127个4k，既十六进制0x7f
     * data: 打印内容
     */


    /**
     * 综合打印测试
     */


    /**
     * 手动输入指令打印
     */


    /**
     * 连续打印测试
     */


    /**
     * 波形测试
     */


    /**
     * 循环打印
     */


    /**
     * 并发多线程打印
     */




    /**
     * 每次下发内容以64k为单位递增，最大512k
     */

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("IPosPrinterTestDemo Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


}
