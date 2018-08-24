package com.github.rookiewutongshu;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

public class WangYiNewsPuller implements NewsPuller {
    private static final Logger logger = Logger.getGlobal();

    @Override
    public void pullNews(String url) {
        logger.info("开始拉取网易要闻");
        Document doc = null;
        try {
            doc = getDocFromUrl(url, false);
        } catch (Exception e) {
            logger.warning("拉取网易首页失败");
            e.printStackTrace();
            return;
        }

        Elements els = doc
                .select("div.yaowen_news").select("div.news_default_yw")
                .select("li").select("a[href~=^http://news.163.com.*]");
        //原本我是想直接最后一个select“a”的，直到我发现原来网易的页面里参杂有它自己的广告链接才知道原作者有先见之明

        HashSet<News> hashSet = new HashSet<News>();

        for (Element e : els) {
            News news = new News();
            news.setSource("网易");
            news.setCreateDate(new Date());
            news.setUrl(e.attr("href"));
            hashSet.add(news);
        }

        logger.info("开始拉取网易新闻内容");

        hashSet.forEach(news -> {
            try {
                Document document = getDocFromUrl(news.getUrl(), false);
                Elements elements = document.select("div#endText");
                Element title = document.select("div#epContentLeft").select("h1").first();
                news.setContent(elements.text());
                //原文这里用的是toString，会保留一些html标记，我认为作者是为了下面方便获取图片链接，个人认为
                //用text内容干净一点好，可自由选择
                news.setImage(NewsUtil.getImageFromContent(elements.toString()));
                news.setTitle(title.text());
                //由于本人不想存入数据库（爬着玩），所以想了解新闻拉取详情可以取消下面的注释
//                System.out.println("image地址"+news.getImage());
                logger.info("新闻内容抽取成功");

            } catch (Exception e) {
                logger.warning("拉取新闻内容失败，url：" + news.getUrl());
                e.printStackTrace();
            }

        });
        logger.info("网易新闻拉取结束");

    }
}
