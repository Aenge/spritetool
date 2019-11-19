package com.OpenRSC.IO.Workspace;

import com.OpenRSC.IO.Info.InfoReader;
import com.OpenRSC.Model.Entry;
import com.OpenRSC.Model.Format.Animation;
import com.OpenRSC.Model.Format.Info;
import com.OpenRSC.Model.Format.Sprite;
import com.OpenRSC.Model.Subspace;
import com.OpenRSC.Model.Workspace;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.utils.JFXUtilities;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WorkspaceReader {

    private SimpleIntegerProperty progressCounter;
    public WorkspaceReader() {}

    public void setProgressCounter(SimpleIntegerProperty ip) { this.progressCounter = ip; }
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
                    if (info.getType() == Entry.TYPE.SPRITE) {
                        ss.getEntryList().add(new Entry(sprite));
                    } else if (info.getType() == Entry.TYPE.ANIMATION) {
                        boolean exists = false;
                        for (int i = 0; i < ss.getEntryCount(); ++i) {
                            if (ss.getEntryList().get(i).isAnimation()) {
                                Animation animation = ((Animation)ss.getEntryList().get(i).getSpriteData());
                                if (animation != null &&
                                        animation.getID().equalsIgnoreCase(info.getID())) {
                                    exists = true;
                                    animation.addFrame(sprite);
                                }
                            }
                        }
                        if (!exists) {
                            Animation animation = new Animation(info.getID(), info.getFrameCount());
                            animation.addFrame(sprite);
                            ss.getEntryList().add(new Entry(animation));
                        }
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
