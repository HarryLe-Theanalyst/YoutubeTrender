import java.util.ArrayList;
import java.util.List;

public class YouTubeWordItem {

    private String word;
    private List<YouTubeVideo> associatedVideos;
    private int count; // Count of occurrences of the word

    // Constructor
    public YouTubeWordItem(String word) {
        this.word = word;
        this.count = 1;
        this.associatedVideos = new ArrayList<>();

    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }

    public void addAssociatedVideo(YouTubeVideo video) {
        this.associatedVideos.add(video);
    }

    public List<YouTubeVideo> getAssociatedVideos() {
        return associatedVideos;
    }
}
