package com.github.rookiewutongshu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsUtil {

    public static String getImageFromContent(String content) {
        try {
            Document doc = Jsoup.parse(content);
            Elements els = doc.select("p").select("img");
            StringBuilder string = new StringBuilder();
            for (Element e : els) {
                string = string.append(e.attr("src")).append(" || ");
                //为什么这么写呢，因为我发现有些新闻是多图流，而有些新闻又是完全无图的文字流,多张图片
                // url间用 || 隔开。也可不加
            }
            String s = string.toString();
            if (" || ".equals(s)) {
//                System.out.println("此条新闻无图片");
                return null;
            } else
                return s;

        } catch (Exception e) {
            System.out.println("获取图片url时出错");
            e.printStackTrace();
            return null;
        }


    }
}
