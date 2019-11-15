package com.OpenRSC.IO.Workspace;

import com.OpenRSC.IO.Image.ImageWriter;
import com.OpenRSC.IO.Info.InfoWriter;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Animation;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;

import java.io.File;

public class WorkspaceWriter {

    private Workspace ws;

    public WorkspaceWriter(Workspace ws) {
        this.ws = ws;
    }

    public boolean createSubspace(Subspace ss) {
        if (ws == null)
            return false;

        File directory = ss.getPath().toFile();
        if (directory.exists())
            return false;

        if (directory.mkdir())
            return true;

        return false;
    }
    public boolean updateEntry(Subspace subspace, Entry oldEntry, Entry newEntry) {
        if (oldEntry.getType() != newEntry.getType())
            return false;

        if (newEntry.getType() == Entry.TYPE.SPRITE) {
            return updateSprite(subspace, (Sprite)oldEntry.getSpriteData(), (Sprite)newEntry.getSpriteData());
        } else if (newEntry.getType() == Entry.TYPE.ANIMATION) {
            return updateAnimation(subspace, (Animation)oldEntry.getSpriteData(), (Animation)newEntry.getSpriteData());
        }

        return false;
    }

    //Returns if operation successful
    public boolean updateSprite(Subspace subspace, Sprite oldSprite, Sprite newSprite) {
        if (subspace == null ||
            oldSprite == null ||
            newSprite == null)
            return true;

        if (oldSprite.equals(newSprite))
            return true;

        if (!oldSprite.getInfo().equals(newSprite.getInfo())) {
            File oldFile = new File(subspace.getPath().toString(), oldSprite.getFileName() + ".info");
            File newFile = new File(subspace.getPath().toString(), newSprite.getFileName() + ".info");

            InfoWriter infoWriter = new InfoWriter(newFile, newSprite.getInfo());

            if (infoWriter.write()) {
                if (!oldSprite.getFileName().equalsIgnoreCase(newSprite.getFileName()) && oldFile.exists())
                    oldFile.delete();
            } else
                return false;
        }

        if (!oldSprite.getImageData().equals(newSprite.getImageData())) {
            File oldFile = new File(subspace.getPath().toString(), oldSprite.getFileName() + ".png");
            File newFile = new File(subspace.getPath().toString(), newSprite.getFileName() + ".png");


            ImageWriter imageWriter = new ImageWriter(newFile, newSprite.getImageData());

            if (imageWriter.write()) {
                if (!oldSprite.getFileName().equalsIgnoreCase(newSprite.getFileName()))
                    oldFile.delete();
            } else
                return false;
        }

        return true;
    }

    public boolean writeSprite(Subspace subspace, Sprite sprite) {
        File newFile = new File(subspace.getPath().toString(), sprite.getFileName() + ".info");

        InfoWriter infoWriter = new InfoWriter(newFile, sprite.getInfo());

        if (!infoWriter.write())
            return false;

        newFile = new File(subspace.getPath().toString(), sprite.getFileName() + ".png");

        ImageWriter imageWriter = new ImageWriter(newFile, sprite.getImageData());

        if (!imageWriter.write())
            return false;

        return true;
    }

    public boolean updateAnimation(Subspace subspace, Animation oldAnimation, Animation newAnimation) {
        for (int i = 0; i < newAnimation.getFrameCount(); ++i) {
            if (i < oldAnimation.getFrameCount()) {
                if (!oldAnimation.getFrames()[i].equals(newAnimation.getFrames()[i])) {
                    if (!updateSprite(subspace, oldAnimation.getFrames()[i], newAnimation.getFrames()[i]))
                        return false;
                }
            } else {
                if (!writeSprite(subspace, (newAnimation.getFrames())[i]))
                    return false;
            }
        }
        return true;
    }

}
