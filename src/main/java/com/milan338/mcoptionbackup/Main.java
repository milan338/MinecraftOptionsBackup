package com.milan338.mcoptionbackup;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.milan338.mcoptionbackup.client.bkup.MakeZip;
import com.milan338.mcoptionbackup.client.gui.ConfirmImport;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Main.MODID, version = Main.VERSION, clientSideOnly = true, useMetadata = true)
public class Main {

    public static final String MODID = "mcoptionsbackup";
    public static final String VERSION = "1.0.0";

    public static String directory = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    public static String newdir = directory.replace("\\", "/") + "/";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiOptions) {
            GuiButton bkupButton = new GuiButton(701, 5, 5, "Backup Options");
            GuiButton importButton = new GuiButton(702, 5, 29, "Import Options");
            GuiButton openBkups = new GuiButton(703, 5, 53, "Backups Folder");

            event.buttonList.add(bkupButton);
            event.buttonList.add(importButton);
            event.buttonList.add(openBkups);

            for (GuiButton button : event.buttonList) {
                if (button.id == 701 || button.id == 702 || button.id == 703) {
                    button.setWidth(100);
                }
            }
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) throws IOException {
        if (event.gui instanceof GuiOptions) {
            if (event.button.id == 701)
                MakeZip.saveToFile();
            else if (event.button.id == 702)
                Minecraft.getMinecraft().displayGuiScreen(new ConfirmImport(event.gui));
            else if (event.button.id == 703 && Desktop.isDesktopSupported()) {
                // Ensure backups folder exists
                File f = new File(Main.newdir + "backups");
                if (!f.exists())
                    f.mkdirs();
                Desktop.getDesktop().open(new File(newdir + "backups"));
                if (Minecraft.getMinecraft().isFullScreen())
                    Minecraft.getMinecraft().toggleFullscreen();
            }
        }
    }

}
