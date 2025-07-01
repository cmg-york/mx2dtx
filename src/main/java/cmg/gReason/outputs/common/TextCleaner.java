package cmg.gReason.outputs.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TextCleaner {
    public String clean(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        Document doc = Jsoup.parse(html);
        return doc.text();  // Extracts clean text content
    }
    
    
	public String toCamelCase(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		// Keep only letters and digits, treat others as word separators
		String[] parts = input.split("[^a-zA-Z0-9]+");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.isEmpty()) continue;

			if (i == 0) {
				// First word: lower case first letter
				sb.append(part.substring(0, 1).toLowerCase());
				sb.append(part.substring(1));
			} else {
				// Other words: capitalize first letter
				sb.append(part.substring(0, 1).toUpperCase());
				sb.append(part.substring(1));
			}
		}

		// Ensure first character is lowercase (required for Prolog atoms)
		if (sb.length() > 0) {
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	
    
}
