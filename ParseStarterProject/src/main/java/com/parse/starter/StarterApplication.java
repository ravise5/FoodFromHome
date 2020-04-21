/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

  public static final String CHANNEL_ID = "exampleServiceChannel";

  @Override
  public void onCreate() {
    super.onCreate();
    createNotificationChannel();
    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("6128a450b83a26c5f733d5836d9363e4b9f2fa13")
            .clientKey("e9ae7ddb0df539319eb075b6b0eb36afdef31ede")
            .server("http://18.222.30.154:80/parse/")
            .build()
    );



    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }

  private void createNotificationChannel(){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      NotificationChannel serviceChannel = new NotificationChannel(
              CHANNEL_ID,
              "OTP",
              NotificationManager.IMPORTANCE_DEFAULT
      );

      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(serviceChannel);
    }


  }
}
