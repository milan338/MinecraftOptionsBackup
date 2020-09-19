package com.milan338.mcoptionbackup.client.bkup;

import com.milan338.mcoptionbackup.Main;

import java.awt.FileDialog;
import java.awt.Frame;

public class GetFile {

    public static String selectFile() {
        FileDialog dialog = new FileDialog((Frame) null, "Select Config Backup File", FileDialog.LOAD);
        dialog.setFile("*.mcconfigs");
        dialog.setDirectory((Main.newdir + "backups").replace("/", "\\"));
        dialog.setVisible(true);

        // Check if user selected file
        if (dialog.getFiles().length != 0) {
            return (dialog.getFiles()[0].getAbsolutePath());
        }
        return null;
    }

}