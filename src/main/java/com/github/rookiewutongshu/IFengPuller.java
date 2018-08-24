package com.github.rookiewutongshu;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

public class IFengPuller implements NewsPuller {
    private static final Logger logger = Logger.getGlobal();

    @Override
    public void pullNews(String url) {
        logger.info("开始拉取凤凰网新闻");
        Document doc = null;

        try {
            doc = getDocFromUrl(url, false);
        } catch (Exception e) {
            logger.warning("拉取凤凰网首页失败,失败url:" + url);
            e.printStackTrace();
        }

        Elements els = doc.select("div#headLineDefault").select("ul.FNewMTopLis")
                .select("li").select("a");

        HashSet<News> newsSet = new HashSet<News>();
        for (Element e : els) {
            News news = new News();
            String title = e.text();
            String url2 = e.attr("href");
            news.setTitle(title);
            news.setUrl(url2);
            news.setSource("凤凰");
            news.setCreateDate(new Date());
            newsSet.add(news);
        }

        newsSet.forEach(news -> {
            logger.info("逐个拉取新闻内容，url:" + news.getUrl());
            try {
                Document document = getDocFromUrl(news.getUrl(), false);
                Elements content = document.select("yc_con_txt");
                if (content.isEmpty()) {
                    content = document.select("div#main_content");
                }
                if (content.isEmpty())
                    System.out.println("此条新闻为纯图片新闻或为视屏新闻（由url可知），" +
                            "如若不想存入此类新闻可在此判断中remove后直接continue,本条可注释");
                ;
                news.setContent(content.text());
                //原文这里用的是toString，会保留一些html标记，我认为作者是为了下面方便获取图片链接，个人认为
                //用text内容干净一点好，可自由选择
                news.setImage(NewsUtil.getImageFromContent(content.toString()));
                //由于本人不想存入数据库（爬着玩），所以想了解新闻拉取详情可以取消下面的注释
//                System.out.println(news.toString());
                logger.info("新闻内容拉取成功");
            } catch (Exception e) {
                logger.warning("新闻内容拉取失败，url：" + news.getUrl());
                e.printStackTrace();
            }

        });

        logger.info("凤凰网新闻拉取完成");


    }
}
