package foo;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Spider {
    private LinksParser parser;

    public Spider(LinksParser linksParser) {
        this.parser = linksParser;
    }

    public Map<URL, Integer> crawl(String startPage) throws IOException {
        Map<URL, Integer> linkMap = new HashMap<>();
        URL start = new URL(startPage);
        linkMap.put(start, 0);
        Queue<URL> frontier = new LinkedList<>();
        frontier.add(start);

        while (!frontier.isEmpty()) {
            URL head = frontier.remove();
            List<URL> links = parser.getLinks(head.toString());
            for (URL url : links) {
                if (!linkMap.containsKey(url)) {
                    linkMap.put(url, 1);
                    if (url.getHost().equals(start.getHost()))
                        frontier.add(url);
                } else {
                    linkMap.put(url, linkMap.get(url) + 1);
                }
            }
        }
        return linkMap;
    }
}
