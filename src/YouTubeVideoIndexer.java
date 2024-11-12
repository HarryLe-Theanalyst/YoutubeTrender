import java.util.HashMap;
import java.util.Map;

public class YouTubeVideoIndexer {

    private Map<String, YouTubeWordItem> wordMap;

    // Constructor
    public YouTubeVideoIndexer() {
        wordMap = new HashMap<>();
    }

    public void indexVideo(YouTubeVideo video) {
        // Split the title and description into words
        String[] titleWords = video.getTitle().split("\\s+");
        String[] descriptionWords = video.getDescription().split("\\s+");

        // Add each word from the title and description to the index
        for (String word : titleWords) {
            addWordToIndex(word.toLowerCase(), video);
        }
        for (String word : descriptionWords) {
            addWordToIndex(word.toLowerCase(), video);
        }
    }

    // Helper method to add a word to the index
    private void addWordToIndex(String word, YouTubeVideo video) {
        YouTubeWordItem wordItem = wordMap.get(word);
        if (wordItem == null) {
            wordItem = new YouTubeWordItem(word);
            wordMap.put(word, wordItem);
        } else {
            wordItem.incrementCount(); // Tăng số lần xuất hiện của từ
        }
        wordItem.addAssociatedVideo(video);
    }

    // Method to find a YouTubeWordItem by word
    public YouTubeWordItem findWord(String word) {
        return wordMap.get(word.toLowerCase()); // Return the wordItem, if found
    }

    // Method to get all indexed words
    public Map<String, YouTubeWordItem> getIndexedWords() {
        return wordMap;
    }
}

