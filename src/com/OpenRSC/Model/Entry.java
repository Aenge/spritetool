package com.OpenRSC.Model;
import com.OpenRSC.Model.Format.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Entry {

    private List<Sprite> frames = new ArrayList<>();
    private String id;

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

        if (entry.isAnimation() != this.isAnimation())
            return false;

        try {
            if (!entry.getID().equalsIgnoreCase(id) ||
                entry.frameCount() != this.frameCount())
                return false;
            else {
                for (int i = 0; i < this.frameCount(); ++i) {
                    if (!entry.getFrame(i).equals(this.getFrame(i)))
                        return false;
                }
            }
        } catch (NullPointerException a) {
            a.printStackTrace();
            return false;
        }

        return true;
    }

    public Entry() {}

    public void addFrame(Sprite sprite) {
        this.frames.add(sprite);
    }

    public String getID() { return id; }
    public void setID(String id) { this.id = id; }
    public List<Sprite> getFrames() { return this.frames; }
    public Sprite getFrame(int index) { return this.frames.get(index); }
    public int frameCount() { return this.frames.size(); }
    public boolean isAnimation() { return frames.size() > 1; }

    public Entry clone() {
        Entry entry = new Entry();
        for (Sprite frame : frames)
            entry.addFrame(frame.clone());
        return entry;
    }
}
