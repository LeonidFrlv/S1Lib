package org.s1queence.api.countdown;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.s1queence.api.countdown.progressbar.ProgressBar;

import java.util.HashMap;
import java.util.Map;

import static org.s1queence.api.S1TextUtils.getTextWithInsertedPlayerName;
import static org.s1queence.api.S1Utils.sendActionBarMsg;

public class CountDownAction {
    public static final Float CDA_WALK_SPEED = 0.040013135F;
    private final Player player;
    private final Player target;
    private final JavaPlugin plugin;
    private final ItemStack launchItem;
    private final float initialPlayerSpeed;
    private final float initialTargetSpeed;
    private static final Map<Player, Player> PreprocessActionHandlers = new HashMap<>();
    private static final Map<Player, Player> DoubleRunnableActionHandlers = new HashMap<>();
    private final boolean isDoubleRunnableAction;
    private final String everyTickBothActionBarMsg;
    private final String everyTickTargetTitle;
    private final String everyTickTargetSubtitle;
    private final String everyTickPlayerTitle;
    private final String everyTickPlayerSubtitle;
    private final String completeBothActionBarMsg;
    private final String completePlayerTitle;
    private final String completePlayerSubtitle;
    private final String completeTargetTitle;
    private final String completeTargetSubtitle;
    private final String cancelBothActionBarMsg;
    private final String cancelPlayerTitle;
    private final String cancelPlayerSubtitle;
    private final String cancelTargetTitle;
    private final String cancelTargetSubtitle;
    private final int seconds;
    private final org.s1queence.api.countdown.progressbar.ProgressBar pb;

    public CountDownAction(
            @NotNull Player player,
            @NotNull Player target,
            int seconds,
            boolean isDoubleRunnableAction,
            @NotNull org.s1queence.api.countdown.progressbar.ProgressBar pb,
            @NotNull JavaPlugin plugin,

            @NotNull String everyTickBothActionBarMsg,
            @NotNull String everyTickPlayerTitle,
            @NotNull String everyTickPlayerSubtitle,
            @NotNull String everyTickTargetTitle,
            @NotNull String everyTickTargetSubtitle,

            @NotNull String completeBothActionBarMsg,
            @NotNull String completePlayerTitle,
            @NotNull String completePlayerSubtitle,
            @NotNull String completeTargetTitle,
            @NotNull String completeTargetSubtitle,

            @NotNull String cancelBothActionBarMsg,
            @NotNull String cancelPlayerTitle,
            @NotNull String cancelPlayerSubtitle,
            @NotNull String cancelTargetTitle,
            @NotNull String cancelTargetSubtitle
        )
    {
        this.player = player;
        this.target = target;
        this.seconds = seconds;
        this.pb = pb;
        this.isDoubleRunnableAction = isDoubleRunnableAction;
        this.plugin = plugin;
        this.launchItem = player.getInventory().getItemInMainHand();
        this.initialPlayerSpeed = player.getWalkSpeed();
        this.initialTargetSpeed = target.getWalkSpeed();

        this.everyTickBothActionBarMsg = everyTickBothActionBarMsg;
        this.completeBothActionBarMsg = completeBothActionBarMsg;
        this.cancelBothActionBarMsg = cancelBothActionBarMsg;

        String pName = player.getName();
        this.everyTickTargetTitle = getTextWithInsertedPlayerName(everyTickTargetTitle, pName);
        this.everyTickTargetSubtitle = getTextWithInsertedPlayerName(everyTickTargetSubtitle, pName);
        this.completeTargetTitle = getTextWithInsertedPlayerName(completeTargetTitle, pName);
        this.completeTargetSubtitle = getTextWithInsertedPlayerName(completeTargetSubtitle, pName);
        this.cancelTargetTitle = getTextWithInsertedPlayerName(cancelTargetTitle, pName);
        this.cancelTargetSubtitle = getTextWithInsertedPlayerName(cancelTargetSubtitle, pName);

        String tName = target.getName();
        this.everyTickPlayerTitle = getTextWithInsertedPlayerName(everyTickPlayerTitle, tName);
        this.everyTickPlayerSubtitle = getTextWithInsertedPlayerName(everyTickPlayerSubtitle, tName);
        this.completePlayerTitle = getTextWithInsertedPlayerName(completePlayerTitle, tName);
        this.completePlayerSubtitle = getTextWithInsertedPlayerName(completePlayerSubtitle, tName);
        this.cancelPlayerTitle = getTextWithInsertedPlayerName(cancelPlayerTitle, tName);
        this.cancelPlayerSubtitle = getTextWithInsertedPlayerName(cancelPlayerSubtitle, tName);
    }

