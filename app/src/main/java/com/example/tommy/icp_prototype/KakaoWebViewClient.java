package com.example.tommy.icp_prototype;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KakaoWebViewClient extends WebViewClient {
    private Activity activity;

    public KakaoWebViewClient(Activity activity) {
        this.activity = activity;
    }
    // intent://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/7ff8447cfe4f08dfb2497bf8478d2e441295aade15e6031c39be882e3826e072/order/#Intent;scheme=kakaotalk;package=com.kakao.talk;end
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("url: ",url);
        if (url != null && url.startsWith("intent://")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent existPackage = view.getContext().getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    view.getContext().startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                    view.getContext().startActivity(marketIntent);
                }
                return true;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
