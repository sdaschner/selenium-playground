package com.sebastian_daschner.selenium_playground;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;

public class BlogEntryCrawler {

    public static void main(String[] args) {
        Configuration.timeout = 20_000;
        Configuration.browser = "firefox";

        System.out.println("Title;URI;Date;Tags");
        List<URI> uris = crawlLinks();
        uris.stream()
                .limit(2)
                .map(BlogEntryCrawler::crawlEntryPage)
                .filter(Objects::nonNull)
                .forEach(e -> System.out.println(e.title() + ";" + e.uri() + ";" + e.date() + ";" + String.join(":", e.tags())));

    }

    private static Entry crawlEntryPage(URI uri) {
        try {
            open(uri.toString());

            String title = $("main > h2").getText().trim();
            String dateString = $("main > span.date").getText().trim();
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("EEEE, MMMM dd, yyyy")
                    .toFormatter().withLocale(Locale.ENGLISH);
            LocalDate date = formatter.parse(dateString, LocalDate::from);

            Set<String> tags = $$("main > span.tags a").stream()
                    .map(el -> el.getText().trim().replace("#", ""))
                    .collect(Collectors.toSet());

            return new Entry(title, uri, date, tags);
        } catch (Exception e) {
            e.printStackTrace();
            // continue with next page;
            System.err.println("Could not parse page " + uri);
            return null;
        }
    }

    private static List<URI> crawlLinks() {
        // TODO change to your site :)
        open("http://blog.sebastian-daschner.com/");

        SelenideElement link = $$("main a").find(exactText("All blog entries"));
        link.click();

        return $$("main > p a").stream()
                .map(el -> URI.create(el.getAttribute("href")))
                .collect(Collectors.toList());
    }

    record Entry(String title, URI uri, LocalDate date, Set<String> tags) {
    }

}
