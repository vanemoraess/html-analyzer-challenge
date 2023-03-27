import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

class HtmlAnalyzer {
    public static void main(String[] args) throws Exception {
        if (args.length == 0 || !isValidURL(args[0])) {
            System.out.println("Usage: java HtmlAnalyser <url>");
            return;
        }

        List<String> html;
        try {
            html = readHtmlFromUrl(new URL(args[0]));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(findDeepestText(html));
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static List<String> readHtmlFromUrl(URL url) throws Exception {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception ignored) {
            throw new Exception("URL connection error");
        }

        var html = new ArrayList<String>();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            html.add(inputLine);
        in.close();

        return html;
    }

    private static String findDeepestText(List<String> html) {
        int maxDepth = -1;
        String deepestText = null;
        var tagStack = new Stack<String>();

        for (String line : html) {
           String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            if (trimmedLine.startsWith("</")) {
                tagStack.pop();
            } else if (trimmedLine.startsWith("<")) {
                tagStack.push(trimmedLine);
            } else {
                int depth = tagStack.size();
                if (depth > maxDepth) {
                    maxDepth = depth;
                    deepestText = trimmedLine;
                }
            }
        }

        if (tagStack.size() > 0) {
            return "malformed HTML";
        }

        return deepestText;
    }
}