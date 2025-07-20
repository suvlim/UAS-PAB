package com.uph23.edu.sahabattani;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SahabatTaniApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm
        Realm.init(this);
        // Set up Realm configuration
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("sahabattani.realm")
                .schemaVersion(1) // Set to match or exceed the last version (1 in this case)
                .deleteRealmIfMigrationNeeded() // Use this for development to avoid migration issues
                .build();
        Realm.setDefaultConfiguration(config);
}
}
