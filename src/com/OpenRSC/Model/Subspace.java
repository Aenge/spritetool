package com.OpenRSC.Model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.nio.file.Path;

public class Subspace {

    private Path path;
    private StringProperty pather = new SimpleStringProperty();
    private ObservableList<Entry> entryList = FXCollections.observableArrayList();

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
    public ObservableList<Entry> getEntryList() { return entryList; }

    public int getEntryCount() { return this.entryList.size(); }

    public int getSpriteCount() {
        int spriteCount = 0;
        for (Entry entry : entryList) {
            if (!entry.isAnimation())
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

    public Entry getEntryByName(String name) {
        for (Entry entry : entryList) {
            if (entry.getID().equalsIgnoreCase(name)) {
                return entry;
            }
        }

        return null;
    }
}
