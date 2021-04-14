package com.producevideos.crearvideosconfotosymusicaytextoeditor.libffmpeg.exceptions;

public class FFmpegCommandAlreadyRunningException extends Exception {
    private static final long serialVersionUID = -6772450076393566039L;

    public FFmpegCommandAlreadyRunningException(String message) {
        super(message);
    }
}
