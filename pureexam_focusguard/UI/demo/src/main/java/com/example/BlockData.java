package com.example;

import java.util.ArrayList;
import java.util.List;

// 將多個BlockItem儲存成list
public class BlockData {

    private List<BlockItem> blockedWebsites;
    private List<BlockItem> blockedApps;

    public BlockData() {

        blockedWebsites = new ArrayList<>();
        blockedApps = new ArrayList<>();
    }

    public List<BlockItem> getBlockedWebsites() {
        return blockedWebsites;
    }

    public List<BlockItem> getBlockedApps() {
        return blockedApps;
    }
}