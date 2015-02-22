package com.development.napptime.pix;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Napptime on 2/22/15.
 */
public class SuperApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "czKYh8xJNgkx86LmZTL1APShlqXSmw2KQXlKevPd", "KpwZwzlwdbIcwpAB9rfCsgTmrrB7UazKTNekBwy3");

    }
}
