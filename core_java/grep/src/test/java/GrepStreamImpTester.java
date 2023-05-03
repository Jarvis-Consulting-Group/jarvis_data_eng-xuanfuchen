import ca.jrvs.apps.grep.JavaGrepStreamImp;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GrepStreamImpTester {
    public final static String TEST_DIR = "src/test/resources";
    @Test
    public void testListFiles(){
        JavaGrepStreamImp grepStream = new JavaGrepStreamImp();
        List<File> files = grepStream.listFiles(TEST_DIR + "/TestFiles");

        // Check that all expected files are included in the list
        assertEquals(3, files.size());
        assertTrue(files.contains(new File(TEST_DIR + "/TestFiles/testfile1.txt")));
        assertTrue(files.contains(new File(TEST_DIR + "/TestFiles/testfile2.txt")));
        assertTrue(files.contains(new File(TEST_DIR + "/TestFiles/subDir/testfile3.txt")));
    }

    @Test
    public void testReadLines(){
        JavaGrepStreamImp grepStream = new JavaGrepStreamImp();
        List<File> files = grepStream.listFiles(TEST_DIR + "/TestFiles");
        List<String> lines = new ArrayList<>();
        for(File file: files){
            lines.addAll(grepStream.readLines(file));
        }
        //for loop for 3 tester files
        for(int i = 1; i <= 3; i++){
            //for loop for 5 lines in each tester file
            for(int j = 1; j <= 5; j++){
                String expected = "Line " + j + " in file " + i + ".";
                assertTrue(lines.contains(expected));
            }
        }
    }

    @Test
    public void testContainsPattern(){
        JavaGrepStreamImp grepStream = new JavaGrepStreamImp();
        //built a map that contains regex as keys and test string as values
        Map<String, String> testCases = new HashMap<>();
        testCases.put("[a-z]+\\d+", "hello123");
        testCases.put("\\d{3}-\\d{2}-\\d{4}", "123-45-6789");
        testCases.put("[a-zA-Z]+@[a-zA-Z]+\\.com", "john@example.com");
        testCases.put("^\\d{4}-\\d{2}-\\d{2}$", "2022-01-01");
        testCases.put("^\\$\\d+(\\.\\d{2})?$", "$10.99");
        testCases.put("[A-Z]{3}-\\d{4}", "ABC-1234");
        testCases.put("\\d+\\.\\d+", "3.14");
        testCases.put("[a-z]+", "Hello");
        testCases.put("\\d{4}", "123456");
        testCases.put("\\d{3}-\\d{2}-\\d{4}", "12-34-5678");

        for(String regex: testCases.keySet()){
            String testString = testCases.get(regex);
            grepStream.setRegex(regex);
            assertEquals(testString.matches(regex), grepStream.containsPattern(testString));
        }
    }

    @Test
    public void testWriteToFile() throws IOException {
        JavaGrepStreamImp grepStream = new JavaGrepStreamImp();
        grepStream.setOutFile(TEST_DIR + "/testOutput.txt");
        List<String> expected = new ArrayList<>();
        expected.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        expected.add("Nullam quis ante dignissim, ullamcorper lorem non, consequat urna.");
        expected.add("Duis vitae sem non odio ultricies dignissim.");
        expected.add("Suspendisse euismod eros ac nisi vestibulum, eu hendrerit sem hendrerit.");
        expected.add("Phasellus vestibulum libero vel eros commodo, vel ultricies mi aliquet.");

        grepStream.writeToFile(expected);
        File out = new File(TEST_DIR + "/testOutput.txt");
        List<String> actual = grepStream.readLines(out);

        assertTrue(actual.equals(expected));

        out.delete();
    }

    @Test
    public void testProcess() throws IOException{
        JavaGrepStreamImp grepStream = new JavaGrepStreamImp();
        String expectedOutputPath = TEST_DIR + "/output/expected.txt";
        String actualOutputPath = TEST_DIR + "/output/actual.txt";

        grepStream.setRegex(".*Romeo.*Juliet.*");
        grepStream.setRootPath("data/");
        grepStream.setOutFile(actualOutputPath);
        //delete the output file if it already exists
        File output = new File(actualOutputPath);
        if(output.exists()){
            output.delete();
        }
        //execute the function
        grepStream.process();

        //built the contents of expected file and output file as two Strings, then compare them
        File expectedFile = new File(expectedOutputPath);
        String actual = new String(Files.readAllBytes(output.toPath()));
        String expected = new String(Files.readAllBytes(expectedFile.toPath()));
        assertEquals(actual, expected);
    }

    @Test
    public void testMainMethod() throws IOException {
        String expectedOutputPath = TEST_DIR + "/output/expected.txt";
        String actualOutputPath = TEST_DIR + "/output/actual.txt";

        String[] args = {".*Romeo.*Juliet.*",
                "data/",
                actualOutputPath};
        //delete the output file if it already exists
        File actualOutput = new File(actualOutputPath);
        if(actualOutput.exists()){
            actualOutput.delete();
        }

        JavaGrepStreamImp.main(args);

        File expectedFile = new File(expectedOutputPath);
        String actual = new String(Files.readAllBytes(actualOutput.toPath()));
        String expected = new String(Files.readAllBytes(expectedFile.toPath()));
        assertEquals(actual, expected);
    }
}
