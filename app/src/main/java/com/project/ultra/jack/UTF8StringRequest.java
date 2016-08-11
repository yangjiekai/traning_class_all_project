package com.project.ultra.jack;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by ultra-jack on 2016/8/3.
 */
public class UTF8StringRequest extends StringRequest {

    public UTF8StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(url,listener,errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String utf8String=null;
        try {
            utf8String=new String(response.data,"UTF-8");
            return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));



        } catch (UnsupportedEncodingException e) {
          return Response.error(new ParseError(e));
        }


    }
}

