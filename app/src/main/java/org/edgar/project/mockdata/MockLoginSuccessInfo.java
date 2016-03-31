package org.edgar.project.mockdata;

import com.google.gson.Gson;

import org.edgar.net.Response;

/**
 * Created by ChangLing on 16/3/29.
 */
public class MockLoginSuccessInfo extends MockService {
    @Override
    public String getJsonData() {
        Response string=new Response();
        string.setError(false);
        string.setResult("hello");

        return new Gson().toJson(string,Response.class);
    }
}
