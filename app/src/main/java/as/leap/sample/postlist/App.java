package as.leap.sample.postlist;

import android.app.Application;

import as.leap.LCACL;
import as.leap.LeapCloud;

public class App extends Application {

    public static final String APP_ID = "Replace this with your App Id";
    public static final String API_KEY = "Replace this with your Rest Key";

    @Override
    public void onCreate() {
        super.onCreate();

        if (APP_ID.startsWith("Replace") || API_KEY.startsWith("Replace")) {
            throw new IllegalArgumentException("Please replace with your app id and api key first before" +
                    "using LeapCloud SDK.");
        }

        /*
         * Fill in this section with your LeapCloud credentials
		 */
        LeapCloud.setLogLevel(LeapCloud.LOG_LEVEL_VERBOSE);
        LeapCloud.initialize(this, APP_ID, API_KEY);

        LCACL defaultACL = new LCACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        LCACL.setDefaultACL(defaultACL, true);
    }

}
