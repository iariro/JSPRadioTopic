package kumagai.radiotopic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import kumagai.radiotopic.struts2.TrimImageException;

/**
 * 画像の動画部分の切り出し処理
 */
public class ImageTrimming
{
	/**
	 * 動画部分を切り出してファイル保存。
	 * @param sourceFile 対象ファイル
	 * @param destFile 保存ファイル
	 * @param contentType 画像フォーマット
	 * @return true=成功／false=失敗
	 */
	static public boolean trimNiconicoImage(File sourceFile, File destFile, String contentType)
		throws IOException
	{
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		Integer x1 = 66;
		Integer x2 = 200;
		Integer y1 = null;
		Integer y2 = null;

		for (int y=500 ; y<sourceImage.getHeight() ; y++)
		{
			int rgb = sourceImage.getRGB(70, y);
			if ((((rgb & 0xf00000) <= 0xa00000) && ((rgb & 0xf00000) >= 0x600000)) &&
				(((rgb & 0xf000) <= 0x3000) && ((rgb & 0xf000) >= 0x2000)) &&
				((rgb & 0xf0) == 0x00))
			{
				// 再生ボタンの色

				y1 = y;
				break;
			}
		}

		if (y1 == null)
		{
			// 見つからなかった

			return false;
		}

		for (int y=sourceImage.getHeight()-1 ; y>y1 ; y--)
		{
			int rgb = sourceImage.getRGB(70, y);
			if ((((rgb & 0xf00000) <= 0xa00000) && ((rgb & 0xf00000) >= 0x600000)) &&
				(((rgb & 0xf000) <= 0x3000) && ((rgb & 0xf000) >= 0x2000)) &&
				((rgb & 0xf0) == 0x00))
			{
				// 再生ボタンの色

				y2 = y;
				break;
			}
		}

		if (!((x2 - x1 == 134) && ((y2 - y1 == 21) || (y2 - y1 == 33))))
		{
			// 再生ボタンのサイズは正しい

			return false;
		}

		if (y2 - y1 == 21)
		{
			y1 -= 12;
		}

		MovieRectangle outline = new MovieRectangle(x1, x2, y1, y2);
		cutImage(sourceImage, outline, destFile, contentType);

		return true;
	}

	/**
	 * 画像の一部を別ファイル化
	 * @param sourceImage 対象イメージ
	 * @param outline 切り出し座標
	 * @param destFile 保存ファイル
	 * @param contentType 画像フォーマット
	 */
	static public void cutImage(BufferedImage sourceImage, MovieRectangle outline, File destFile, String contentType)
		throws IOException
	{
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int width2 = outline.x2 - outline.x1;
		int height2 = outline.y2 - outline.y1;

		BufferedImage resizeImage =
			new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		java.awt.Image resizeImage2 =
			sourceImage.getScaledInstance
				(width, height, java.awt.Image.SCALE_AREA_AVERAGING);
		resizeImage.getGraphics().drawImage
			(resizeImage2, -outline.x1, -outline.y1, width, height, null);
		ImageIO.write(resizeImage, contentType, destFile);
	}

