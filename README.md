
**闲来无聊想看看java爬虫怎么写的，这个东东基本都是参照CSDN用户@小小Tiny写的这篇敲的[https://blog.csdn.net/gx304419380/article/details/80619043 ]**

只有一点点小区别
- 原作者采用spring的注入方式传url值，我是直接传的

- 没写数据库存取的类（这里我推荐想要的人使用mybatis来写，用过都说好）

其他
- 使用方式，直接运行MainCraler类即可。若想看到打印信息区各自对应的puller类把注释掉打印的打印语句取消注释
- 在照着敲的时候发现凤凰网易新闻的一些标签名改了，这些网站也许还会改标签名，目前代码只保证能爬取到2018/8/24的新闻。当然了，
就算改了你自己也可以很轻易地自己对着改
- HttpUnit我好像导错包，没能把今日头条的爬虫写出来，新鲜劲也过了，就没继续写了
