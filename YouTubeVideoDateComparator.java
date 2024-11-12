import java.util.Comparator;

public class YouTubeVideoDateComparator implements Comparator<YouTubeVideo> {
    @Override
    public int compare(YouTubeVideo o1, YouTubeVideo o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
