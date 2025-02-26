package org.example;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

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

                writeCsvFile(url, priceElement.asNormalizedText());

                DiscordNotifier.sendToDiscord(title, price, url);

            }
            CsnToJsonConverter.converter();
        } catch (FailingHttpStatusCodeException | IOException e) {
                e.printStackTrace();
        }finally {
            webClient.close();
        }
    }


    public static WebClient createWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    public static void writeCsvFile(String link, String price) throws IOException {
        File file = new File("export.csv");
        boolean fileExists = file.exists();

        FileWriter recipesFile = new FileWriter(file, true);

        if(!fileExists){
            recipesFile.write("link, price\n");
        }

        recipesFile.write(link + ", " + price + "\n");

        recipesFile.close();
    }

    public static ArrayList<String> createUrlList(){
        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("https://streetlab.nu/product/carhartt-wip-industry-beanie-lumo-tellow/");
        urlList.add("https://streetlab.nu/product/eva-skateboards-eva-og-beanie-grey/");
        urlList.add("https://streetlab.nu/product/eva-skateboards-eva-beanie-red-pink/");
        return urlList;
    }

//    public static void csvParser(){
//        File input = new File("export.csv");
//        File output = new File("output.json");
//
//        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
//    }
}