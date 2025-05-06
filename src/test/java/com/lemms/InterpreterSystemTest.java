package com.lemms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class MainTest {

    @Test
    void helloWorldTest() {
        List<String> receivedPrints = new ArrayList<>();
        
        List<String> expectedPrints = Arrays.asList("Hello World!");
        
        try (MockedStatic<Printer> mockedPrinter = mockStatic(Printer.class)) {
            mockedPrinter.when(() -> Printer.print(anyInt(), anyString())).then(invocation -> {
                receivedPrints.add(invocation.getArgument(1));                
                return null;
            });
        }

        try {
            Interpreter.runFile("src/test/resources/HelloWorld.lemms");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        Assertions.assertIterableEquals(expectedPrints, receivedPrints);

    }
}