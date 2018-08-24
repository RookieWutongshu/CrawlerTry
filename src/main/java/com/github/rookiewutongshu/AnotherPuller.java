package com.github.rookiewutongshu;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnotherPuller implements NewsPuller {
    private static final Logger logger = Logger.getGlobal();
    private static final String TOUTIAO_URL = "https://www.toutiao.com";


    @Override
    public void pullNews(String url) {
        logger.info("开始拉取今日头条热门新闻！");
        // 1.load html from url
        Document html = null;
        try {
            html = getDocFromUrl(url, true);
        } catch (Exception e) {
            logger.warning("获取今日头条主页失败！");
            e.printStackTrace();
            return;
        }

        // 2.parse the html to news information and load into POJO
        Map<String, News> newsMap = new HashMap<>();
        for (Element a : html.select("a[href~=/group/.*]:not(.comment)")) {
            logger.info("标签a: \n{}" + a);
            String href = TOUTIAO_URL + a.attr("href");

            String image = a.select("img").attr("src");

            News news = newsMap.get(href);
            if (news == null) {
                News n = new News();
                n.setSource("今日头条");
                n.setUrl(href);
                n.setCreateDate(new Date());


                newsMap.put(href, n);
            } else {
                if (a.hasClass("img-wrap")) {

                } else if (a.hasClass("title")) {

                }
            }
        }

        logger.info("今日头条新闻标题拉取完成!");
        logger.info("开始拉取新闻内容...");
        newsMap.values().parallelStream().forEach(news -> {
            logger.info("===================={}====================" + news.getTitle());
            Document contentHtml = null;
            try {
                contentHtml = getDocFromUrl(news.getUrl(), true);
            } catch (Exception e) {
                logger.warning("获取新闻内容失败！" + news.getTitle());
                return;
            }
            Elements scripts = contentHtml.getElementsByTag("script");
            scripts.forEach(script -> {
                String regex = "articleInfo: \\{\\s*[\\n\\r]*\\s*title: '.*',\\s*[\\n\\r]*\\s*content: '(.*)',";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(script.toString());
                if (matcher.find()) {
                    String content = matcher.group(1)
                            .replace("&lt;", "<")
                            .replace("&gt;", ">")
                            .replace("&quot;", "\"")
                            .replace("&#x3D;", "=");
                    logger.info("content: " + content);
                    news.setContent(content);
                }
                System.out.println(news.toString());
            });
        });

        logger.info("今日头条新闻内容拉取完成!");

    }
}
