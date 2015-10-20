package augsburg.se.alltagsguide.gcm;

/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import augsburg.se.alltagsguide.gcm.command.AnnouncementCommand;
import augsburg.se.alltagsguide.gcm.command.TestCommand;
import augsburg.se.alltagsguide.utilities.CommonUtilities;
import roboguice.util.Ln;

/**
 * {@link android.app.IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final Map<String, GCMCommand> MESSAGE_RECEIVERS;

    static {
        // Known messages and their GCM message receivers
        Map<String, GCMCommand> receivers = new HashMap<>();
        receivers.put("test", new TestCommand());
        receivers.put("announcement", new AnnouncementCommand());
        MESSAGE_RECEIVERS = Collections.unmodifiableMap(receivers);
    }

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String regId) {
        Ln.i("Device registered: regId=" + regId);
    }

    @Override
    protected void onUnregistered(Context context, String regId) {
        Ln.i("Device unregistered");
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        String extraData = intent.getStringExtra("extraData");
        Ln.d("Got GCM message, action=" + action + ", extraData=" + extraData);

        if (action == null) {
            Ln.e("Message received without command action");
            return;
        }

        action = action.toLowerCase();
        GCMCommand command = MESSAGE_RECEIVERS.get(action);
        if (command == null) {
            Ln.e("Unknown command received: " + action);
        } else {
            command.execute(this, action, extraData);
        }

    }

    @Override
    public void onError(Context context, String errorId) {
        Ln.e("Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Ln.w("Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }
}