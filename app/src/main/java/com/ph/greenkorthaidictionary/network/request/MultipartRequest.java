package com.ph.greenkorthaidictionary.network.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by preparkha on 15. 6. 10..
 */
public class MultipartRequest extends Request<String> {

    public static final String KEY_PICTURE = "mypicture";
    public static final String KEY_PICTURE_NAME = "filename";
    public static final String KEY_ROUTE_ID = "route_id";

    private HttpEntity mHttpEntity;

    private String mRouteId;
    private Response.Listener mListener;


    public MultipartRequest(String url, String filePath, String routeId,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        mRouteId = routeId;
        mListener = listener;
//        mHttpEntity = buildMultipartEntity(filePath);
    }

//    public MultipartRequest(String url, File file, String routeId,
//                            Response.Listener<String> listener,
//                            Response.ErrorListener errorListener) {
//        super(Method.POST, url, errorListener);
//
//        mRouteId = routeId;
//        mListener = listener;
//        mHttpEntity = buildMultipartEntity(file);
//    }
//
//    private HttpEntity buildMultipartEntity(String filePath) {
//        File file = new File(filePath);
//        return buildMultipartEntity(file);
//    }

//    private HttpEntity buildMultipartEntity(File file) {
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        String fileName = file.getName();
//        FileBody fileBody = new FileBody(file);
//        builder.addPart(KEY_PICTURE, fileBody);
//        builder.addTextBody(KEY_PICTURE_NAME, fileName);
//        builder.addTextBody(KEY_ROUTE_ID, mRouteId);
//        return builder.build();
//    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);

    }
}
