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

    public Subspace loadSubspace(Path path) {

        final Subspace ss = new Subspace(path);

        File[] files = ss.getPath().toFile().listFiles(File::isFile);

        if (files == null) {
            return null;
        }

        //Pair .png files to their .info
        List<String> pngFiles = new ArrayList<String>();
        List<String> infoFiles = new ArrayList<String>();


        for (File file : files) {

            String[] fileName = splitFileName(file);
            if (fileName == null)
                continue;
            if (fileName[1].equalsIgnoreCase("png"))
                pngFiles.add(fileName[0]);
            else if (fileName[1].equalsIgnoreCase("info"))
                infoFiles.add(fileName[0]);
        }
        List<String> pairs = new ArrayList<String>();
        List<String> noPairs = new ArrayList<String>();

        for (String png : pngFiles) {
            if (infoFiles.contains(png))
                pairs.add(png);
            else
                noPairs.add(png);
        }

        if (noPairs.size() > 0) {
            System.out.print("Looks like a new png was added.");
        }

        //Generate entries
        for (String pair : pairs) {
            InfoReader reader = new InfoReader();
            File infoFile = new File(ss.getPath().toString(), pair + ".info");
            File pngFile = new File(ss.getPath().toString(), pair + ".png");
            if (!infoFile.exists()
                    || !pngFile.exists()) {
                System.out.print("An expected file did not exist.");
                return null;
            }

            Info info = reader.read(infoFile);
            info.setFileName(pair);

            Sprite sprite = new Sprite(pngFile, info);

            boolean exists = false;
            for (int i = 0; i < ss.getEntryCount(); ++i) {
                Entry entry = ss.getEntryList().get(i);
                if (entry != null &&
                        entry.getID().equalsIgnoreCase(info.getID())) {
                    exists = true;
                    entry.addFrame(sprite);
                }
            }

            if (!exists) {
                Entry entry = new Entry();
                entry.setID(info.getID());
                entry.addFrame(sprite);
                ss.getEntryList().add(entry);
            }

            if (this.progressCounter != null)
                this.progressCounter.set(this.progressCounter.get() + 2);
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
