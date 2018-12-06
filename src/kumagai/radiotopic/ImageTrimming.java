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
	 * 画像の動画部分の切り出し処理
	 * @param args [0]=inputfile [1]=outputfile
	 */
	static public void main(String [] args)
		throws IOException
	{
		String infile;
		String outfile;

		if (args.length < 1)
		{
			System.out.println("Usage : inputfile [outputfile]");
			return;
		}
		infile = args[0];
		if (args.length < 2)
		{
			int dotPosition = infile.lastIndexOf(".");
			outfile = String.format("%s_%s", infile.substring(0, dotPosition), infile.substring(dotPosition));
		}
		else
		{
			outfile = args[1];
		}

		File file = new File(infile);
		File destFile = new File(outfile);
		BufferedImage sourceImage = ImageIO.read(file);
		MovieRectangle outline = ImageTrimming.findMovieOutline(sourceImage);
		if (!outline.isAnyNull())
		{
			// 境界検出成功

			ImageTrimming.cutImage(sourceImage, outline, destFile, "png");
		}
		else
		{
			// 境界検出失敗

			System.out.printf("境界検出失敗 %s\n", outline.toString());
		}
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

			if (((eqCount * 100) / totalCount) < 50)
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
		int centerX = image.getWidth() / 3 + 3;
		int centerY = image.getHeight() / 2;
		int maxmaxYDiffCount = 0;
		Integer maxmaxYDiffY = null;
		for (int y=centerY ; y<image.getHeight() ; y++)
		{
			int yDiffCount = 0;
			int maxYDiffCount = 0;
			for (int x=1 ; x<image.getWidth()/2 ; x++)
			{
				int rgb1 = image.getRGB(centerX + x, y) & 0xf8f8f8;
				int rgb2 = image.getRGB(centerX + x, y-1) & 0xf8f8f8;

				if (rgb1 == rgb2)
				{
					yDiffCount = 0;
				}
				else
				{
					yDiffCount++;
					if (maxYDiffCount < yDiffCount)
					{
						maxYDiffCount = yDiffCount;
					}
				}
			}

			if (maxmaxYDiffCount < maxYDiffCount)
			{
				maxmaxYDiffCount = maxYDiffCount;
				maxmaxYDiffY = y;
			}
			//System.out.printf("%d : %d\n", y, maxmaxYDiffCount);
		}

		return maxmaxYDiffY;
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

			//System.out.printf("\t%d,%d-%d,%d\n", outline.x1, outline.y1, outline.x2, outline.y2);
			int width = outline.getWidth();
			int height = outline.getHeight();
			//System.out.printf("%d,%d\n", width, height);
			if (width == 636 || width == 640)
			{
				// ニコニコ旧形式

				if (height == 405)
				{
					outline.x1 -= 1;
					outline.x2 += 1;
					outline.y1 += 44;
					outline.y2 -= 0;
				}
				else
				{
					outline.x1 -= 1;
					outline.x2 += 1;
					outline.y1 += 44;
					outline.y2 -= 77;
				}
			}
			else if (width == 672)
			{
				// ニコニコ新形式

				outline.y1 += 44;
				outline.y2 -= (747 - 661);
			}
			else if (width == 683 || width == 685)
			{
				// bilibili

				outline.x1 += 3;
				if (height == 581)
				{
					outline.y2 -= (726 - 604);
				}
				else if (height == 559 || height == 560)
				{
					outline.x1 -= 2;
					outline.y1 += (197 - 147);
					outline.y2 -= (707 - 639);
				}
				else if (height == 562)
				{
					outline.y1 += (197 - 147);
					outline.y2 -= (707 - 638);
				}
				else
				{
					outline.y1 += (197 - 145);
					outline.y2 -= (745 - 553);
				}
			}
			else if (width == 854 || width == 856)
			{
				// Youtube

				if (height == 674)
				{
					outline.y2 -= (732 - 540);
				}
				else if (height == 574)
				{
					outline.y2 -= (653 - 560);
				}
				outline.x1 -= 1;
				outline.x2 += 1;
				outline.y1 += 1;
				outline.y2 += 1;
			}
			else if (width == 893)
			{
				// Youtube
			}
			else if (width == 899)
			{
				// ニコニコ新形式

				if (height == 523)
				{
					outline.x2 += 1;
					outline.y1 += 40;
				}
				else if (height == 568)
				{
					outline.x2 += 1;
					outline.y1 += 40;
					outline.y2 -= (746 - 701) - 2;
				}
				else if (height == 599)
				{
					outline.x2 += 1;
					outline.y1 += 40;
					outline.y2 -= 77;
				}
				else if (height == 608)
				{
					outline.x2 += 1;
					outline.y1 += 40;
					outline.y2 -= (732 - 635);
				}
				else if (height == 643)
				{
					outline.x2 += 1;
					outline.y1 += 40;
					outline.y2 -= (767 - 636);
				}
				else
				{
					outline.x2 += 1;
					outline.y2 -= (610 - 525);
				}
			}
			else if (width == 960 || width == 1239)
			{
				// ニコ生

				if (width == 960)
				{
					outline.x1 += 55;
					outline.x2 -= 55;
					outline.y1 += 61;
					outline.y2 += 1;
				}
				else if (width == 1239)
				{
					//outline.x1 -= 5;
					outline.x2 -= 384;
					outline.y1 += 92;
					outline.y2 -= 79;
				}
			}
			else if (width == 1280)
			{
				// Youtube - シリーズ

				outline.x2 -= (1280 - 852);
				if (height == 481)
				{
				}
				else
				{
					outline.y2 -= (653 - 560);
				}
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
