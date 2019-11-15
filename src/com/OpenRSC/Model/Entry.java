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
    private Object spriteData;

    @Override
    public String toString() {
        return getID();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!Entry.class.isAssignableFrom(o.getClass()))
            return false;

        Entry entry = (Entry)o;
        try {
            if (!entry.getID().equalsIgnoreCase(this.getID()) ||
                    entry.getType() != this.getType())
                return false;
            else
                return entry.getSpriteData().equals(this.getSpriteData());
        } catch (NullPointerException a) {
            a.printStackTrace();
        }

        return true;
    }
    public Entry(Sprite sprite) {
        this.type = TYPE.SPRITE;
        this.spriteData = sprite;
    }

    public Entry(Animation animation) {
        this.type = TYPE.ANIMATION;
        this.spriteData = animation;
    }

    Entry() {}

    private void setType(TYPE type) { this.type = type; }
    private void setSpriteData(Sprite sprite) { this.spriteData = sprite; }
    private void setSpriteData(Animation animation) { this.spriteData = animation; }
    public boolean isAnimation() { return this.type == TYPE.ANIMATION; }
    public boolean isSprite() { return this.type == TYPE.SPRITE; }

    public Object getSpriteData() { return this.spriteData; }

    public Sprite getSprite() {
        if (isSprite())
            return (Sprite)this.spriteData;
        else if (isAnimation())
            return ((Animation)this.spriteData).getViewedFrame();
        return null;
    }
    public String getID() {
        if (isSprite())
            return ((Sprite)spriteData).getID();
        else if (isAnimation())
            return ((Animation)spriteData).getID();

        return null;
    }

    public Entry.TYPE getType() { return this.type; }

    public Entry clone() {
        Entry entry = new Entry();
        if (this.isSprite()) {
            entry.setType(TYPE.SPRITE);
            entry.setSpriteData(((Sprite)this.getSpriteData()).clone());
        } else if (this.isAnimation()) {
            entry.setType(TYPE.ANIMATION);
            entry.setSpriteData(((Animation)this.getSpriteData()).clone());
        }
        return entry;
    }
}
