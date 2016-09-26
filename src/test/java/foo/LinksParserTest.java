package foo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LinksParserTest {
    @Test
    public void getsEmptyListOfUrlsForBlankPage() throws Exception {
        LinksParser parser = new LinksParser();
        List<URL> links = parser.getLinks(new Document(""));
        assertTrue(links.isEmpty());
    }

    @Test
    public void parsesLinkOnPage() throws Exception {
        String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
        LinksParser parser = new LinksParser();
        List<URL> links = parser.getLinks(Jsoup.parseBodyFragment(html));
        assertEquals(links.size(), 1);
        assertEquals(links.get(0), new URL("http://example.com/"));
    }

    @Test
    public void parsesMultipleRelativeLinks() throws Exception {
        String html = "<p>An <a href='/foo'><b>example</b></a> link.</p><a href='/bar'>foo</a>";
        LinksParser parser = new LinksParser();
        Document doc = Jsoup.parseBodyFragment(html, "http://example.com");
        List<URL> links = parser.getLinks(doc);
        assertEquals(links.size(), 2);
        assertEquals(links.get(0), new URL("http://example.com/foo"));
        assertEquals(links.get(1), new URL("http://example.com/bar"));
    }

    @Test
    public void ignoresBadLinks() throws Exception {
        String html = "<p>An <a href='foo://foo://foo'><b>example</b></a> link.</p><a href='/bar'>foo</a>";
        LinksParser parser = new LinksParser();
        Document doc = Jsoup.parseBodyFragment(html, "http://example.com");
        List<URL> links = parser.getLinks(doc);
        assertEquals(links.size(), 1);
    }
}
