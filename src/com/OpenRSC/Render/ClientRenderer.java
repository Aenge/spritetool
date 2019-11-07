/*
package com.SpriteTool.Render;

import com.SpriteTool.Model.Format.Sprite;

public class ClientRenderer {

    public final void drawSpriteClipping(Sprite e, int x, int y, int width, int height, int colorMask, int colorMask2, int blueMask,
                                         boolean mirrorX, int topPixelSkew, int dummy, int colourTransform) {
        try {
            try {

                if (colorMask2 == 0) {
                    colorMask2 = 0xFFFFFF;
                }

                if (colorMask == 0) {
                    colorMask = 0xFFFFFF;
                }

                int spriteWidth = e.getWidth();
                int spriteHeight = e.getHeight();
                int srcStartX = 0;
                int srcStartY = 0;
                int destFirstColumn = topPixelSkew << 16;
                int scaleX = (spriteWidth << 16) / width;
                int scaleY = (spriteHeight << 16) / height;
                int destColumnSkewPerRow = -(topPixelSkew << 16) / height;
                int destRowHead;
                int skipEveryOther;
                if (e.requiresShift()) {
                    destRowHead = e.getSomething1();
                    skipEveryOther = e.getSomething2();
                    if (destRowHead == 0 || skipEveryOther == 0) {
                        return;
                    }

                    scaleX = (destRowHead << 16) / width;
                    scaleY = (skipEveryOther << 16) / height;
                    int var21 = e.getXShift();
                    if (mirrorX) {
                        var21 = destRowHead - e.getWidth() - var21;
                    }

                    int var22 = e.getYShift();
                    x += (destRowHead + var21 * width - 1) / destRowHead;
                    int var23 = (var22 * height + skipEveryOther - 1) / skipEveryOther;
                    if (var21 * width % destRowHead != 0) {
                        srcStartX = (destRowHead - width * var21 % destRowHead << 16) / width;
                    }

                    y += var23;
                    destFirstColumn += var23 * destColumnSkewPerRow;
                    if (var22 * height % skipEveryOther != 0) {
                        srcStartY = (skipEveryOther - height * var22 % skipEveryOther << 16) / height;
                    }

                    width = (scaleX + ((e.getWidth() << 16) - (srcStartX + 1))) / scaleX;
                    height = ((e.getHeight() << 16) - srcStartY - (1 - scaleY)) / scaleY;
                }

                destRowHead = this.width2 * y;
                destFirstColumn += x << 16;
                if (y < this.clipTop) {
                    skipEveryOther = this.clipTop - y;
                    destFirstColumn += destColumnSkewPerRow * skipEveryOther;
                    height -= skipEveryOther;
                    srcStartY += skipEveryOther * scaleY;
                    destRowHead += this.width2 * skipEveryOther;
                    y = this.clipTop;
                }

                if (y + height >= this.clipBottom) {
                    height -= 1 + y + height - this.clipBottom;
                }

                skipEveryOther = destRowHead / this.width2 & dummy;
                if (!this.interlace) {
                    skipEveryOther = 2;
                }
                // TODO:Make sure this works.
                if (colorMask2 == 0xFFFFFF) {
                    if (null != e.getPixels()) {
                        if (mirrorX) {
                            this.plot_tran_scale_with_mask(dummy ^ 74, e.getPixels(), scaleY, 0,
                                    srcStartY, (e.getWidth() << 16) - (srcStartX + 1), width,
                                    this.pixelData, height, destColumnSkewPerRow, destRowHead, -scaleX, destFirstColumn,
                                    spriteWidth, skipEveryOther, colorMask, colourTransform, blueMask);
                        } else {
                            this.plot_tran_scale_with_mask(dummy + 89, e.getPixels(), scaleY, 0,
                                    srcStartY, srcStartX, width, this.pixelData, height, destColumnSkewPerRow,
                                    destRowHead, scaleX, destFirstColumn, spriteWidth, skipEveryOther, colorMask, colourTransform, blueMask);
                        }
                    }
                } else if (mirrorX) {
                    this.plot_trans_scale_with_2_masks(this.pixelData, e.getPixels(), width,
                            destColumnSkewPerRow, destFirstColumn, dummy + 1603920391, 0, colorMask2, scaleY, -scaleX,
                            (e.getWidth() << 16) - srcStartX - 1, skipEveryOther, srcStartY, spriteWidth,
                            colorMask, height, destRowHead, colourTransform, blueMask);
                } else {
                    this.plot_trans_scale_with_2_masks(this.pixelData, e.getPixels(), width,
                            destColumnSkewPerRow, destFirstColumn, 1603920392, 0, colorMask2, scaleY, scaleX, srcStartX,
                            skipEveryOther, srcStartY, spriteWidth, colorMask, height, destRowHead, colourTransform, blueMask);
                }
            } catch (Exception var24) {
                System.out.println("error in sprite clipping routine");
            }

        } catch (RuntimeException var25) {
            throw GenUtil.makeThrowable(var25, "ua.AB(" + y + ',' + colorMask + ',' + colorMask2 + ',' + mirrorX + ','
                    + topPixelSkew + ',' + e + ',' + height + ',' + width + ',' + x + ',' + dummy + ')');
        }
    }
    private void plot_tran_scale_with_mask(int dummy2, int[] src, int scaleY, int dummy1, int srcStartY,
                                           int srcStartX, int destColumnCount, int[] dest, int destHeight, int destColumnSkewPerRow, int destRowHead,
                                           int scaleX, int destFirstColumn, int srcWidth, int skipEveryOther, int spritePixel, int colourTransform, int blueMask) {
        try {

            int spritePixelR = spritePixel >> 16 & 0xFF;
            int spritePixelG = spritePixel >> 8 & 0xFF;
            int spritePixelB = spritePixel & 0xFF;

            if (blueMask == 0)
                blueMask = 0xFFFFFF;
            try {
                int firstColumn = srcStartX;

                for (int i = -destHeight; i < 0; ++i) {
                    int srcRowHead = (srcStartY >> 16) * srcWidth;
                    int duFirstColumn = destFirstColumn >> 16;
                    int duColumnCount = destColumnCount;
                    if (duFirstColumn < this.clipLeft) {
                        int lost = this.clipLeft - duFirstColumn;
                        duFirstColumn = this.clipLeft;
                        duColumnCount = destColumnCount - lost;
                        srcStartX += scaleX * lost;
                    }

                    skipEveryOther = 1 - skipEveryOther;
                    if (duFirstColumn + duColumnCount >= this.clipRight) {
                        int lost = duColumnCount + duFirstColumn - this.clipRight;
                        duColumnCount -= lost;
                    }

                    if (skipEveryOther != 0) {
                        for (int j = duFirstColumn; j < duColumnCount + duFirstColumn; ++j) {
                            int newColor = src[srcRowHead + (srcStartX >> 16)];
                            if (newColor != 0) {
                                int opacity = colourTransform >> 24 & 0xFF;
                                int inverseOpacity = 256 - opacity;

                                int transformR = colourTransform >> 16 & 0xFF;
                                int transformG = colourTransform >> 8 & 0xFF;
                                int transformB = colourTransform & 0xFF;

                                int newR = newColor >> 16 & 0xFF;
                                int newG = newColor >> 8 & 0xFF;
                                int newB = newColor & 0xFF;

                                // Is the colour from the sprite gray?
                                if (newR == newG && newG == newB) {
                                    newR = (spritePixelR * newR) >> 8;
                                    newG = (spritePixelG * newG) >> 8;
                                    newB = (spritePixelB * newB) >> 8;
                                } else if (blueMask != 0xFFFFFF && newR == newG && newB != newR) {//blue mask?
                                    int blueMaskR = blueMask >> 16 & 0xFF;
                                    int blueMaskG = blueMask >> 8 & 0xFF;
                                    int blueMaskB = blueMask & 0xFF;
                                    int shifter = newR*newB;
                                    newR = (blueMaskR * shifter) >> 16;
                                    newG = (blueMaskG * shifter) >> 16;
                                    newB = (blueMaskB * shifter) >> 16;
                                }

                                int spriteR = ((newR * transformR) >> 8) * opacity;
                                int spriteG = ((newG * transformG) >> 8) * opacity;
                                int spriteB = ((newB * transformB) >> 8) * opacity;

                                int canvasR = (dest[destRowHead + j] >> 16 & 0xff) * inverseOpacity;
                                int canvasG = (dest[destRowHead + j] >> 8 & 0xff) * inverseOpacity;
                                int canvasB = (dest[destRowHead + j] & 0xff) * inverseOpacity;

                                int finalColour =
                                        (((spriteR + canvasR) >> 8) << 16) +
                                                (((spriteG + canvasG) >> 8) << 8) +
                                                ((spriteB + canvasB) >> 8);
                                dest[destRowHead + j] = finalColour;

								*/
