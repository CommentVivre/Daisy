import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class JobProcessor implements PageProcessor {

    //负责解析页面
    @Override
    public void process(Page page) {
        // 解析Page，返回的数据Page，并且把解析的结果放到
        // page.putField("divTop",page.getHtml().css("div.sidebar-box div").all());
        // XPath
        // page.putField("div",page.getHtml().xpath("//div[@id=main-left-cloumn]/div"));
        // CSS选择器
        // page.putField("divCSS",page.getHtml().css("div > a.item-top > strong").toString());
    }

    private Site site = Site.me();

    @Override
    public Site getSite() {
        return site;
    }
    //主函数
    public static void main(String[] args) {

        Spider.create(new JobProcessor())
                .addUrl("https://www.runoob.com/")  //设置要爬取数据的页面
                .run(); //执行爬虫

        /*
        Spider.create(new JobProcessor())
                .addUrl("https://www.runoob.com/")  //设置要爬取数据的页面
                .addPipeline(new ConsolePipeline())
                .addPipeline(new JsonFilePipeline("C:\\Users\\Unenfantseul\\Documents"))
                .thread(10)//设置线程数
                .run(); //执行爬虫
        */
    }

}