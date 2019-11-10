package com.OpenRSC.Model.Format;

public class Animation {

    private String name;
    private Sprite[] frames;

    public Animation(String name, int frameCount) {
        this.name = name;
        this.frames = new Sprite[frameCount];
    }

    public void addFrame(Sprite sprite) {
        if (this.frames != null &&
                sprite.getInfo().getFrame() <= this.frames.length)
        this.frames[sprite.getInfo().getFrame()-1] = sprite;
    }

    public Sprite getFrame(int index) {
        if (index <= frames.length)
            return this.frames[index-1];
        return null;
    }

    public int getFrameCount() { return frames.length; }
    public String getName() { return this.name; }
    public Sprite getSprite() { return frames[0]; }
}
