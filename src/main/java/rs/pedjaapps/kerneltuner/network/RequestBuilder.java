package rs.pedjaapps.kerneltuner.network;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;
import rs.pedjaapps.kerneltuner.utility.Utility;


/**
 * Created by pedja on 2/21/14 10.17.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 *
 * RequestBuilder is used for building http requests
 *
 * @author Predrag Čokulov
 */

public class RequestBuilder
{
    public enum PARAM
    {
        device, device_id, frequency, memory, device_name, mflops, norm_res, precision, exec_time, cores;

        String value;

        PARAM(String value)
        {
            this.value = value;
        }

        PARAM()
        {

        }

        @Override
        public String toString()
        {
            return value == null ? super.toString() : value;
        }
    }

    public enum CONTROLLER
    {
        scores
    }
    public enum ACTION
    {
        add;

        String value;

        ACTION(String value)
        {
            this.value = value;
        }

        ACTION()
        {

        }

        @Override
        public String toString()
        {
            return value == null ? super.toString() : value;
        }
    }

    public enum Method
    {
        POST, GET, PUT, DELETE,
    }

    public enum PostMethod
    {
        BODY, X_WWW_FORM_URL_ENCODED, FORM_DATA
    }

    private final StringBuilder builder;
    private boolean commandSet = false;
    private final List<NameValuePair> params;

    public String action;

    @NotNull
    private final Method mMethod;

    /**
     * */
    private String requestUrl;

    /**
     * Method for POST request<br>
     * For now either {@link com.preezm.preezm.network.RequestBuilder.PostMethod#BODY}, {@link com.preezm.preezm.network.RequestBuilder.PostMethod#X_WWW_FORM_URL_ENCODED} or {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}<br>
     * Cannot be null<br>
     * Default is {@link com.preezm.preezm.network.RequestBuilder.PostMethod#X_WWW_FORM_URL_ENCODED}*/
    @NotNull
    private PostMethod postMethod = PostMethod.X_WWW_FORM_URL_ENCODED;

    /**
     * Content type for request<br>
     * Only used if PostMethod is {@link com.preezm.preezm.network.RequestBuilder.PostMethod#BODY}'*/
    private String contentType = "text/plain";

    /**
     * Raw body if post method is {@link com.preezm.preezm.network.RequestBuilder.PostMethod#BODY}*/
    @Nullable
    private String requestBody;

    /**
     * File path (including name) for uploading file to server<br>
     * Used only with {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}<br>
     * Cannot be null if PostType is {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}*/
    @Nullable
    private String filePath;

    /**
     * Mime type of file for uploading file to server<br>
     * Used only with {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}<br>
     * Cannot be null if PostType is {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}*/
    private String mimeType;

    /**
     * Post parameter name for file upload<br>
     * Used only with {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}<br>
     * Cannot be null if PostType is {@link com.preezm.preezm.network.RequestBuilder.PostMethod#FORM_DATA}*/
    private String fileParamName;

    private int maxRetries = 3;
    public int retriesLeft = maxRetries;

    /**
     * Create new request builder<br>
     * If no other option is specified default options following:
     * <pre>
     * * requestUrl - {@link Constants#HTTP_REQUEST_ROOT()}
     * * postMethod - {@link com.preezm.preezm.network.RequestBuilder.PostMethod#X_WWW_FORM_URL_ENCODED}
     * * contentType - "text/plain"
     * </pre>*/
    public RequestBuilder(@NotNull Method method)
    {
        builder = new StringBuilder();
        params = new ArrayList<>();
        mMethod = method;
    }

