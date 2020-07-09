package lyrics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class DuckDuckGoSearch {
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";

    public static List<SearchResult> getSearchResults(String query){
        Document doc = null;
        ArrayList<SearchResult> searches = new ArrayList<>();
        try {
            doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).get();
            Elements results = doc.getElementById("links").getElementsByClass("results_links");

            for(Element result: results){

                Element title = result.getElementsByClass("links_main").first().getElementsByTag("a").first();
                searches.add(new SearchResult(title.attr("href"), title.text(), result.getElementsByClass("result__snippet").first().text()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searches;
    }
}
