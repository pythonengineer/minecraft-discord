package com.mojang.minecraft.player;

import com.mojang.minecraft.mob.ai.BasicAI;

/**
 * aka Player$1
 */
final class PlayerInput extends BasicAI {
    private Input input;

    PlayerInput(Player player, Input input) {
        this.input = input;
    }

    protected final void tick() {
        this.jumping = this.input.jumping;
        this.xxa = this.input.ya;
        this.yya = this.input.xa;
    }
}