	/**
	 * 動画部分を切り出してファイル保存。
	 * @param sourceFile 対象ファイル
	 * @param destFile 保存ファイル
	 * @param contentType 画像フォーマット
	 * @return true=成功／false=失敗
	 */
	static public boolean trimBorderImage(File sourceFile, File destFile, String contentType)
		throws TrimImageException, IOException
	{
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		Integer x1 = null;
		Integer x2 = null;
		Integer y1 = null;
		Integer y2 = null;

		int centerx = sourceImage.getWidth() / 2;
		int centery = sourceImage.getHeight() / 2;

		for (int x=centerx ; x<sourceImage.getWidth() ; x++)
		{
			int rgb = sourceImage.getRGB(x, centery);
			if (((rgb & 0xffffff) == 0xf4f4f4) ||
				((rgb & 0xffffff) == 0xf1f1f1) ||
				((rgb & 0xffffff) == 0x000000))
			{
				if (x2 == null)
				{
					x2 = x;
				}
				else
				{
					if (x == x2 + 1)
					{
						break;
					}
					else
					{
						x2 = null;
					}
				}
			}
		}

		for (int x=centerx ; x>=0 ; x--)
		{
			int rgb = sourceImage.getRGB(x, centery);
			if (((rgb & 0xffffff) == 0xf4f4f4) ||
				((rgb & 0xffffff) == 0xf1f1f1) ||
				((rgb & 0xffffff) == 0x000000))
			{
				if (x1 == null)
				{
					x1 = x;
				}
				else
				{
					if (x == x1 - 1)
					{
						x1++;
						break;
					}
					else
					{
						x1 = null;
					}
				}
			}
		}

		for (int y=centery ; y<sourceImage.getHeight() ; y++)
		{
			int rgb = sourceImage.getRGB(centerx, y);
			if (((rgb & 0xffffff) == 0xf4f4f4) ||
				((rgb & 0xffffff) == 0xf1f1f1) ||
				((rgb & 0xffffff) == 0x000000))
			{
				if (y2 == null)
				{
					y2 = y;
				}
				else
				{
					if (y == y2 + 1)
					{
						break;
					}
					else
					{
						y2 = null;
					}
				}
			}
		}

		for (int y=centery ; y>=0 ; y--)
		{
			int rgb = sourceImage.getRGB(centerx, y);
			if (((rgb & 0xffffff) == 0xf4f4f4) ||
				((rgb & 0xffffff) == 0xf1f1f1) ||
				((rgb & 0xffffff) == 0x000000))
			{
				if (y1 == null)
				{
					y1 = y;
				}
				else
				{
					if (y == y1 - 1)
					{
						y1++;
						break;
					}
					else
					{
						y1 = null;
					}
				}
			}
		}

		if (x1 == null || x2 == null || y1 == null || y2 == null)
		{
			// 見つからなかった

			throw new TrimImageException(String.format("境界が見つからなかった %d,%d-%d,%d", x1, y1, x2, y2));
		}

		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int width2 = x2 - x1;
		int height2 = y2 - y1;

		if ((width2 == 1024 && height2 == 360) ||	// niconico
			  (width2 == 808 && height2 == 455) ||	// ニコ生
			  (width2 == 816 && height2 == 455) ||	// ニコ生
			  (width2 == 854 && height2 == 480) ||	// YouTube
			  (width2 == 855 && height2 == 480) ||	// YouTube
			  (width2 == 664 && height2 == 374))	// bilibili
		{
			// 対応するいずれかのサイズである

			if (width2 == 1024 && height2 == 360)
			{
				// niconico

				x2 -= (1024 - 640);
				width2 = x2 - x1;
			}
			else if (width2 == 808 && height2 == 455)
			{
				// ニコ生用

				x1 -= 21;
				x2 += 21;
				y1 -= 16;
				y2 += 16;
				width2 = x2 - x1;
				height2 = y2 - y1;
			}
			else if (width2 == 816 && height2 == 455)
			{
				// ニコ生用

				x1 -= 21;
				x2 += 13;
				y1 -= 16;
				y2 += 16;
				width2 = x2 - x1;
				height2 = y2 - y1;
			}
		}
		else
		{
			// いずれの動画サイズでもない

			throw new TrimImageException(String.format("認知されないサイズ %d,%d-%d,%d=%d,%d", x1, y1, x2, y2, width2, height2));
		}

		BufferedImage resizeImage =
			new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		java.awt.Image resizeImage2 =
			sourceImage.getScaledInstance
				(width, height, java.awt.Image.SCALE_AREA_AVERAGING);
		resizeImage.getGraphics().drawImage
			(resizeImage2, -x1, -y1, width, height, null);
		ImageIO.write(resizeImage, contentType, destFile);

		return true;
	}

	/**
	 * 動画左側の境界を見つける
	 * @param image 対象画像
	 * @param divStart 二分探索ステップ開始値
	 * @param startX 検索開始位置
	 * @return 境界X座標
	 */
	static public Integer findLeftBorderline(BufferedImage image, int divStart, int startX)
	{
		int x1 = divStart;
		for (int step=divStart/2 ; step>=1 ; step/=2)
		{
			int totalCount = 0;
			int eqCount = 0;
			for (int y=0 ; y<image.getHeight() ; y+=20)
			{
				int rgb1 = (image.getRGB(0, y) & 0xf0f0f0);
				int rgb2 = (image.getRGB(x1, y) & 0xf0f0f0);
				if (rgb1 == rgb2)
				{
					// 近い色

					eqCount++;
				}
				totalCount++;
			}

			if (((eqCount * 100) / totalCount) < 25)
			{
				// 変化している

				//System.out.printf("%d-%d %d\n", x1, step, (eqCount * 100) / totalCount);
				x1 -= step;
			}
			else
			{
				// 変化していない

				//System.out.printf("%d+%d %d\n", x1, step, (eqCount * 100) / totalCount);
				x1 += step;
			}
			if (step == 1)
			{
				return x1;
			}
		}

		return null;
	}

