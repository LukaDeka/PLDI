package com.lemms;
import org.junit.Test;
import java.util.ArrayList;
import static org.testng.AssertJUnit.assertEquals;

public class TokenizerTest {

    @Test
    public void testTokenizerOutput() {
        Tokenizer tokenizer = new Tokenizer("src/main/resources/example1.txt");
        ArrayList<Token> tokens = tokenizer.getTokens();

        String expected = """
[
IDENTIFIER(funny_var1), 
ASSIGNMENT(null), 
INT(100100134), 
SEMICOLON(null), 
IDENTIFIER(funny_var), 
ASSIGNMENT(null), 
STRING("epic string"string"), 
SEMICOLON(null), 
IF(null), 
BRACKET_OPEN(null), 
IDENTIFIER(funny_var2), 
NEQ(null), 
INT(100100134), 
BRACKET_CLOSED(null), 
CURLY_OPEN(null), 
IDENTIFIER(print), 
BRACKET_OPEN(null), 
STRING("awman"), 
SEMICOLON(null), 
CURLY_CLOSED(null), 
INT(3), 
LEQ(null), 
INT(4), 
SEMICOLON(null), 
SEMICOLON(null), 
SEMICOLON(null), 
IDENTIFIER(whiletrue), 
ASSIGNMENT(null), 
BOOL(true), 
SEMICOLON(null), 
IDENTIFIER(whilefalse), 
ASSIGNMENT(null), 
BOOL(false), 
SEMICOLON(null), 
IDENTIFIER(string____test_____), 
ASSIGNMENT(null), 
STRING("")]
""";

        assertEquals(expected, tokens.toString());
    }
}
