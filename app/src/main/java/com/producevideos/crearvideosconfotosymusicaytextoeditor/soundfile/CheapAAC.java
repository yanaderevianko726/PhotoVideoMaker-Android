package com.producevideos.crearvideosconfotosymusicaytextoeditor.soundfile;

//import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class CheapAAC extends CheapSoundFile {
    public static final int kDINF = 1684631142;
    public static final int kFTYP = 1718909296;
    public static final int kHDLR = 1751411826;
    public static final int kMDAT = 1835295092;
    public static final int kMDHD = 1835296868;
    public static final int kMDIA = 1835297121;
    public static final int kMINF = 1835626086;
    public static final int kMOOV = 1836019574;
    public static final int kMP4A = 1836069985;
    public static final int kMVHD = 1836476516;

    public static final int kSMHD = 1936549988;
    public static final int kSTBL = 1937007212;
    public static final int kSTCO = 1937007471;
    public static final int kSTSC = 1937011555;
    public static final int kSTSD = 1937011556;
    public static final int kSTSZ = 1937011578;
    public static final int kSTTS = 1937011827;
    public static final int kTKHD = 1953196132;
    public static final int kTRAK = 1953653099;
    public static final int[] kRequiredAtoms = new int[]{kDINF, kHDLR, kMDHD, kMDIA, kMINF, kMOOV, kMVHD, kSMHD, kSTBL, kSTSD, kSTSZ, kSTTS, kTKHD, kTRAK};

    public static final int[] kSaveDataAtoms = new int[]{kDINF, kHDLR, kMDHD, kMVHD, kSMHD, kTKHD, kSTSD};

    private HashMap<Integer, Atom> mAtomMap;
    private int mBitrate;
    private int mChannels;
    private int mFileSize;
    private int[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;
    private int mMaxGain;
    private int mMdatLength;
    private int mMdatOffset;
    private int mMinGain;
    private int mNumFrames;
    private int mOffset;
    private int mSampleRate;
    private int mSamplesPerFrame;
    private byte[] atomHeader;

    class Atom {
        public byte[] data;
        public int len;
        public int start;

        Atom() {
        }
    }

    static class C11701 implements Factory {
        C11701() {
        }

        public CheapSoundFile create() {
            return new CheapAAC();
        }

        public String[] getSupportedExtensions() {
            return new String[]{"aac", "m4a"};
        }
    }

    public static Factory getFactory() {
        return new C11701();
    }

    public int getNumFrames() {
        return this.mNumFrames;
    }

    public int getSamplesPerFrame() {
        return this.mSamplesPerFrame;
    }

    public int[] getFrameOffsets() {
        return this.mFrameOffsets;
    }

    public int[] getFrameLens() {
        return this.mFrameLens;
    }

    public int[] getFrameGains() {
        return this.mFrameGains;
    }

    public int getFileSizeBytes() {
        return this.mFileSize;
    }

    public int getAvgBitrateKbps() {
        return this.mFileSize / (this.mNumFrames * this.mSamplesPerFrame);
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannels() {
        return this.mChannels;
    }

    public String getFiletype() {
        return "AAC";
    }

    public String atomToString(int atomType) {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + ((char) ((atomType >> 24) & 255)))).append((char) ((atomType >> 16) & 255)).toString())).append((char) ((atomType >> 8) & 255)).toString())).append((char) (atomType & 255)).toString();
    }

    public void ReadFile(File inputFile) throws FileNotFoundException, IOException {
        super.ReadFile(inputFile);
        this.mChannels = 0;
        this.mSampleRate = 0;
        this.mBitrate = 0;
        this.mSamplesPerFrame = 0;
        this.mNumFrames = 0;
        this.mMinGain = 255;
        this.mMaxGain = 0;
        this.mOffset = 0;
        this.mMdatOffset = -1;
        this.mMdatLength = -1;
        this.mAtomMap = new HashMap();
        this.mFileSize = (int) this.mInputFile.length();
        if (this.mFileSize < 128) {
            throw new IOException("File too small to parse");
        }
        byte[] header = new byte[8];
        new FileInputStream(this.mInputFile).read(header, 0, 8);
        if (header[0] == (byte) 0 && header[4] == (byte) 102 && header[5] == (byte) 116 && header[6] == (byte) 121 && header[7] == (byte) 112) {
            parseMp4(new FileInputStream(this.mInputFile), this.mFileSize);
            if (this.mMdatOffset <= 0 || this.mMdatLength <= 0) {
                throw new IOException("Didn't find mdat");
            }
            FileInputStream stream = new FileInputStream(this.mInputFile);
            stream.skip((long) this.mMdatOffset);
            this.mOffset = this.mMdatOffset;
            parseMdat(stream, this.mMdatLength);
            boolean bad = false;
            for (int requiredAtomType : kRequiredAtoms) {
                if (!this.mAtomMap.containsKey(Integer.valueOf(requiredAtomType))) {
                    System.out.println("Missing atom: " + atomToString(requiredAtomType));
                    bad = true;
                }
            }
            if (bad) {
                throw new IOException("Could not parse MP4 file");
            }
            return;
        }
        throw new IOException("Unknown file format");
    }

    private void parseMp4(InputStream stream, int maxLen) throws IOException {
        while (maxLen > 8) {
            int initialOffset = this.mOffset;
            byte[] atomHeader = new byte[8];
            stream.read(atomHeader, 0, 8);
            int atomLen = ((((atomHeader[0] & 255) << 24) | ((atomHeader[1] & 255) << 16)) | ((atomHeader[2] & 255) << 8)) | (atomHeader[3] & 255);
            if (atomLen > maxLen) {
                atomLen = maxLen;
            }
            int atomType = ((((atomHeader[4] & 255) << 24) | ((atomHeader[5] & 255) << 16)) | ((atomHeader[6] & 255) << 8)) | (atomHeader[7] & 255);
            Atom atom = new Atom();
            atom.start = this.mOffset;
            atom.len = atomLen;
            this.mAtomMap.put(Integer.valueOf(atomType), atom);
            this.mOffset += 8;
            if (atomType == kMOOV || atomType == kTRAK || atomType == kMDIA || atomType == kMINF || atomType == kSTBL) {
                parseMp4(stream, atomLen);
            } else if (atomType == kSTSZ) {
                parseStsz(stream, atomLen - 8);
            } else if (atomType == kSTTS) {
                parseStts(stream, atomLen - 8);
            } else if (atomType == kMDAT) {
                this.mMdatOffset = this.mOffset;
                this.mMdatLength = atomLen - 8;
            } else {
                for (int savedAtomType : kSaveDataAtoms) {
                    if (savedAtomType == atomType) {
                        byte[] data = new byte[(atomLen - 8)];
                        stream.read(data, 0, atomLen - 8);
                        this.mOffset += atomLen - 8;
                        ((Atom) this.mAtomMap.get(Integer.valueOf(atomType))).data = data;
                    }
                }
            }
            if (atomType == kSTSD) {
                parseMp4aFromStsd();
            }
            maxLen -= atomLen;
            int skipLen = atomLen - (this.mOffset - initialOffset);
            if (skipLen < 0) {
                throw new IOException("Went over by " + (-skipLen) + " bytes");
            }
            stream.skip((long) skipLen);
            this.mOffset += skipLen;
        }
    }

    void parseStts(InputStream stream, int maxLen) throws IOException {
        byte[] sttsData = new byte[16];
        stream.read(sttsData, 0, 16);
        this.mOffset += 16;
        this.mSamplesPerFrame = ((((sttsData[12] & 255) << 24) | ((sttsData[13] & 255) << 16)) | ((sttsData[14] & 255) << 8)) | (sttsData[15] & 255);
    }

    void parseStsz(InputStream stream, int maxLen) throws IOException {
        byte[] stszHeader = new byte[12];
        stream.read(stszHeader, 0, 12);
        this.mOffset += 12;
        this.mNumFrames = ((((stszHeader[8] & 255) << 24) | ((stszHeader[9] & 255) << 16)) | ((stszHeader[10] & 255) << 8)) | (stszHeader[11] & 255);
        this.mFrameOffsets = new int[this.mNumFrames];
        this.mFrameLens = new int[this.mNumFrames];
        this.mFrameGains = new int[this.mNumFrames];
        byte[] frameLenBytes = new byte[(this.mNumFrames * 4)];
        stream.read(frameLenBytes, 0, this.mNumFrames * 4);
        this.mOffset += this.mNumFrames * 4;
        for (int i = 0; i < this.mNumFrames; i++) {
            this.mFrameLens[i] = ((((frameLenBytes[(i * 4) + 0] & 255) << 24) | ((frameLenBytes[(i * 4) + 1] & 255) << 16)) | ((frameLenBytes[(i * 4) + 2] & 255) << 8)) | (frameLenBytes[(i * 4) + 3] & 255);
        }
    }

    void parseMp4aFromStsd() {
        byte[] stsdData = ((Atom) this.mAtomMap.get(Integer.valueOf(kSTSD))).data;
        this.mChannels = ((stsdData[32] & 255) << 8) | (stsdData[33] & 255);
        this.mSampleRate = ((stsdData[40] & 255) << 8) | (stsdData[41] & 255);
    }

    void parseMdat(InputStream stream, int maxLen) throws IOException {
        int initialOffset = this.mOffset;
        int i = 0;
        while (i < this.mNumFrames) {
            this.mFrameOffsets[i] = this.mOffset;
            if ((this.mOffset - initialOffset) + this.mFrameLens[i] > maxLen - 8) {
                this.mFrameGains[i] = 0;
            } else {
                readFrameAndComputeGain(stream, i);
            }
            if (this.mFrameGains[i] < this.mMinGain) {
                this.mMinGain = this.mFrameGains[i];
            }
            if (this.mFrameGains[i] > this.mMaxGain) {
                this.mMaxGain = this.mFrameGains[i];
            }
            if (this.mProgressListener == null || this.mProgressListener.reportProgress((((double) this.mOffset) * 1.0d) / ((double) this.mFileSize))) {
                i++;
            } else {
                return;
            }
        }
    }

    void readFrameAndComputeGain(InputStream stream, int frameIndex) throws IOException {
        if (this.mFrameLens[frameIndex] < 4) {
            this.mFrameGains[frameIndex] = 0;
            stream.skip((long) this.mFrameLens[frameIndex]);
            return;
        }
        int initialOffset = this.mOffset;
        byte[] data = new byte[4];
        stream.read(data, 0, 4);
        this.mOffset += 4;
        switch ((data[0] & 224) >> 5) {
            case 0:
                this.mFrameGains[frameIndex] = ((data[0] & 1) << 7) | ((data[1] & 254) >> 1);
                break;
            case 1:
                int maxSfb;
                int scaleFactorGrouping;
                int maskPresent;
                int startBit;
                int b;
                int windowShape = (data[1] & 16) >> 4;
                if (((data[1] & 96) >> 5) == 2) {
                    maxSfb = data[1] & 15;
                    scaleFactorGrouping = (data[2] & 254) >> 1;
                    maskPresent = ((data[2] & 1) << 1) | ((data[3] & 128) >> 7);
                    startBit = 25;
                } else {
                    maxSfb = ((data[1] & 15) << 2) | ((data[2] & 192) >> 6);
                    scaleFactorGrouping = -1;
                    maskPresent = (data[2] & 24) >> 3;
                    startBit = 21;
                }
                if (maskPresent == 1) {
                    int sfgZeroBitCount = 0;
                    for (b = 0; b < 7; b++) {
                        if (((1 << b) & scaleFactorGrouping) == 0) {
                            sfgZeroBitCount++;
                        }
                    }
                    startBit += (sfgZeroBitCount + 1) * maxSfb;
                }
                int bytesNeeded = ((startBit + 7) / 8) + 1;
                byte[] oldData = data;
                data = new byte[bytesNeeded];
                data[0] = oldData[0];
                data[1] = oldData[1];
                data[2] = oldData[2];
                data[3] = oldData[3];
                stream.read(data, 4, bytesNeeded - 4);
                this.mOffset += bytesNeeded - 4;
                int firstChannelGain = 0;
                for (b = 0; b < 8; b++) {
                    int b1 = 7 - ((b + startBit) % 8);
                    firstChannelGain += (((1 << b1) & data[(b + startBit) / 8]) >> b1) << (7 - b);
                }
                this.mFrameGains[frameIndex] = firstChannelGain;
                break;
            default:
                if (frameIndex > 0) {
                    this.mFrameGains[frameIndex] = this.mFrameGains[frameIndex - 1];
                    break;
                } else {
                    this.mFrameGains[frameIndex] = 0;
                    break;
                }
        }
        int skip = this.mFrameLens[frameIndex] - (this.mOffset - initialOffset);
        stream.skip((long) skip);
        this.mOffset += skip;
    }

    public void StartAtom(FileOutputStream out, int atomType) throws IOException {
        atomHeader = new byte[8];
        int atomLen = ((Atom) this.mAtomMap.get(Integer.valueOf(atomType))).len;
        atomHeader[0] = (byte) ((atomLen >> 24) & 255);
        atomHeader[1] = (byte) ((atomLen >> 16) & 255);
        atomHeader[2] = (byte) ((atomLen >> 8) & 255);
        atomHeader[3] = (byte) (atomLen & 255);
        atomHeader[4] = (byte) ((atomType >> 24) & 255);
        atomHeader[5] = (byte) ((atomType >> 16) & 255);
        atomHeader[6] = (byte) ((atomType >> 8) & 255);
        atomHeader[7] = (byte) (atomType & 255);
        out.write(atomHeader, 0, 8);
    }

    public void WriteAtom(FileOutputStream out, int atomType) throws IOException {
        Atom atom = (Atom) this.mAtomMap.get(Integer.valueOf(atomType));
        StartAtom(out, atomType);
        out.write(atom.data, 0, atom.len - 8);
    }

    public void SetAtomData(int atomType, byte[] data) {
        Atom atom = (Atom) this.mAtomMap.get(Integer.valueOf(atomType));
        if (atom == null) {
            atom = new Atom();
            this.mAtomMap.put(Integer.valueOf(atomType), atom);
        }
        atom.len = data.length + 8;
        atom.data = data;
    }

    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
        int i;
        outputFile.createNewFile();
        FileInputStream in = new FileInputStream(this.mInputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        SetAtomData(kFTYP, new byte[24]);
        SetAtomData(kSTTS, new byte[16]);
        byte[] r13 = new byte[20];
        r13[7] = (byte) 1;
        r13[11] = (byte) 1;
        r13[12] = (byte) ((numFrames >> 24) & 255);
        r13[13] = (byte) ((numFrames >> 16) & 255);
        r13[14] = (byte) ((numFrames >> 8) & 255);
        r13[15] = (byte) (numFrames & 255);
        r13[19] = (byte) 1;
        SetAtomData(kSTSC, r13);
        byte[] stszData = new byte[((numFrames * 4) + 12)];
        stszData[8] = (byte) ((numFrames >> 24) & 255);
        stszData[9] = (byte) ((numFrames >> 16) & 255);
        stszData[10] = (byte) ((numFrames >> 8) & 255);
        stszData[11] = (byte) (numFrames & 255);
        for (i = 0; i < numFrames; i++) {
            stszData[(i * 4) + 12] = (byte) ((this.mFrameLens[startFrame + i] >> 24) & 255);
            stszData[(i * 4) + 13] = (byte) ((this.mFrameLens[startFrame + i] >> 16) & 255);
            stszData[(i * 4) + 14] = (byte) ((this.mFrameLens[startFrame + i] >> 8) & 255);
            stszData[(i * 4) + 15] = (byte) (this.mFrameLens[startFrame + i] & 255);
        }
        SetAtomData(kSTSZ, stszData);
        int mdatOffset = (((((((((numFrames * 4) + 144) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSTSD))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSTSC))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kMVHD))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kTKHD))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kMDHD))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kHDLR))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSMHD))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kDINF))).len;
        r13 = new byte[12];
        r13[7] = (byte) 1;
        r13[8] = (byte) ((mdatOffset >> 24) & 255);
        r13[9] = (byte) ((mdatOffset >> 16) & 255);
        r13[10] = (byte) ((mdatOffset >> 8) & 255);
        r13[11] = (byte) (mdatOffset & 255);
        SetAtomData(kSTCO, r13);
        ((Atom) this.mAtomMap.get(Integer.valueOf(kSTBL))).len = (((Atom) this.mAtomMap.get(Integer.valueOf(kSTSZ))).len + (((((Atom) this.mAtomMap.get(Integer.valueOf(kSTSD))).len + 8) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSTTS))).len) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSTSC))).len)) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSTCO))).len;
        ((Atom) this.mAtomMap.get(Integer.valueOf(kMINF))).len = (((Atom) this.mAtomMap.get(Integer.valueOf(kSMHD))).len + (((Atom) this.mAtomMap.get(Integer.valueOf(kDINF))).len + 8)) + ((Atom) this.mAtomMap.get(Integer.valueOf(kSTBL))).len;
        ((Atom) this.mAtomMap.get(Integer.valueOf(kMDIA))).len = (((Atom) this.mAtomMap.get(Integer.valueOf(kHDLR))).len + (((Atom) this.mAtomMap.get(Integer.valueOf(kMDHD))).len + 8)) + ((Atom) this.mAtomMap.get(Integer.valueOf(kMINF))).len;
        ((Atom) this.mAtomMap.get(Integer.valueOf(kTRAK))).len = (((Atom) this.mAtomMap.get(Integer.valueOf(kTKHD))).len + 8) + ((Atom) this.mAtomMap.get(Integer.valueOf(kMDIA))).len;
        ((Atom) this.mAtomMap.get(Integer.valueOf(kMOOV))).len = (((Atom) this.mAtomMap.get(Integer.valueOf(kMVHD))).len + 8) + ((Atom) this.mAtomMap.get(Integer.valueOf(kTRAK))).len;
        int mdatLen = 8;
        for (i = 0; i < numFrames; i++) {
            mdatLen += this.mFrameLens[startFrame + i];
        }
        ((Atom) this.mAtomMap.get(Integer.valueOf(kMDAT))).len = mdatLen;
        WriteAtom(out, kFTYP);
        StartAtom(out, kMOOV);
        WriteAtom(out, kMVHD);
        StartAtom(out, kTRAK);
        WriteAtom(out, kTKHD);
        StartAtom(out, kMDIA);
        WriteAtom(out, kMDHD);
        WriteAtom(out, kHDLR);
        StartAtom(out, kMINF);
        WriteAtom(out, kDINF);
        WriteAtom(out, kSMHD);
        StartAtom(out, kSTBL);
        WriteAtom(out, kSTSD);
        WriteAtom(out, kSTTS);
        WriteAtom(out, kSTSC);
        WriteAtom(out, kSTSZ);
        WriteAtom(out, kSTCO);
        StartAtom(out, kMDAT);
        int maxFrameLen = 0;
        for (i = 0; i < numFrames; i++) {
            if (this.mFrameLens[startFrame + i] > maxFrameLen) {
                maxFrameLen = this.mFrameLens[startFrame + i];
            }
        }
        byte[] buffer = new byte[maxFrameLen];
        int pos = 0;
        for (i = 0; i < numFrames; i++) {
            int skip = this.mFrameOffsets[startFrame + i] - pos;
            int len = this.mFrameLens[startFrame + i];
            if (skip >= 0) {
                if (skip > 0) {
                    in.skip((long) skip);
                    pos += skip;
                }
                in.read(buffer, 0, len);
                out.write(buffer, 0, len);
                pos += len;
            }
        }
        in.close();
        out.close();
    }
}
