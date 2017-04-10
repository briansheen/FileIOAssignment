import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        String homeDir = System.getProperty("user.home");
        System.out.println(homeDir);
        String fileSep = System.getProperty("file.separator");
        System.out.println(fileSep);

        Path homeTempDir = Paths.get(homeDir + fileSep + "TEMP_FileIO");

        if (!Files.exists(homeTempDir)) {
            try {
                Files.createDirectory(homeTempDir);
                System.out.println("created FileIO Temp directory " + homeTempDir);
            } catch (IOException e) {
                System.out.println("error creating FileIO Temp directory");
            }
        } else {
            System.out.println("File IO Temp directory already exists");
        }

        homeTempDir = Paths.get(homeTempDir + fileSep + ".temp");

        if (!Files.exists(homeTempDir)) {
            try {
                Files.createFile(homeTempDir);
                System.out.println("created hidden temp file " + homeTempDir);
            } catch (IOException e) {
                System.out.println("error creating .temp file");
            }
        } else {
            System.out.println(".temp file already exists ");
        }

        System.out.println(".temp is writable: " + Files.isWritable(homeTempDir));

        try (BufferedWriter writer = Files.newBufferedWriter(homeTempDir, StandardOpenOption.APPEND)) {
            writer.write(LocalDateTime.now().toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("could not open file for writing");
        }

//        try {
//            Files.delete(homeTempDir);
//            System.out.println("hidden temp file .temp deleted " + homeTempDir);
//        } catch (IOException e) {
//            System.out.println("error deleting .temp file");
//            e.printStackTrace();
//        }

        try (BufferedReader reader = Files.newBufferedReader(homeTempDir)) {
            Stream<String> stream = reader.lines();
            Object[] tempLines = stream.toArray();
            System.out.println("Contents of .temp:");
            for (int i = 1; i < tempLines.length; ++i) {
                System.out.printf("%d. ", i);
                System.out.println(tempLines[i].toString());
            }
            stream.close();
        } catch (IOException e) {
            System.out.println("error could not open file for reading");
        }

        System.out.println(".temp exists: " + Files.exists(homeTempDir));

        try {
            System.out.println(".temp is hidden: " + Files.isHidden(homeTempDir));
        } catch (IOException e) {
            System.out.println("error checking if file is hidden ");
        }

        Path newTempDir = Paths.get(homeDir + fileSep + "TEMP_FileIO" + fileSep + "temp");

        System.out.println(newTempDir);

        try {
            Files.copy(homeTempDir, newTempDir, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(".temp copied to " + newTempDir);
        } catch (IOException e) {
            System.out.println("error copying to unhidden temp file");
        }

    }
}
