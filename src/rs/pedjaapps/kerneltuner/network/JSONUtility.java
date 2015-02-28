package rs.pedjaapps.kerneltuner.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;

/**
 * Created by pedja on 1/22/14.
 * Class responsible for handling all parsing of server responses in JSON format
 *
 * @author Predrag Čokulov
 */
public class JSONUtility
{
    private JSONObject jsonObject;
    private final Internet.Response serverResponse;

    private Object parseObject;
    private Response response;

    public enum Key
    {
        status, message;

        String mValue;

        Key(String mValue)
        {
            this.mValue = mValue;
        }

        Key()
        {
        }


        @Override
        public String toString()
        {
            return mValue == null ? super.toString() : mValue;
        }
    }

    public JSONUtility(String stringObject)
    {
        this.serverResponse = new Internet.Response();
        serverResponse.code = 200;
        serverResponse.request = null;
        serverResponse.responseData = stringObject;
        serverResponse.responseMessage = null;
        if (serverResponse.isResponseOk())
        {
            try
            {
                fixResponse();
                jsonObject = new JSONObject(this.serverResponse.responseData);
                checkErrors();
            }
            catch (Exception e)
            {
                if (PrefsManager.DEBUG()) Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
                if (PrefsManager.DEBUG())
                    Log.e(Constants.LOG_TAG, "JSONUtility :: Failed to parse json");
                Crashlytics.setString("response message", serverResponse.responseMessage);
                Crashlytics.setString("response data", serverResponse.responseData);
                Crashlytics.setInt("response code", serverResponse.code);
                Crashlytics.setString("request url", serverResponse.request);
                Crashlytics.logException(e);
            }
        }
    }

    public JSONUtility(Internet.Response serverResponse)
    {
        this.serverResponse = serverResponse;
        if (serverResponse.isResponseOk())
        {
            try
            {
                fixResponse();
                jsonObject = new JSONObject(this.serverResponse.responseData);
                checkErrors();
            }
            catch (Exception e)
            {
                if (PrefsManager.DEBUG()) Log.e(Constants.LOG_TAG, "JSONUtility " + e.getMessage());
                if (PrefsManager.DEBUG())
                    Log.e(Constants.LOG_TAG, "JSONUtility :: Failed to parse json");
                Crashlytics.setString("response message", serverResponse.responseMessage);
                Crashlytics.setString("response data", serverResponse.responseData);
                Crashlytics.setInt("response code", serverResponse.code);
                Crashlytics.setString("request url", serverResponse.request);
                Crashlytics.logException(e);
            }
        }
    }

    /**
     * Sometimes shit happens so server returns some "Notices" and/or "Warnings"
     * This method tries to fix that
     * This method assumes that there are no json object/array("{,},[,]") characters before json content starts
     *
     * This is for debugging only while app is still in development
     *
     * This method is not error-proof, it wont handle all scenarios where response is wrong
     * */
    private void fixResponse()
    {
        if(serverResponse.responseData.startsWith("{") || serverResponse.responseData.startsWith("["))//response is ok
            return;

        //response is not json or it has php warnings/notices before json response. try to fix it

        int jsonStartIndex = serverResponse.responseData.indexOf("{");//first assume root element is json object

        if(jsonStartIndex == -1)//if its -1, try with "["
        {
            jsonStartIndex = serverResponse.responseData.indexOf("[");
            if(jsonStartIndex != -1)
            {
                //fix it
                serverResponse.responseData = serverResponse.responseData.substring(jsonStartIndex, serverResponse.responseData.length());
            }
        }
        else
        {
            //fix it
            serverResponse.responseData = serverResponse.responseData.substring(jsonStartIndex, serverResponse.responseData.length());
        }
    }

    private void checkErrors()
    {
        if (jsonObject == null) return;
        response = new Response();
        response.status = jsonObject.optInt(Key.status.toString(), 1);//assume success if no error
        response.message = jsonObject.optString(Key.message.toString(), null);
        //workaround until response is fixed on server
        if (response.message == null) response.message = jsonObject.optString("0", null);
    }

    public void parseSomething(String inboxType)
    {
        if (jsonObject == null) return;
        //parseObject = something;
    }

    public <T> T getParseObject()
    {
        return (T) parseObject;
    }

    public <T> T getParseObject(Class<T> type)
    {
        return (T) parseObject;
    }

    public boolean isSuccess()
    {
        return jsonObject != null;
    }

    public Internet.Response getServerResponse()
    {
        return serverResponse;
    }

    @Override
    public String toString()
    {
        return jsonObject.toString();
    }

    public boolean checkResponse()
    {
        if (response == null) return true;//assume success if response is null
        return response.status == 1;
    }

    public Response getResponse()
    {
        return response;
    }

    public static class Response
    {
        public int status;
        public String message;
    }
}
