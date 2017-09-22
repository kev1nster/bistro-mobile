package thundrware.com.bistromobile;

import android.content.Context;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;

import thundrware.com.bistromobile.utils.StringUtils;

public class Message {

    public static void showError(Context context, String content) {
        new MaterialDialog.Builder(context)
                .title("Eroare")
                .content(content)
                .positiveText("OK")
                .show();
    }

    public static void showUnauthorizedAccess(Context context) {
        new MaterialDialog.Builder(context)
                .title("Acces restricționat")
                .content("Nu poți trimite date către server fără o cheie de autentificare. Verifică dacă ai setat una sau dacă cea adăugată este validă")
                .positiveText("OK")
                .show();
    }

    public static void showUnexpectedError(Context context, String errorMessage) {
        new MaterialDialog.Builder(context)
                .title("Eroare necunoscută")
                .content(errorMessage)
                .positiveText("OK")
                .show();
    }
}
