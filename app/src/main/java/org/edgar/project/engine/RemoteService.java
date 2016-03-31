package org.edgar.project.engine;

import com.google.gson.Gson;

import org.edgar.activity.BaseActivity;
import org.edgar.net.DefaultThreadPool;
import org.edgar.net.HttpRequest;
import org.edgar.net.RequestCallback;
import org.edgar.net.RequestParameter;
import org.edgar.net.Response;
import org.edgar.net.URLData;
import org.edgar.net.UrlConfigManager;
import org.edgar.project.R;
import org.edgar.project.mockdata.MockService;

import java.util.List;

/**
 * Created by ChangLing on 16/3/29.
 */
public class RemoteService {
    private static RemoteService service = null;

    private RemoteService() {

    }

    public static synchronized RemoteService getInstance() {
        if (RemoteService.service == null) {
            RemoteService.service = new RemoteService();
        }
        return RemoteService.service;
    }

    public void invoke(final BaseActivity activity,
                       final String apiKey,
                       final List<RequestParameter> params,
                       final RequestCallback callBack) {

        final URLData urlData = UrlConfigManager.findURL(activity, apiKey, R.xml.url);
        if (urlData.getMockClass() != null) {
            try {
                MockService mockService = (MockService) Class.forName(
                        urlData.getMockClass()).newInstance();
                String strResponse = mockService.getJsonData();

                final Response responseInJson = new Gson().fromJson(strResponse,
                        Response.class);
                if (callBack != null) {
                    if (responseInJson.hasError()) {
                        callBack.onFail(responseInJson.getErrorMessage());
                    } else {
                        callBack.onSuccess(responseInJson.getResult());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            HttpRequest request = activity.getRequestManager().createRequest(
                    urlData, params, callBack);
            DefaultThreadPool.getInstance().execute(request);
        }
    }
}
