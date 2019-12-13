package com.OpenRSC.Model;

import com.OpenRSC.IO.Archive.Packer;
import com.OpenRSC.IO.Archive.Unpacker;
import com.OpenRSC.Render.PlayerRenderer;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.util.Callback;

import java.io.File;
import java.nio.file.Path;

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
            if (entry.getFrames().length == 1)
                ++spriteCount;
        }
        return spriteCount;
    }
    public int getAnimationCount() {
        int animationCount = 0;
        for (Entry entry : entryList) {
            if (entry.getFrames().length > 1)
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

    public boolean addNewEntry(String name, Entry.TYPE type, PlayerRenderer.LAYER layer) {
        File newOSPR = new File(home.toString(), name + ".ospr");
        if (newOSPR.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "That entry already exists.");
            alert.showAndWait();
            return false;
        }

        String entryToCopy = "head1";

        switch (type) {
            case SPRITE:
                entryToCopy = "0";
                break;
            case PLAYER_PART:
                switch (layer) {
                    case HEAD_NO_SKIN:
                        entryToCopy = "head1";
                        break;
                    case BODY_NO_SKIN:
                        entryToCopy = "body1";
                        break;
                    case LEGS_NO_SKIN:
                        entryToCopy = "legs1";
                    default:
                        break;
                }
                break;
            case NPC:
                entryToCopy = "goblin";
                break;
            case PLAYER_EQUIPPABLE_HASCOMBAT:
                switch (layer) {
                    case HEAD_NO_SKIN:
                        entryToCopy = "fullhelm";
                        break;
                    case BODY_NO_SKIN:
                        entryToCopy = "platemailtop";
                        break;
                    case LEGS_NO_SKIN:
                        entryToCopy = "platemaillegs";
                        break;
                    case MAIN_HAND:
                        entryToCopy = "2hander";
                        break;
                    case OFF_HAND:
                        entryToCopy = "squareshield";
                        break;
                    case HEAD_WITH_SKIN:
                        entryToCopy = "mediumhelm";
                        break;
                    case BODY_WITH_SKIN:
                        entryToCopy = "chainmail";
                        break;
                    case LEGS_WITH_SKIN:
                        entryToCopy = "skirt";
                        break;
                    case GLOVES:
                        entryToCopy = "leathergloves";
                        break;
                    case BOOTS:
                        entryToCopy = "boots";
                        break;
                    case NECK:
                        entryToCopy = "necklace";
                        break;
                    case CAPE:
                    default:
                        entryToCopy = "cape";
                        break;
                }
                break;
            case PLAYER_EQUIPPABLE_NOCOMBAT:
            default:
                switch(layer) {
                    case MAIN_HAND:
                        entryToCopy = "crossbow";
                        break;
                    case OFF_HAND:
                    default:
                        entryToCopy = "longbow";
                        break;
                }
                break;
        }

        File oldOSPR = new File("resource/animations", entryToCopy + ".ospr");

        Unpacker unpacker = new Unpacker();
        Entry newEntry = unpacker.unpackEntry(oldOSPR);
        newEntry.changeID(name);

        entryList.add(newEntry);

        Packer packer = new Packer();
        packer.packEntry(newEntry, newOSPR);
        return true;
    }
//    public boolean createEntry(String name, Entry.TYPE type, PlayerRenderer.LAYER layer) {

//
//
//        Entry newEntry = new Entry(
//                name,
//                type,
//                layer,
//
//        );
//
//        File[] files = copyPath.listFiles(File::isFile);
//
//        for (File file : files) {
//            if (file.getName().endsWith(".info"))
//                continue;
//
//            String[] filename = file.getName().split(Pattern.quote("."));
//            File infoFile = new File(file.getParent(), filename[0] + ".info");
//
//            Frame newSprite = new Frame(file, infoFile);
//            newSprite.getInfo().setID(name);
//            newEntry.addFrame(newSprite);
//        }
//
//        Collections.sort(newEntry.getFrames(), new Comparator<Frame>() {
//            @Override
//            public int compare(Frame o1, Frame o2) {
//                return o1.getInfo().getFrame() - o2.getInfo().getFrame();
//            }
//        });
//
//        newEntry.setID(name);
//        newEntry.setType(type);
//        newEntry.setLayer(layer);
//
//        entryList.add(newEntry);
//
//        WorkspaceWriter workspaceWriter = new WorkspaceWriter(home);
//        workspaceWriter.updateEntry(this, null, newEntry);
//        return true;
//    }
}
