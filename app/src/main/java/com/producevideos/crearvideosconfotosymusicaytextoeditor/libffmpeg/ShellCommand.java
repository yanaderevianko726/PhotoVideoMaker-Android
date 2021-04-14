package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg;

import java.io.IOException;

class ShellCommand {
    ShellCommand() {
    }

    Process run(String[] commandString) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commandString);
        } catch (IOException e) {
            Log.m21e("Exception while trying to run: " + commandString, e);
        }
        return process;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.CommandResult runWaitFor(String[] r7) {
        /*
        r6 = this;
        r3 = r6.run(r7);
        r1 = 0;
        r2 = 0;
        if (r3 == 0) goto L_0x0021;
    L_0x0008:
        r4 = r3.waitFor();	 Catch:{ InterruptedException -> 0x0037 }
        r1 = java.lang.Integer.valueOf(r4);	 Catch:{ InterruptedException -> 0x0037 }
        r4 = com.androworld.photovideomaker.libffmpeg.CommandResult.success(r1);	 Catch:{ InterruptedException -> 0x0037 }
        if (r4 == 0) goto L_0x002e;
    L_0x0016:
        r4 = r3.getInputStream();	 Catch:{ InterruptedException -> 0x0037 }
        r2 = com.androworld.photovideomaker.libffmpeg.Util.convertInputStreamToString(r4);	 Catch:{ InterruptedException -> 0x0037 }
    L_0x001e:
        com.androworld.photovideomaker.libffmpeg.Util.destroyProcess(r3);
    L_0x0021:
        com.androworld.photovideomaker.libffmpeg.Util.destroyProcess(r3);
        r4 = new com.androworld.photovideomaker.libffmpeg.CommandResult;
        r5 = com.androworld.photovideomaker.libffmpeg.CommandResult.success(r1);
        r4.<init>(r5, r2);
        return r4;
    L_0x002e:
        r4 = r3.getErrorStream();	 Catch:{ InterruptedException -> 0x0037 }
        r2 = com.androworld.photovideomaker.libffmpeg.Util.convertInputStreamToString(r4);	 Catch:{ InterruptedException -> 0x0037 }
        goto L_0x001e;
    L_0x0037:
        r0 = move-exception;
        r4 = "Interrupt exception";
        com.androworld.photovideomaker.libffmpeg.Log.m21e(r4, r0);	 Catch:{ all -> 0x0041 }
        com.androworld.photovideomaker.libffmpeg.Util.destroyProcess(r3);
        goto L_0x0021;
    L_0x0041:
        r4 = move-exception;
        com.androworld.photovideomaker.libffmpeg.Util.destroyProcess(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.androworld.photovideomaker.libffmpeg.ShellCommand.runWaitFor(java.lang.String[]):com.androworld.photovideomaker.libffmpeg.CommandResult");
    }
}
