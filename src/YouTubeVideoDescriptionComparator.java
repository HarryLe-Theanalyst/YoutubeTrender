import java.util.Comparator;

public class YouTubeVideoDescriptionComparator implements Comparator<YouTubeVideo> {
    @Override
    public int compare(YouTubeVideo o1, YouTubeVideo o2) {
        return Integer.compare(o1.getDescription().length(), o2.getDescription().length());
    }
}