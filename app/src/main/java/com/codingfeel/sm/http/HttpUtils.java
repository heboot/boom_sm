package com.codingfeel.sm.http;


import com.codingfeel.sm.BuildConfig;
import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.api.ApiClient;
import com.codingfeel.sm.model.UserModel;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.utils.EncryptUtils;
import com.codingfeel.sm.utils.LogUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Heboot on 16/6/24.
 */
public class HttpUtils {

    private String TAG = HttpUtils.class.getName();

    private OkHttpClient client = new OkHttpClient();

    /**
     * 请求对象
     */
    private Request request;

    /**
     * 头部信息
     */
    private Headers headers;

    /**
     * JSON类型参数类型
     */
    private final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * 用户ID
     */
    private Integer uid = 0;

    private String token;

    private String secret;

    private static HttpUtils httpUtils;

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    /**
     * post方法 json参数
     *
     * @return
     */
    public void execute(final HttpRequest httpRequest, final HttpResponse callback ) {
        doSignature(httpRequest);

        initRequest(httpRequest);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.e(TAG, httpRequest.toString() + ">>>" + result);
                callback.onResponse(result);
            }
        });
    }


    /**
     * 初始化请求体
     */
    private void initRequest(HttpRequest httpRequest) {

        if (httpRequest.getMethod() == HttpRequest.Method.GET) {
            setHttpRequestUrl(httpRequest);
            request = new Request.Builder().headers(headers)
                    .url(httpRequest.getHttpUrl())
                    .build();
        } else if (httpRequest.getMethod() == HttpRequest.Method.POST) {
            request = new Request.Builder()
                    .headers(headers)
                    .url(httpRequest.getHttpUrl())
                    .post(httpRequest.getFormBody())
                    .build();
        } else if (httpRequest.getMethod() == HttpRequest.Method.DELETE) {
            setHttpRequestUrl(httpRequest);
            request = new Request.Builder()
                    .headers(headers)
                    .url(httpRequest.getHttpUrl())
                    .delete(httpRequest.getFormBody())
                    .build();
        }


    }

    private void setHttpRequestUrl(HttpRequest httpRequest) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(httpRequest.getHttpUrl());
        urlBuilder.append("?");
        int mapSize = httpRequest.getParams().size();
        int position = 0;
        for (Map.Entry<String, String> entry : httpRequest.getParams().entrySet()) {
            urlBuilder.append(entry.getKey());
            urlBuilder.append("=");
            try {
                urlBuilder.append(URLEncoder.encode(entry.getValue().toString(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            if (position != mapSize - 1) {
                urlBuilder.append("&");
            }
            position++;
        }
        httpRequest.setHttpUrl(urlBuilder.toString());
    }


    /**
     * 初始化头部信息
     */
    private void doSignature(HttpRequest httpRequest) {
        String time = String.valueOf(new Date().getTime());
        String canonicalizedHeaders = "";
        headers = new Headers.Builder()
                .add("x-codingfeel-terminal", "android")
                .add("x-codingfeel-version", MyApplication.getInstance().getVersion())
                .add("Charset", "utf-8")
                .add("x-codingfeel-time", time)

                .build();
        httpRequest.addHeader("x-codingfeel-terminal", "android");
        UserModel user = UserService.getInstance().getSharePrefUser(MyApplication.getInstance());

        if (httpRequest.isGuide()) {
            return;
        }


        if (user != null) {
            uid = user.getUid();
            token = user.getToken();
            secret = user.getSecret();
            canonicalizedHeaders =
                    "x-codingfeel-uid:" + uid + "\n" +
                            "x-codingfeel-time:" + time + "\n" +
                            "x-codingfeel-terminal:" + "android\n" +
                            "x-codingfeel-version:" + MyApplication.getInstance().getVersion();
        } else {
            return;
        }


        //请求参数格式化
        String canonicalizedParmas = getCanonicalizedParams(httpRequest);


        //拼接成要加密的内容
        String content =
                httpRequest.matchMethod(httpRequest.getMethod()) + "\n"
                        + getSignNetworkUri(httpRequest) + "\n"
                        + token + "\n"
                        + canonicalizedHeaders + "\n"
                        + canonicalizedParmas + "\n";

        LogUtils.e(TAG + "jiami", content);


        String signature = "";
        //调用签名工具类进行签名
        try {
            String result = new String(android.util.Base64.encode(EncryptUtils.hmacSHA1Encrypt(content, secret), android.util.Base64.DEFAULT | android.util.Base64.NO_WRAP));
            signature = URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        headers = new Headers.Builder()
                .add("x-codingfeel-terminal", "android")
                .add("x-codingfeel-version", MyApplication.getInstance().getVersion())
                .add("Charset", "utf-8")
                .add("x-codingfeel-uid", String.valueOf(uid))
                .add("x-codingfeel-time", time)
                .add("Authorization", "Basic " + token + ":" + signature)
                .build();


        httpRequest.addHeader("x-codingfeel-terminal", "android");
        httpRequest.addHeader("x-codingfeel-version", MyApplication.getInstance().getVersion());
        httpRequest.addHeader("Charset", "utf-8");
        httpRequest.addHeader("x-codingfeel-uid", String.valueOf(uid));
        httpRequest.addHeader("x-codingfeel-time", time);
        httpRequest.addHeader("Authorization", "Basic " + token + ":" + signature);

    }

    private String getSignNetworkUri(HttpRequest httpRequest) {
        String signUri = httpRequest.getHttpUrl().replaceFirst(BuildConfig.HTTP_SERVER, "");
        if (signUri.contains("?")) {
            int index = signUri.indexOf("?");
            return signUri.substring(0, index);
        } else {
            return signUri;
        }
    }


    private String getCanonicalizedParams(HttpRequest httpRequest) {


        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : httpRequest.getParams().entrySet()) {
            try {
                paramList.add(new StringBuffer(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8")).toString().replaceAll("\\+", "%20"));

//            paramList.add(new StringBuffer(entry.getKey()).append("=")
//                    .append(entry.getValue()).toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        Collections.sort(paramList);
        StringBuilder paramBuilder = new StringBuilder();
        boolean first = true;
        for (String item : paramList) {
            if (first) {
                first = false;
            } else {
                paramBuilder.append("&");
            }
            paramBuilder.append(item);
        }

        return paramBuilder.toString();
    }


}
