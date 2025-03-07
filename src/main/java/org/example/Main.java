package org.example;

import com.gargoylesoftware.htmlunit.*;
import org.example.scrapers.LeagueNewsScraper;
import org.example.scrapers.LeaguePatchScraper;
import org.example.scrapers.StreetLabScraper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Main {
    public static void main(String[] args) {
//        LeaguePatchScraper.patchMonitor();
//        LeaguePatchScraper.getLastPatchFromCSV("leaguePatch.csv");
//        LeagueNewsScraper.leagueNewsScraper();
        LeagueNewsScraper.monitor();

    }
    public static WebClient createWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }

    public static void writeCsvFileStreetLab(String link, String price) throws IOException {
        File file = new File("export.csv");
        boolean fileExists = file.exists();

        FileWriter recipesFile = new FileWriter(file, true);

        if(!fileExists){
            recipesFile.write("link, price\n");
        }

        recipesFile.write(link + ", " + price + "\n");

        recipesFile.close();
    }

    public static void writeCsvFileLeaguePatch(String url, String patch, String date) throws IOException {
        File file = new File("leaguePatch.csv");
        boolean fileExists = file.exists();

        FileWriter recipesFile = new FileWriter(file, true);

        if(!fileExists){
            recipesFile.write("patch, date, url\n");
        }

        recipesFile.write(patch + ", " + date + ", " + url + "\n");

        recipesFile.close();
    }

    public static void writeCsvFileLeagueNews(String url, String title, String date) throws IOException {
        File file = new File("leagueNews.csv");
        boolean fileExists = file.exists();

        FileWriter recipesFile = new FileWriter(file, true);

        if(!fileExists){
            recipesFile.write("date, title, url\n");
        }

        recipesFile.write(date + ", " + title + ", " + url + "\n");

        recipesFile.close();
    }

    public static ArrayList<String> createUrlList(){
        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("https://streetlab.nu/product/carhartt-wip-industry-beanie-lumo-tellow/");
        urlList.add("https://streetlab.nu/product/eva-skateboards-eva-og-beanie-grey/");
        urlList.add("https://streetlab.nu/product/eva-skateboards-eva-beanie-red-pink/");
        return urlList;
    }

}