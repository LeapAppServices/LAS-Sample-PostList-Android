package as.leap.sample.postlist;

import android.app.Application;

import as.leap.LASACL;
import as.leap.LASConfig;
import as.leap.TestUtils;

public class App extends Application {

    public static final String APP_ID = "Replace this with your App Id";
    public static final String API_KEY = "Replace this with your Rest Key";

    @Override
    public void onCreate() {
        super.onCreate();

        if (APP_ID.startsWith("Replace") || API_KEY.startsWith("Replace")) {
            throw new IllegalArgumentException("Please replace with your app id and api key first before" +
                    "using LAS SDK.");
        }

        /*
         * Fill in this section with your LAS credentials
		 */
        LASConfig.setLogLevel(LASConfig.LOG_LEVEL_VERBOSE);
        LASConfig.initialize(this, APP_ID, API_KEY);

        LASACL defaultACL = new LASACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        LASACL.setDefaultACL(defaultACL, true);
    }

}
