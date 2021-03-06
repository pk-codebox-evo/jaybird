/*
 * Firebird Open Source J2ee connector - jdbc driver
 *
 * Distributable under LGPL license.
 * You may obtain a copy of the License at http://www.gnu.org/copyleft/lgpl.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LGPL License for more details.
 *
 * This file was created by members of the firebird development team.
 * All individual contributions remain the Copyright (C) of those
 * individuals.  Contributors to this file are either listed here or
 * can be obtained from a CVS history command.
 *
 * All rights reserved.
 */
package org.firebirdsql.jdbc.escape;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.firebirdsql.jdbc.escape.FBEscapedParser.EscapeParserMode;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:rrokytskyy@users.sourceforge.net">Roman Rokytskyy</a>
 */
public class TestFBEscapedFunctionHelper extends TestCase {

    /**
     * Create instance of this class.
     * 
     * @param name name of the test case.
     */
    public TestFBEscapedFunctionHelper(String name) {
        super(name);
    }
    
    /**
     * Test function call that contains quoted identifiers as well as 
     * commas and double quotes in string literals.
     */
    public static final String ESCAPED_FUNCTION_CALL = "test(\"arg1\", 12, ',\"')";
    public static final String ESCAPED_FUNCTION_NAME = "test";
    public static final List<String> ESCAPED_FUNCTION_PARAMS = new ArrayList<String>();
    static {
        ESCAPED_FUNCTION_PARAMS.add("\"arg1\"");
        ESCAPED_FUNCTION_PARAMS.add("12");
        ESCAPED_FUNCTION_PARAMS.add("',\"'");
    }
    
    public void testParseArguments() throws SQLException {
        List<String> parsedParams = FBEscapedFunctionHelper.parseArguments(ESCAPED_FUNCTION_CALL);
        
        assertEquals("Parsed params should be equal to the test ones.", 
            ESCAPED_FUNCTION_PARAMS, parsedParams);
    }
    
    /**
     * Test if function name is parsed correctly.
     */
    public void testParseName() throws SQLException {
        String name = FBEscapedFunctionHelper.parseFunction(ESCAPED_FUNCTION_CALL);
        
        assertEquals("Parsed function name should be equal to the test one.",
            ESCAPED_FUNCTION_NAME, name);
    }
    
    public static final String LCASE_FUNCTION_CALL = "{fn lcase('some name')}";
    public static final String LCASE_FUNCTION_TEST = "LOWER('some name')";
    
    public static final String UCASE_FUNCTION_CALL = "{fn ucase(some_identifier)}";
    public static final String UCASE_FUNCTION_TEST = "UPPER(some_identifier)";
    
    public void testEscapedFunctionCall() throws SQLException {
        FBEscapedParser parser = new FBEscapedParser(EscapeParserMode.USE_BUILT_IN);
        
        String ucaseTest = parser.parse(UCASE_FUNCTION_CALL);
        assertEquals("ucase function parsing should be correct",
                UCASE_FUNCTION_TEST, ucaseTest);
    }
    
    public void testUseStandardUdf() throws SQLException {
        FBEscapedParser parser = new FBEscapedParser(EscapeParserMode.USE_STANDARD_UDF);
        
        String lcaseTest = parser.parse(LCASE_FUNCTION_CALL);
        assertEquals("lcase function parsing should be correct",
                LCASE_FUNCTION_TEST, lcaseTest);
    }
}
