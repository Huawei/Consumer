
package com.example.carddemo;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;

import com.huawei.hicarsdk.controller.AbstractCarOperationService;

public class MyDemoOperateMgr extends AbstractCarOperationService {
    private CardMgr mCardMgr;

    /**
     * When hicar starts, it will notify AbstractCarOperationMgr that focuses on the hicar startup event.
     * If the application focuses on hicar startup, it can implement its own business here. For example,
     * in the demonstration use case, we pay attention to hicar startup time and call CardMgr
     * in the startup callback to complete the card construction sending service.
     *
     * @param bundle
     */
    @Override
    public void hiCarStarted(Bundle bundle) {
        mCardMgr = new CardMgr(getApplicationContext());
        mCardMgr.createCards();
    }

    /**
     * When Hicar stops, it will notify AbstractCarOperationMgr that focuses on the hicar stop event.
     * The application can complete some related services here, such as cleaning up resource data.
     *
     * @param bundle
     */
    @Override
    public void hiCarStopped(Bundle bundle) {

    }

    /**
     * When hicar actively cleans up a card and the app pays attention to hicar's card cleanup event,
     * hicar will notify the app card and clean it up, and the app should complete its own business data cleanup work here.
     *
     * @param cardId remove cardId
     */
    @Override
    public void removeCard(int cardId) {

    }

    /**
     *If the application pays attention to the event of the card button callback,
     * hicar will callback this method to notify the application when the control is sent to the remote card drawn by the hicar data.
     * The bundle here is the bundle that is attached to the application using the builder settings control.
     * The application can use this bundle to confirm that the control was triggered. In order to complete the business scenario,
     * hicar will put the cardId of the card triggered by the event in the bundle and bring it back to the three parties. The key is “cardId”.
     *
     * @param bundle
     */
    @Override
    public void callBackApp(Bundle bundle) {
        if (bundle.containsKey("hiCarAction")) {
            String action = bundle.getString("hiCarAction");
            if (TextUtils.equals(action, "BootUp")) {
                int displayId = bundle.getInt("displayId");
                DisplayManager displayManager =
                    (DisplayManager) getApplication().getSystemService(Context.DISPLAY_SERVICE);
                Display display = displayManager.getDisplay(displayId);
                Context carContext = getApplication().createDisplayContext(display);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                carContext.startActivity(intent);
            }
        }
    }

    /**
     * When the application is applied on the hicar link, it will automatically ask the application
     * whether hicar is required to keep the link. If the application wants hicar to keep the link application,
     * it can return true on this interface, otherwise return false;
     * when it returns true, hicar next time The old link will be used when the application interface needs to be called back.
     * After returning false, hicar will actively release the link after the callback is completed. Next time,
     * if there is a related callback, hicar will establish the link again.
     *
     * @return
     */
    @Override
    public boolean isKeepConnect() {
        return true;
    }
}
