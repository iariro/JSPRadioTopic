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

		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int width2 = 966 - 66;
		int height2 = 679 - 196;

		BufferedImage resizeImage =
			new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
		java.awt.Image resizeImage2 =
			sourceImage.getScaledInstance
				(width, height, java.awt.Image.SCALE_AREA_AVERAGING);
		resizeImage.getGraphics().drawImage
			(resizeImage2, -x1+1, -(196 + y1 - 689), width, height, null);
		ImageIO.write(resizeImage, contentType, destFile);

		return true;
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
}
