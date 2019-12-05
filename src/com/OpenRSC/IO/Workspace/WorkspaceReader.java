package com.OpenRSC.IO.Workspace;

import com.OpenRSC.IO.Archive.Unpacker;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Frame;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
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
            Subspace ss = loadSubspace(dir);
            ws.getSubspaces().add(ss);
        }

        return ws;
    }

//    public Workspace loadWorkspace(Path path) {
//        Workspace ws = new Workspace(path);
//
//        File[] directories = path.toFile().listFiles(File::isDirectory);
//
//        for (File dir : directories) {
//            Subspace ss = loadSubspace(dir.toPath());
//            ws.getSubspaces().add(ss);
//        }
//
//        return ws;
//    }

    public Subspace loadSubspace(File home) {
        String name = home.getName();
        final Subspace ss = new Subspace(name, home.toPath());
        File subspaceHome = home;
        if (!subspaceHome.exists())
            return null;
        File[] entryFiles = subspaceHome.listFiles(File::isFile);

        if (entryFiles.length == 0)
            return ss;

        Unpacker unpacker = new Unpacker();

        try {
            for (File entryFile : entryFiles) {
                Entry entry = unpacker.unpack(entryFile);
                ss.getEntryList().add(entry);
                if (this.progressCounter != null)
                    this.progressCounter.set(this.progressCounter.get() + 1);
            }
        } catch (Exception a) { a.printStackTrace(); return null; }
        return ss;
    }
//    public Subspace loadSubspace(Path home) {
//        String name = home.toFile().getName();
//        final Subspace ss = new Subspace(name, home);
//        File subspaceHome = home.toFile();
//        if (!subspaceHome.exists())
//            return null;
//
//        File[] entries = subspaceHome.listFiles(File::isDirectory);
//
//        for (File entry : entries) {
//            File[] files = entry.listFiles(File::isFile);
//
//            if (files.length == 0) {
//                continue;
//            }
//
//            Entry newEntry = new Entry();
//
//            for (File file : files) {
//                if (file.getName().endsWith(".info"))
//                    continue;
//
//                String[] filename = file.getName().split(Pattern.quote("."));
//                File infoFile = new File(file.getParent(), filename[0] + ".info");
//
//                Frame newSprite = new Frame(file, infoFile);
//                newEntry.addFrame(newSprite);
//                if (this.progressCounter != null)
//                    this.progressCounter.set(this.progressCounter.get() + 2);
//            }
//
//            Collections.sort(newEntry.getFrames(), new Comparator<Frame>() {
//                @Override
//                public int compare(Frame o1, Frame o2) {
//                    return o1.getInfo().getFrame() - o2.getInfo().getFrame();
//                }
//            });
//
//            newEntry.setID(newEntry.getFrame(0).getID());
//            newEntry.setLayer(newEntry.getFrame(0).getInfo().getLayer());
//            newEntry.setType(newEntry.getFrame(0).getInfo().getType());
//            ss.getEntryList().add(newEntry);
//        }
//
//        return ss;
//    }


    private static String[] splitFileName(File file) {
        String fileName = file.getName();
        String[] parts = fileName.split(Pattern.quote("."));
        if (parts.length == 2)
            return parts;

        return null;
    }


}
