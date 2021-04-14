package com.producevideos.crearvideosconfotosymusicaytextoeditor.soundfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class CheapSoundFile {
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    static HashMap<String, Factory> sExtensionMap = new HashMap();
    static Factory[] sSubclassFactories = new Factory[]{CheapAAC.getFactory(), CheapAMR.getFactory(), CheapMP3.getFactory(), CheapWAV.getFactory()};
    static ArrayList<String> sSupportedExtensions = new ArrayList();
    protected File mInputFile = null;
    protected ProgressListener mProgressListener = null;

    public interface Factory {
        CheapSoundFile create();

        String[] getSupportedExtensions();
    }

    public interface ProgressListener {
        boolean reportProgress(double d);
    }

    static {
        for (Factory f : sSubclassFactories) {
            for (String extension : f.getSupportedExtensions()) {
                sSupportedExtensions.add(extension);
                sExtensionMap.put(extension, f);
            }
        }
    }

    public static CheapSoundFile create(String fileName, ProgressListener progressListener) throws FileNotFoundException, IOException {
        File f = new File(fileName);
        if (f.exists()) {
            String[] components = f.getName().toLowerCase().split("\\.");
            if (components.length < 2) {
                return null;
            }
            Factory factory = (Factory) sExtensionMap.get(components[components.length - 1]);
            if (factory == null) {
                return null;
            }
            CheapSoundFile soundFile = factory.create();
            soundFile.setProgressListener(progressListener);
            soundFile.ReadFile(f);
            return soundFile;
        }
        throw new FileNotFoundException(fileName);
    }

    public static boolean isFilenameSupported(String filename) {
        String[] components = filename.toLowerCase().split("\\.");
        if (components.length < 2) {
            return false;
        }
        return sExtensionMap.containsKey(components[components.length - 1]);
    }

    public static String[] getSupportedExtensions() {
        return (String[]) sSupportedExtensions.toArray(new String[sSupportedExtensions.size()]);
    }

    protected CheapSoundFile() {
    }

    public void ReadFile(File inputFile) throws FileNotFoundException, IOException {
        this.mInputFile = inputFile;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    public int getNumFrames() {
        return 0;
    }

    public int getSamplesPerFrame() {
        return 0;
    }

    public int[] getFrameOffsets() {
        return null;
    }

    public int[] getFrameLens() {
        return null;
    }

    public int[] getFrameGains() {
        return null;
    }

    public int getFileSizeBytes() {
        return 0;
    }

    public int getAvgBitrateKbps() {
        return 0;
    }

    public int getSampleRate() {
        return 0;
    }

    public int getChannels() {
        return 0;
    }

    public String getFiletype() {
        return "Unknown";
    }

    public int getSeekableFrameOffset(int frame) {
        return -1;
    }

    public static String bytesToHex(byte[] hash) {
        char[] buf = new char[(hash.length * 2)];
        int x = 0;
        for (int i = 0; i < hash.length; i++) {
            int i2 = x + 1;
            buf[x] = HEX_CHARS[(hash[i] >>> 4) & 15];
            x = i2 + 1;
            buf[i2] = HEX_CHARS[hash[i] & 15];
        }
        return new String(buf);
    }

    public String computeMd5OfFirst10Frames() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        int[] frameOffsets = getFrameOffsets();
        int[] frameLens = getFrameLens();
        int numFrames = frameLens.length;
        if (numFrames > 10) {
            numFrames = 10;
        }
        MessageDigest digest = MessageDigest.getInstance("MD5");
        FileInputStream in = new FileInputStream(this.mInputFile);
        int pos = 0;
        for (int i = 0; i < numFrames; i++) {
            int skip = frameOffsets[i] - pos;
            int len = frameLens[i];
            if (skip > 0) {
                in.skip((long) skip);
                pos += skip;
            }
            byte[] buffer = new byte[len];
            in.read(buffer, 0, len);
            digest.update(buffer);
            pos += len;
        }
        in.close();
        return bytesToHex(digest.digest());
    }

    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
    }
}
