package org.edgar.project;


import android.os.Bundle;
import android.widget.TextView;

import org.edgar.project.activity.AppBaseActivity;
import org.edgar.project.engine.RemoteService;

public class MainActivity extends AppBaseActivity {

    private TextView tv;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tv);


    }

    @Override
    protected void loadData() {


        RemoteService.getInstance().invoke(this, "login", null, new AbstractRequestCallback() {
            @Override
            public void onSuccess(String content) {
                tv.setText(content+"\n");
            }
        });

    }
}
