package com.producevideos.crearvideosconfotosymusicaytextoeditor.soundfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CheapWAV extends CheapSoundFile {
    private int mChannels;
    private int mFileSize;
    private int mFrameBytes;
    private int[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;
    private int mNumFrames;
    private int mOffset;
    private int mSampleRate;

    static class C07581 implements Factory {
        C07581() {
        }

        public CheapSoundFile create() {
            return new CheapWAV();
        }

        public String[] getSupportedExtensions() {
            return new String[]{"wav"};
        }
    }

    public static Factory getFactory() {
        return new C07581();
    }

    public int getNumFrames() {
        return this.mNumFrames;
    }

    public int getSamplesPerFrame() {
        return this.mSampleRate / 50;
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
        return ((this.mSampleRate * this.mChannels) * 2) / 1024;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannels() {
        return this.mChannels;
    }

    public String getFiletype() {
        return "WAV";
    }

    public void ReadFile(File inputFile) throws FileNotFoundException, IOException {
        super.ReadFile(inputFile);
        this.mFileSize = (int) this.mInputFile.length();
        if (this.mFileSize < 128) {
            throw new IOException("File too small to parse");
        }
        FileInputStream stream = new FileInputStream(this.mInputFile);
        byte[] header = new byte[12];
        stream.read(header, 0, 12);
        this.mOffset += 12;
        if (header[0] == (byte) 82 && header[1] == (byte) 73 && header[2] == (byte) 70 && header[3] == (byte) 70 && header[8] == (byte) 87 && header[9] == (byte) 65 && header[10] == (byte) 86 && header[11] == (byte) 69) {
            this.mChannels = 0;
            this.mSampleRate = 0;
            while (this.mOffset + 8 <= this.mFileSize) {
                byte[] chunkHeader = new byte[8];
                stream.read(chunkHeader, 0, 8);
                this.mOffset += 8;
                int chunkLen = ((((chunkHeader[7] & 255) << 24) | ((chunkHeader[6] & 255) << 16)) | ((chunkHeader[5] & 255) << 8)) | (chunkHeader[4] & 255);
                if (chunkHeader[0] != (byte) 102 || chunkHeader[1] != (byte) 109 || chunkHeader[2] != (byte) 116 || chunkHeader[3] != (byte) 32) {
                    if (chunkHeader[0] == (byte) 100 && chunkHeader[1] == (byte) 97 && chunkHeader[2] == (byte) 116 && chunkHeader[3] == (byte) 97) {
                        if (this.mChannels != 0 && this.mSampleRate != 0) {
                            this.mFrameBytes = ((this.mSampleRate * this.mChannels) / 50) * 2;
                            this.mNumFrames = ((this.mFrameBytes - 1) + chunkLen) / this.mFrameBytes;
                            this.mFrameOffsets = new int[this.mNumFrames];
                            this.mFrameLens = new int[this.mNumFrames];
                            this.mFrameGains = new int[this.mNumFrames];
                            byte[] oneFrame = new byte[this.mFrameBytes];
                            int i = 0;
                            int frameIndex = 0;
                            while (i < chunkLen) {
                                int oneFrameBytes = this.mFrameBytes;
                                if (i + oneFrameBytes > chunkLen) {
                                    i = chunkLen - oneFrameBytes;
                                }
                                stream.read(oneFrame, 0, oneFrameBytes);
                                int maxGain = 0;
                                int j = 1;
                                while (j < oneFrameBytes) {
                                    int val = Math.abs(oneFrame[j]);
                                    if (val > maxGain) {
                                        maxGain = val;
                                    }
                                    j += this.mChannels * 4;
                                }
                                this.mFrameOffsets[frameIndex] = this.mOffset;
                                this.mFrameLens[frameIndex] = oneFrameBytes;
                                this.mFrameGains[frameIndex] = maxGain;
                                frameIndex++;
                                this.mOffset += oneFrameBytes;
                                i += oneFrameBytes;
                                if (this.mProgressListener != null && !this.mProgressListener.reportProgress((((double) i) * 1.0d) / ((double) chunkLen))) {
                                    break;
                                }
                            }
                        } else {
                            throw new IOException("Bad WAV file: data chunk before fmt chunk");
                        }
                    }
                    stream.skip((long) chunkLen);
                    this.mOffset += chunkLen;
                } else if (chunkLen < 16 || chunkLen > 1024) {
                    throw new IOException("WAV file has bad fmt chunk");
                } else {
                    byte[] fmt = new byte[chunkLen];
                    stream.read(fmt, 0, chunkLen);
                    this.mOffset += chunkLen;
                    int format = ((fmt[1] & 255) << 8) | (fmt[0] & 255);
                    this.mChannels = ((fmt[3] & 255) << 8) | (fmt[2] & 255);
                    this.mSampleRate = ((((fmt[7] & 255) << 24) | ((fmt[6] & 255) << 16)) | ((fmt[5] & 255) << 8)) | (fmt[4] & 255);
                    if (format != 1) {
                        throw new IOException("Unsupported WAV file encoding");
                    }
                }
            }
            return;
        }
        throw new IOException("Not a WAV file");
    }

    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
        int i;
        outputFile.createNewFile();
        FileInputStream in = new FileInputStream(this.mInputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        long totalAudioLen = 0;
        for (i = 0; i < numFrames; i++) {
            totalAudioLen += (long) this.mFrameLens[startFrame + i];
        }
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = (long) this.mSampleRate;
        long byteRate = (long) ((this.mSampleRate * 2) * this.mChannels);
        out.write(new byte[]{(byte) 82, (byte) 73, (byte) 70, (byte) 70, (byte) ((int) (255 & totalDataLen)), (byte) ((int) ((totalDataLen >> 8) & 255)), (byte) ((int) ((totalDataLen >> 16) & 255)), (byte) ((int) ((totalDataLen >> 24) & 255)), (byte) 87, (byte) 65, (byte) 86, (byte) 69, (byte) 102, (byte) 109, (byte) 116, (byte) 32, (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) this.mChannels, (byte) 0, (byte) ((int) (255 & longSampleRate)), (byte) ((int) ((longSampleRate >> 8) & 255)), (byte) ((int) ((longSampleRate >> 16) & 255)), (byte) ((int) ((longSampleRate >> 24) & 255)), (byte) ((int) (255 & byteRate)), (byte) ((int) ((byteRate >> 8) & 255)), (byte) ((int) ((byteRate >> 16) & 255)), (byte) ((int) ((byteRate >> 24) & 255)), (byte) (this.mChannels * 2), (byte) 0, (byte) 16, (byte) 0, (byte) 100, (byte) 97, (byte) 116, (byte) 97, (byte) ((int) (255 & totalAudioLen)), (byte) ((int) ((totalAudioLen >> 8) & 255)), (byte) ((int) ((totalAudioLen >> 16) & 255)), (byte) ((int) ((totalAudioLen >> 24) & 255))}, 0, 44);
        byte[] buffer = new byte[this.mFrameBytes];
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
