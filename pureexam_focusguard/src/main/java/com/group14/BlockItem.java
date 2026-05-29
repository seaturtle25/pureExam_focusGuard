package com.group14;

// block or unblock的網址與軟體
public class BlockItem {

    private String name;
    private boolean enabled;

    public BlockItem(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}