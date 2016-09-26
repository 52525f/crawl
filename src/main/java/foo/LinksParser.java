package foo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinksParser {
    public List<URL> getLinks(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .followRedirects(true)
                    .get();
            return getLinks(doc);
        } catch (IOException e) {
            System.out.println("Unable to connect to url " + url + " - " + e.getMessage());
        }
        return new ArrayList<>();
    }

    List<URL> getLinks(Document doc) {
        List<URL> urls =  new ArrayList<>();
        try {
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String url = link.attr("abs:href");
                if (!url.isEmpty())
                    urls.add(new URL(url));
            }
        } catch (MalformedURLException e) {
            System.out.printf("Unable to parse link: " + e.getMessage());
        }
        return urls;
    }
}
