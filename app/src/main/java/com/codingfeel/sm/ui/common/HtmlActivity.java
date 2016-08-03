package com.codingfeel.sm.ui.common;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codingfeel.sm.R;
import com.codingfeel.sm.common.ContentKey;
import com.codingfeel.sm.enums.HtmlFrom;
import com.codingfeel.sm.ui.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Heboot on 16/7/24.
 */
public class HtmlActivity extends ToolBarActivity {

    @BindView(R.id.wv_html)
    WebView wvHtml;
    @BindView(R.id.indeterminate_horizontal_progress_toolbar)
    MaterialProgressBar indeterminateHorizontalProgressToolbar;


    private String htmlUrl;

    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        ButterKnife.bind(this);
        indeterminateHorizontalProgressToolbar.setVisibility(View.VISIBLE);
        from = getIntent().getExtras().getString(ContentKey.HTML_FROM);

        if (from.equals(HtmlFrom.INFO.toString())) {
            showToolBar("载入中", true, null);
        } else {
            showToolBar(getResources().getString(R.string.page_title_html), true, null);
        }


        initWebView();
        loadWebView();
        initData();
    }

    private void initData() {
        htmlUrl = getIntent().getExtras().getString(ContentKey.HTML_URL);
        wvHtml.loadUrl(htmlUrl);
    }


    private void initWebView() {
        wvHtml.getSettings().setJavaScriptEnabled(true);
        wvHtml.getSettings().setDefaultTextEncodingName("UTF-8");
        wvHtml.getSettings().setSupportZoom(false);
        wvHtml.getSettings().setDisplayZoomControls(false);
        // 缩放按钮
        wvHtml.getSettings().setBuiltInZoomControls(true);
        wvHtml.getSettings().setUseWideViewPort(true);

    }


    private void loadWebView() {
        wvHtml.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, final String t) {
                super.onReceivedTitle(view, t);
//                showToolBar(t, true, null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String barTitle = t;
                        if (barTitle.length() > 13) {
                            barTitle = barTitle.substring(0, 13) + "...";
                        }
                        setToolBarTitle(barTitle);

                    }
                });
            }
        });
        wvHtml.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    wvHtml.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                indeterminateHorizontalProgressToolbar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
