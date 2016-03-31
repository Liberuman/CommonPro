package org.edgar.project.mockdata;

import org.edgar.net.Response;

/**
 * Created by ChangLing on 16/3/29.
 */
public abstract class MockService {
    public abstract String getJsonData();

    public Response getSuccessResponse() {
        Response response = new Response();
        response.setError(false);
        response.setErrorType(0);
        response.setErrorMessage("");

        return response;
    }

    public Response getFailResponse(int errorType, String errorMessage) {
        Response response = new Response();
        response.setError(true);
        response.setErrorType(errorType);
        response.setErrorMessage(errorMessage);

        return response;
    }
}

