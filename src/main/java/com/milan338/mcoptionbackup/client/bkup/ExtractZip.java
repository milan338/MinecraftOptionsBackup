package com.milan338.mcoptionbackup.client.bkup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.milan338.mcoptionbackup.Main;

import net.minecraft.crash.CrashReport;

public class ExtractZip {

    public static void extractFromFile() throws IOException {
        @SuppressWarnings("resource")
        String zipFilePath = GetFile.selectFile();
        String destDir = Main.newdir + "config";

        // Ensure user had selected file
        if (zipFilePath != null) {
            unzip(zipFilePath, destDir);
        }
    }

    private static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists())
            dir.mkdirs();

        String[] rootFiles = { "options.txt", "optionsof.txt", "optionsshaders.txt", ".ReAuth.cfg", "servers.dat" };

        byte[] buffer = new byte[1024];

        try (FileInputStream fis = new FileInputStream(zipFilePath); ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                // Check if config file was in sub-directory
                if (ze.getName().contains(".in-")) {
                    String[] split = ze.getName().split(".in-");
                    File splitdir = new File(Main.newdir + "/config/" + split[1]);
                    if (!splitdir.exists())
                        splitdir.mkdirs();
                    else {
                        File newFile = new File(splitdir.getAbsolutePath() + "/" + split[0]);
                        // Debug
                        System.out.println("Unzipping to " + newFile.getAbsolutePath());

                        try (FileOutputStream fos = new FileOutputStream(newFile)) {
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                        ze = zis.getNextEntry();
                    }
                } else {
                    File newFile = new File(destDir + File.separator + fileName);
                    // Should config file be placed in root dir
                    if (Arrays.asList(rootFiles).contains(fileName)) {
                        newFile = new File(Main.newdir + File.separator + fileName);
                    } else {
                        newFile = new File(destDir + File.separator + fileName);
                    }
                    // Debug
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());

                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    ze = zis.getNextEntry();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close Minecraft and provide message to player
        net.minecraft.client.Minecraft.getMinecraft()
                .crashed(new CrashReport("Restart your game for configs to update", new Throwable()));
    }

}