package com.OpenRSC.Render;

import com.OpenRSC.IO.Workspace.WorkspaceReader;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;

import java.io.File;

public class PlayerRenderer {


    private Subspace shippedAnimations;
    private Entry[] layers = new Entry[12];
    private final int[][] animDirLayer_To_CharLayer = new int[][]{{11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4},
            {11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4}, {11, 3, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4},
            {3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5}, {3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5},
            {4, 3, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5}, {11, 4, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3},
            {11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4, 3}};
    private final int[] frameToDir = new int[]{4,4,4,5,5,5,6,6,6,7,7,7,0,0,0,8,8,8};
    public PlayerRenderer() {
        WorkspaceReader workspaceReader = new WorkspaceReader();
        File folder = new File("resource/animations");
        this.shippedAnimations = workspaceReader.loadSubspace(folder.toPath());
    }

    public Entry[] getLayers() { return this.layers; };

    public Subspace getShippedAnimations() { return this.shippedAnimations; }

    public final void bufferPlayer(SpriteRenderer spriteRenderer, int frame) {
        try {
            int wantedAnimDir = frameToDir[frame];

            for (int lay = 0; lay < 12; ++lay) {
                int mappedLayer = this.animDirLayer_To_CharLayer[wantedAnimDir][lay];
                if (layers[mappedLayer] != null) {
                    byte spriteOffsetX = 0;
                    byte spriteOffsetY = 0;

                    Entry entry = layers[mappedLayer];
                    Sprite sprite = entry.getFrame(frame);
                    if (sprite == null)
                        continue;
                    int xOffset = (spriteRenderer.getWidth2() - sprite.getInfo().getBoundWidth())/2;
                    int yOffset = (spriteRenderer.getHeight2() - sprite.getInfo().getBoundHeight())/2;
                    int something1 = sprite.getInfo().getBoundWidth();
                    int something2 = sprite.getInfo().getBoundHeight();
                    int something3 = entry.getFrame(0).getInfo().getBoundWidth();
                    if (something1 != 0 && something2 != 0 && something3 != 0) {
                        int colorMask1 = 0;
                        int blueScaleColor = 0;
//                        if (colorMask1 == 1) {
//                            colorMask1 = this.getPlayerHairColors()[player.colourHair];
//                        } else if (colorMask1 == 2) {
//                            colorMask1 = this.getPlayerClothingColors()[player.colourTop];
//                        } else if (colorMask1 == 3) {
//                            colorMask1 = this.getPlayerClothingColors()[player.colourBottom];
//                        }

                        spriteRenderer.bufferSprite(sprite, xOffset, yOffset, something1,
                                something2, colorMask1, 15523536, blueScaleColor, false, 0, 1, 0xFFFFFFFF);
                    }

                }
            }

        } catch (RuntimeException a) {
            a.printStackTrace();
        }
    }


    public enum LAYER {
        HEAD_NO_SKIN, //can be basic head or full helm
        BODY_NO_SKIN, //can be basic body or plate mail
        LEGS_NO_SKIN, //can be basic legs or plate legs
        MAIN_HAND,
        OFF_HAND,
        HEAD_WITH_SKIN, //medium helms / hats
        BODY_WITH_SKIN, //chainmails
        LEGS_WITH_SKIN, //robes
        NECK,
        BOOTS,
        GLOVES,
        CAPE;

        public int getIndex() { return this.ordinal(); }

        public LAYER get(int index) {
            for (LAYER layer : LAYER.values()) {
                if (layer.getIndex() == index)
                    return layer;
            }

            return null;
        }
    }
    //Frame dirs
//    0: 4, 0
//    1: 4, 1
//    2: 4, 2
//    3: 5, 0
//    4: 5, 1
//    5: 5, 2
//    6: 6, 0
//    7: 6, 1
//    8: 6, 2
//    9: 7, 0
//    10: 7, 1
//    11: 7, 2
//    12: 0, 0
//    13: 0, 1
//    14: 0, 2
//    15: 8, 0
//    16: 8, 1
//    17: 8, 2
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
