package com.SpriteTool.IO;

import com.SpriteTool.Model.Entry;
import com.SpriteTool.Model.FileInfoPair;
import com.SpriteTool.Model.Subspace;
import com.SpriteTool.Model.Workspace;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WorkspaceReader {
    public static void loadWS(Workspace ws) {
        File[] directories = new File(ws.getHome().toUri()).listFiles(File::isDirectory);

        if (directories == null || directories.length == 0) {
            new Alert(Alert.AlertType.ERROR, "The chosen directory is not a valid workspace.").showAndWait();
            ws = null;
            return;
        }

        for (File dir : directories) {
            ws.getSubspaces().add(new Subspace(dir.toPath()));
        }
    }

    public static void loadSS(Subspace ss) {
        //Find all files in the subspace
        File[] files = new File(ss.getPath().toUri()).listFiles(File::isFile);

        if (files == null) {
            return;
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

        List<String> noPairs = new ArrayList<String>();

        for (String png : pngFiles) {
            if (infoFiles.contains(png))
                ss.getPairList().add(new FileInfoPair(ss.getPath(), png));
            else
                noPairs.add(png);
        }

        if (noPairs.size() > 0) {
            System.out.print("Looks like a new png was added.");
        }

        //Generate entries
        for (FileInfoPair pair : ss.getPairList()) {
            if (pair.getType() == Entry.TYPE.SPRITE) {
                ss.getEntryList().add(new Entry(pair));
            }
        }
    }


    public static String[] splitFileName(File file) {
        String fileName = file.getName();
        String[] parts = fileName.split(Pattern.quote("."));
        if (parts.length == 2)
            return parts;

        return null;
    }


}
