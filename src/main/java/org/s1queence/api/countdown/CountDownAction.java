package org.s1queence.api.countdown;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.s1queence.api.countdown.progressbar.ProgressBar;

import java.util.HashMap;
import java.util.Map;

import static org.s1queence.api.S1TextUtils.getTextWithInsertedPlayerName;
import static org.s1queence.api.S1Utils.sendActionBarMsg;

public class CountDownAction {
    public static final float CDA_WALK_SPEED = 0.03432F;
    private final Player player;
    private final Location startLocation;
    private final Player target;
    private final JavaPlugin plugin;
    private final ItemStack launchItem;
    private final float initialPlayerSpeed;
    private final float initialTargetSpeed;
    private static final Map<Player, Player> PreprocessActionHandlers = new HashMap<>();
    private static final Map<Player, Player> DoubleRunnableActionHandlers = new HashMap<>();
    private final boolean isDoubleRunnableAction;
    private final boolean isClosePlayersInventoriesEveryTick;
    private final String everyTickBothActionBarMsg;
    private String everyTickTargetTitle;
    private String everyTickTargetSubtitle;
    private final String everyTickPlayerTitle;
    private final String everyTickPlayerSubtitle;
    private final String completeBothActionBarMsg;
    private final String completePlayerTitle;
    private final String completePlayerSubtitle;
    private String completeTargetTitle;
    private String completeTargetSubtitle;
    private int ticksLeft;
    private final String cancelBothActionBarMsg;
    private final String cancelPlayerTitle;
    private final String cancelPlayerSubtitle;
    private String cancelTargetTitle;
    private String cancelTargetSubtitle;
    private final int seconds;
    private final ProgressBar pb;
    private boolean preprocessActionComplete = false;

    public CountDownAction(
            @NotNull Player player,
            @NotNull Player target,
            int seconds,
            boolean isDoubleRunnableAction,
            boolean isClosePlayersInventoriesEveryTick,
            @NotNull ProgressBar pb,
            @NotNull JavaPlugin plugin,

            @NotNull String everyTickBothActionBarMsg,
            @NotNull String everyTickPlayerTitle,
            @NotNull String everyTickPlayerSubtitle,
            @Nullable String everyTickTargetTitle,
            @Nullable String everyTickTargetSubtitle,

            @NotNull String completeBothActionBarMsg,
            @NotNull String completePlayerTitle,
            @NotNull String completePlayerSubtitle,
            @Nullable String completeTargetTitle,
            @Nullable String completeTargetSubtitle,

            @NotNull String cancelBothActionBarMsg,
            @NotNull String cancelPlayerTitle,
            @NotNull String cancelPlayerSubtitle,
            @Nullable String cancelTargetTitle,
            @Nullable String cancelTargetSubtitle
        )
    {
        this.player = player;
        this.target = target;
        this.seconds = seconds;
        this.pb = pb;
        this.isDoubleRunnableAction = isDoubleRunnableAction;
        this.isClosePlayersInventoriesEveryTick = isClosePlayersInventoriesEveryTick;
        this.plugin = plugin;
        this.startLocation = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        this.launchItem = player.getInventory().getItemInMainHand();
        this.initialPlayerSpeed = player.getWalkSpeed();
        this.initialTargetSpeed = target.getWalkSpeed();

        this.everyTickBothActionBarMsg = everyTickBothActionBarMsg;
        this.completeBothActionBarMsg = completeBothActionBarMsg;
        this.cancelBothActionBarMsg = cancelBothActionBarMsg;

        String pName = player.getName();
        if (everyTickTargetTitle != null) this.everyTickTargetTitle = getTextWithInsertedPlayerName(everyTickTargetTitle, pName);
        if (everyTickTargetSubtitle != null) this.everyTickTargetSubtitle = getTextWithInsertedPlayerName(everyTickTargetSubtitle, pName);
        if (completeTargetTitle != null) this.completeTargetTitle = getTextWithInsertedPlayerName(completeTargetTitle, pName);
        if (completeTargetSubtitle != null) this.completeTargetSubtitle = getTextWithInsertedPlayerName(completeTargetSubtitle, pName);
        if (cancelTargetTitle != null) this.cancelTargetTitle = getTextWithInsertedPlayerName(cancelTargetTitle, pName);
        if (cancelTargetSubtitle != null) this.cancelTargetSubtitle = getTextWithInsertedPlayerName(cancelTargetSubtitle, pName);

        String tName = target.getName();
        this.everyTickPlayerTitle = getTextWithInsertedPlayerName(everyTickPlayerTitle, tName);
        this.everyTickPlayerSubtitle = getTextWithInsertedPlayerName(everyTickPlayerSubtitle, tName);
        this.completePlayerTitle = getTextWithInsertedPlayerName(completePlayerTitle, tName);
        this.completePlayerSubtitle = getTextWithInsertedPlayerName(completePlayerSubtitle, tName);
        this.cancelPlayerTitle = getTextWithInsertedPlayerName(cancelPlayerTitle, tName);
        this.cancelPlayerSubtitle = getTextWithInsertedPlayerName(cancelPlayerSubtitle, tName);
    }

    public static Map<Player, Player> getPreprocessActionHandlers() {
        return PreprocessActionHandlers;
    }

    public static boolean isPlayerInPreprocessAction(Player player) {
        return PreprocessActionHandlers.containsKey(player) || PreprocessActionHandlers.containsValue(player);
    }

    public static boolean isPlayerInDoubleRunnableAction(Player player) {
        return DoubleRunnableActionHandlers.containsKey(player) || DoubleRunnableActionHandlers.containsValue(player);
    }

