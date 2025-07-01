package com.lemms;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class LemmsTest {

    private static class execResult {
        Process process;
        String output;
        int exitCode;

        execResult(Process process, String output, int exitCode) {
            this.process = process;
            this.output = output;
            this.exitCode = exitCode;
        }
    }

    private execResult runLemms(String arg) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(
            "java", "src/main/java/com/lemms/Lemms.java", arg
        );

        builder.redirectErrorStream(true); // Merge stdout and stderr
        Process process = builder.start();

        // Capture output
        String output;

        try (InputStream is = process.getInputStream()) {
            output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        int exitCode = process.waitFor();

        return new execResult(process, output, exitCode);
    }

    void makeTest(String filename, String expectedOutput, int expectedExitCode) throws IOException, InterruptedException {
        execResult res = runLemms(filename);

        assertEquals(expectedExitCode, res.exitCode,
                "Lemms exited with non-zero code." +
                        "Error message: " + res.output);
        if (expectedExitCode == 0) {
            assertEquals(expectedOutput, res.output);
        }
    }

    @Test
    void testExample1() throws IOException, InterruptedException {
        makeTest("src/main/resources/example1.lemms", "hi", 0);
    }

    @Test
    void testHelloWorld() throws IOException, InterruptedException {
        makeTest("src/main/resources/HelloWorld.lemms", "Hello World!", 0);
    }

    @Test
    void testMissingSemicolon() throws IOException, InterruptedException {
        makeTest("src/main/resources/missingSemicolon.lemms", "", 1);
    }

    @Test
    void testPrintTest1() throws IOException, InterruptedException {
        makeTest("src/main/resources/print_test1.lemms", "400", 0);
    }

    @Test
    void testPrintTest2() throws IOException, InterruptedException {
        makeTest("src/main/resources/print_test2.lemms", "hallo :)", 0);
    }

    @Test
    void testPrintTest3() throws IOException, InterruptedException {
        makeTest("src/main/resources/print_test3.lemms", "hi98765hi", 0);
    }

    @Test
    void testBasicFunctions() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/basicFunctions.lemms", "returning before hello...\n", 0);
    }

    @Test
    void testCommentTest() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/commenttest.lemms", "12hellow # world", 0);
    }

    @Test
    void testFibonacci() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/fibonacci.lemms", "1 2 3 5 8 13 21 34 55 89 ", 0);
    }

    @Test // Ackermann
    void testFunctionWithReturnValue() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/functionWithReturnValue.lemms", "3 4 5 6 5 7 9 11 13 29 61 125 ", 0);
    }

    @Test
    void testIfTest() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/ifTest.lemms", "nothing to see here", 0);
    }

    @Test
    void testNegativeNumberStuff() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/negativeNumberStuff.lemms", "8 5 4 3 2 1 0 -1 -2 -3 -4 ", 0);
    }

    @Test
    void testSumOfAllNumbers() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/sumOfAllNumbers.lemms", "20100", 0);
    }

    @Test
    void testExitStatus1() throws IOException, InterruptedException {
        makeTest("src/main/resources/failedTests/exitstatus1.lemms", "", 1);
    }

    @Test
    void testNoSemicolon() throws IOException, InterruptedException {
        makeTest("src/main/resources/failedTests/noSemicolon.lemms", "", 1);
    }

    @Test
    void testWrongName() throws IOException, InterruptedException {
        makeTest("src/main/resources/failedTests/wrongName.semml", "", 1);
    }

    @Test
    void testThousandCountdown() throws IOException, InterruptedException {
        makeTest("src/main/resources/successTests/hundredCountdown.lemms", "Done!", 0);
    }
}