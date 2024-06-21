package eu.vytenis.mp3tagger;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

class InspectTest {
    String path;

    @Test
    void printTags() throws UnsupportedTagException, InvalidDataException, IOException {
        assumeTrue(path != null);

        Mp3File file = new Mp3File(path);
        System.out.println(file.hasId3v1Tag());
        System.out.println(file.hasId3v2Tag());
        System.out.println(file.hasCustomTag());
        ID3v1 v1 = file.getId3v1Tag();
        ID3v2 v2 = file.getId3v2Tag();

        System.out.println(v1.getTrack());
        System.out.println(v2.getTrack());
        System.out.println(v2.getPartOfSet());
        System.out.println(v1.getTitle());
        System.out.println(v2.getTitle());
    }
}
