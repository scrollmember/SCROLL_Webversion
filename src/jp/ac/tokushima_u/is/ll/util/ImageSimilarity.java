package jp.ac.tokushima_u.is.ll.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 
 * @author maou
 */
public class ImageSimilarity {

	static final int maxPix = 10000;
	
	public ImageSimilarity () {
	}
	
	
	/**
	 * resize
	 * 指定サイズにリサイズ
	 * 
	 * @param baseImage
	 * @param width
	 * @param height
	 * @return
	 */
	private BufferedImage resize(BufferedImage baseImage, int width, int height) {
		BufferedImage newImage = new BufferedImage(
				width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		// resize
		newImage.getGraphics().drawImage(
                baseImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 
                  0, 0, width, height, null);

		return newImage;
	}

	/**
	 * resize
	 * 比率を掛け合わしてリサイズ
	 * 
	 * @param baseImage
	 * @param ratio
	 * @return
	 */
	@SuppressWarnings("unused")
	private BufferedImage resize(BufferedImage baseImage, double ratio) {
		BufferedImage newImage = new BufferedImage(
				(int)(baseImage.getWidth()*ratio),
				(int)(baseImage.getHeight()*ratio), BufferedImage.TYPE_3BYTE_BGR);
		
		AffineTransformOp ato = null;
		ato = new AffineTransformOp(
                   AffineTransform.getScaleInstance(ratio, ratio), 
				                                    null);
		ato.filter(baseImage, newImage);
		
		return newImage;
	}
	
	/**
	 * getHistogram
	 * 画像からヒストグラムを返す
	 * 
	 * @param baseImage
	 * @return int[] 64色のヒストグラム
	 */
	public int[] getHistogram(BufferedImage baseImage) {
		int[] histogram = new int[64];
	    for (int i = 0; i < 64; i++) {
	        histogram[i] = 0;
	    }
		
		for (int i=0; i<baseImage.getWidth(); i++) {
			for (int j=0; j<baseImage.getHeight(); j++) {
				Color pixel = new Color(baseImage.getRGB(i, j));
				int r = pixel.getRed()/64;
				int g = pixel.getGreen()/64;
				int b = pixel.getBlue()/64;
				histogram[r*16 + g*4 + b]++;
			}
		}
		return histogram;
	}

	/**
	 * getHistogram
	 * 画像を読み込んでヒストグラムを返す
	 * 
	 * @return int[] 64色のヒストグラム
	 */
	public int[] getHistogram(String path) {
		if (path.equals("")) {
			return null;
		}
		
		// 画像データの読み込み
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new URL(path));
		} catch (Exception e) {
			e.printStackTrace();
			bi = null;
			return null;
		}

		// maxPix収まる縦横を算出
		double ratio = Math.sqrt(maxPix / (double)(bi.getWidth() * bi.getHeight()));
		int width  = (int)(bi.getWidth() * ratio);
		int height = (int)(bi.getHeight() * ratio);
		
		// リサイズ
		BufferedImage oimg = resize(bi, width, height);

		// ヒストグラムの配列取得
		return getHistogram(oimg);
	}
	/**
	 * getCorrelBaseData
	 * 相関係数で使うデータ一覧
	 * [0-63] Xi - (平均)
	 * [64] ↑の二乗の合計の平方根
	 * 
	 * @param hist
	 * @return
	 */
	public double[] getCorrelBaseData(String url) {
		return getCorrelBaseData(getHistogram(url));
	}

	/**
	 * getCorrelBaseData
	 * 相関係数で使うデータ一覧
	 * [0-63] Xi - (平均)
	 * [64] ↑の二乗の合計の平方根
	 * 
	 * @param hist
	 * @return
	 */
	public double[] getCorrelBaseData(int[] hist) {
		if (hist == null || hist.length != 64) {
			return null;
		}
		
		double[] correl = new double[65];
		
		// 平均を算出
		double total = 0.;
		for (int i=0; i<64; i++) {
			total += (double)hist[i];
		}
		double avg = total / 64.;
		
		// データ列作成
		total = 0.;
		for (int i=0; i<64; i++) {
			correl[i] = (double)hist[i] - avg;
			total += Math.pow(correl[i], 2.);
		}
		correl[64] = Math.sqrt(total);
		
		return correl;
	}
	
	
	
	/**
	 * getHistogramIntersection
	 * ヒストグラムインターセクションの結果を返す
	 * 
	 * @param hist1
	 * @param hist2
	 * @return
	 */
	public Double getHistogramIntersection(int[] hist1, int[] hist2) {
		
		int min_sgm = 0;
		int sgm = 0;
	    for (int i = 0; i < 64; i++) {
	    	min_sgm += Math.min(hist1[i], hist2[i]);
	    	sgm += hist1[i];
	    }
	    return (min_sgm/(double)sgm);

	}
}
