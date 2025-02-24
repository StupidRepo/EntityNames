package io.github.stupidrepo.EntityNames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(EntityNames.MODID)
public class EntityNames
{
    public static final String MODID = "entitynames";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EntityNames(ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    private void renderEntitiesEvent(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack pose = event.getPoseStack();

        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            HitResult hit = mc.hitResult;
            if(hit != null && hit.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hit).getEntity();
                if(!(entity instanceof Player) && (entity instanceof LivingEntity) && !entity.hasCustomName()) {
                    renderTextOverEntity(pose, mc.renderBuffers().bufferSource(), getEntityRandomName(entity), entity);
                }
            }
        }
    }

    private static void renderTextOverEntity(PoseStack poseStack, MultiBufferSource buffer, String text, Entity entity) {
        double x = entity.getX();
        double y = entity.getY() + entity.getBoundingBox().getYsize() + 0.2;
        double z = entity.getZ();

        DebugRenderer.renderFloatingText(poseStack,
                buffer,
                text,
                x, y, z,
                Config.textColor.getRGB(),
                0.02F,
                true,
                0.0F,
                true
        );
    }

    public static String getEntityRandomName(Entity entity) {
        var random = RandomSource.create(entity.getUUID().hashCode() >> 2);

        var firstName = Util.getRandom(Config.firstNames, random);
        var lastName = Util.getRandom(Config.lastNames, random);

        var nickname = DebugEntityNameGenerator.getEntityName(entity);

        return switch(Config.displayFormat) {
            case FIRST_NAME_ONLY -> firstName;
            case FIRST_AND_LAST_NAME -> String.format("%s %s", firstName, lastName);
	        case FIRST_LAST_AND_NICKNAME -> String.format("%s \"%s\" %s", firstName, nickname, lastName);
	        case NICKNAME_ONLY -> nickname;
        };
    }
}
