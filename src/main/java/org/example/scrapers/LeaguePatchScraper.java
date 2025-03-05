package org.example.scrapers;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.example.CsnToJsonConverter;
import org.example.DiscordNotifier;

import java.io.IOException;

import static org.example.Main.*;

public class LeaguePatchScraper {
    public static void leaguePatchNotesScraper(){
        WebClient webClient = createWebClient();
        String url = "https://www.leagueoflegends.com/en-us/news/tags/patch-notes/";
        CsnToJsonConverter converter = new CsnToJsonConverter();

        try{
            HtmlPage page = webClient.getPage(url);

            //Xpath's
            String urlXpath = "//*[@id=\"news\"]/div/div[2]/a[1]";
            HtmlElement urlElement = (HtmlElement) page.getByXPath(urlXpath).get(0);

            String patchXpath = "//*[@id=\"news\"]/div/div[2]/a[1]/span/div/div[2]/div[2]";
            HtmlElement patchElement = (HtmlElement) page.getByXPath(patchXpath).get(0);

            String dateXpath = "//*[@id=\"news\"]/div/div[2]/a[1]/span/div/div[2]/div[1]/div[3]/time";
            HtmlElement dateElement = (HtmlElement) page.getByXPath(dateXpath).get(0);

//            String imgXpath = "//a[contains(@href, 'sanity/images')]/@href";
//            HtmlElement imgElement = (HtmlElement) page.getByXPath(imgXpath).get(0);

            //variabler
            String patchUrl = "https://www.leagueoflegends.com" + urlElement.getAttribute("href");
            String patch = patchElement.getTextContent();
            String date = dateElement.getTextContent();
//            String img = imgElement.getAttribute("src");



            //souts
            System.out.println(patchElement.getTextContent());
            System.out.println(dateElement.getTextContent());
            System.out.println("https://www.leagueoflegends.com" + urlElement.getAttribute("href"));

            writeCsvFileLeaguePatch(patchUrl, patch, date);
            DiscordNotifier.leaguePatchNotifier(patch, date, patchUrl);


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
