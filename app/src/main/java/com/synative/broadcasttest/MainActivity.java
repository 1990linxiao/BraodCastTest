package com.synative.broadcasttest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 第一次测试，一个普通的activity，不停的开关，最后使用profiler强制回收内存,在退出后，内存中没有activity
 * 第二次测试，添加broadcast，并且不取消注册，测试同上,回收稍慢，但是仍然可以回收，最后内存中没有activity
 * 第三次测试，添加取消注册，测试同上,效果同第一次。
 * 第四次测试，取消broadcast，添加一个超大延迟发送的handler，造成内存泄漏，activity不会被回收，但是很快进程中的内存都被回收掉。
 * <p>
 * 小结：由于测试中，在点击返回后，进程属于空进城，所以会被系统直接回收掉，导致测试结果不是很准备，重新测试，这一次不要把进程做成空进程。
 * <p>
 * 添加firstActivity重新测试
 * 测试手机 ：meizu pro7 plus  android version:7.0
 * 第一次测试，取消broadcast，添加一个超大延迟发送的handler，造成内存泄漏，activity不会被回收。
 * 第二次测试，一个普通的activity，不停的开关，最后使用profiler强制回收内存,在退出后，内存中没有activity.
 * 第二次测试，添加broadcast，并且不取消注册，测试同上,仍然可以回收，最后内存中没有activity
 * 第三次测试，添加取消注册，测试同上,最后内存中没有activity。
 * <p>
 * 测试小结：
 * 1.在7.0的手机上面，即使不取消注册动态广播，也不会导致内存泄漏.
 * 2.由于只测试了7.0版本，所以不敢以偏概全，以前一直以为不取消注册，会导致泄漏，可能google在7.0以后的版本修复了这个问题，可能在低版本上确实存在这个问题。
 * 3.无论怎样，还是要在onDestroy中取消广播，防止出问题
 *
 * 佐证：leakcanary也没有报广播的内存泄漏
 */
public class MainActivity extends Activity {

  public static final String TEST_BROAD = "test_broad";
  private static final String TAG = "MainActivity";
  private MyReceiver receiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_second);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(TEST_BROAD);
    receiver = new MyReceiver();
    registerReceiver(receiver, intentFilter);
  }

  public void sendBroadcast(View view) {
    finish();
//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        Intent intent = new Intent(TEST_BROAD);
//        sendBroadcast(intent);
//      }
//    }, 20000000);
  }

  private class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      Log.i(TAG, "接收到消息");
      Toast.makeText(context, "接收到消息", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (receiver != null) {
      unregisterReceiver(receiver);
    }
  }
}
