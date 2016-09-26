package foo;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.when;

public class SpiderTest {
    @Mock
    private LinksParser parser;

    private Spider spider;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        spider = new Spider(parser);
    }

    @Test
    public void handlesPageWithNoLinks() throws Exception {
        assertThat(spider.crawl("http://foo.com").entrySet(), hasSize(1));
    }

    @Test
    public void getsLinksOnPage() throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://www.foo.com/bar"));
        urls.add(new URL("http://www.foo.com/baz"));
        when(parser.getLinks(matches("http://www.foo.com"))).thenReturn(urls);
        assertThat(spider.crawl("http://www.foo.com").entrySet(), hasSize(3));
    }

    @Test
    public void includesChildPages() throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://www.foo.com/bar"));
        urls.add(new URL("http://www.foo.com/baz"));
        when(parser.getLinks(matches("http://www.foo.com"))).thenReturn(urls);
        urls = new ArrayList<>();
        urls.add(new URL("http://www.foo.com/fun"));
        when(parser.getLinks(matches("http://www.foo.com/baz"))).thenReturn(urls);
        urls = new ArrayList<>();
        urls.add(new URL("http://www.foo.com/fuss"));
        when(parser.getLinks(matches("http://www.foo.com/fun"))).thenReturn(urls);

        assertThat(spider.crawl("http://www.foo.com").entrySet(), hasSize(5));
    }

    @Test
    public void avoidsLoops() throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://www.foo.com/bar"));
        when(parser.getLinks(matches("http://www.foo.com"))).thenReturn(urls);
        urls = new ArrayList<>();
        urls.add(new URL("http://www.foo.com"));
        when(parser.getLinks(matches("http://www.foo.com/bar"))).thenReturn(urls);

        assertThat(spider.crawl("http://www.foo.com").entrySet(), hasSize(2));
    }

    @Test
    public void doesNotFollowLinksToOtherSites() throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://www.bar.com/"));
        when(parser.getLinks(matches("http://www.foo.com"))).thenReturn(urls);
        urls = new ArrayList<>();
        urls.add(new URL("http://www.bar.com/blah"));
        when(parser.getLinks(matches("http://www.bar.com/"))).thenReturn(urls);

        assertThat(spider.crawl("http://www.foo.com").entrySet(), hasSize(2));
    }
}
