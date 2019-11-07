package com.OpenRSC.Model.Format;

import com.OpenRSC.Model.Entry;

public class Info {
    private Entry.TYPE type;
    private int entryID;
    private int frameCount;
    private int frame;
    private int offsetX;
    private int offsetY;
    private int boundwidth;
    private int boundheight;

    public Entry.TYPE getType() { return this.type; }
    public void setType(Entry.TYPE type) { this.type = type; }
    public void setEntryID(int id) { this.entryID = id; }
    public void setFrame(int frame) { this.frame = frame; }
    public void setFrameCount(int framecount) { this.frameCount = framecount; }
    public void setOffsetX(int value) { this.offsetX = value; }
    public void setOffsetY(int value) { this.offsetY = value; }
    public void setBoundWidth(int value) { this.boundwidth = value; }
    public void setBoundHeight(int value) { this.boundheight = value; }
}
