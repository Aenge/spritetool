package com.OpenRSC.Model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Subspace {

    private Path path;
    private StringProperty pather = new SimpleStringProperty();
    private List<Entry> entryList = new ArrayList<Entry>();

    @Override
    public String toString() { return getName(); }

    public static Callback<Subspace, Observable[]> extractor() {
        return (Subspace p) -> new Observable[]{p.pather};
    }
    public Subspace(Path path) {
        setPath(path);
    }

    public Path getPath() { return this.path; }
    public void setPath(Path path) {
        this.path = path;
        pather.set(path.getFileName().toString());
    }
    public String getName() { return pather.getValue().toString(); }
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
