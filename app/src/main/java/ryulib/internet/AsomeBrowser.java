package ryulib.internet;

import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ryulib.MessageControl;
import ryulib.listeners.OnMessageListener;
import ryulib.listeners.OnNotifyListener;
import ryulib.listeners.OnStringListener;

public class AsomeBrowser extends WebViewClient {

    private static final int COMMAND_SHOWKEYBOARD = 1;
    private static final int COMMAND_GOURL = 2;
    private static final int COMMAND_RUNCMD = 3;

    public AsomeBrowser(Context context, WebView web) {
        context_ = context;
        webView = web;

        webAppInterface = new WebAppInterface(context);

        webAppInterface.setOnShowKeyboard(new OnStringListener() {
            @Override
            public void onString(Object sender, String text) {
                Log.i("setOnShowKeyboard", text);
                messageControl.send(COMMAND_SHOWKEYBOARD, text);
            }
        });

        webAppInterface.setOnURL(new OnStringListener() {
            @Override
            public void onString(Object sender, String text) {
                Log.i("OnURL", text);
                messageControl.send(COMMAND_GOURL, text);
            }
        });

        webAppInterface.setOnCommand(new OnStringListener() {
            @Override
            public void onString(Object sender, String text) {
                Log.i("OnCommand", text);
                messageControl.send(COMMAND_RUNCMD, text);
            }
        });

        messageControl.setOnMessageListener(new OnMessageListener() {
            @Override
            public void onMessage(Message message) {
                Log.i("setOnMessageListener", Integer.toString(message.what));
                switch (message.what) {
                    case COMMAND_SHOWKEYBOARD:
                        if (onShowKeyboard_ != null) onShowKeyboard_.onNotify(this);
                        break;

                    case COMMAND_GOURL:
                        if (onURL_ != null) onURL_.onString(this, (String) message.obj);
                        break;

                    case COMMAND_RUNCMD:
                        if (onCommand_ != null) onCommand_.onString(this, (String) message.obj);
                        break;
                }
            }
        });

        webView.setWebViewClient(this);
        webView.addJavascriptInterface(webAppInterface, "App");
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);

            try {
                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(settings, "/data/data/" + context.getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(settings, 1024*1024*8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(settings, "/data/data/" + context.getPackageName() + "/cache/");
            }
            catch (NoSuchMethodException e) {
                Log.e("AsomeBrowser", "Reflection fail", e);
            }
            catch (InvocationTargetException e) {
                Log.e("AsomeBrowser", "Reflection fail", e);
            }
            catch (IllegalAccessException e) {
                Log.e("AsomeBrowser", "Reflection fail", e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPageFinished(WebView view, String url) {
        Log.i("onPageFinished", url);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

    public void setOnShowkeyboard(OnNotifyListener event) { onShowKeyboard_ = event; }
    public void setOnURL(OnStringListener event) { onURL_ = event; }
    public void setOnCommand(OnStringListener event) { onCommand_ = event; }

    private Context context_;
    private WebView webView;
    private String current_url_ = "";
    private WebAppInterface webAppInterface;
    private MessageControl messageControl = new MessageControl();

    private OnNotifyListener onShowKeyboard_ = null;
    private OnStringListener onURL_ = null;
    private OnStringListener onCommand_ = null;
}
