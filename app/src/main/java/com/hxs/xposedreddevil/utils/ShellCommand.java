package com.hxs.xposedreddevil.utils;

import java.io.DataOutputStream;
import java.io.IOException;

public class ShellCommand {

    public static boolean shellCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
}
