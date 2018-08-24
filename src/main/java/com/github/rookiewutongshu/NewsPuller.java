package com.github.rookiewutongshu;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.ws.WebServiceClient;
import java.io.IOException;

public interface NewsPuller {
    void pullNews(String url);

    default Document getDocFromUrl(String url, boolean useHtmlUnit) throws Exception {
        if (!useHtmlUnit) {
            return Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").get();
        } else {


            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setActiveXNative(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setTimeout(10000);

            HtmlPage page = null;
            try {
                page = webClient.getPage(url);
                webClient.waitForBackgroundJavaScript(10000);
                String htmlString = page.asXml();
                return Jsoup.parse(htmlString);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                webClient.closeAllWindows();
            }

        }

    }
}
