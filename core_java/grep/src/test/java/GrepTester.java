import ca.jrvs.apps.grep.JavaGrepImp;
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

public class GrepTester {
    @Test
    public void testListFiles(){
        JavaGrepImp grep = new JavaGrepImp();
        List<File> files = grep.listFiles("TestFiles/");

        // Check that all expected files are included in the list
        assertEquals(3, files.size());
        assertTrue(files.contains(new File("TestFiles/testfile1.txt")));
        assertTrue(files.contains(new File("TestFiles/testfile2.txt")));
        assertTrue(files.contains(new File("TestFiles/subDir/testfile3.txt")));
    }

    @Test
    public void testReadLines(){
        JavaGrepImp grep = new JavaGrepImp();
        List<File> files = grep.listFiles("TestFiles/");
        List<String> lines = new ArrayList<>();
        for(File file: files){
            lines.addAll(grep.readLines(file));
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
        JavaGrepImp grep = new JavaGrepImp();
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
            grep.setRegex(regex);
            assertEquals(testString.matches(regex), grep.containsPattern(testString));
        }
    }

    @Test
    public void testWriteToFile() throws IOException {
        JavaGrepImp grep = new JavaGrepImp();
        grep.setOutFile("TestFiles/testOutput.txt");
        List<String> expected = new ArrayList<>();
        expected.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        expected.add("Nullam quis ante dignissim, ullamcorper lorem non, consequat urna.");
        expected.add("Duis vitae sem non odio ultricies dignissim.");
        expected.add("Suspendisse euismod eros ac nisi vestibulum, eu hendrerit sem hendrerit.");
        expected.add("Phasellus vestibulum libero vel eros commodo, vel ultricies mi aliquet.");

        grep.writeToFile(expected);
        File out = new File("TestFiles/testOutput.txt");
        List<String> actual = grep.readLines(out);

        assertTrue(actual.equals(expected));

        out.delete();
    }

    @Test
    public void testProcess() throws IOException{
        JavaGrepImp grep = new JavaGrepImp();
        String expectedOutputPath = "output/expected.txt";
        String actualOutputPath = "output/actual.txt";

        grep.setRegex(".*Romeo.*Juliet.*");
        grep.setRootPath("data/");
        grep.setOutFile(actualOutputPath);
        //delete the output file if it already exists
        File output = new File(actualOutputPath);
        if(output.exists()){
            output.delete();
        }
        //execute the function
        grep.process();

        //built the contents of expected file and output file as two Strings, then compare them
        File expectedFile = new File(expectedOutputPath);
        String actual = new String(Files.readAllBytes(output.toPath()));
        String expected = new String(Files.readAllBytes(expectedFile.toPath()));
        assertEquals(actual, expected);
    }

    @Test
    public void testMainMethod() throws IOException {
        String expectedOutputPath = "output/expected.txt";
        String actualOutputPath = "output/actual.txt";

        String[] args = {".*Romeo.*Juliet.*",
                "data/",
                actualOutputPath};
        //delete the output file if it already exists
        File actualOutput = new File(actualOutputPath);
        if(actualOutput.exists()){
            actualOutput.delete();
        }

        JavaGrepImp.main(args);

        File expectedFile = new File(expectedOutputPath);
        String actual = new String(Files.readAllBytes(actualOutput.toPath()));
        String expected = new String(Files.readAllBytes(expectedFile.toPath()));
        assertEquals(actual, expected);
    }
}
