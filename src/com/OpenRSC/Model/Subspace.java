package com.OpenRSC.Model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Subspace {

    private Path path;
    private List<Entry> entryList = new ArrayList<Entry>();

    @Override
    public String toString() { return this.path.getFileName().toString(); }
    public Subspace(Path path) {
        this.path = path;
    }

    public Path getPath() { return this.path; }
    public String getName() { return this.path.getFileName().toString(); }
    public List<Entry> getEntryList() { return entryList; }

    public int getEntryCount() { return this.entryList.size(); }
    public int getSpriteCount() {
        int spriteCount = 0;
        for (Entry entry : entryList) {
            if (entry.isSprite())
                ++spriteCount;
        }
        return spriteCount;
    }
    public int getAnimationCount() {
        int animationCount = 0;
        for (Entry entry : entryList) {
            if (entry.isAnimation())
                ++animationCount;
        }
        return animationCount;
    }

}
