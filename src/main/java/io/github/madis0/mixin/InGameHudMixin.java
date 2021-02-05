package io.github.madis0.mixin;

import io.github.madis0.FakeBossBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    private MinecraftClient client;
    private MatrixStack stack;
    private PlayerEntity playerEntity;
    private HungerManager hungerManager;
    private ClientBossBar clientBossBar;

    boolean showVanilla = false;
    boolean showOneBar = true;
    boolean showArmor = true;
    boolean showJump = true;

    int baseStartW;
    int baseEndW;
    int baseStartH;
    int baseEndH;
    int xpStartW;
    int xpEndW;
    int xpStartH;
    int xpEndH;
    int jumpStartW;
    int jumpEndW;
    int jumpStartH;
    int jumpEndH;

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        client = MinecraftClient.getInstance();
        stack = matrixStack;
        playerEntity = this.getCameraPlayer();
        hungerManager = playerEntity.getHungerManager();

        baseStartW = this.scaledWidth / 2 - 91;
        baseEndW = baseStartW + 182;
        baseStartH = this.scaledHeight - 33;
        baseEndH = baseStartH + 9;

        xpStartW = baseEndW + 2;
        xpEndW = xpStartW + 18;
        xpStartH = baseStartH + 28;
        xpEndH = xpStartH + 1;

        jumpStartW = (this.scaledWidth / 2) - 2;
        jumpStartH = (this.scaledHeight / 2) + 10;
        jumpEndW = jumpStartW + 3;
        jumpEndH = jumpStartH + 50;

        if(showOneBar && !playerEntity.isSpectator() && !playerEntity.isCreative()) renderBar();
        if(showOneBar && showArmor && !playerEntity.isSpectator() && !playerEntity.isCreative()) armorBar();

        mountBossBar();

    }

    //TODO: overwrite only on-demand

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE"), cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci){
        if(!showVanilla) ci.cancel();
    }
    @Inject(method = "renderExperienceBar", at = @At(value = "INVOKE"), cancellable = true)
    public void renderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        if(!showVanilla) ci.cancel();
    }

    @Inject(method = "renderMountJumpBar", at = @At(value = "INVOKE"), cancellable = true)
    public void renderMountJumpBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if(!showVanilla) ci.cancel();
        if(showOneBar && showJump) jumpBar();
    }

    private void renderBar(){
        barBackground();
        healthBar();
        hungerBar();
        airBar();
        xpBar();
        barText();
    }

    private void barBackground(){
        int backgroundColor = 0xFF000000;

        DrawableHelper.fill(stack, baseStartW, baseStartH, baseEndW, baseEndH, backgroundColor);
        DrawableHelper.fill(stack, xpStartW, xpStartH, xpEndW, xpEndH, backgroundColor);
    }

    private void healthBar(){
        float rawHealth = playerEntity.getHealth();
        float maxHealth = playerEntity.getMaxHealth();

        int healthColor = 0xFFD32F2F;
        DrawableHelper.fill(stack, baseStartW, baseStartH, baseRelativeEndW(getPreciseInt(rawHealth), getPreciseInt(maxHealth)), baseEndH, healthColor);
    }

    private void armorBar(){
        int maxArmor = 20;
        int armor = playerEntity.getArmor();

        int armorColor = 0x99FFFFFF;
        DrawableHelper.fill(stack, baseStartW, baseStartH - 1, baseRelativeEndW(armor, maxArmor), baseStartH, armorColor);
    }

    private void hungerBar(){
        int maxHunger = 20;
        int hunger = maxHunger - hungerManager.getFoodLevel();
        //float saturation = hungerManager.getSaturationLevel(); //TODO: usage TBD

        int hungerColor = 0xBB3E2723;
        DrawableHelper.fill(stack, baseRelativeStartW(hunger, maxHunger), baseStartH, baseEndW, baseEndH, hungerColor);
    }

    private void airBar(){
        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();

        int airColor = 0xBB1A237E;
        DrawableHelper.fill(stack, baseRelativeStartW(rawAir, maxAir), baseStartH, baseEndW, baseEndH, airColor);
    }

    private void barText(){
        int health = MathHelper.ceil(playerEntity.getHealth());
        int maxHunger = 20;

        int hunger = maxHunger - hungerManager.getFoodLevel();

        int maxAir = playerEntity.getMaxAir();
        int rawAir = maxAir - playerEntity.getAir();
        int air = rawAir / 15;

        boolean hardcore = playerEntity.world.getLevelProperties().isHardcore();

        String value;
        if (hunger < 1 && air < 1)
            value = String.valueOf(health);
        else if(hunger >= 1 && air < 1)
            value = health + "-" + hunger;
        else
            value = health + "-A" + air;

        if(hardcore) value = value + "!";
        int textColor = 0x99FFFFFF;
        client.textRenderer.draw(stack, value, baseEndW - client.textRenderer.getWidth(value), baseStartH + 1, textColor);
    }

    private void xpBar(){
        int xpLevel = playerEntity.experienceLevel;
        int maxXp = 183;
        int xp = (int)(playerEntity.experienceProgress * maxXp);

        int xpColor = 0xFF00C853;
        int relativeEndW = relativeW(xpStartW, xpEndW, xp, maxXp);

        int textX = xpStartW + 3;
        int textY = xpStartH - 10;

        DrawableHelper.fill(stack, xpStartW, xpStartH, relativeEndW, xpEndH, xpColor);
        client.textRenderer.drawWithShadow(stack, String.valueOf(xpLevel), textX, textY, xpColor);
    }

    private void jumpBar(){
        if (client.player == null) throw new AssertionError();

        int maxHeight = getPreciseInt(1.0F);
        int height = getPreciseInt(client.player.method_3151());

        int jumpColor = 0xFF795548;
        int backgroundColor = 0xFF000000;

        int relativeStartH = relativeW(jumpEndH, jumpStartH, height, maxHeight);
        DrawableHelper.fill(stack, jumpStartW, jumpStartH, jumpEndW, jumpEndH, backgroundColor);
        DrawableHelper.fill(stack, jumpStartW, jumpEndH, jumpEndW, relativeStartH, jumpColor);
    }

    private int relativeW(int start, int end, int value, int total){
        if(value < total)
            return MathHelper.ceil(start + ((float)(end - start) / total * value));
        else
            return end;
    }

    private int baseRelativeEndW(int value, int total){
        return relativeW(baseStartW, baseEndW, value, total);
    }

    private int baseRelativeStartW(int value, int total){
        return relativeW(baseEndW, baseStartW, value, total);
    }

    private void debugText(String value){
        client.textRenderer.drawWithShadow(stack, value, baseEndW + 15, baseStartH + 1, 0xFFFFFFFF);
    }

    private int getPreciseInt(float number){
        float precision = 10000.0F;
        return MathHelper.ceil(number * precision);
    }

    private void mountBossBar(){
        LiteralText name = new LiteralText("piggy");
        BossBar bossBar = new FakeBossBar(UUID.randomUUID(), name, BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
        BossBarS2CPacket packet = new BossBarS2CPacket(BossBarS2CPacket.Type.ADD, bossBar);
        clientBossBar = new ClientBossBar(packet);
        clientBossBar.setPercent(60);

    }
}
