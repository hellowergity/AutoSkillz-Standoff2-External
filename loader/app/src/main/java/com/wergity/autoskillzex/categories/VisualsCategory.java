package com.wergity.autoskillzex.categories;

import com.google.android.material.tabs.TabLayout;

public class VisualsCategory extends Category {
    private static final int FRIENDLY_ESP = 0;
    private static final int BOUNDING_BOX = 1;
    private static final int LINE         = 2;
    private static final int DISTANCE     = 3;
    private static final int HEALTH_BAR   = 4;
    private static final int ALLY_COLOR   = 5;
    private static final int ENEMY_COLOR  = 6;

    public VisualsCategory(TabLayout tabLayout) {
        super(tabLayout, 1, "VISUALS");
    }

    @Override
    public void create() {
        super.create();

        addSwitch("Friendly ESP", false, isChecked -> signal(FRIENDLY_ESP, isChecked));
        addText("Enable ESP functions for team mates as well.", true);
        addSeparator();

        addSwitch("Bounding Box", false, isChecked -> signal(BOUNDING_BOX, isChecked));
        addText("Display bounding boxes on players.", true);
        addSeparator();

        addSwitch("Line", false, isChecked -> signal(LINE, isChecked));
        addText("Display lines to players.", true);
        addSeparator();

        addSwitch("Distance", false, isChecked -> signal(DISTANCE, isChecked));
        addText("Display players distance.", true);
        addSeparator();

        addSwitch("Health Bar", false, isChecked -> signal(HEALTH_BAR, isChecked));
        addText("Display players health.", true);
        addSeparator();

        addSpinner("Ally Color", 6, new String[] {
                "White",
                "Black",
                "Red",
                "Yellow",
                "Green",
                "Cyan",
                "Blue",
                "Purple"
        }, position -> signal(ALLY_COLOR, position));
        addSeparator();

        addSpinner("Enemy Color", 2, new String[] {
                "White",
                "Black",
                "Red",
                "Yellow",
                "Green",
                "Cyan",
                "Blue",
                "Purple"
        }, position -> signal(ENEMY_COLOR, position));
        addSeparator();

        signal(ALLY_COLOR, 6);
        signal(ENEMY_COLOR, 2);
    }
}