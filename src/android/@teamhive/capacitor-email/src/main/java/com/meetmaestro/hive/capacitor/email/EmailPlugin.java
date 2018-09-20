package com.meetmaestro.hive.capacitor.email;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.Html;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONException;

import android.os.Build;

import java.util.List;

@NativePlugin(
        requestCodes = EmailPlugin.REQUEST_CODE
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
        String type = call.getString("type");
        String app = call.getString("app", "mailto");
        String chooserHeader = call.getString("Open with", "Open with");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
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
        } else {
            call.resolve();
        }

    }

    @PluginMethod()
    public void isAvailable(PluginCall call) {
        String app = call.getString("app", "");
        PackageManager pm = getActivity().getPackageManager();
        if (aliases.has(app)) {
            app = aliases.getString(app);
        }
        try {
            ApplicationInfo info = pm.getApplicationInfo(aliases.getString(app), 0);
            if (info.enabled) {
                call.success();
            }
        } catch (PackageManager.NameNotFoundException e) {
            call.success();
        }

    }

    @PluginMethod()
    public void openDraft(PluginCall call) {
        open(call);
    }

    @PluginMethod()
    public void requestPermission(PluginCall call) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.GET_ACCOUNTS"}, REQUEST_CODE);
    }

    @PluginMethod()
    public void hasPermission(PluginCall call) {
        boolean has = (ActivityCompat.checkSelfPermission(getContext(), "android.permission.GET_ACCOUNTS") == PackageManager.PERMISSION_GRANTED);
        JSObject object = new JSObject();
        object.put("value", has);
        call.resolve(object);
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
