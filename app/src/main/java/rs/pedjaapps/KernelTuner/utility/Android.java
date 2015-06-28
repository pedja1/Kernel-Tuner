package rs.pedjaapps.KernelTuner.utility;

import android.os.Build;

/**
 * Created by pedja on 12.2.15. 08.40.
 * This class is part of the Preezm
 * Copyright © 2015 ${OWNER}
 */
public final class Android
{
    private Android()
    {

    }

    /**
     * Check if system is minimum Android 5.0 Lollipop (SDK 21)
     * @return true if system os is 21 or higher, false otherwise*/
    public static boolean hasLollipop()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
