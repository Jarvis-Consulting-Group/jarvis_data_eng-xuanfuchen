package ca.jrvs.apps.grep;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepStreamImp extends JavaGrepImp{
    public static void main(String[] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        Configurator.initialize(new DefaultConfiguration());
        //Set log level to: ERROR, WARN, INFO, DEBUG, or ALL
        Configurator.setRootLevel(Level.INFO);

        JavaGrepStreamImp grepStreamImp = new JavaGrepStreamImp();
        grepStreamImp.setRegex(args[0]);
        grepStreamImp.setRootPath(args[1]);
        grepStreamImp.setOutFile(args[2]);

        logger.info("Regex: " + grepStreamImp.getRegex());
        logger.info("RootPath: " + grepStreamImp.getRootPath());
        logger.info("OutfilePath: " + grepStreamImp.getOutFile());

        try {
            grepStreamImp.process();
        } catch (Exception e) {
            grepStreamImp.logger.error("Error: Unable to process", e);
        }
    }

    @Override
    public void process() throws IOException {
        LinkedList<String> matchedLines = new LinkedList<>();
        List<File> fileList = listFiles(this.getRootPath());
        AtomicInteger count = new AtomicInteger();

        logger.info("Processing Using Stream...");

        fileList.stream()
                .map(file -> readLines(file)) //each file would be a stream of List<String>
                .flatMap(lines -> lines.stream()) //Flatten the stream, so we only work on one stream (List<String>) at a time
                .filter(line -> containsPattern(line)) //filter lines that matches the pattern
                .forEach(line -> {
                    matchedLines.addLast(line);
                    count.getAndIncrement();
                });

        logger.info("Creating output file.");
        writeToFile(matchedLines);
        logger.info("Complete! " + count + " matched" + (count.get() == 1 ? " line" : " lines"));
    }

    /**
     * recursively find all files under rootDir and return as a list
     * @param rootDir input directory
     * @return List that contains all the files
     */
    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        File directory = new File(rootDir);

        File[] fileList = directory.listFiles();

        if(fileList != null){
            files = Arrays.stream(fileList)
                    .flatMap(file -> { //we need to flatten the stream because subdir might create stream of streams.
                        if (file.isFile()){
                            return Stream.of(file);  //return type must be a stream
                        } else if (file.isDirectory()) {
                            return listFiles(file.getPath()).stream();  //recursion
                        } else {
                            return Stream.empty();  //return type must be a stream
                        }
                    })
                    .collect(Collectors.toList());
        }
        return files;
    }

    /**
     * Read a file and return all the lines
     * @param inputFile file to be read
     * @return
     */
    @Override
    public List<String> readLines(File inputFile) {
        //linked list for storing all lines from the file, and add it to the end efficiently
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            //Use try-with-resources to automatically close the buffered reader
            lines = reader.lines()
                    .collect(Collectors.toList());
        } catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }
}