/*//*
/ Are we a grey?
								if (newR == newG && newB == newG) {
									dest[destRowHead + j] = (newB * backgroundB >> 8) + ((backgroundG * newG >> 8) << 8)
											+ ((backgroundR * newR >> 8) << 16);
								} else {
									dest[destRowHead + j] = newColor;
								}*//*

                            }

                            srcStartX += scaleX;
                        }
                    }

                    srcStartY += scaleY;
                    srcStartX = firstColumn;
                    destFirstColumn += destColumnSkewPerRow;
                    destRowHead += this.width2;
                }
            } catch (Exception var29) {
                System.out.println("error in transparent sprite plot routine");
            }

            if (dummy2 < 20) {
                this.m_t = (int[]) null;
            }

        } catch (RuntimeException var30) {
            throw GenUtil.makeThrowable(var30,
                    "ua.GA(" + dummy2 + ',' + (src != null ? "{...}" : "null") + ',' + scaleY + ',' + dummy1 + ','
                            + srcStartY + ',' + srcStartX + ',' + destColumnCount + ','
                            + (dest != null ? "{...}" : "null") + ',' + destHeight + ',' + destColumnSkewPerRow + ','
                            + destRowHead + ',' + scaleX + ',' + destFirstColumn + ',' + srcWidth + ',' + skipEveryOther
                            + ',' + spritePixel + ')');
        }
    }

    private void plot_trans_scale_with_2_masks(int[] dest, int[] src, int destColumnCount,
                                               int destColumnSkewPerRow, int destFirstColumn, int dummy1, int spritePixel, int mask2, int scaleY, int scaleX,
                                               int srcStartX, int skipEveryOther, int srcStartY, int srcWidth, int mask1, int destHeight,
                                               int destRowHead, int colourTransform, int blueMask) {
        try {

            int mask1R = mask1 >> 16 & 0xFF;
            int mask1G = mask1 >> 8 & 0xFF;
            int mask1B = mask1 & 0xFF;
            int mask2R = mask2 >> 16 & 0xFF;
            int mask2G = mask2 >> 8 & 0xFF;
            int mask2B = mask2 & 0xFF;

            if (dummy1 != 1603920392) {
                this.clipBottom = 29;
            }

            if (blueMask == 0)
                blueMask = 0xFFFFFF;

            try {
                int var27 = srcStartX;

                for (int var28 = -destHeight; var28 < 0; ++var28) {
                    int var29 = (srcStartY >> 16) * srcWidth;
                    int var30 = destFirstColumn >> 16;
                    int var31 = destColumnCount;
                    int var32;
                    if (this.clipLeft > var30) {
                        var32 = this.clipLeft - var30;
                        var31 = destColumnCount - var32;
                        srcStartX += var32 * scaleX;
                        var30 = this.clipLeft;
                    }

                    if (this.clipRight <= var30 + var31) {
                        var32 = var30 - this.clipRight + var31;
                        var31 -= var32;
                    }

                    skipEveryOther = 1 - skipEveryOther;
                    if (skipEveryOther != 0) {
                        for (var32 = var30; var30 + var31 > var32; ++var32) {
                            spritePixel = src[var29 + (srcStartX >> 16)];
                            if (spritePixel != 0) {
                                int spritePixelR = spritePixel >> 16 & 0xFF;
                                int spritePixelG = spritePixel >> 8 & 0xFF;
                                int spritePixelB = spritePixel & 0xFF;

                                // Is the colour from the sprite gray?
                                if (spritePixelR == spritePixelG && spritePixelG == spritePixelB) {
                                    spritePixelR = (spritePixelR * mask1R) >> 8;
                                    spritePixelG = (spritePixelG * mask1G) >> 8;
                                    spritePixelB = (spritePixelB * mask1B) >> 8;
                                } else if (spritePixelR == 255 && spritePixelG == spritePixelB) { // Is sprite colour full white?
                                    spritePixelR = (spritePixelR * mask2R) >> 8;
                                    spritePixelG = (spritePixelG * mask2G) >> 8;
                                    spritePixelB = (spritePixelB * mask2B) >> 8;
                                } else if (blueMask != 0xFFFFFF && spritePixelR == spritePixelG && spritePixelB != spritePixelG) {
                                    int blueMaskR = blueMask >> 16 & 0xFF;
                                    int blueMaskG = blueMask >> 8 & 0xFF;
                                    int blueMaskB = blueMask & 0xFF;
                                    int shifter = spritePixelR*spritePixelB;
                                    spritePixelR = (blueMaskR * shifter) >> 16;
                                    spritePixelG = (blueMaskG * shifter) >> 16;
                                    spritePixelB = (blueMaskB * shifter) >> 16;
                                }

                                int opacity = colourTransform >> 24 & 0xFF;
                                int inverseOpacity = 0xFF - opacity;

                                int transformR = (colourTransform >> 16) & 0xFF;
                                int transformG = (colourTransform >> 8) & 0xFF;
                                int transformB = colourTransform & 0xFF;

                                int spriteR = ((spritePixelR * transformR) >> 8) * opacity;
                                int spriteG = ((spritePixelG * transformG) >> 8) * opacity;
                                int spriteB = ((spritePixelB * transformB) >> 8) * opacity;

                                int canvasR = (dest[var32 + destRowHead] >> 16 & 0xff) * inverseOpacity;
                                int canvasG = (dest[var32 + destRowHead] >> 8 & 0xff) * inverseOpacity;
                                int canvasB = (dest[var32 + destRowHead] & 0xff) * inverseOpacity;

                                int finalColour =
                                        (((spriteR + canvasR) >> 8) << 16) +
                                                (((spriteG + canvasG) >> 8) << 8) +
                                                ((spriteB + canvasB) >> 8);
                                dest[var32 + destRowHead] = finalColour;

								*/
