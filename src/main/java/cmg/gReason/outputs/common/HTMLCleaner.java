package cmg.gReason.outputs.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLCleaner {
    public String clean(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        Document doc = Jsoup.parse(html);
        return doc.text();  // Extracts clean text content
    }
}