    public static Map<Player, Player> getPreprocessActionHandlers() { // reload делать не забудь этой фигни и через плагины другие тоже чтобы релодилось, когда их релодишь (типо чтобы не надо было библиотеку релодить вместе с плагином)
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
            target.sendTitle(cancelTargetTitle, cancelTargetSubtitle, 0, 75, 20);
            sendActionBarMsg(player, cancelBothActionBarMsg);
            sendActionBarMsg(target, cancelBothActionBarMsg);
        }

        player.setWalkSpeed(initialPlayerSpeed);
        target.setWalkSpeed(initialTargetSpeed);
        player.closeInventory();
        target.closeInventory();
    }

    protected void reducePlayersWalkSpeed() {
        if (initialPlayerSpeed > CDA_WALK_SPEED) player.setWalkSpeed(CDA_WALK_SPEED);
        if (initialTargetSpeed > CDA_WALK_SPEED) target.setWalkSpeed(CDA_WALK_SPEED);
    }

    protected boolean isActionCanceled() {
        boolean isSneaking = player.isSneaking() || target.isSneaking();
        boolean isLaunchItemInitial = player.getInventory().getItemInMainHand().equals(launchItem);
        boolean isTargetNearby = player.getNearbyEntities(1.65f, 0.5f, 1.65f).contains(target);
        boolean isInAction = isPlayerInCountDownAction(player) || isPlayerInCountDownAction(target);
        boolean isOnline = player.isOnline() || target.isOnline();
        boolean isDead = player.isDead() || target.isDead();

        return isSneaking || !isLaunchItemInitial || !isTargetNearby || !isInAction || !isOnline || isDead;
    }

    protected String getTextWithInsertedProgressBar(String toInsert, String pb, String percent) {
        return ChatColor.translateAlternateColorCodes('&', toInsert.replace("%progress_bar%", pb).replace("%percent%", percent));
    }

    protected void actionCountDown() {
        if (isPlayerMakingSoloCDAction(target)) {
            PreprocessActionHandlers.remove(target);
            DoubleRunnableActionHandlers.remove(target);
        }

        if (isPlayerInCountDownAction(player) || isPlayerInCountDownAction(target)) return;

        reducePlayersWalkSpeed();
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
                    PreprocessActionHandlers.remove(player);
                    if (isDoubleRunnableAction) DoubleRunnableActionHandlers.put(player, target);
                    sendActionBarMsg(player, completeBothActionBarMsg);
                    sendActionBarMsg(target, completeBothActionBarMsg);

                    target.sendTitle(completeTargetTitle, completeTargetSubtitle, 0, 75, 15);
                    player.sendTitle(completePlayerTitle, completePlayerSubtitle, 0, 75, 15);

                    cancel();
                    return;
                }

                currentTicks--;
                player.closeInventory();
                target.closeInventory();

                pb.setCurrent((int)ACTION_TIME - currentTicks);
                pb.setMax((int)ACTION_TIME);
                String stringedBar = pb.getProgressBar();
                String percent = pb.getPercent();

                sendActionBarMsg(player, getTextWithInsertedProgressBar(everyTickBothActionBarMsg, stringedBar, percent));
                sendActionBarMsg(target, getTextWithInsertedProgressBar(everyTickBothActionBarMsg, stringedBar, percent));
                player.sendTitle(getTextWithInsertedProgressBar(everyTickPlayerTitle, stringedBar, percent), getTextWithInsertedProgressBar(everyTickPlayerSubtitle, stringedBar, percent), 0, 20, 0);
                target.sendTitle(getTextWithInsertedProgressBar(everyTickTargetTitle, stringedBar, percent), getTextWithInsertedProgressBar(everyTickTargetSubtitle, stringedBar, percent), 0, 20, 0);
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    protected Player getPlayer() {return player;}
    protected Player getTarget() {return target;}
    protected ProgressBar getProgressBar() {return pb;}
}
