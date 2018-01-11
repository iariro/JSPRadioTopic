package kumagai.radiotopic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 画像の動画部分の切り出し処理
 */
public class ImageTrimming
{
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

			if (((eqCount * 100) / totalCount) < 40)
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
	 * @param image 対象画像
	 * @return 境界
	 */
	static public MovieRectangle findMovieOutline(BufferedImage image)
		throws IOException
	{
		Integer rightX = findLeftBorderline(image, 256, 0);
		Integer topY = findTopBorderline(image);
		Integer bottomY = findBottomBorderline(image);

		if (rightX != null && topY != null && bottomY != null)
		{
			// 上下左右の境界が得られた

			MovieRectangle outline =
				new MovieRectangle(
					rightX,
					topY,
					findRightBorderline(image, 0, topY, bottomY),
					bottomY);

			int width = outline.getWidth();
			if (width == 636 || width == 640)
			{
				// ニコニコ旧形式

				outline.x1 -= 1;
				outline.x2 += 1;
				outline.y1 += 44;
				outline.y2 -= 77;
			}
			else if (width == 685)
			{
				// bilibili

				outline.x1 += 3;
				outline.y1 += 51;
			}
			else if (width == 854)
			{
				// Youtube

				outline.x1 -= 1;
				outline.x2 += 1;
				outline.y1 += 1;
				outline.y2 += 1;
			}
			else if (width == 899)
			{
				// ニコニコ新形式

				outline.x2 += 1;
				outline.y1 += outline.getHeight() - (599 - 40);
				outline.y2 -= 75;
			}
			else if (width == 960)
			{
				// ニコ生

				outline.x1 -= 1;
				outline.x2 += 1;
				outline.y1 += 58;
				outline.y2 -= 37;
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
