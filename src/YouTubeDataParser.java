import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class YouTubeDataParser {

    public List<YouTubeVideo> parse(String fileName) throws YouTubeDataParserException {
        List<YouTubeVideo> list = new ArrayList<>();
        JsonReader jsonReader;

        try {
            // Read data
            jsonReader = Json.createReader(new FileInputStream(fileName));
        } catch (FileNotFoundException fnfe) {
            throw new YouTubeDataParserException("File not found: " + fileName);
        }

        JsonObject jobj;
        try {
            jobj = jsonReader.readObject();
        } catch (JsonParsingException jpe) {
            throw new YouTubeDataParserException("Parsing exception: " + jpe.getMessage());
        }

        // Read the values of the item field
        JsonArray items = jobj.getJsonArray("items");
        if (items == null) {
            throw new YouTubeDataParserException("No 'items' field found in the JSON.");
        }

        for (JsonObject video : items.getValuesAs(JsonObject.class)) {
            JsonObject snippet = video.getJsonObject("snippet");
            JsonObject statistics = video.getJsonObject("statistics");

            // Ensure all required fields are available and parse them
            if (snippet == null || statistics == null) continue;

            String channelId = snippet.getString("channelId", "Unknown Channel");
            String channelTitle = snippet.getString("channelTitle", "Unknown Channel Title");
            String publishAt = snippet.getString("publishedAt", "Unknown Date");
            String title = snippet.getString("title", "Untitled");
            String description = snippet.getString("description", "No Description");

            int viewCount = statistics.containsKey("viewCount")
                    ? Integer.parseInt(statistics.getString("viewCount", "0"))
                    : 0;

            YouTubeVideo ytv = new YouTubeVideo(channelTitle, publishAt, title, description, viewCount, channelId);
            list.add(ytv);
        }
        return list;
    }
}

