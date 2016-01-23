package info.androidhive.materialdesign.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import info.androidhive.materialdesign.app.AppConfig;

/**
 * Created by TARUN on 11/17/2015.
 */
public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();

                        String last6 = msg_from == null || msg_from.length() < 6 ?
                                msg_from : msg_from.substring(msg_from.length() - 6);

                        String message = msgs[i].getDisplayMessageBody();

                        if(last6.equals(AppConfig.MSG_SENDER_ID) && message.toLowerCase().contains("logging")) {
                            System.out.println("Msg received from kitchenvilla");
                           // SessionManager.setLogin(true);
                           // Toast.makeText(context, "Logged in successfully", Toast.LENGTH_LONG).show();
                            Intent local = new Intent();
                            local.setAction("com.hello.action");
                            context.sendBroadcast(local);
                            return;
                        }
                        String msgBody = msgs[i].getMessageBody();
                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
