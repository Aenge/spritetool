package com.OpenRSC.Render;

import com.OpenRSC.IO.Workspace.WorkspaceReader;
import com.OpenRSC.Model.Subspace;

import java.io.File;
import java.net.URL;

public class PlayerRenderer {


    private Subspace shippedAnimations;
    private String[] layers = new String[12];
    private int[] animFrameToSprite_Walk = new int[]{0, 1, 2, 1};
    private final int[] animFrameToSprite_CombatA = new int[]{0, 1, 2, 1, 0, 0, 0, 0};
    private final int[] animFrameToSprite_CombatB = new int[]{0, 0, 0, 0, 0, 1, 2, 1};
    private final int[][] animDirLayer_To_CharLayer = new int[][]{{11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4},
            {11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4}, {11, 3, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4},
            {3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5}, {3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5},
            {4, 3, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5}, {11, 4, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3},
            {11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4, 3}};

    public PlayerRenderer() {
        WorkspaceReader workspaceReader = new WorkspaceReader();
        File folder = new File("resource/animations");
        this.shippedAnimations = workspaceReader.loadSubspace(folder.toPath());
    }

    public String[] getLayers() { return this.layers; };

    public Subspace getShippedAnimations() { return this.shippedAnimations; }
/*
    public final void drawPlayer(int index, int x, int y, int width, int height, int topPixelSkew, int var3,
                                 int overlayMovement) {
        try {

            int wantedAnimDir = 2;
            boolean mirrorX = false;
            int actualAnimDir = wantedAnimDir;
            if (wantedAnimDir == 5) {
                mirrorX = true;
                actualAnimDir = 3;
            } else if (wantedAnimDir == 7) {
                actualAnimDir = 1;
                mirrorX = true;
            } else if (wantedAnimDir == 6) {
                actualAnimDir = 2;
                mirrorX = true;
            }


            int spriteOffset = this.animFrameToSprite_Walk[player.stepFrame / 6 % 4] + actualAnimDir * 3;
            if (player.direction == ORSCharacterDirection.COMBAT_B) {
                wantedAnimDir = 2;
                actualAnimDir = 5;
                x += overlayMovement * 5 / 100;
                mirrorX = true;
                spriteOffset = this.animFrameToSprite_CombatB[this.getFrameCounter() / 6 % 8] + actualAnimDir * 3;
            } else if (player.direction == ORSCharacterDirection.COMBAT_A) {
                x -= overlayMovement * 5 / 100;
                actualAnimDir = 5;
                mirrorX = false;
                wantedAnimDir = 2;
                spriteOffset = this.animFrameToSprite_CombatA[this.getFrameCounter() / 5 % 8] + actualAnimDir * 3;
            }

            for (int lay = 0; lay < 12; ++lay) {
                int mappedLayer = this.animDirLayer_To_CharLayer[wantedAnimDir][lay];
                int animID = player.layerAnimation[mappedLayer] - 1;
                if (animID >= 0) {
                    byte spriteOffsetX = 0;
                    byte spriteOffsetY = 0;
                    int mySpriteOffset = spriteOffset;
                    if (mirrorX && actualAnimDir >= 1 && actualAnimDir <= 3) {
                        if (EntityHandler.getAnimationDef(animID).hasF()) {
                            mySpriteOffset = spriteOffset + 15;
                        } else if (mappedLayer == 4 && actualAnimDir == 1) {
                            mySpriteOffset = actualAnimDir * 3
                                    + this.animFrameToSprite_Walk[(player.stepFrame / 6 + 2) % 4];
                            spriteOffsetY = -3;
                            spriteOffsetX = -22;
                        } else if (mappedLayer == 4 && actualAnimDir == 2) {
                            spriteOffsetX = 0;
                            spriteOffsetY = -8;
                            mySpriteOffset = this.animFrameToSprite_Walk[(player.stepFrame / 6 + 2) % 4]
                                    + actualAnimDir * 3;
                        } else if (mappedLayer == 4 && actualAnimDir == 3) {
                            spriteOffsetY = -5;
                            mySpriteOffset = actualAnimDir * 3
                                    + this.animFrameToSprite_Walk[(2 + player.stepFrame / 6) % 4];
                            spriteOffsetX = 26;
                        } else if (mappedLayer == 3 && actualAnimDir == 1) {
                            mySpriteOffset = actualAnimDir * 3
                                    + this.animFrameToSprite_Walk[(2 + player.stepFrame / 6) % 4];
                            spriteOffsetX = 22;
                            spriteOffsetY = 3;
                        } else if (mappedLayer == 3 && actualAnimDir == 2) {
                            spriteOffsetY = 8;
                            mySpriteOffset = actualAnimDir * 3
                                    + this.animFrameToSprite_Walk[(player.stepFrame / 6 + 2) % 4];
                            spriteOffsetX = 0;
                        } else if (mappedLayer == 3 && actualAnimDir == 3) {
                            spriteOffsetX = -26;
                            mySpriteOffset = this.animFrameToSprite_Walk[(2 + player.stepFrame / 6) % 4]
                                    + actualAnimDir * 3;
                            spriteOffsetY = 5;
                        }
                    }


                    Sprite sprite = spriteSelect(EntityHandler.getAnimationDef(animID), mySpriteOffset);
                    int something1 = sprite.getSomething1();
                    int something2 = sprite.getSomething2();
                    int something3 = this.spriteSelect(EntityHandler.getAnimationDef(animID), 0).getSomething1();
                    if (something1 != 0 && something2 != 0 && something3 != 0) {
                        int xOffset = (spriteOffsetX * width) / something1;
                        int yOffset = (spriteOffsetY * height) / something2;
                        int spriteWidth = (something1 * width) / something3;
                        xOffset -= (spriteWidth - width) / 2;
                        int colorMask1 = EntityHandler.getAnimationDef(animID).getCharColour();
                        int blueScaleColor = EntityHandler.getAnimationDef(animID).getBlueMask();
                        if (colorMask1 == 1) {
                            colorMask1 = this.getPlayerHairColors()[player.colourHair];
                        } else if (colorMask1 == 2) {
                            colorMask1 = this.getPlayerClothingColors()[player.colourTop];
                        } else if (colorMask1 == 3) {
                            colorMask1 = this.getPlayerClothingColors()[player.colourBottom];
                        }

                        int colorMask2 = this.getPlayerSkinColors()[player.colourSkin];

                        this.getSurface().drawSpriteClipping(sprite, xOffset + x, y + yOffset, spriteWidth,
                                height, colorMask1, colorMask2, blueScaleColor, mirrorX, topPixelSkew, 1, 0);
                    }

                }
            }

        } catch (RuntimeException a) {
            a.printStackTrace();
        }
    }
*/
    public enum ORSCharacterDirection {
        NORTH(0, 0, -1),
        NORTH_WEST(1, 1, -1),
        WEST(2, 1, 0),
        SOUTH_WEST(3, 1, 1),
        SOUTH(4, 0, 1),
        SOUTH_EAST(5, -1, 1),
        EAST(6, -1, 0),
        NORTH_EAST(7, -1, -1),
        COMBAT_A(8, 0, 0),
        COMBAT_B(9, 0, 0);
        private static final ORSCharacterDirection[] rsDir_Lookup;

        static {
            int max = 0;
            for (ORSCharacterDirection c : values())
                max = Math.max(max, c.rsDir + 1);
            rsDir_Lookup = new ORSCharacterDirection[max];
            for (ORSCharacterDirection c : values())
                rsDir_Lookup[c.rsDir] = c;
        }

        public final int x0, z0;
        public final int rsDir;

        private ORSCharacterDirection(int rsDir, int x0, int z0) {
            this.rsDir = rsDir;
            this.x0 = x0;
            this.z0 = z0;
        }

        public static ORSCharacterDirection lookup(int rsDir) {
            if (rsDir >= 0 && rsDir < rsDir_Lookup.length)
                return rsDir_Lookup[rsDir];
            for (ORSCharacterDirection c : values())
                if (c.rsDir == rsDir)
                    return c;
            System.out.println("Lookup fail: " + rsDir);
            return null;
        }
    }
}
