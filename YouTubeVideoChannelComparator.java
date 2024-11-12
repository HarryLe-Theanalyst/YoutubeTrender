import java.util.Comparator;

public class YouTubeVideoChannelComparator implements Comparator<YouTubeVideo> {

    @Override
    public int compare(YouTubeVideo o1, YouTubeVideo o2) {
        return o1.getChannel().compareTo(o2.getChannel());
    }

}