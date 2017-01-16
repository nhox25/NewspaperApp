package nhoxs25.yenvo.newspaperapp.appmenu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import nhoxs25.yenvo.newspaperapp.R;

/**
 * Created by yenvo on 16/01/2017.
 */

public class DisplayBaiBao extends AppCompatActivity {
    private WebView webView;
    private String link;
    private String title;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bai_bao);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webView);

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");
        title = bundle.getString("title");
        setTitle(title);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new NeroWebView());

        dialog = ProgressDialog.show(this, "", "Loading...");
        webView.loadUrl(link);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.docbao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item1) {
            Toast.makeText(this, "Design By Nhoxs25", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    class NeroWebView extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (dialog != null) {
                dialog.dismiss();
            }
            super.onPageFinished(view, url);

        }
    }
}
