package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum EnumElement implements StringRepresentable {
    FIRE("fire"),
    WATER("water"),
    EARTH("earth"),
    CHAOS("chaos"),
    ORDER("order");

    private final String name;
    EnumElement(String name) { this.name = name; }
    @Override public @NotNull String getSerializedName() { return name; }

    public static final com.mojang.serialization.Codec<EnumElement> CODEC =
            StringRepresentable.fromEnum(EnumElement::values);

    public static EnumElement safeValueOf(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null); // Or return a default enum constant
    }

    public int removeElement(ServerPlayer player, int value, boolean simulate) {
        switch (this) {
            case FIRE -> {return DustElementAttachmentHelper.removeFromElementFire(player, value, simulate);}
            case WATER -> {return DustElementAttachmentHelper.removeFromElementWater(player, value, simulate);}
            case EARTH -> {return DustElementAttachmentHelper.removeFromElementEarth(player, value, simulate);}
            case CHAOS -> {return DustElementAttachmentHelper.removeFromElementChaos(player, value, simulate);}
            case ORDER -> {return DustElementAttachmentHelper.removeFromElementOrder(player, value, simulate);}
            default -> {return 0;}
        }
    }

    public int addElement(ServerPlayer player, int value, boolean simulate) {
        switch (this) {
            case FIRE -> {return DustElementAttachmentHelper.addToElementFire(player, value, simulate);}
            case WATER -> {return DustElementAttachmentHelper.addToElementWater(player, value, simulate);}
            case EARTH -> {return DustElementAttachmentHelper.addToElementEarth(player, value, simulate);}
            case CHAOS -> {return DustElementAttachmentHelper.addToElementChaos(player, value, simulate);}
            case ORDER -> {return DustElementAttachmentHelper.addToElementOrder(player, value, simulate);}
            default -> {return 0;}
        }
    }

    public int getElementCount(ServerPlayer player) {
        switch (this) {
            case FIRE -> {return DustElementAttachmentHelper.getElementInfoFire(player).count();}
            case WATER -> {return DustElementAttachmentHelper.getElementInfoWater(player).count();}
            case EARTH -> {return DustElementAttachmentHelper.getElementInfoEarth(player).count();}
            case CHAOS -> {return DustElementAttachmentHelper.getElementInfoChaos(player).count();}
            case ORDER -> {return DustElementAttachmentHelper.getElementInfoOrder(player).count();}
            default -> {return 0;}
        }
    }

    public int getElementMaxCount(ServerPlayer player) {
        switch (this) {
            case FIRE -> {return DustElementAttachmentHelper.getElementInfoFire(player).max();}
            case WATER -> {return DustElementAttachmentHelper.getElementInfoWater(player).max();}
            case EARTH -> {return DustElementAttachmentHelper.getElementInfoEarth(player).max();}
            case CHAOS -> {return DustElementAttachmentHelper.getElementInfoChaos(player).max();}
            case ORDER -> {return DustElementAttachmentHelper.getElementInfoOrder(player).max();}
            default -> {return 0;}
        }
    }

}

