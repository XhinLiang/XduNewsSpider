package com.xdu.xhin;


import com.xdu.xhin.spider.XduNewsSpider;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String firstUrl = "http://news.xidian.edu.cn/list-1-1.html";
        try {
            XduNewsSpider.getInstance().findNewsList(firstUrl, data -> {
                System.out.println(data.toString());
                System.out.println(Config.PRINT_STARS);
            });
        } catch (IOException e) {
            //捕获到IO异常,暂时没有解决办法
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

}
