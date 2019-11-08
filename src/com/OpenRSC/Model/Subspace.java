package com.OpenRSC.Model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Subspace {

    private Path path;
    private List<Entry> entryList = new ArrayList<Entry>();

    @Override
    public String toString() { return this.path.getFileName().toString(); }
    public Subspace(Path path) {
        this.path = path;
    }

    public Path getPath() { return this.path; }
    public String getName() { return this.path.getFileName().toString(); }
    public List<Entry> getEntryList() { return entryList; }
}
