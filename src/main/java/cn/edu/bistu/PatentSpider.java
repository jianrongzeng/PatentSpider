package cn.edu.bistu;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Description:
 * @Author: Zeng Jianrong
 * @Date: 2019/1/16
 **/
public class PatentSpider extends BreadthCrawler {

    public static String domain = "http://www1.soopat.com";

    public PatentSpider(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        if (page.meta("type").equals("list")) {
            Elements h2s = page.select("h2.PatentTypeBlock");
            for (Element h2 : h2s) {
                String url = "";
                String href = h2.select("a").first().attr("href");
                if (!href.contains("www")) {
                    url = domain + href;
                }
                crawlDatums.add(new CrawlDatum(url).meta("type", "detail"));
            }
        } else if (page.meta("type").equals("detail")) {
            // System.out.println(page.html());
            String title = page.select("span.detailtitle h1").text();
            System.out.println(title);
        }

    }

    public static void main(String[] args) throws Exception {
        PatentSpider patentSpider = new PatentSpider("crawler/PatentSpider", false);
        String seedUrl = "http://www1.soopat.com/Home/Result?SearchWord=%E6%9D%8E%E6%B6%93%E5%AD%90&FMZL=Y&SYXX=Y" +
                "&WGZL=Y&FMSQ=Y";
        patentSpider.addSeed(new CrawlDatum(seedUrl).meta("type", "list"));
        patentSpider.setThreads(10);
        patentSpider.start(3);

    }
}
