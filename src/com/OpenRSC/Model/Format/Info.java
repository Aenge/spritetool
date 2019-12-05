package com.OpenRSC.Model.Format;

import com.OpenRSC.Model.Entry;
import com.OpenRSC.Render.PlayerRenderer;


public class Info {

    private Entry.TYPE type;
    private PlayerRenderer.LAYER layer;
    private String id;
    private int frameCount;
    private int frame;
    public boolean useShift;
    public int offsetX;
    public int offsetY;
    public int boundwidth;
    public int boundheight;

    public Info() {
        useShift = false;
        offsetX = 0;
        offsetY = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!Info.class.isAssignableFrom(o.getClass()))
            return false;

        Info info = (Info)o;

        if (!this.id.equalsIgnoreCase(info.id) ||
                this.layer != info.layer ||
                this.type != info.type ||
                this.frameCount != info.frameCount ||
                this.frame != info.frame ||
                this.useShift != info.useShift ||
                this.offsetX != info.offsetX ||
                this.offsetY != info.offsetY ||
                this.boundwidth != info.boundwidth ||
                this.boundheight != info.boundheight)

            return false;


        return true;
    }
    public void setType(Entry.TYPE type) { this.type = type;}
    public Entry.TYPE getType() { return this.type; }
    public void setLayer(PlayerRenderer.LAYER layer) { this.layer = layer; }
    public PlayerRenderer.LAYER getLayer() { return this.layer; }
    public void setID(String id) { this.id = id; }
    public String getID() { return this.id; }
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
   // public Entry.TYPE getType() { return this.type; }
    //public void setType(Entry.TYPE type) { this.type = type; }
    //public PlayerRenderer.LAYER getLayer() { return this.layer; }
    //public void setLayer(PlayerRenderer.LAYER layer) { this.layer = layer; }
    public Info clone() {
        Info info = new Info();
        info.setType(this.getType());
        info.setLayer(this.getLayer());
        info.setID(this.getID());
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
