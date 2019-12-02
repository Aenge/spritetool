package com.OpenRSC.Model;

import com.OpenRSC.IO.Workspace.WorkspaceWriter;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class Subspace {
    private Path home;
    private StringProperty name = new SimpleStringProperty();
    private ObservableList<Entry> entryList = FXCollections.observableArrayList();

    @Override
    public String toString() { return getName(); }

    public static Callback<Subspace, Observable[]> extractor() {
        return (Subspace p) -> new Observable[]{p.name};
    }
    public Subspace(String name, Path home) {
        this.home = home;
        this.name.set(name);
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() { return name.getValue(); }
    public Path getHome() { return this.home; }

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

    public boolean createEntry(String name, Entry.TYPE type, PlayerRenderer.LAYER layer) {
        File checkExists = new File(home.toString(), name);
        if (checkExists.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "That entry already exists.");
            alert.showAndWait();
            return false;
        }

        File copyPath = null;

        switch (type) {
            case PLAYER_PART:
                copyPath = new File("resource/animations/head1");
                break;
            default:
                break;
        }

        if (copyPath == null)
            return false;

        Entry newEntry = new Entry();

        File[] files = copyPath.listFiles(File::isFile);

        for (File file : files) {
            if (file.getName().endsWith(".info"))
                continue;

            String[] filename = file.getName().split(Pattern.quote("."));
            File infoFile = new File(file.getParent(), filename[0] + ".info");

            Sprite newSprite = new Sprite(file, infoFile);
            newSprite.getInfo().setID(name);
            newEntry.addFrame(newSprite);
        }

        Collections.sort(newEntry.getFrames(), new Comparator<Sprite>() {
            @Override
            public int compare(Sprite o1, Sprite o2) {
                return o1.getInfo().getFrame() - o2.getInfo().getFrame();
            }
        });

        newEntry.setID(name);
        newEntry.setType(type);
        newEntry.setLayer(layer);

        entryList.add(newEntry);

        WorkspaceWriter workspaceWriter = new WorkspaceWriter(home);
        workspaceWriter.updateEntry(this, null, newEntry);
        return true;
    }
}
