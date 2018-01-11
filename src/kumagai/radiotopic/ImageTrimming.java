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

	static final int eqThresh = 40;

	/**
	 * 動画左側の境界を見つける
	 * @param image 対象画像
	 * @param divStart 二分探索ステップ開始値
	 * @param startX 検索開始位置
	 * @return 境界X座標
	 */
	static public Integer findLeftBorderline(BufferedImage image, int divStart, int startX)
	{
		int x1 = startX;
		for (int step=divStart/2 ; step>=1 ; step/=2)
		{
			int totalCount = 0;
			int eqCount = 0;
			for (int y=0 ; y<image.getHeight() ; y+=20)
			{
				int rgb1 = (image.getRGB(startX, y) & 0xf0f0f0);
				int rgb2 = (image.getRGB(x1, y) & 0xf0f0f0);
				if (rgb1 == rgb2)
				{
					// 近い色

					eqCount++;
				}
				totalCount++;
			}

			if (((eqCount * 100) / totalCount) < eqThresh)
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
	 * 背景色を持つ水平線を探しY座標を求める
	 * @param image 対象イメージ
	 * @return 背景色を持つ水平線のY座標
	 */
	static public Integer findTopBorderline(BufferedImage image)
	{
		int centerX = image.getWidth() / 2 + 3;
		int centerY = image.getHeight() / 2;
		for (int y=centerY ; y>=30 ; y--)
		{
			int count = 0;
			int eqCount = 0;
			for (int x=1 ; x<image.getWidth()/2-3 ; x++)
			{
				int rgb1 = image.getRGB(centerX, y) & 0xf0f0f0;
				int rgb2 = image.getRGB(centerX + x, y) & 0xf0f0f0;
				int rgb3 = image.getRGB(centerX - x, y) & 0xf0f0f0;

				if (rgb1 == rgb2)
				{
					eqCount++;
				}
				count++;
				if (rgb1 == rgb3)
				{
					eqCount++;
				}
				count++;

				if (eqCount <= 2 && count >= 20)
				{
					// 背景色の可能性はない

					break;
				}
			}

			if ((eqCount * 100 / count) > 95)
			{
				// 背景色と思われる

				return y;
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
	static public Integer findRightBorderline(BufferedImage image, int leftX, int topY, int bottomY)
	{
		for (int x=image.getWidth() / 2 ; x<image.getWidth() ; x++)
		{
			int count = 0;
			int eqCount = 0;
			for (int y=topY ; y<=bottomY ; y++)
			{
				int rgb1 = image.getRGB(leftX, y) & 0xf0f0f0;
				int rgb2 = image.getRGB(x, y) & 0xf0f0f0;
				if (rgb1 == rgb2)
				{
					// 近い色

					eqCount++;
				}
				count++;

				if (eqCount <= 2 && count >= 20)
				{
					// 背景色の可能性はない

					break;
				}
			}

			if (((eqCount * 100) / count) > 90)
			{
				// ほぼ一色

				return x;
			}
		}
		return null;
	}

	/**
	 * 背景色を持つ水平線を探しY座標を求める
	 * @param image 対象イメージ
	 * @return 背景色を持つ水平線のY座標
	 */
	static public Integer findBottomBorderline(BufferedImage image)
	{
		int centerX = image.getWidth() / 2 + 3;
		int centerY = image.getHeight() / 2;
		for (int y=centerY ; y<image.getHeight() ; y++)
		{
			int count = 0;
			int eqCount = 0;
			for (int x=1 ; x<image.getWidth()/2-3 ; x++)
			{
				int rgb1 = image.getRGB(centerX, y) & 0xf0f0f0;
				int rgb2 = image.getRGB(centerX + x, y) & 0xf0f0f0;
				int rgb3 = image.getRGB(centerX - x, y) & 0xf0f0f0;

				if (rgb1 == rgb2)
				{
					eqCount++;
				}
				count++;
				if (rgb1 == rgb3)
				{
					eqCount++;
				}
				count++;

				if (eqCount <= 2 && count >= 20)
				{
					// 背景色の可能性はない

					break;
				}
			}

			if ((eqCount * 100 / count) > 95)
			{
				// 背景色と思われる

				return y;
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
		Integer rightX = findLeftBorderline(sourceImage, 256, 0);
		Integer topY = findTopBorderline(sourceImage);
		Integer bottomY = findBottomBorderline(sourceImage);

		if (rightX != null && topY != null && bottomY != null)
		{
			// 上下左右の境界が得られた

			MovieRectangle outline =
				new MovieRectangle(
					rightX,
					topY,
					findRightBorderline(sourceImage, 0, topY, bottomY),
					bottomY);

			int width = outline.getWidth();
			if (width == 636 || width == 640)
			{
				// ニコニコ旧形式

				outline.y1 += 44;
				outline.y2 -= 80;
			}
			else if (width == 685)
			{
				// bilibili

				outline.y1 += 52;
			}
			else if (width == 854)
			{
				// Youtube

				// 何もしない
			}
			else if (width == 899)
			{
				// ニコニコ新形式

				outline.x2 += 1;
				outline.y1 += outline.getHeight() - (599 - 40);
				outline.y2 -= 77;
			}
			else if (width == 960)
			{
				// ニコ生

				outline.y1 += 58;
				outline.y2 -= 38;
			}

			return outline;
		}
		else
		{
			// 上下左右の境界が得られなかった

			return new MovieRectangle(rightX, topY, null, bottomY);
		}
	}
}
