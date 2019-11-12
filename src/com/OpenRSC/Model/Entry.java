package com.OpenRSC.Model;

import com.OpenRSC.Model.Format.Animation;
import com.OpenRSC.Model.Format.Sprite;

public class Entry {

    public enum TYPE {
        SPRITE,
        ANIMATION;

        @Override
        public String toString() {
            if (this == SPRITE)
                return "Sprite";
            else
                return "Animation";
        }
    }

    private TYPE type;
    private Sprite sprite;
    private Animation animation;

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!Entry.class.isAssignableFrom(o.getClass()))
            return false;

        Entry entry = (Entry)o;
        try {
            if (!entry.getName().equalsIgnoreCase(this.getName()) ||
                    entry.getType() != this.getType())
                return false;
            else if (entry.getType() == TYPE.SPRITE) {
                return entry.getSprite().equals(this.getSprite());
            } else if (entry.getType() == TYPE.ANIMATION) {
                return entry.getAnimation().equals(this.getAnimation());
            }
        } catch (NullPointerException a) {
            a.printStackTrace();
        }

        return true;
    }
    public Entry(Sprite sprite) {
        this.type = TYPE.SPRITE;
        this.sprite = sprite;
    }

    public Entry(Animation animation) {
        this.type = TYPE.ANIMATION;
        this.animation = animation;
    }

    Entry() {}

    private void setType(TYPE type) { this.type = type; }
    private void setSprite(Sprite sprite) { this.sprite = sprite; }
    private void setAnimation(Animation animation) { this.animation = animation; }
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

    public Entry.TYPE getType() { return this.type; }

    public Entry clone() {
        Entry entry = new Entry();
        if (this.isSprite()) {
            entry.setType(TYPE.SPRITE);
            entry.setSprite(this.getSprite().clone());
        } else if (this.isAnimation()) {
            entry.setType(TYPE.ANIMATION);
            entry.setAnimation(this.getAnimation().clone());
        }
        return entry;
    }
}
