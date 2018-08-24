package com.github.rookiewutongshu;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SohuNewsPuller implements NewsPuller {
    private static final Logger logger = Logger.getGlobal();


    @Override
    public void pullNews(String url) {
//        logger.setLevel(Level.WARNING);
        logger.info("开始拉取搜狐网的信息");
        Document doc = null;

        try {
            doc = getDocFromUrl(url, false);
        } catch (Exception e) {
            logger.warning("==================抓取搜狐网失败============｛url｝" + url);
            e.printStackTrace();
            return;
        }

        Elements elements = doc.select("div.focus-news").select("div.list16").
                select("li")
                .select("a");

        HashSet<News> newsSet = new HashSet<News>();

        for (Element e : elements) {
            News news = new News();
            String title = e.attr("title");
            String href = e.attr("href");
            news.setCreateDate(new Date());
            news.setTitle(title);
            news.setUrl(href);
            news.setSource("搜狐");
            newsSet.add(news);
            System.out.println("第一次测试" + news.getTitle());
        }
        System.out.println("shuliang 27?:" + newsSet.size());
        newsSet.forEach(news -> {
            logger.info("=======二次跳转新闻详情页======｛标题与链接｝" + news.getTitle() +
                    news.getUrl());
            try {
                Document doc2 = getDocFromUrl(news.getUrl(), false);
                Element els = doc2.select("div.wrapper-box").select("div.text").first().
                        select("article.article").first();
                news.setContent(els.text());
                //原文这里用的是toString，会保留一些html标记，我认为作者是为了下面方便获取图片链接，个人认为
                //用text内容干净一点好，可自由选择
                news.setImage(NewsUtil.getImageFromContent(els.toString()));
                //由于本人不想存入数据库（爬着玩），所以想了解新闻拉取详情可以取消下面的注释
//                System.out.println(news.toString());
                logger.info("新闻内容抽取成功");


            } catch (Exception e) {
                //TODO 后续根据调试是所遇到的详细异常类型分别写报错信息
                logger.warning("===========二次跳转新闻详情页或此条新闻是图集出错====" +
                        "==========｛url｝"
                        + news.getUrl());
                e.printStackTrace();

            }
        });

        logger.info("搜狐网新闻拉取结束");
    }
}