    public RequestBuilder setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
        return this;
    }

    public RequestBuilder setFilePath(@Nullable String filePath)
    {
        this.filePath = filePath;
        return this;
    }

    public RequestBuilder setFileParamName(String fileParamName)
    {
        this.fileParamName = fileParamName;
        return this;
    }

    public RequestBuilder setRequestBody(@Nullable String body)
    {
        this.requestBody = body;
        return this;
    }

    public RequestBuilder setContentType(String contentType)
    {
        this.contentType = contentType;
        return this;
    }

    public RequestBuilder setPostMethod(@NotNull PostMethod postMethod)
    {
        this.postMethod = postMethod;
        return this;
    }

    public RequestBuilder setRequestUrl(String url)
    {
        if(TextUtils.isEmpty(url))return this;
        requestUrl = url;
        return this;
    }

    public RequestBuilder setMaxRetires(int maxRetries)
    {
        this.maxRetries = maxRetries;
        this.retriesLeft = maxRetries;
        return this;
    }

    public int getMaxRetries()
    {
        return maxRetries;
    }

    /**
     * This method should only be called once
     * In the future maybe it will just overwrite previously set command instead of throwing exception
     * @param controller controller for this request
     * @param action action for this request. Passing null will not set action
     * @throws IllegalArgumentException if command is already set
     * @throws NullPointerException if controller is null
     * @return same object for method chaining
     * */
    public RequestBuilder setCommand(CONTROLLER controller, ACTION action)
    {
        return setCommand(controller, action.toString());
    }

    /**
     * This method should only be called once
     * In the future maybe it will just overwrite previously set command instead of throwing exception
     * @param controller controller for this request
     * @param action action for this request. Passing null or empty string will not set action
     * @throws IllegalArgumentException if command is already set
     * @throws NullPointerException if controller is null
     * @return same object for method chaining
     * */
    public RequestBuilder setCommand(CONTROLLER controller, String action)
    {
        if(controller == null)
        {
            throw new NullPointerException("Controller cannot be null or empty");
        }
        if(commandSet)
        {
            throw new IllegalStateException("Command is already set");
        }
        this.action = action;
        if(!requestUrl.contains("?"))builder.append("?");
        builder.append("cmd=").append(controller);
        if(action != null && !action.isEmpty())
        {
            builder.append(".").append(action);
        }
        commandSet = true;
        return this;
    }

    /**
     * This method should only be called once
     * In the future maybe it will just overwrite previously set command instead of throwing exception
     * @param controller controller for this request
     * @throws IllegalArgumentException if command is already set
     * @throws NullPointerException if controller is null
     * @return same object for method chaining
     * */
    public RequestBuilder setCommand(CONTROLLER controller)
    {
        return setCommand(controller, (String)null);
    }

    /**
     * Add parameters for request
     * @param param parameter for this request. Passing null will not set action
     * @param value value of this parameter. Passing null or "" will not set parameter
     * @return same object for method chaining
     * */
    public RequestBuilder addParam(PARAM param, String value)
    {
        if(param == null || value == null || value.isEmpty())
        {
            if(PrefsManager.DEBUG())Log.e(Constants.LOG_TAG, "RequestBuilder >> addParam : param not set");
            return this;
        }
        if(mMethod == Method.GET)
        {
            if(!builder.toString().contains("?"))
                builder.append("?");
            else
                builder.append("&");
            builder.append(param).append("=").append(Utility.encodeString(value));
        }
        else if(mMethod == Method.POST || mMethod == Method.PUT) params.add(new BasicNameValuePair(param.toString(), value));
        return this;
    }

    /**
     * Add parameters for request
     * @param key parameter for this request. Passing null will not set action
     * @param value value of this parameter. Passing null or "" will not set paramert
     * @return same object for method chaining
     * */
    public RequestBuilder addParam(String key, String value)
    {
        if(key == null || key.isEmpty() || value == null || value.isEmpty())
        {
            if(PrefsManager.DEBUG())Log.e(Constants.LOG_TAG, "RequestBuilder >> addParam : param not set");
            return this;
        }
        if(mMethod == Method.GET)
        {
            if(!builder.toString().contains("?"))
                builder.append("?");
            else
                builder.append("&");
            builder.append(key).append("=").append(Utility.encodeString(value));
        }
        else if(mMethod == Method.POST || mMethod == Method.PUT) params.add(new BasicNameValuePair(key, value));
        return this;
    }

    /**
     * Add parameters for request
     * @param value url encoded param eg.: www.example.com/controller/action/{parame1}/{param2}
     * @return same object for method chaining
     * */
    public RequestBuilder addParam(String value)
    {
        if(value == null || value.isEmpty())
        {
            if(PrefsManager.DEBUG())Log.e(Constants.LOG_TAG, "RequestBuilder >> addParam : param not set");
            return this;
        }
        if(!builder.toString().endsWith("/"))
        {
            builder.append("/");
        }
        builder.append(value);
        return this;
    }

    /**
     * Convert this builder to String
     * */
    public String getUrlParams()
    {
        String urlParams = builder.toString();
        /*if(urlParams.endsWith("&"))
        {
            urlParams = urlParams.substring(0, urlParams.length() - 1);
        }*/
        return urlParams == null ? "" : urlParams;
    }

    public List<NameValuePair> getPOSTParams()
    {
        return params;
    }

    public static String getPOSTParams(List<NameValuePair> params)
    {
        StringBuilder builder = new StringBuilder();
        for(NameValuePair pair : params)
        {
            builder.append("&").append(pair.getName()).append("=").append(pair.getValue());
        }
        return builder.toString();
    }

    public String getRequestUrl()
    {
        return requestUrl + getUrlParams();
    }

    public static int[] isParamSet(List<NameValuePair> params, String name)
    {
        int offset = 0;
        for(NameValuePair pair : params)
        {
            if(pair.getName().equals(name))
                return new int[]{1, offset};
            offset++;
        }
        return new int[]{0, 0};
    }

    public void setParam(PARAM param, String value)
    {
        setParam(param.toString(), value);
    }

    public void setParam(String param, String value)
    {
        if (mMethod == Method.POST || mMethod == Method.PUT)
        {
            int[] isSet_Offset = RequestBuilder.isParamSet(params, param);
            if (isSet_Offset[0] == 1)
            {
                params.remove(isSet_Offset[1]);
            }
            params.add(new BasicNameValuePair(param, value));
        }
        else
        {
            Pattern pattern = Pattern.compile("[&|\\?](" + param + "=(\\S+?)(&|\\z))");
            Matcher matcher = pattern.matcher(builder.toString());
            if(matcher.find())
            {
                String match = matcher.group(1);
                int start = builder.toString().indexOf(match);
                int end = start + match.length();
                builder.replace(start, end, param + "=" + value);
            }
            else
            {
                addParam(param, value);
            }
        }
    }

    public Method getMethod()
    {
        return mMethod;
    }

    @NotNull
    public PostMethod getPostMethod()
    {
        return postMethod;
    }

    public String getContentType()
    {
        return contentType;
    }

    @Nullable
    public String getRequestBody()
    {
        return requestBody;
    }

    @Nullable
    public String getFilePath()
    {
        return filePath;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getFileParamName()
    {
        return fileParamName;
    }
}
