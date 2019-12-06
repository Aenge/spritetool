package com.OpenRSC.Render;

import com.OpenRSC.IO.Workspace.WorkspaceReader;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Frame;
import com.OpenRSC.Model.Subspace;
import javafx.scene.paint.Color;

import java.io.File;

public class PlayerRenderer {

    private Subspace shippedAnimations;
    private Entry[] layers = new Entry[LAYER.values().length];
    private SpriteRenderer spriteRenderer;

    private final int[][] animDirLayer_To_CharLayer = new int[][]{{11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4},
            {11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4}, {11, 3, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4},
            {3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5}, {3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5},
            {4, 3, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5}, {11, 4, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3},
            {11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4, 3}};
    private final int[] frameToDir = new int[]{0, 0, 0, 1, 1, 1, 6, 6, 6, 3, 3, 3, 4, 4, 4, 6, 6, 6};

    public PlayerRenderer(SpriteRenderer spriteRenderer) {
        WorkspaceReader workspaceReader = new WorkspaceReader();
        File folder = new File("resource/animations");
        this.shippedAnimations = workspaceReader.loadSubspace(folder);
        this.spriteRenderer = spriteRenderer;
    }

    public Entry[] getLayers() {
        return this.layers;
    }

    public Subspace getShippedAnimations() {
        return this.shippedAnimations;
    }

    public void updateLook(Entry headns, Entry bodyns, Entry legsns, Entry mh, Entry oh, Entry headws, Entry bodyws, Entry legsws, Entry neck, Entry boots, Entry gloves, Entry cape) {
        layers[0] = headns;
        layers[1] = bodyns;
        layers[2] = legsns;
        layers[3] = mh;
        layers[4] = oh;
        layers[5] = headws;
        layers[6] = bodyws;
        layers[7] = legsws;
        layers[8] = neck;
        layers[9] = boots;
        layers[10] = gloves;
        layers[11] = cape;
    }

    public final void bufferPlayer(int frameIndex, Entry override, Color grayscale, Color bluescale) {
        try {

            int wantedAnimDir = frameToDir[frameIndex];

            for (int lay = 0; lay < 12; ++lay) {
                int mappedLayer = this.animDirLayer_To_CharLayer[wantedAnimDir][lay];
                int grayscaleint = 0;
                int bluescaleint = 0;
                Entry entry;

                if (override != null &&
                        mappedLayer == override.getLayer().getIndex()) {
                    entry = override;
                    grayscaleint |= (int) (grayscale.getRed() * 255) << 16;
                    grayscaleint |= (int) (grayscale.getGreen() * 255) << 8;
                    grayscaleint |= (int) (grayscale.getBlue() * 255);
                    bluescaleint |= (int) (bluescale.getRed() * 255) << 16;
                    bluescaleint |= (int) (bluescale.getGreen() * 255) << 8;
                    bluescaleint |= (int) (bluescale.getBlue() * 255);
                } else {
                    if (layers[mappedLayer] == null)
                        continue;

                    entry = layers[mappedLayer];
                }

                if (frameIndex >= entry.getFrames().length)
                    continue;

                Frame frame = entry.getFrames()[frameIndex];
                if (frame == null)
                    continue;
                int xOffset = (spriteRenderer.getWidth2() - frame.getBoundWidth()) / 2;
                int yOffset = (spriteRenderer.getHeight2() - frame.getBoundHeight()) / 2;
                int something1 = frame.getBoundWidth();
                int something2 = frame.getBoundHeight();
                //int something3 = entry.frame(0).getInfo().getBoundWidth();
                if (something1 != 0 && something2 != 0)
                    spriteRenderer.bufferSprite(frame, xOffset, yOffset, something1,
                            something2, grayscaleint, 15523536, bluescaleint, false, 0, 1, 0xFFFFFFFF);
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

        public int getIndex() {
            return this.ordinal();
        }

        public static LAYER get(int index) {
            return LAYER.values()[index];
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
}
