package com.draustine.myfilereader;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;


public class DropBoxConnect {
    private final static String ACCESS_TOKEN = "sl.BL-NVXkYMUHgMFErlaBfrchEkL-PUAm2pOGcWwCC4y2iEXwudxkWRp8ELvklvLmQ_tiDqD_A-gWDxG6ZmE-fDbVrmuZQS3bDzddqrPaFKCEWvNu4djMe6KKBwHnvdAbwPqfFD6HbQmay";
    private final static String ACCESS_SECRET = "cyw2edpw31upt9t";
   // private final static AccessType ACCESS_TYPE = AccessType.DROPBOX;

    public static DbxClientV2 getClient() throws DbxException {
        //Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.APP_KEY));
        //DbxStreamReader reader = new DbxStreamReader(getInputStream());
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        return new DbxClientV2(config, ACCESS_TOKEN);
    }


}
