package com.xdu.xhin.spider;

import com.xdu.xhin.Config;
import com.xdu.xhin.bean.XduNews;
import com.xdu.xhin.util.TextUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xhinliang on 15-10-4.
 * Spider For Xdu News
 */
public class XduNewsSpider {

    private static XduNewsSpider sXduNewsSpider;

    private XduNewsSpider() {
    }

    public static XduNewsSpider getInstance() {
        if (sXduNewsSpider == null)
            sXduNewsSpider = new XduNewsSpider();
        return sXduNewsSpider;
    }

    public interface SpiderCallback {
        void getElement(XduNews data) throws IOException;
    }

    public void findNewsList(String url, SpiderCallback spiderCallback) throws IOException {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                getList(url, spiderCallback);
            } catch (IOException e) {
                //捕获到IO异常,暂时没有解决办法
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    private void getList(String url, SpiderCallback spiderCallback) throws IOException {
        int id = 1;
        while (!TextUtil.getInstance().isEmpty(url)) {
            XduNews data;
            Connection conn = Jsoup.connect(url);
            url = null;
            Document doc = conn.timeout(Config.DEFAULT_TIMEOUT).get();
            Element result = doc.getElementsByTag(Config.NODE_BODY).first();
            Elements links = result.getElementsByTag(Config.NODE_A);
            for (Element link : links) {
                //匹配下一页
                if (link.text().startsWith(Config.VALUE_NEXT_PAGE)) {
                    if (!TextUtil.getInstance().isEmpty(Config.ATTR_HREF)) {
                        url = TextUtil.getInstance().getAbsoluteUrl(link.attr(Config.ATTR_HREF));
                        continue;
                    }
                }
                String source = link.attr(Config.ATTR_TITLE);
                if (!source.startsWith(Config.VALUE_NEXT_SOURCE))
                    continue;
                //来到这里说明已经是我们需要的内容
                String href = TextUtil.getInstance().getAbsoluteUrl(link.attr(Config.ATTR_HREF));
                String title = link.text();
                data = new XduNews(id++, href, title, source);
                spiderCallback.getElement(data);
                writeFile(data.getNewsHref(), data.getFileName());
            }
        }
    }

    public void writeFile(String url, String fileName) throws IOException {
        //如果不是文字新闻则不保存
        if (!url.contains(Config.VALUE_NEWS_URL))
            return;
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file.getName(), true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWriter);

        Connection conn = Jsoup.connect(url);
        Document doc = conn.timeout(Config.DEFAULT_TIMEOUT).get();

        Element pageBody = doc.getElementsByTag(Config.NODE_BODY).first();
        Element article = pageBody.getElementsByAttributeValueContaining(Config.ATTR_ID, Config.VALUE_NEXT_CONTENT).first();

        //写入标题
        bufferWritter.write(Config.PRINT_STARS);
        bufferWritter.write(Config.PRINT_SPACES + article.getElementsByTag(Config.NODE_HEAD).text());
        bufferWritter.write(TextUtil.getInstance().newLine());
        bufferWritter.write(Config.PRINT_STARS);

        //写入正文,每行分段
        Elements paragraphs = article.getElementsByTag(Config.NODE_PARAGRAPH);
        for (Element paragraph : paragraphs) {
            bufferWritter.write(Config.PRINT_NEWLINE_SPACES + paragraph.text());
            bufferWritter.write(TextUtil.getInstance().newLine());
        }
        bufferWritter.write(TextUtil.getInstance().newLine());
        bufferWritter.close();
    }
}