/*if (spritePixelR == spritePixelG && spritePixelB == spritePixelG) {
									dest[var32 + destRowHead] = (spritePixelB * mask1B >> 8) + (mask1G * spritePixelG >> 8 << 8)
											+ (spritePixelR * mask1R >> 8 << 16);
								} else if (spritePixelR == 255 && spritePixelG == spritePixelB) {
									dest[var32 + destRowHead] = (mask2B * spritePixelB >> 8) + (spritePixelR * mask2R >> 8 << 16)
											+ (spritePixelG * mask2G >> 8 << 8);
								} else {
									dest[var32 + destRowHead] = spritePixel;
								}*//*

                            }

                            srcStartX += scaleX;
                        }
                    }

                    srcStartY += scaleY;
                    srcStartX = var27;
                    destRowHead += this.width2;
                    destFirstColumn += destColumnSkewPerRow;
                }
            } catch (Exception var33) {
                System.out.println("error in transparent sprite plot routine");
            }

        } catch (RuntimeException var34) {
            throw GenUtil.makeThrowable(var34,
                    "ua.AA(" + (dest != null ? "{...}" : "null") + ',' + (src != null ? "{...}" : "null") + ','
                            + destColumnCount + ',' + destColumnSkewPerRow + ',' + destFirstColumn + ',' + dummy1 + ','
                            + spritePixel + ',' + mask2 + ',' + scaleY + ',' + scaleX + ',' + srcStartX + ','
                            + skipEveryOther + ',' + srcStartY + ',' + srcWidth + ',' + mask1 + ',' + destHeight + ','
                            + destRowHead + ')');
        }
    }
}
*/
