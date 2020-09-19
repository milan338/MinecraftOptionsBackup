package com.milan338.mcoptionbackup.client.gui;

import java.io.IOException;

import com.milan338.mcoptionbackup.client.bkup.ExtractZip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ConfirmImport extends GuiScreen {

    private GuiButton proceed;
    private GuiButton back;
    private final GuiScreen previousGuiScreen;

    public ConfirmImport(GuiScreen previousGuiScreen) {
        this.previousGuiScreen = previousGuiScreen;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList
                .add(proceed = new GuiButton(704, this.width / 2 - 204, this.height / 2 + this.height / 6, "Proceed"));
        this.buttonList.add(back = new GuiButton(705, this.width / 2, this.height / 2 + this.height / 6, "Back"));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();

        FontRenderer fontRenderer = this.fontRendererObj;

        drawCenteredString(fontRenderer, "IMPORTING OPTIONS WILL CAUSE YOUR GAME TO RESTART", width / 2,
                height / 2 - height / 6, -1);
        drawCenteredString(fontRenderer, "ALL EXISTING CONFIGS WILL BE OVERWRITTEN", width / 2, height / 2 - height / 9,
                -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == back) {
            Minecraft.getMinecraft().displayGuiScreen(previousGuiScreen);
        } else if (button == proceed) {
            if (Minecraft.getMinecraft().isFullScreen())
                Minecraft.getMinecraft().toggleFullscreen();
            ExtractZip.extractFromFile();
        }
    }

}