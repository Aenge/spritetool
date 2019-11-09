package com.OpenRSC.Model;

import com.OpenRSC.Model.Format.Animation;
import com.OpenRSC.Model.Format.Sprite;
import java.util.EnumSet;

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
    private Sprite sprite;
    private Animation animation;

    @Override
    public String toString() {
        return this.getName();
    }

    public Entry(Sprite sprite) {
        this.type = TYPE.SPRITE;
        this.sprite = sprite;
    }

    public Entry(Animation animation) {
        this.type = TYPE.ANIMATION;
        this.animation = animation;
    }


    public boolean isAnimation() { return this.type == TYPE.ANIMATION; }
    public boolean isSprite() { return this.type == TYPE.SPRITE; }

    public Sprite getSprite() { return this.sprite; }
    public Animation getAnimation() { return this.animation; }

    public Sprite getSpriteRep() {
        if (isSprite())
            return this.sprite;
        else if (isAnimation())
            return this.animation.getSprite();
        return null;
    }
    public String getName() {
        if (isSprite())
            return this.sprite.getName();
        else if (isAnimation())
            return this.animation.getName();

        return null;
    }
}
