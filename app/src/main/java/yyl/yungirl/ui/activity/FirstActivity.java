package yyl.yungirl.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by yinyiliang on 2016/7/2 0002.
 */
public class FirstActivity extends Activity {

    private MyHandler mMyHandler = new MyHandler(this);

    private static class MyHandler extends Handler{
        private final WeakReference<FirstActivity> mWeakReference;

        private MyHandler(FirstActivity weakReference) {
            mWeakReference = new WeakReference<>(weakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FirstActivity firstActivity = mWeakReference.get();
            if (firstActivity != null) {
                Intent intent = new Intent(firstActivity,MainActivity.class);
                firstActivity.startActivity(intent);
                firstActivity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                firstActivity.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyHandler.sendEmptyMessageDelayed(1,1000);
    }
}
