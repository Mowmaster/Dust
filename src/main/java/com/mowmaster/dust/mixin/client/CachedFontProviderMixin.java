package com.mowmaster.dust.mixin.client;

import com.mowmaster.dust.Features.AncientLanguage.AncientLanguage;
import com.mowmaster.dust.Features.AncientLanguage.Client.AncientGlyphSource;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.network.chat.FontDescription;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// A mixin changes a Minecraft class while the game loads. This one adds a few lines to the start of
// FontManager.CachedFontProvider#getGlyphSource, the method every text renderer uses to turn a font name into the
// glyphs to draw. When it's asked for "dust:ancient", it answers with an AncientGlyphSource that picks, per letter,
// the ancient glyph or the normal one depending on what the local player has learned.
//
// Why a mixin: there's no NeoForge event for fonts, and doing it per screen (tooltips, signs, books, chat...) would
// miss places. Without this mixin the font still works, it just never reveals anything.
// CachedFontProvider is a private inner class, so it's named by string ("$" separates inner classes).
@Mixin(targets = "net.minecraft.client.gui.font.FontManager$CachedFontProvider")
public abstract class CachedFontProviderMixin
{
    // @Shadow = "this method already exists in the target class, let me call it"
    @Shadow
    private GlyphSource getGlyphSource(FontDescription description)
    {
        throw new AssertionError();
    }

    // Runs at the start (HEAD) of getGlyphSource. cir.setReturnValue returns our answer instead of running the rest.
    @Inject(method = "getGlyphSource", at = @At("HEAD"), cancellable = true)
    private void dust$ancientFont(FontDescription description, CallbackInfoReturnable<GlyphSource> cir)
    {
        if (AncientLanguage.FONT.equals(description)) {
            // These two lookups are for other fonts, so they don't come back through this branch
            GlyphSource ancient = this.getGlyphSource(AncientLanguage.GLYPHS);
            GlyphSource plain = this.getGlyphSource(FontDescription.DEFAULT);
            cir.setReturnValue(new AncientGlyphSource(ancient, plain));
        }
    }
}
