package com.OpenRSC.IO.Workspace;

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

        File directory = new File(ss.getPath().toUri());
        if (directory.exists())
            return false;

        if (directory.mkdir())
            return true;

        return false;
    }

}
