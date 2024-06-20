package eu.vytenis.mp3tagger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

class ModifyTrackNumbersTest {
    private Path source = Paths.get("/tmp/1/input");
    private Path destination = Paths.get("/tmp/1/output");

    @Test
    void renumberTracks() {
        List<LoadedFile> files = readFiles();
        update(files);
        save(files);
    }

    private void update(List<LoadedFile> files) {
        int index = 1;
        for (LoadedFile file : files) {
            ID3v2 tag = file.id3v2Tag();
            tag.setTrack(Integer.toString(index));
            ++index;
        }
    }

    private void save(List<LoadedFile> files) {
        for (LoadedFile file : files)
            save(file);
    }

    private void save(LoadedFile file) {
        try {
            Path p = destination.resolve(file.relativePath());
            Files.createDirectories(p.getParent());
            file.file().save(p.toString());
            System.out.println(file);
        } catch (NotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LoadedFile> readFiles() {
        List<LoadedFile> files = new ArrayList<>();
        for (String line : readLines())
            files.add(readFile(line));
        return files;
    }

    private LoadedFile readFile(String relativePath) {
        try {
            Mp3File f = new Mp3File(source.resolve(relativePath));
            return new LoadedFile(f, relativePath);
        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readLines() {
        Path p = Paths.get("src/test/resources/files.txt");
        try {
            return Files.readAllLines(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    private String findNewTrackNumber(ID3v2 tag) {
        int part = Integer.parseInt(tag.getPartOfSet());
        int track = Integer.parseInt(tag.getTrack());
        int newTrack = part * 100 + track;
        return String.format("%04d", newTrack);
    }

    public static class LoadedFile {
        private final Mp3File file;
        private final String relativePath;

        public LoadedFile(Mp3File file, String relativePath) {
            this.file = file;
            this.relativePath = relativePath;
        }

        public Mp3File file() {
            return file;
        }

        public String relativePath() {
            return relativePath;
        }

        public ID3v2 id3v2Tag() {
            return file.getId3v2Tag();
        }

        @Override
        public String toString() {
            return relativePath;
        }
    }
}
