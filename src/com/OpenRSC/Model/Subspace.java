package com.OpenRSC.Model;

import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Render.PlayerRenderer;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.util.Callback;

import java.io.File;

public class Subspace {
    private StringProperty name = new SimpleStringProperty();
    private ObservableList<Entry> entryList = FXCollections.observableArrayList();

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

    public boolean createEntry(String name, File image, Entry.TYPE type, PlayerRenderer.LAYER layer) {
        if (getEntryByName(name) != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An entry with that name already exists.");
            alert.showAndWait();
            return false;
        }

        Entry newEntry = new Entry();
        newEntry.setID(name);

        Info info = new Info();
        info.setID(name);
        info.setType(type);
        info.setLayer(layer);
        info.setFrame(1);
        info.setFrameCount(1);
        switch (type) {
            case SPRITE:
                info.setBoundWidth(48);
                info.setBoundHeight(32);
                break;
            case PLAYER_PART:
            case PLAYER_EQUIPPABLE_NOCOMBAT:
            case PLAYER_EQUIPPABLE_HASCOMBAT:
                info.setBoundWidth(64);
                info.setBoundHeight(102);
                break;
        }

        Sprite newSprite = new Sprite(image, info);
        newEntry.addFrame(newSprite);

        entryList.add(newEntry);
        return true;
    }
}
