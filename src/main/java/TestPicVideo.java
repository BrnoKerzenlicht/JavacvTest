import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avfilter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 * 视频转图片
 * 图片转视频
 */
public class TestPicVideo {

    public static void main(String[] args) throws Exception {
        System.out.println(avfilter.avfilter_version());
        System.out.println("start...");
        // 原视频名称
        String srcVideo = "E:\\test\\jpgs\\output\\Wildlife.avi";
        // 保存的视频名称
        String saveMp4name = "E:\\test\\jpgs\\output\\1.mp4";
        // 图片集合的目录
        String imagesPath = "E:\\test\\jpgs\\output\\png\\";
        videoToImage(srcVideo, imagesPath);
        imageToVideo(saveMp4name, imagesPath);
        System.out.println("end...");
    }

    private static void videoToImage(String filename, String imagesPath) throws Exception {
        System.out.println("------------- video to image start ----------------");
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(filename);
        grabber.start();
        int frameRate = (int) grabber.getFrameRate();
        Java2DFrameConverter converter = new Java2DFrameConverter();
        for (int i = 0; i < grabber.getLengthInFrames(); i++) {
            BufferedImage bi = converter.getBufferedImage(grabber.grabImage());
            if (i % frameRate == 0) {
                // 每个frameRate帧进行一次抓取
                ImageIO.write(bi, "png", new File(imagesPath + i + ".png"));
            }
        }
        grabber.stop();
        System.out.println("------------- video to image end ----------------");
    }

    public static void imageToVideo(String saveMp4name, String imagesPath) throws Exception {
        System.out.println("------------- image to video start ----------------");
        File dir = new File(imagesPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println(imagesPath + " 不存在或者不是目录");
            return;
        }
        BufferedImage image = ImageIO.read(Objects.requireNonNull(dir.listFiles())[0]);
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(saveMp4name, image.getWidth(), image.getHeight());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
        // mp4,flv,mov,mp4,m4a,3gp,3g2,mj2,h264,ogg,MPEG4
        recorder.setFormat("mp4");
        // 此处说明每一秒多少帧，即说明1秒会录多少张照片
        recorder.setFrameRate(10);
        //8000kb/s 这个说明视频每秒大小，值越大图片转过来的压缩率就越小质量就会越高
        recorder.setVideoBitrate(Integer.MAX_VALUE);
        recorder.start();
        //
        Java2DFrameConverter converter = new Java2DFrameConverter();
        // 列出目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
        // 循环所有图片
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(File::isFile)
                .sorted(Comparator.comparing(TestPicVideo::getFileNum, Integer::compareTo)).forEach(file -> {
                    if (file.isDirectory()) {
                        System.out.println("跳过目录：" + file.getName());
                    }
                    else {
                        BufferedImage imageTemp;
                        try {
                            imageTemp = ImageIO.read(file);
                            System.out.println("读入文件：" + file.getName());
                            recorder.record(converter.convert(imageTemp));
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        recorder.stop();
        System.out.println("------------- image to video end ----------------");
    }

    private static Integer getFileNum(File file) {
        String numStr = file.getName().substring(0, file.getName().indexOf('.'));
        return Integer.valueOf(numStr);
    }
}
