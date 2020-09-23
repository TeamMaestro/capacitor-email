package com.meetmaestro.hive.capacitor.email;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;

import com.getcapacitor.*;
import com.google.android.gms.common.AccountPicker;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@NativePlugin(
        requestCodes = {EmailPlugin.REQUEST_CODE}
)
public class EmailPlugin extends Plugin {
    private JSObject aliases = new JSObject();
    private JSObject defaults = new JSObject();
    static final int REQUEST_CODE = 1869;

    @Override
    public void load() {
        super.load();
        aliases.put("gmail", "com.google.android.gm");
        aliases.put("outlook", "com.microsoft.office.outlook");
        aliases.put("yahoo", "com.yahoo.mobile.client.android.mail");
        JSArray emptyArray = new JSArray();
        defaults.put("to", emptyArray);
        defaults.put("cc", emptyArray);
        defaults.put("bcc", emptyArray);
        defaults.put("attachments", emptyArray);
        defaults.put("subject", "");
        defaults.put("isHtml", true);
        defaults.put("type", "message/rfc822");
        defaults.put("chooserHeader", "Open with");
    }

    private String[] getArray(JSArray array) {

        int size = array.length();
        String[] newList = new String[size];
        try {
            List<String> emails = array.toList();
            int count = 0;
            for (String email : emails) {
                newList[count] = email;
                count++;
            }
            return newList;
        } catch (JSONException e) {
            return newList;
        }
    }

    @SuppressWarnings("deprecation")
    @PluginMethod()
    public void open(PluginCall call) {
        JSArray emptyArray = new JSArray();
        JSArray to = call.getArray("to", emptyArray);
        JSArray cc = call.getArray("cc", emptyArray);
        JSArray bcc = call.getArray("bcc", emptyArray);
        JSArray attachments = call.getArray("attachments", emptyArray);
        String subject = call.getString("subject", "");
        String body = call.getString("body", "");
        boolean isHtml = call.getBoolean("isHtml", true);
        String type = call.getString("type", defaults.getString("type"));
        String app = call.getString("app", "mailto://");
        String chooserHeader = call.getString("Open with", "Open with");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
        ArrayList<Uri> uris = new ArrayList<Uri>();
        if (aliases.has(app)) {
            PackageManager pm = getActivity().getPackageManager();
            try {
                ApplicationInfo info = pm.getApplicationInfo(aliases.getString(app), 0);
                if (info.enabled) {
                    intent.setPackage(info.packageName);
                }
            } catch (PackageManager.NameNotFoundException ignored) {

            }
        }

        try {
            List<String> list = attachments.toList();
            for (String attachment : list) {
                if (attachment.startsWith("_capacitor_")) {
                    attachment = attachment.replace("_capacitor_", "file://");
                }
                Uri uri = Uri.parse(attachment);
                if (uri != null && uri != Uri.EMPTY) uris.add(uri);
            }
        } catch (JSONException ignored) {

        }

        if (uris.size() > 1) {
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uris);
        } else if (uris.size() == 1) {
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        }

        intent.putExtra(Intent.EXTRA_EMAIL, getArray(to));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_BCC, getArray(bcc));
        intent.putExtra(Intent.EXTRA_CC, getArray(cc));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(Intent.EXTRA_TEXT, isHtml ? Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY).toString() : body);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, isHtml ? Html.fromHtml(body).toString() : body);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getActivity().startActivity(Intent.createChooser(intent, chooserHeader));
        call.success();

    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        PluginCall call = getSavedCall();
        if (requestCode == REQUEST_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    call.reject("");
                }
            }
            call.resolve();
        } else {
            call.resolve();
        }

    }

    @PluginMethod()
    public void isAvailable(PluginCall call) {
        String app = call.getString("alias", "");
        boolean has = checkPermission();
        JSObject object = new JSObject();
        PackageManager pm = getActivity().getPackageManager();
        if (aliases.has(app)) {
            app = aliases.getString(app);
        }
        object.put("hasAccount", false);

        if (has) {
            AccountManager am;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                am = AccountManager.get(getContext());
            } else {
                am = AccountManager.get(getContext());
            }

            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            for (Account account : am.getAccounts()) {
                if (emailPattern.matcher(account.name).matches()) {
                    object.put("hasAccount", true);
                }
            }

        }

        try {
            ApplicationInfo info = pm.getApplicationInfo(app, 0);
            object.put("hasApp", true);
        } catch (PackageManager.NameNotFoundException e) {
            object.put("hasApp", false);
        } finally {
            call.success(object);
        }

    }

    @PluginMethod()
    public void openDraft(PluginCall call) {
        open(call);
    }

    private void requestAccountPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_CODE);
    }

    @PluginMethod()
    public void requestPermission(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    null, false, null, null, null, null);
            startActivityForResult(call, intent, REQUEST_CODE);
        } else {
            requestAccountPermission();
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AccountManager am = AccountManager.get(getContext());
            return am.getAccounts().length > 0;
        }
        return (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED);
    }

    @PluginMethod()
    public void hasPermission(PluginCall call) {
        if (checkPermission()) {
            call.resolve();
        }
        call.reject("");
    }

    @PluginMethod()
    public void getDefaults(PluginCall call) {
        JSObject object = new JSObject();
        object.put("value", defaults);
        call.resolve(object);
    }

    @PluginMethod()
    public void getAliases(PluginCall call) {
        call.success(aliases);
    }

}
