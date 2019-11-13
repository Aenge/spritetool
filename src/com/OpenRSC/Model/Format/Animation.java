package com.OpenRSC.Model.Format;

public class Animation {

    private String name;
    private Sprite[] frames;

    public Animation(String name, int frameCount) {
        this.name = name;
        this.frames = new Sprite[frameCount];
    }

    Animation() { }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!Animation.class.isAssignableFrom(o.getClass()))
            return false;

        Animation animation = (Animation)o;

        if (!this.getName().equalsIgnoreCase(animation.getName()))
            return false;

        for (int i = 0; i < frames.length; ++i) {
            if (!frames[i].equals(animation.frames[i]))
                return false;
        }

        return true;
    }
    private void setName(String name) { this.name = name; }
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

    public Animation clone() {
        Animation animation = new Animation();

        animation.setName(String.copyValueOf(this.name.toCharArray()));
        animation.frames = new Sprite[this.getFrameCount()];

        for (int i=0; i < this.getFrameCount(); ++i) {
            animation.frames[i] = this.frames[i].clone();
        }

        return animation;
    }
}
