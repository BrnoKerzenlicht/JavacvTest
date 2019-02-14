import org.mp4parser.Container;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.mp4parser.muxer.tracks.AppendTrack;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 视频合并
 */
public class AppendVideo {
    public static void main(String[] args) throws IOException {

        String[] srcVideoPath = new String[] { "e:\\cutOutput-0.mp4", "e:\\cutOutput-1.mp4", "e:\\cutOutput-2.mp4" };
        String dstVideoPath = "e:\\";
        videoMerge(srcVideoPath, dstVideoPath);
    }

    public static void videoMerge(String[] srcVideoPath, String dstVideoPath) throws IOException {

        List<Movie> inMovies = new ArrayList<Movie>();
        for (String videoUri : srcVideoPath) {
            inMovies.add(MovieCreator.build(videoUri));
        }

        List<Track> videoTracks = new LinkedList<Track>();
        List<Track> audioTracks = new LinkedList<Track>();

        for (Movie m : inMovies) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }

        Movie result = new Movie();

        if (audioTracks.size() > 0) {
            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
        }
        if (videoTracks.size() > 0) {
            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
        }

        Container out = new DefaultMp4Builder().build(result);

        FileChannel fc = new RandomAccessFile(String.format(dstVideoPath + "mergeOutput.mp4"), "rw").getChannel();
        out.writeContainer(fc);
        fc.close();
    }
}
