package org.edgar.project.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import org.edgar.activity.BaseActivity;
import org.edgar.net.RequestCallback;

/**
 * Created by ChangLing on 16/3/29.
 */
public abstract class AppBaseActivity extends BaseActivity {
    protected boolean needCallback;

    protected ProgressDialog dlg;

    public abstract class AbstractRequestCallback implements RequestCallback {

        public abstract void onSuccess(String content);

        public void onFail(String errorMessage) {
            //dlg.dismiss();

            new AlertDialog.Builder(AppBaseActivity.this).setTitle("出错啦")
                    .setMessage(errorMessage).setPositiveButton("确定", null)
                    .show();
        }

        public void onCookieExpired() {
            //dlg.dismiss();

            new AlertDialog.Builder(AppBaseActivity.this)
                    .setTitle("出错啦")
                    .setMessage("Cookie过期，请重新登录")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    //跳转到登陆页面


                                }
                            }).show();
        }
    }
}