	/**
	 * 動画上側の境界を見つける
	 * @param image 対象画像
	 * @param divStart 二分探索ステップ開始値
	 * @param startY 検索開始位置
	 * @return 境界Y座標
	 */
	static public Integer findTopBorderline(BufferedImage image, int divStart, int startY)
	{
		int y1 = divStart;
		for (int step=divStart/2 ; step>=1 ; step/=2)
		{
			int totalCount = 0;
			int eqCount = 0;
			for (int x=0 ; x<image.getWidth() ; x+=20)
			{
				int rgb1 = (image.getRGB(x, 0) & 0xf0f0f0);
				int rgb2 = (image.getRGB(x, y1) & 0xf0f0f0);
				if (rgb1 == rgb2)
				{
					// 近い色

					eqCount++;
				}
				totalCount++;
			}

			if (((eqCount * 100) / totalCount) < 25)
			{
				// 変化している

				//System.out.printf("%d-%d %d\n", x1, step, (eqCount * 100) / totalCount);
				y1 -= step;
			}
			else
			{
				// 変化していない

				//System.out.printf("%d+%d %d\n", x1, step, (eqCount * 100) / totalCount);
				y1 += step;
			}
			if (step == 1)
			{
				return y1;
			}
		}

		return null;
	}

	/**
	 * 動画右側の境界を見つける
	 * @param image 対象画像
	 * @param divStart 二分探索ステップ開始値
	 * @param startX 検索開始位置
	 * @return 境界X座標
	 */
	static public Integer fildRightBorderline(BufferedImage image, int divStart, int startX)
	{
		int x2 = image.getWidth() / 2;
		for (int step=divStart/2 ; step>=1 ; step/=2)
		{
			int totalCount = 0;
			int eqCount = 0;
			for (int y=0 ; y<image.getHeight() ; y+=20)
			{
				int rgb1 = (image.getRGB(0, y) & 0xf0f0f0);
				int rgb2 = (image.getRGB(x2, y) & 0xf0f0f0);
				if (rgb1 == rgb2)
				{
					// 近い色

					eqCount++;
				}
				totalCount++;
			}

			if (((eqCount * 100) / totalCount) < 75)
			{
				// 変化している

				x2 += step;
			}
			else
			{
				// 変化していない

				x2 -= step;
			}

			if (step == 1)
			{
				return x2;
			}

			if (step < 64 && step > 32)
			{
				// １ドットに向けて絞り込めるよう丸め処理

				step = 32;
			}
		}
		return null;
	}

	/**
	 * 動画下側の境界を見つける
	 * @param image 対象画像
	 * @param divStart 二分探索ステップ開始値
	 * @param startY 検索開始位置
	 * @return 境界Y座標
	 */
	static public Integer fildBottomBorderline(BufferedImage image, int divStart, int startY)
	{
		int bottom = image.getHeight() - 1;
		int y2 = image.getHeight() / 2;

		for (int step=divStart/2 ; step>=1 ; step/=2)
		{
			int totalCount = 0;
			int eqCount = 0;
			for (int x=0 ; x<image.getWidth() ; x+=20)
			{
				int rgb1 = (image.getRGB(x, bottom) & 0xf0f0f0);
				int rgb2 = (image.getRGB(x, y2) & 0xf0f0f0);
				if (rgb1 == rgb2)
				{
					// 近い色

					eqCount++;
				}
				totalCount++;
			}

			if (((eqCount * 100) / totalCount) < 75)
			{
				// 変化している

				y2 += step;
			}
			else
			{
				// 変化していない

				y2 -= step;
			}

			if (step == 1)
			{
				return y2;
			}

			if (step < 64 && step > 32)
			{
				// １ドットに向けて絞り込めるよう丸め処理

				step = 32;
			}
		}
		return null;
	}

	/**
	 * 境界切り出し
	 * @param sourceFile 対象画像ファイル
	 * @return 境界
	 * @throws IOException
	 */
	static public MovieRectangle findMovieOutline(File sourceFile)
		throws IOException
	{
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		MovieRectangle outline =
			new MovieRectangle(
				findLeftBorderline(sourceImage, 256, 0),
				findTopBorderline(sourceImage, 64, 0),
				fildRightBorderline(sourceImage, sourceImage.getWidth() / 2, sourceImage.getWidth() / 2),
				fildBottomBorderline(sourceImage, sourceImage.getHeight() / 2, sourceImage.getHeight() / 2));

		return outline;
	}
}
