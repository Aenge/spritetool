package com.OpenRSC.IO.Workspace;

import com.OpenRSC.IO.Image.ImageWriter;
import com.OpenRSC.IO.Info.InfoWriter;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Frame;
import com.OpenRSC.Model.Subspace;
import java.io.File;
import java.nio.file.Path;

public class WorkspaceWriter {
    private Path home;

    public WorkspaceWriter(Path home) {
        this.home = home;
    }


    public boolean createSubspace(Subspace ss) {
        if (home == null)
            return false;

        File directory = new File(home.toString(), ss.getName());
        if (directory.exists())
            return false;

        if (directory.mkdir())
            return true;

        return false;
    }

    public boolean updateEntry(Subspace subspace, Entry oldEntry, Entry newEntry) {
        File entryHome = new File(subspace.getHome().toString(), newEntry.getID());
        if (!entryHome.exists())
            entryHome.mkdir();

        for (int i = 0; i < newEntry.frameCount(); ++i) {
            if (oldEntry != null &&
                    i < oldEntry.frameCount()) {
                if (!oldEntry.getFrame(i).equals(newEntry.getFrame(i))) {
                    if (!updateSprite(subspace, oldEntry.getFrame(i), newEntry.getFrame(i)))
                        return false;
                }
            } else {
                if (!writeSprite(subspace, (newEntry.getFrame(i))))
                    return false;
            }
        }
        return true;
    }

    //Returns if operation successful
    public boolean updateSprite(Subspace subspace, Frame oldSprite, Frame newSprite) {
        if (subspace == null ||
            oldSprite == null ||
            newSprite == null)
            return true;

        if (oldSprite.equals(newSprite))
            return true;

        if (home == null)
            return false;

        File subspaceHome = new File(home.toString(), subspace.getName());
        if (!subspaceHome.exists())
            return false;

        if (!oldSprite.getInfo().equals(newSprite.getInfo())) {
            File oldFile = new File(subspaceHome.toString(), oldSprite.getFileName() + ".info");
            File newFile = new File(subspaceHome.toString(), newSprite.getFileName() + ".info");

            InfoWriter infoWriter = new InfoWriter(newFile, newSprite.getInfo());

            if (infoWriter.write()) {
                if (!oldSprite.getFileName().equalsIgnoreCase(newSprite.getFileName()) && oldFile.exists())
                    oldFile.delete();
            } else
                return false;
        }

        if (!oldSprite.getImageData().equals(newSprite.getImageData())) {
            File oldFile = new File(subspaceHome.toString(), oldSprite.getFileName() + ".png");
            File newFile = new File(subspaceHome.toString(), newSprite.getFileName() + ".png");


            ImageWriter imageWriter = new ImageWriter(newFile, newSprite.getImageData());

            if (imageWriter.write()) {
                if (!oldSprite.getFileName().equalsIgnoreCase(newSprite.getFileName()))
                    oldFile.delete();
            } else
                return false;
        }

        return true;
    }

    public boolean writeSprite(Subspace subspace, Frame sprite) {
        File subspaceHome = subspace.getHome().toFile();
        if (!subspaceHome.exists())
            return false;

        File entryHome = new File(subspaceHome, sprite.getID());

        File newFile = new File(entryHome, sprite.getFileName() + ".info");

        InfoWriter infoWriter = new InfoWriter(newFile, sprite.getInfo());

        if (!infoWriter.write())
            return false;

        newFile = new File(entryHome, sprite.getFileName() + ".png");

        ImageWriter imageWriter = new ImageWriter(newFile, sprite.getImageData());

        if (!imageWriter.write())
            return false;

        return true;
    }
}
