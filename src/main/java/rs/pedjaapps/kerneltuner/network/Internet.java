package rs.pedjaapps.kerneltuner.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.apache.http.NameValuePair;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import rs.pedjaapps.kerneltuner.Constants;
import rs.pedjaapps.kerneltuner.MainApp;
import rs.pedjaapps.kerneltuner.R;
import rs.pedjaapps.kerneltuner.utility.PrefsManager;

/**
 * @author Predrag Čokulov
 */

public class Internet
{
    private static final boolean log = PrefsManager.DEBUG();
    private static final boolean printResponse = log && true;

    private static final String LINE_FEED = "\r\n";
    private static final String BOUNDARY = "===" + System.currentTimeMillis() + "===";

    private Internet()
    {
    }

    /**
     * Executes HTTP POST request and returns response as string<br>
     * This method will not check if response code from server is OK ( < 400)<br>
     *
     * @param requestBuilder request builder object, used to build request. cannot be null
     * @return server response as string
     */
    public static Response executeHttpRequest(@NotNull RequestBuilder requestBuilder)
    {
        Response response = new Response();
        InputStream is = null;
        try
        {

            HttpURLConnection conn = (HttpURLConnection) new URL(requestBuilder.getRequestUrl()).openConnection();
            //conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(Constants.CONN_TIMEOUT);
            conn.setRequestMethod(requestBuilder.getMethod().toString());
            conn.setDoInput(true);


            //httpPost.setHeader("Authorization", "Basic " + Base64.encodeToString((Constants.HTTP_USER + ":" + Constants.HTTP_PASS).getBytes(), Base64.NO_WRAP));
            switch (requestBuilder.getMethod())
            {
                case POST:
                case PUT://TODO are put and post same in httpurlconnection?
                    conn.setDoOutput(true);
                    switch (requestBuilder.getPostMethod())
                    {
                        case BODY:
                            /*((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(requestBuilder.getRequestBody()));
                            httpRequest.setHeader("Accept", "application/json");
                            httpRequest.setHeader("Content-type", "application/json");*/
                            //TODO
                            break;
                        case X_WWW_FORM_URL_ENCODED:
                            for(NameValuePair pair : requestBuilder.getPOSTParams())
                            {
                                conn.setRequestProperty(pair.getName(), pair.getValue());
                            }
                            break;
                        case FORM_DATA:

                            OutputStream os = conn.getOutputStream();
                            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, Constants.ENCODING), true);
                            for (NameValuePair pair : requestBuilder.getPOSTParams())
                            {
                                writer.append("--").append(BOUNDARY).append(LINE_FEED);
                                writer.append("Content-Disposition: form-data; name=\"").append(pair.getName()).append("\"").append(LINE_FEED);
                                writer.append("Content-Type: text/plain; charset=" + Constants.ENCODING).append(LINE_FEED);
                                writer.append(LINE_FEED);
                                writer.append(pair.getValue()).append(LINE_FEED);
                                writer.flush();
                            }

                            if (requestBuilder.getFilePath() != null)
                            {
                                File file = new File(requestBuilder.getFilePath());
                                writer.append("--").append(BOUNDARY).append(LINE_FEED);
                                writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"").append(file.getName()).append("\"").append(LINE_FEED);
                                writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
                                writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                                writer.append(LINE_FEED);
                                writer.flush();

                                FileInputStream inputStream = new FileInputStream(file);
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1)
                                {
                                    os.write(buffer, 0, bytesRead);
                                }
                                os.flush();
                                inputStream.close();

                                writer.append(LINE_FEED);
                                writer.flush();
                            }
                            writer.append(LINE_FEED).flush();
                            writer.append("--").append(BOUNDARY).append("--").append(LINE_FEED);
                            writer.close();

                            os.close();
                            break;
                    }
                    break;
                case GET:
                    break;
                case DELETE:
                    break;
            }

            conn.connect();

            response.responseData = readStreamToString(is = conn.getInputStream());
            response.code = conn.getResponseCode();
            response.responseMessage = conn.getResponseMessage();
            if (log)
                Log.d(Constants.LOG_TAG, "httpPost[" + requestBuilder.getRequestUrl() + "]: " + response);
        }
        catch (UnsupportedEncodingException e)
        {
            response.code = Response.RESPONSE_CODE_UNSUPPORTED_ENCODING;
            response.responseMessage = MainApp.getContext().getString(R.string.network_error);
            Crashlytics.logException(e);
        }
        catch (MalformedURLException e)
        {
            response.code = Response.RESPONSE_CODE_MALFORMED_URL;
            response.responseMessage = MainApp.getContext().getString(R.string.network_error);
            Crashlytics.logException(e);
        }
        catch (IOException e)
        {
            response.code = Response.RESPONSE_CODE_IO_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.network_error);
            Crashlytics.logException(e);
        }
        catch (Exception e)
        {
            response.code = Response.RESPONSE_CODE_UNKNOWN_ERROR;
            response.responseMessage = MainApp.getContext().getString(R.string.unknown_error);
            Crashlytics.logException(e);
        }
        finally
        {
            response.request = requestBuilder.getRequestUrl();
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException ignored){}
            }
        }

        return response;
    }

    public static String readStreamToString(InputStream stream) throws IOException
    {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder string = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null)
        {
            string.append(line);
        }
        return string.toString();
    }


    public static class Response
    {
        public static final int RESPONSE_CODE_UNKNOWN_ERROR = -1;
        public static final int RESPONSE_CODE_UNSUPPORTED_ENCODING = -2;
        public static final int RESPONSE_CODE_MALFORMED_URL = -3;
        public static final int RESPONSE_CODE_IO_ERROR = -4;

        public int code = RESPONSE_CODE_UNKNOWN_ERROR;
        public String responseMessage;
        public String responseData;
        public String request;

        public boolean isResponseOk()
        {
            return code < 400 && code > 0;
        }

        @Override
        public String toString()
        {
            return "Response{" +
                    "code=" + code +
                    ", responseMessage='" + responseMessage + '\'' +
                    (printResponse ? ", responseData='" + responseData : "") + '\'' +
                    '}';
        }
    }

    /*public PersistentCookieStore getCookieStore()
    {
        return cookieStore;
    }*/
}