    public static boolean isPlayerInCountDownAction(Player player) {
        return isPlayerInDoubleRunnableAction(player) || isPlayerInPreprocessAction(player);
    }

    public static Map<Player, Player> getDoubleRunnableActionHandlers() {
        return DoubleRunnableActionHandlers;
    }

    public static boolean isPlayerMakingSoloCDAction(Player player) {
        Player t1 = PreprocessActionHandlers.get(player);
        Player t2 = DoubleRunnableActionHandlers.get(player);

        return (t1 != null && t1.equals(player)) || (t2 != null && t2.equals(player));
    }

    protected void cancelAction(boolean sendDefaultMessages) {
        DoubleRunnableActionHandlers.remove(player);
        PreprocessActionHandlers.remove(player);

        if (sendDefaultMessages) {
            player.sendTitle(cancelPlayerTitle, cancelPlayerSubtitle, 0, 75, 20);
            if (!isSoloAction()) target.sendTitle(cancelTargetTitle, cancelTargetSubtitle, 0, 75, 20);
            sendActionBarMsg(player, cancelBothActionBarMsg);
            if (!isSoloAction()) sendActionBarMsg(target, cancelBothActionBarMsg);
        }

        player.setWalkSpeed(initialPlayerSpeed);
        if (!isSoloAction()) target.setWalkSpeed(initialTargetSpeed);
        player.closeInventory();
        if (!isSoloAction()) target.closeInventory();
    }

    protected void reducePlayersWalkSpeed() {
        if (player.getWalkSpeed() > CDA_WALK_SPEED) player.setWalkSpeed(CDA_WALK_SPEED);
        if (target.getWalkSpeed() > CDA_WALK_SPEED && !isSoloAction()) target.setWalkSpeed(CDA_WALK_SPEED);
    }

    protected boolean isActionCanceled() {
        boolean isSneaking = player.isSneaking() || target.isSneaking();
        boolean isLaunchItemInitial = player.getInventory().getItemInMainHand().equals(launchItem);
        boolean isTargetNearby = player.getNearbyEntities(1.65f, 0.5f, 1.65f).contains(target);
        boolean isInAction = isPlayerInCountDownAction(player) || isPlayerInCountDownAction(target);
        boolean isOnline = player.isOnline() || target.isOnline();
        boolean isDead = player.isDead() || target.isDead();
        boolean isLeaveFromStartLocation = !(new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()).equals(startLocation));

        return (isSneaking || !isLaunchItemInitial || !isTargetNearby || !isInAction || !isOnline || isDead || isLeaveFromStartLocation) && !preprocessActionComplete;
    }

    protected String getTextWithInsertedProgressBar(String toInsert, String pb, String percent) {
        return ChatColor.translateAlternateColorCodes('&', toInsert.replace("%progress_bar%", pb).replace("%percent%", percent));
    }

    protected boolean isPreprocessActionComplete() {
        return preprocessActionComplete;
    }

    protected boolean isSoloAction() {
        return target.equals(player);
    }

    protected void actionCountDown() {
        if (isPlayerInCountDownAction(player) || isPlayerInCountDownAction(target)) return;

        PreprocessActionHandlers.put(player, target);

        final float ACTION_TIME = seconds * 20;
        new BukkitRunnable() {
            int currentTicks = (int) ACTION_TIME;
            @Override
            public void run() {
                if (isActionCanceled()) {
                    cancelAction(true);
                    cancel();
                    return;
                }

                if (currentTicks == 0) {
                    preprocessActionComplete = true;
                    PreprocessActionHandlers.remove(player);
                    if (isDoubleRunnableAction) DoubleRunnableActionHandlers.put(player, target);
                    sendActionBarMsg(player, completeBothActionBarMsg);
                    player.sendTitle(completePlayerTitle, completePlayerSubtitle, 0, 75, 15);
                    if (!isSoloAction()) sendActionBarMsg(target, completeBothActionBarMsg);
                    if (!isSoloAction()) target.sendTitle(completeTargetTitle, completeTargetSubtitle, 0, 75, 15);
                    cancel();
                    return;
                }

                currentTicks--;
                ticksLeft = currentTicks;
                if (isClosePlayersInventoriesEveryTick) {
                    player.closeInventory();
                    if (!isSoloAction()) target.closeInventory();
                }

                pb.setCurrent((int)ACTION_TIME - currentTicks);
                pb.setMax((int)ACTION_TIME);
                String stringedBar = pb.getProgressBar();
                String percent = pb.getPercent();

                sendActionBarMsg(player, getTextWithInsertedProgressBar(everyTickBothActionBarMsg, stringedBar, percent));
                if (!isSoloAction()) sendActionBarMsg(target, getTextWithInsertedProgressBar(everyTickBothActionBarMsg, stringedBar, percent));
                player.sendTitle(getTextWithInsertedProgressBar(everyTickPlayerTitle, stringedBar, percent), getTextWithInsertedProgressBar(everyTickPlayerSubtitle, stringedBar, percent), 0, 20, 0);
                if (!isSoloAction()) target.sendTitle(getTextWithInsertedProgressBar(everyTickTargetTitle, stringedBar, percent), getTextWithInsertedProgressBar(everyTickTargetSubtitle, stringedBar, percent), 0, 20, 0);
                reducePlayersWalkSpeed();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    protected Player getPlayer() {return player;}
    protected Player getTarget() {return target;}
    protected ItemStack getLaunchItem() {return launchItem;}
    protected Location getStartLocation() {return startLocation;}
    protected int getTicksLeft() {return ticksLeft;}
    protected ProgressBar getProgressBar() {return pb;}
}
