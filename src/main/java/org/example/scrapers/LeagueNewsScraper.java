package org.example.scrapers;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.example.CsnToJsonConverter;
import org.example.DiscordNotifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.example.Main.createWebClient;
import static org.example.Main.writeCsvFileLeagueNews;

public class LeagueNewsScraper {
    static LocalTime time = LocalTime.now();
    public static void leagueNewsScraper() {
        WebClient webClient = createWebClient();
        String url = "https://www.leagueoflegends.com/en-us/news/";
        CsnToJsonConverter converter = new CsnToJsonConverter();

        try{
            HtmlPage page = webClient.getPage(url);

            String urlXpath = "//*[@id=\"news\"]/div/div[2]/a[1]";
            HtmlElement urlElement = (HtmlElement) page.getByXPath(urlXpath).get(0);

            String dateXpath = "(//time)[1]";
            HtmlElement dateElement = (HtmlElement) page.getByXPath(dateXpath).get(0);

            String titleXpath = "//*[@id=\"news\"]/div/div[2]/a[1]/span/div/div[2]/div[2]";
            HtmlElement titleElement = (HtmlElement) page.getByXPath(titleXpath).get(0);

            //Variabler
            String baseUrl = "https://www.leagueoflegends.com";
            String hrefUrl = urlElement.getAttribute("href");
            String newsUrl = "";
            if(!hrefUrl.startsWith("http")){
                newsUrl = baseUrl + hrefUrl;
            }else
                newsUrl = hrefUrl;


            String date = dateElement.getTextContent();
            String title = titleElement.getTextContent();

            String lastNews = LeaguePatchScraper.getLastPatchFromCSV("leagueNews.csv");


            if (!newsUrl.equals(lastNews)) {
                time = LocalTime.now();
                System.out.println("[" + time + "]" + " - New news found!");
                System.out.println("[" + time + "]" + " - Sending to discord...");
                writeCsvFileLeagueNews(title, date, newsUrl);
                DiscordNotifier.leaguePatchNotifier(title, date, newsUrl);
            }else
                time = LocalTime.now();
                System.out.println("[" + time + "]" + " - No news found.");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void monitor(){
        Random rand = new Random();
        int delay = rand.nextInt(3) + 3;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            try{
                time = LocalTime.now();
                System.out.println("[" + time + "]" + " - Checking for news...");
                leagueNewsScraper();
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Something went wrong!");
            }
        }, 0, delay, TimeUnit.MINUTES);
    }
}
