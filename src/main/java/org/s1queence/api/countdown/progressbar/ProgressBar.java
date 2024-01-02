package org.s1queence.api.countdown.progressbar;

import org.bukkit.ChatColor;

public class ProgressBar {
    private String repeat(String ch, int count) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < count; ++i)
            buffer.append(ch);

        return buffer.toString();
    }

    private final int totalBars;
    private int current;
    private int max;
    private final String symbol;
    private final String borderLeft;
    private final String borderRight;
    private final ChatColor color;
    private final ChatColor completeColor;
    private final ChatColor percentColor;

    public ProgressBar(int current, int max, int totalBars, String symbol, String borderLeft, String borderRight, ChatColor color, ChatColor completeColor, ChatColor percentColor) {
        this.current = current;
        this.max = max;
        this.totalBars = totalBars;
        this.symbol = symbol;
        this.borderLeft = borderLeft;
        this.borderRight = borderRight;
        this.color = color;
        this.completeColor = completeColor;
        this.percentColor = percentColor;
    }

    public String getProgressBar() {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return borderLeft + (repeat("" + completeColor  + symbol, progressBars) + repeat("" + color + symbol, totalBars - progressBars)) + borderRight;
    }

    public String getPercent() {
        int percent = (int)((double)current / max * 100);
        return percentColor + "" + percent;
    }

    public void setCurrent(int newCurrent) {
        current = newCurrent;
    }
    public void setMax(int newMax) {
        max = newMax;
    }

}
