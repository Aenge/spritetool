package com.SpriteTool.Model;

import com.SpriteTool.Model.Format.Sprite;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Entry {

    public enum TYPE {
        SPRITE(1),
        ANIMATION(2);
        private int value;
        TYPE(int value) { this.value = value; }
        public int getValue() { return this.value; }
        public static TYPE get(int id) {
            for (TYPE type : EnumSet.allOf(TYPE.class)) {
                if (type.getValue() == id)
                    return type;
            }
            return null;
        }
    }

    private TYPE type;
    private List<File> fileList;

    public Entry(FileInfoPair pair) {
        this.type = pair.getType();
    }

    public boolean isAnimation() { return this.type == TYPE.ANIMATION; }
    public boolean isSprite() { return this.type == TYPE.SPRITE; }

}
