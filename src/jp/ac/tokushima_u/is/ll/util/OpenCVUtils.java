package jp.ac.tokushima_u.is.ll.util;

import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2YCrCb;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class OpenCVUtils {

	public static double judgeBrightness(InputStream is) throws IOException {
		IplImage src = IplImage.createFrom(ImageIO.read(is));
		if (src == null) {
			throw new IOException("Couldn't load source image.");
		}
		ByteBuffer srcBuf = src.getByteBuffer();
		double bright = 0;
		int btmp = 0;
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		for (int y = 0; y < src.height(); y++) {
			for (int x = 0; x < src.width(); x++) {
				p1 = srcBuf.get(y * src.widthStep() + src.nChannels() * x) & 0xFF;
				p2 = srcBuf.get(y * src.widthStep() + src.nChannels() * x + 1) & 0xFF;
				p3 = srcBuf.get(y * src.widthStep() + src.nChannels() * x + 2) & 0xFF;
				btmp += (0.144 * p1 + 0.587 * p2 + 0.299 * p3);// BGR
			}
		}
		bright = (double)btmp / (src.height() * src.width());
		bright = bright * 100/255;
		return bright;
	}
	
	public static double judgeDefocus(InputStream is) throws IOException {
		IplImage src = IplImage.createFrom(ImageIO.read(is));
		IplImage picone = cvCreateImage(cvGetSize(src), 8, 3);
		
		cvCvtColor(src, picone, CV_BGR2YCrCb);
		CvScalar gety;
		double z=0, zy1=0, zy2=0, total = 0;
		double gety1=0, gety2=0;
		double result = 0;
		for(int ix=0;ix<picone.height();ix++){
			gety1=0;
			gety2=0;
			zy1=0;
			zy2=0;
			
			for(int jy=0;jy<picone.width();jy++){
				gety=cvGet2D(picone, ix, jy);
				z=0.5*gety.val(0)-gety1+0.5*gety2+zy1-0.5*zy2;
				total += z;
				gety2=gety1;
				gety1=gety.val(0);
				zy2=zy1;
				zy1=z;
			}
		}
		cvReleaseImage(picone);
		result = Math.abs(total/(src.height()*src.width()));
		return result;
	}
	
	public static void main(String[] args){
		System.out.println(System.getProperty("java.io.tmpdir"));
		try {
			FileInputStream fis = new FileInputStream("D:/User/Documents/Vicon Revue Data/C3207BC8-61D2-7D22-8DD8-80EBA89ECC1E/00020942.JPG");
			System.out.println(OpenCVUtils.judgeDefocus(fis));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
