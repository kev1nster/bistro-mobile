package thundrware.com.bistromobile;

import android.app.Activity;
import android.content.Context;

import com.tapadoo.alerter.Alerter;

public class AlertMessage {

    public static void showNoInternetConnectionMessage(Activity activity) {
        Alerter.create(activity)
                .enableVibration(true)
                .setDuration(2000)
                .setText("Dispozitivul nu este conectat la internet!")
                .setTitle("Lipsă conexiune")
                .show();
    }

    public static void showInvalidServerConnectionDetailsMessage(Activity activity) {
        Alerter.create(activity)
                .enableVibration(true)
                .setDuration(2000)
                .setText("Detaliile introduse sunt incorecte! Incercați din nou")
                .setTitle("Conectare eșuată")
                .show();
    }

    public static void showInvalidWaiterPasswordMessage(Activity activity) {
        Alerter.create(activity)
                .enableVibration(true)
                .setDuration(2000)
                .setText("Parola introdusă nu corespunde niciunui ospătar")
                .setTitle("Autentificare eșuată")
                .show();
    }

    public static void showMessage(Activity activity, String title, String message) {
        Alerter.create(activity)
                .enableVibration(true)
                .setDuration(2000)
                .setText(message)
                .setTitle(title)
                .show();
    }
}
