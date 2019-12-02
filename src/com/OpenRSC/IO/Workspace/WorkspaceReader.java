package com.OpenRSC.IO.Workspace;

import com.OpenRSC.IO.Info.InfoReader;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class WorkspaceReader {

    private SimpleIntegerProperty progressCounter;

    public WorkspaceReader() {
    }

    public void setProgressCounter(SimpleIntegerProperty ip) {
        this.progressCounter = ip;
    }

    public Workspace loadWorkspace(Path path) {
        Workspace ws = new Workspace(path);

        File[] directories = path.toFile().listFiles(File::isDirectory);

        for (File dir : directories) {
            Subspace ss = loadSubspace(dir.toPath());
            ws.getSubspaces().add(ss);
        }

        return ws;
    }

    public Subspace loadSubspace(Path home) {
        String name = home.toFile().getName();
        final Subspace ss = new Subspace(name, home);
        File subspaceHome = home.toFile();
        if (!subspaceHome.exists())
            return null;

        File[] entries = subspaceHome.listFiles(File::isDirectory);

        for (File entry : entries) {
            File[] files = entry.listFiles(File::isFile);

            if (files.length == 0) {
                continue;
            }

            Entry newEntry = new Entry();

            for (File file : files) {
                if (file.getName().endsWith(".info"))
                    continue;

                String[] filename = file.getName().split(Pattern.quote("."));
                File infoFile = new File(file.getParent(), filename[0] + ".info");

                Sprite newSprite = new Sprite(file, infoFile);
                newEntry.addFrame(newSprite);
                if (this.progressCounter != null)
                    this.progressCounter.set(this.progressCounter.get() + 2);
            }

            Collections.sort(newEntry.getFrames(), new Comparator<Sprite>() {
                @Override
                public int compare(Sprite o1, Sprite o2) {
                    return o1.getInfo().getFrame() - o2.getInfo().getFrame();
                }
            });

            newEntry.setID(newEntry.getFrame(0).getID());
            newEntry.setLayer(newEntry.getFrame(0).getInfo().getLayer());
            newEntry.setType(newEntry.getFrame(0).getInfo().getType());
            ss.getEntryList().add(newEntry);
        }

        return ss;
    }


    private static String[] splitFileName(File file) {
        String fileName = file.getName();
        String[] parts = fileName.split(Pattern.quote("."));
        if (parts.length == 2)
            return parts;

        return null;
    }


}
