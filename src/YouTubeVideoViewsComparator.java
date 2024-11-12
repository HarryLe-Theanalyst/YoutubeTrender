import java.util.Comparator;

public class YouTubeVideoViewsComparator implements Comparator<YouTubeVideo> {
    @Override
    public int compare(YouTubeVideo o1, YouTubeVideo o2) {
        return Integer.compare(o1.getViewCount(), o2.getViewCount());
    }
}