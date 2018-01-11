package com.promelle.utils;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

/**
 * This class is intended for providing s3 related functions.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class ImageUtils {

	private static Dimension getScaledDimension(double origWidth,
			double origHeight, double width, double height) {
		double origRatio = origWidth / origHeight;
		double ratio = width / height;
		if (ratio < origRatio) {
			height = width / origRatio;
		} else {
			width = height * origRatio;
		}
		return new Dimension((int) width, (int) height);
	}

	private static BufferedImage resizeImage(BufferedImage originalImage,
			int type, Dimension dimension) {
		BufferedImage resizedImage = new BufferedImage(
				(int) dimension.getWidth(), (int) dimension.getHeight(), type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, (int) dimension.getWidth(),
				(int) dimension.getHeight(), null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}

	public static File scaleImage(String src, int width, int height) {
		try {
			FileInputStream fis = new FileInputStream(new File(src));
			InputStream is = new BufferedInputStream(fis);
			BufferedImage image = ImageIO.read(is);
			Dimension scaledDimension = getScaledDimension(image.getWidth(),
					image.getHeight(), width, height);
			BufferedImage resizeImageJpg = resizeImage(image,
					BufferedImage.TYPE_INT_RGB, scaledDimension);
			String extension = src.substring(src.lastIndexOf(".") + 1)
					.toLowerCase();
			File tempFile = File.createTempFile(UUID.randomUUID().toString(),
					extension);
			ImageIO.write(resizeImageJpg, extension, tempFile);
			return tempFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
