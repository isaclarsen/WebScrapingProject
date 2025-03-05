package org.example.scrapers;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.example.CsnToJsonConverter;
import org.example.DiscordNotifier;

import java.io.IOException;
import java.util.ArrayList;

import static org.example.Main.*;

public class StreetLabScraper {
    public static void streetLabScraper(){
        WebClient webClient = createWebClient();
        ArrayList<String> urls = createUrlList();
        CsnToJsonConverter converter = new CsnToJsonConverter();

        try{
            for (String url : urls) {
                HtmlPage page = webClient.getPage(url);

                System.out.println(page.getTitleText());

                String title = page.getTitleText();

                String xpath = "//p[contains(@class, 'price')]//bdi";

                HtmlElement priceElement = (HtmlElement) page.getByXPath(xpath).get(0);

                System.out.println(priceElement.asNormalizedText());

                String price = priceElement.asNormalizedText();

                writeCsvFileStreetLab(url, priceElement.asNormalizedText());

                DiscordNotifier.sendToDiscord(title, price, url);

            }
            CsnToJsonConverter.converter();
        } catch (FailingHttpStatusCodeException | IOException e) {
            e.printStackTrace();
        }finally {
            webClient.close();
        }
    }
}
