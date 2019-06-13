package tschallacka.mods.rats.render.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tschallacka.mods.rats.entity.EntityRat;
import tschallacka.mods.rats.render.model.entity.ModelRat;

import javax.annotation.Nonnull;

public class RenderRat extends RenderLiving<EntityRat> {

    private ResourceLocation mobTexture = new ResourceLocation("sewerrats:textures/entity/ratentity.png");

    public static final Factory FACTORY = new Factory();

    public RenderRat(RenderManager rendermanagerIn) {
        // We use the vanilla zombie model here and we simply
        // retexture it. Of course you can make your own model
        super(rendermanagerIn, new ModelRat(), 0.5F);
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityRat entity) {
        return mobTexture;
    }

    public static class Factory implements IRenderFactory<EntityRat> {

        @Override
        public Render<? super EntityRat> createRenderFor(RenderManager manager) {
            return new RenderRat(manager);
        }

    }

}
