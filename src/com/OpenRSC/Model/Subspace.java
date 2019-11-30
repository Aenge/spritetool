package com.OpenRSC.Model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.nio.file.Path;

public class Subspace {
    private StringProperty name = new SimpleStringProperty();
    private transient ObservableList<Entry> entryList = FXCollections.observableArrayList();

    @Override
    public String toString() { return getName(); }

    public static Callback<Subspace, Observable[]> extractor() {
        return (Subspace p) -> new Observable[]{p.name};
    }
    public Subspace(String name) {
        this.name.set(name);
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() { return name.getValue(); }
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
