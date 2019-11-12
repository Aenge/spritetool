package com.OpenRSC.Model.Format;

import com.OpenRSC.Model.Entry;

public class Info {
    private Entry.TYPE type;
    private String name;
    private int entryID;
    private int frameCount;
    private int frame;
    private boolean useShift;
    private int offsetX;
    private int offsetY;
    private int boundwidth;
    private int boundheight;

    Info() {}

    public void setType(Entry.TYPE type) { this.type = type; }
    public Entry.TYPE getType() { return this.type; }
    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setEntryID(int id) { this.entryID = id; }
    public int getEntryID() { return this.entryID; }
    public void setFrame(int frame) { this.frame = frame; }
    public int getFrame() { return this.frame; }
    public void setFrameCount(int framecount) { this.frameCount = framecount; }
    public int getFrameCount() { return this.frameCount; }
    public void setOffsetX(int value) { this.offsetX = value; }
    public int getOffsetX() { return this.offsetX; }
    public void setOffsetY(int value) { this.offsetY = value; }
    public int getOffsetY() { return this.offsetY; }
    public void setBoundWidth(int value) { this.boundwidth = value; }
    public int getBoundWidth() { return this.boundwidth; }
    public void setBoundHeight(int value) { this.boundheight = value; }
    public int getBoundHeight() { return this.boundheight; }
    public void setUseShift(boolean bool) { this.useShift = bool; }
    public Boolean getUseShift() { return this.useShift; }

    public Info clone() {
        Info info = new Info();
        if (this.getType() == Entry.TYPE.SPRITE)
            info.setType(Entry.TYPE.SPRITE);
        else
            info.setType(Entry.TYPE.ANIMATION);

        info.setName(String.copyValueOf(this.getName().toCharArray()));
        info.setEntryID(this.getEntryID());
        info.setFrame(this.getFrame());
        info.setFrameCount(this.getFrameCount());
        info.setOffsetX(this.getOffsetX());
        info.setOffsetY(this.getOffsetY());
        info.setBoundWidth(this.getBoundWidth());
        info.setBoundHeight(this.getBoundHeight());
        info.setUseShift(this.getUseShift());

        return info;
    }
}
