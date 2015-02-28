package rs.pedjaapps.kerneltuner.network;

/**
 * Created by pedja on 2/19/14 10.17.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 * @author Predrag Čokulov
 */
public interface ResponseHandler
{
    public static final int RESPONSE_STATUS_OK = 1;
    public static final int RESPONSE_STATUS_ERROR = 0;
    /**
     * Called if response from server is successful and doesn't contains errors
     * */
    public void onResponse(int responseStatus, JSONUtility jsonUtility);
}
