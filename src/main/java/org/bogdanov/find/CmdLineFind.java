package org.bogdanov.find;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class CmdLineFind {

    @Argument(required = true)
    private String[] filenames;

    @Option(name="-r", required = false)
    private Boolean recursively = false;

    @Option(name="-d", required = false)
    private File directory = new File("").getAbsoluteFile();

    public static void main(String[] args) {
        CmdLineFind find = new CmdLineFind();
        CmdLineParser parser = new CmdLineParser(find);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Unable to parse arguments! " + e);
            throw new IllegalArgumentException(e);
        }

        Set<File> foundFiles = find.find();

        if (foundFiles.isEmpty()) {
            System.out.println("File(s) not found!");
        } else {
            for (File file: foundFiles) {
                System.out.println("File found: " + file);
            }
        }
    }

    public Set<File> find() {
        Find find = new Find(recursively, directory, filenames);
        return find.find();
    }
}

 class Find {

    public Find(boolean recursively, File directory, String[] filenames) {
        this.recursively = recursively;
        if (directory != null) this.directory = directory;
        if (filenames != null) this.filenames = filenames;
    }

    @Argument(required = true)
    private String[] filenames;

    @Option(name="-r", required = false)
    private Boolean recursively = false;

    @Option(name="-d", required = false)
    private File directory = new File("").getAbsoluteFile();


    public Set<File> find() {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("option " + directory + " is not a directory! ");
        }

        Set<File> files = new HashSet<>();


        if (!recursively) {
            File[] iterateFiles = directory.listFiles();
            if (iterateFiles == null)
                return files;

            for (File file : iterateFiles) {
                for (String searchedFilename : filenames)
                    if (file.getName().equals(searchedFilename) && file.isFile()) {
                        files.add(file);
                    }
            }

        } else {
            try {
                Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<>() {

                    @Override
                    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
                        File file = filePath.toFile();

                        for (String searchedFilename : filenames)
                            if (file.getName().equals(searchedFilename) && file.isFile()) {
                                files.add(file);
                            }

                        return FileVisitResult.CONTINUE;
                    }

                });
            } catch (IOException ignored) {}
        }


        return files;
    }

}
