package foo;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class App
{
    public static void main( String[] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String startPage = scanner.next();
        Spider spider = new Spider(new LinksParser());
        Map<URL, Integer> links = spider.crawl(startPage);
        links.entrySet()
                .stream()
                .sorted((a,b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));
        System.out.println("\nPages: " + links.size());
    }
}
