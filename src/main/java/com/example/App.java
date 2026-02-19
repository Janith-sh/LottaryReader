package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
    public static void main(String[] args) {

        try {
            String url = "https://www.dlb.lk/result/6/";

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Connection", "keep-alive")
                    .timeout(10000)
                    .get();

            System.out.println("Page Title: " + doc.title());
            System.out.println("================================");

            // Inspect page and adjust selector if needed
            Elements results = doc.select("div");

            for (Element element : results) {

                if (element.text().contains("Jayoda")) {
                    System.out.println("Lottery Info:");
                    System.out.println(element.text());
                    System.out.println("--------------------------------");
                }
            }

        } catch (Exception e) {
             System.out.println("Error occurred:");
            e.printStackTrace();
        }
    }
}
