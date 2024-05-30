package dev.crmodders.puzzle.core.minimixin;

import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.service.IClassProvider;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import org.spongepowered.asm.service.MixinService;

import java.lang.annotation.Annotation;

public class PuzzleTransformerHandle implements ILegacyClassTransformer {
    /**
     * Wrapped transformer
     */
    private final IClassTransformer transformer;

    PuzzleTransformerHandle(IClassTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public String getName() {
        return this.transformer.getClass().getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isDelegationExcluded() {
        try {
            IClassProvider classProvider = MixinService.getService().getClassProvider();
            Class<? extends Annotation> clResource = (Class<? extends Annotation>)classProvider.findClass("javax.annotation.Resource");
            return this.transformer.getClass().getAnnotation(clResource) != null;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.spongepowered.asm.service.ILegacyClassTransformer
     *      #transformClassBytes(java.lang.String, java.lang.String, byte[])
     */
    @Override
    public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
        return this.transformer.transform(name, transformedName, basicClass);
    }
}
