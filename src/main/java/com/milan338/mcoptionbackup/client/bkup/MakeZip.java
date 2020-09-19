package com.milan338.mcoptionbackup.client.bkup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.milan338.mcoptionbackup.Main;

public class MakeZip {

    public static void saveToFile() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String zipFile = (Main.newdir + "backups/backup--" + formatter.format(new Date()) + ".mcconfigs");
        String srcDir = Main.newdir + "config";
        // Make backups folder if non-existent
        File f = new File(Main.newdir + "backups");
        if (!f.exists())
            f.mkdirs();

        try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {
            byte[] buffer = new byte[1024];
            File dir = new File(srcDir);

            if (!dir.exists())
                dir.mkdirs();
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                // If folder is found
                if (files[i].isDirectory()) {
                    File[] folder = files[i].listFiles();

                    for (File subFile : folder) {
                        /**
                         * Ignore further sub-directories Mods generally shouldn't be storing any user
                         * configs in further sub-directories
                         */
                        if (!subFile.isDirectory()) {
                            System.out.println("Adding file: " + subFile.getName());

                            try (FileInputStream fis = new FileInputStream(subFile)) {
                                // Add custom file extension to file for unzip process
                                zos.putNextEntry(new ZipEntry(subFile.getName() + ".in--" + files[i].getName()));
                                int len;
                                while ((len = fis.read(buffer)) > 0) {
                                    zos.write(buffer, 0, len);
                                }
                            }
                        }
                    }
                }
                // If file is not folder
                else {
                    System.out.println("Adding file: " + files[i].getName());

                    try (FileInputStream fis = new FileInputStream(files[i])) {
                        // Begin writing new ZIP entry; position stream to entry data start
                        zos.putNextEntry(new ZipEntry(files[i].getName()));
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                }
            }
            String[] cfgs = { "options.txt", "optionsof.txt", "optionshaders.txt", ".ReAuth.cfg", "servers.dat" };
            // Add additional configs from root dir
            for (String cfg : cfgs) {
                if (new File(Main.newdir + cfg).exists()) {
                    // Debug
                    System.out.println("Adding file " + cfg);
                    try (FileInputStream fis = new FileInputStream(new File(Main.newdir + cfg))) {
                        zos.putNextEntry(new ZipEntry(cfg));
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("Error creating zip file" + ioe);
        }
    }
}