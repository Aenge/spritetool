package com.OpenRSC.Model;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Render.PlayerRenderer;
import com.OpenRSC.Render.PlayerRenderer.LAYER;
import java.util.ArrayList;
import java.util.List;

public class Entry {

    private List<Sprite> frames = new ArrayList<>();
    private String id;
    private TYPE type;
    private LAYER layer;

    public enum TYPE {
        SPRITE(new LAYER[]{}),
        PLAYER_PART(new LAYER[]{LAYER.HEAD_NO_SKIN, LAYER.BODY_NO_SKIN, LAYER.LEGS_NO_SKIN}),
        PLAYER_EQUIPPABLE_HASCOMBAT(LAYER.values().clone()),
        PLAYER_EQUIPPABLE_NOCOMBAT(new LAYER[]{LAYER.MAIN_HAND, LAYER.OFF_HAND}),
        NPC(new LAYER[]{});

        private LAYER[] layers;

        TYPE(LAYER[] layers) {
            this.layers = layers;
        }

        public LAYER[] getLayers() { return this.layers; }

    }

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
        if (this.frames.size() == 1) {
            this.type = sprite.getInfo().getType();
            this.layer = sprite.getInfo().getLayer();
        }
    }

    public String getID() { return id; }
    public void setID(String id) { this.id = id; }
    public List<Sprite> getFrames() { return this.frames; }
    public Sprite getFrame(int index) { return this.frames.get(index); }
    public int frameCount() { return this.frames.size(); }
    public boolean isAnimation() { return frames.size() > 1; }
    public TYPE getType() { return this.type; }
    public void setType(TYPE type) { this.type = type; }
    public LAYER getLayer() { return this.layer; }
    public void setLayer(LAYER layer) { this.layer = layer; }
    public Entry clone() {
        Entry entry = new Entry();
        entry.setID(this.id);
        entry.setType(this.type);
        entry.setLayer(this.layer);
        for (Sprite frame : frames)
            entry.addFrame(frame.clone());
        return entry;
    }
}
