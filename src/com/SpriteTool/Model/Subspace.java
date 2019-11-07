package com.SpriteTool.Model;
import com.SpriteTool.IO.WorkspaceReader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Subspace {

    private Path path;
    private List<Entry> entryList = new ArrayList<Entry>();
    private List<FileInfoPair> pairList = new ArrayList<FileInfoPair>();

    public Subspace(Path path) {
        this.path = path;
        WorkspaceReader.loadSS(this);
    }

    public Path getPath() { return this.path; }
    public String getName() { return this.path.getFileName().toString(); }
    public List<Entry> getEntryList() { return entryList; }
    public List<FileInfoPair> getPairList() { return this.pairList; }
}
