package com.draustine.myfilereader;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;


public class DropBoxConnect {
    private static final String ACCESS_TOKEN = "sl.BMJmzDGl-DFOhOiuv9-T7hpMIw3CBL3ik21Jr5yf7K94WctgfKkpEC4HL6ttrAW4eD8-oCBF-TgY5eGbw-6dIjrIf3GugAEWxk2bFFmq6rsdlSSSyWYUgvfyhPxNGkMYt_IxoKXsBZak";

    public static void main(String args[]) throws DbxException {
        // Create Dropbox client
        DbxCredential credential = new DbxCredential(ACCESS_TOKEN, 2500L, "en", "hsfmx2327oh2f7d", "cyw2edpw31upt9t");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, credential);
    }
}
