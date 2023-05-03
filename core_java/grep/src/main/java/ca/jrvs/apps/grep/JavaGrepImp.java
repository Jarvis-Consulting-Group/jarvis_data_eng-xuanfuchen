package ca.jrvs.apps.grep;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepImp implements JavaGrep{
    static final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        Configurator.initialize(new DefaultConfiguration());
        //Set log level to: ERROR, WARN, INFO, DEBUG, or ALL
        Configurator.setRootLevel(Level.INFO);

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        logger.info("Regex: " + javaGrepImp.getRegex());
        logger.info("RootPath: " + javaGrepImp.getRootPath());
        logger.info("OutfilePath: " + javaGrepImp.getOutFile());

        try {
            javaGrepImp.process();
        } catch (Exception e) {
            javaGrepImp.logger.error("Error: Unable to process", e);
        }
    }

    @Override
    public void process() throws IOException {
        LinkedList<String> matchedLines = new LinkedList<>();
        List<File> fileList = listFiles(this.getRootPath());
        int count = 0;

        logger.info("Processing...");
        for(File file: fileList){
            List<String> lines = readLines(file);
            for(String line: lines){
                if(containsPattern(line)){
                    matchedLines.addLast(line);
                    count++;
                }
            }
        }
        logger.info("Creating output file.");
        writeToFile(matchedLines);
        logger.info("Complete! " + count + " matched" + (count == 1 ? " line" : " lines"));
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
            for(File file: fileList){
                if(file.isFile()){
                    files.add(file);
                } else if(file.isDirectory()) {
                    files.addAll(listFiles(file.getPath()));
                }
            }
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

    /**
     * check if a line contains the regex pattern that passed by user
     * @param line input string
     * @return true if the input line matches this.regex, otherwise return false
     */
    @Override
    public boolean containsPattern(String line) {
        return line.matches(this.getRegex());
    }

    /**
     * Write lines to a file
     * @param lines matched line
     * @throws IOException if write failed
     */
    @Override
    public void writeToFile(List<String> lines) throws IOException {
        File output = new File(this.getOutFile());
        //create the output file if it doesn't exist
        if(!output.exists()){
            output.createNewFile();
        }

        //write lines into the output file
        PrintWriter writer = new PrintWriter(output);
        Files.write(output.toPath(), lines);
        //close the writer
        writer.close();
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}
