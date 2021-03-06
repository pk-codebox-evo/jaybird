/*
 * Firebird Open Source JavaEE Connector - JDBC Driver
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
 * can be obtained from a source control history command.
 *
 * All rights reserved.
 */
package org.firebirdsql.jdbc.field;

import org.firebirdsql.gds.ISCConstants;
import org.firebirdsql.gds.ng.fields.FieldDescriptor;
import org.firebirdsql.gds.ng.fields.RowDescriptorBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.*;

/**
 * Tests for {@link FBBigDecimalField}
 * 
 * @author <a href="mailto:mrotteveel@users.sourceforge.net">Mark Rotteveel</a>
 */
public class TestFBBigDecimalField extends BaseJUnit4TestFBField<FBBigDecimalField, BigDecimal> {
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        // NOTE This definition is necessary for the tests in the superclass, this is overridden by most tests in the class itself
        rowDescriptorBuilder.setType(ISCConstants.SQL_LONG)
                .setScale(-2);
        fieldDescriptor = rowDescriptorBuilder.toFieldDescriptor();
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
    }
    
    // TODO Add set<PrimitiveNumber> tests for out of range condition; in current implementation duplicates setBigDecimal out of range tests
    // TODO Add set/getObject test

    @Test
    public void getBigDecimalNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertNull("Expected null result", field.getBigDecimal());
    }

    @Test
    public void getObject_BigDecimalNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected null result for getObject(BigDecimal.class)", field.getObject(BigDecimal.class));
    }

    @Test
    @Override
    public void getObject_BigDecimal() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-4);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations(37);

        BigDecimal expectedValue = new BigDecimal("0.0037");
        assertEquals("Unexpected value for integer BigDecimal", expectedValue, field.getObject(BigDecimal.class));
    }
    
    @Test
    @Ignore("Ignored in favor of more specific tests")
    @Override
    public void getBigDecimalNonNull() throws SQLException {
    }
    
    @Test
    @Ignore("Ignored in favor of more specific tests")
    @Override
    public void getBigDecimalIntNonNull() throws SQLException {
    }
    
    @Test 
    public void getBigDecimalShort() throws SQLException {
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnShortExpectations((short) 231);
        
        BigDecimal expectedValue = new BigDecimal("23.1");
        assertEquals("Unexpected value for short BigDecimal", expectedValue, field.getBigDecimal());
    }
    
    @Test 
    public void getBigDecimalInteger() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-4);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations(34);
        
        BigDecimal expectedValue = new BigDecimal("0.0034");
        assertEquals("Unexpected value for integer BigDecimal", expectedValue, field.getBigDecimal());
    }
    
    @Test 
    public void getBigDecimalLong() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-8);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(51300000000L);
        
        BigDecimal expectedValue = new BigDecimal("513.00000000");
        assertEquals("Unexpected value for long BigDecimal", expectedValue, field.getBigDecimal());
    }
    
    @Test
    @Ignore("Ignored in favor of more specific tests")
    @Override
    public void setBigDecimalNonNull() throws SQLException {
    }
    
    @Test
    public void setBigDecimalShort() throws SQLException {
        fieldDescriptor = createShortFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setShortExpectations((short)4320);
        
        // TODO Might need to add separate test for the rescaling applied here
        field.setBigDecimal(new BigDecimal("43.2"));
    }
    
    @Test
    public void setBigDecimalShortTooHigh() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createShortFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        field.setBigDecimal(BigDecimal.valueOf(Short.MAX_VALUE + 1, 2));
    }
    
    @Test
    public void setBigDecimalShortTooLow() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createShortFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        field.setBigDecimal(BigDecimal.valueOf(Short.MIN_VALUE - 1, 2));
    }
    
    @Test
    public void setBigDecimalShortNull() throws SQLException {
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setNullExpectations();
        
        field.setBigDecimal(null);
    }
    
    @Test
    public void setBigDecimalInteger() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-3);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setIntegerExpectations(1234567);
        
        field.setBigDecimal(new BigDecimal("1234.567"));
    }
    
    @Test
    public void setBigDecimalIntegerTooHigh() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        field.setBigDecimal(BigDecimal.valueOf(Integer.MAX_VALUE + 1L, 2));
    }
    
    @Test
    public void setBigDecimalIntegerTooLow() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        field.setBigDecimal(BigDecimal.valueOf(Integer.MIN_VALUE - 1L, 2));
    }
    
    @Test
    public void setBigDecimalIntegerNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setNullExpectations();
        
        field.setBigDecimal(null);
    }
    
    @Test
    public void setBigDecimalLong() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-5);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setLongExpectations(1234567890123L);
        
        field.setBigDecimal(new BigDecimal("12345678.90123"));
    }
    
    @Test
    public void setBigDecimalLongTooHigh() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        field.setBigDecimal(new BigDecimal(value, 2));
    }
    
    @Test
    public void setBigDecimalLongTooLow() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        BigInteger value = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE);
        field.setBigDecimal(new BigDecimal(value, 2));
    }
    
    @Test
    public void setBigDecimalLongNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setNullExpectations();
        
        field.setBigDecimal(null);
    }
    
    @Test
    @Ignore("Ignored in favor of more specific tests")
    @Override
    public void getBooleanNonNull() throws SQLException {
    }

    @Test
    public void getObject_BooleanNull() throws SQLException {
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected null for getObject(Boolean.class)", field.getObject(Boolean.class));
    }

    @Test
    @Override
    public void getObject_Boolean() throws SQLException {
        // NOTE: We could use 0 for the test, but in that case Jaybird would not have created a FBBigDecimalField
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnShortExpectations((short)10);

        assertTrue("Expected true from getBoolean", field.getObject(Boolean.class));
    }
    
    @Test
    public void getBooleanTrue() throws SQLException {
        // NOTE: We could use 0 for the test, but in that case Jaybird would not have create a FBBigDecimalField
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnShortExpectations((short)10);
        
        assertTrue("Expected true from getBoolean", field.getBoolean());
    }
    
    @Test
    public void getBooleanFalse() throws SQLException {
        // NOTE: We could use scale 0 for the test, but in that case Jaybird would not have create a FBBigDecimalField
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        // NOTE Any value other than 10 would do
        toReturnShortExpectations((short)0);
        
        assertFalse("Expected false from getBoolean", field.getBoolean());
    }
    
    @Test
    @Ignore("Ignored in favor of more specific tests")
    @Override
    public void setBoolean() throws SQLException {
    }
    
    @Test
    public void setBooleanTrue() throws SQLException {
        // NOTE: We could use scale 0 for the test, but in that case Jaybird would not have create a FBBigDecimalField
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setShortExpectations((short)10);
        
        field.setBoolean(true);
    }
    
    @Test
    public void setBooleanFalse() throws SQLException {
        // NOTE: We could use scale 0 for the test, but in that case Jaybird would not have create a FBBigDecimalField
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setShortExpectations((short)0);
        
        field.setBoolean(false);
    }
    
    @Test
    public void getByteNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertEquals("Expected getByte() to return 0 for NULL value", 0, field.getByte());
    }

    @Test
    public void getObject_ByteNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected getObject(Byte.class) to return null for NULL value", field.getObject(Byte.class));
    }

    @Test
    @Override
    public void getByteNonNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations(Byte.MIN_VALUE * 100);
        
        assertEquals("Unexpected value for getByte()", Byte.MIN_VALUE, field.getByte());
    }

    @Test
    @Override
    public void getObject_Byte() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations(Byte.MIN_VALUE * 100);

        assertEquals("Unexpected value for getObject(Byte.class)",
                Byte.valueOf(Byte.MIN_VALUE), field.getObject(Byte.class));
    }
    
    @Test
    public void getByteTooHigh() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations((Byte.MAX_VALUE + 1)* 100);
        
        field.getByte();
    }
    
    @Test
    public void getByteTooLow() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations((Byte.MIN_VALUE - 1)* 100);
        
        field.getByte();
    }
    
    @Test
    public void setByte() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-7);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setLongExpectations(-340000000L);
        
        field.setByte((byte) -34);
    }
    
    @Test
    public void getDoubleNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertEquals("Expected getDouble() to return 0.0 for NULL value", 0.0, field.getDouble(), 0.0);
    }

    @Test
    public void getObject_DoubleNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected getObject(Double.class) to return null for NUL value", field.getObject(Double.class));
    }
    
    @Test
    @Override
    public void getDoubleNonNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(Long.MIN_VALUE);
        
        assertEquals("Unexpected value for getDouble()", Long.MIN_VALUE / 100.0, field.getDouble(), 0.0);
    }

    @Test
    @Override
    public void getObject_Double() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(Long.MIN_VALUE);

        assertEquals("Unexpected value for getObject(Double.class)",
                Long.MIN_VALUE / 100.0, field.getObject(Double.class), 0.0);
    }
    
    @Test
    public void setDouble() throws SQLException {
        fieldDescriptor = createShortFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setShortExpectations((short)4691);
        
        field.setDouble(469.1234567);
    }
    
    @Test
    public void getFloatNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertEquals("Expected getFloat() to return 0.0 for NULL value", 0.0, field.getFloat(), 0.0);
    }

    @Test
    public void getObject_FloatNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected getObject(Float.class) to return null for NUL value", field.getObject(Float.class));
    }
    
    @Test
    @Override
    public void getFloatNonNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(Long.MAX_VALUE);
        
        assertEquals("Unexpected value for getFloat()", Long.MAX_VALUE / 100.0f, field.getFloat(), 0.0);
    }

    @Test
    @Override
    public void getObject_Float() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(Long.MAX_VALUE);

        assertEquals("Unexpected value for getObject(Float.class)",
                Long.MAX_VALUE / 100.0f, field.getObject(Float.class), 0.0);
    }
    
    @Test
    @Override
    public void setFloat() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-5);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setLongExpectations(46912344L);

        field.setFloat(469.1234567f);
    }
    
    @Test
    public void getIntNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertEquals("Expected getInt() to return 0 for NULL value", 0, field.getInt());
    }

    @Test
    public void getObject_IntegerNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected getObject(Integer.class) to return null for NUL value", field.getObject(Integer.class));
    }
    
    @Test
    @Override
    public void getIntNonNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(987654321098765L);
        
        assertEquals("Unexpected value from getInt()", 987654321, field.getInt());
    }

    @Test
    @Override
    public void getObject_Integer() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-6);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(987654321098765L);

        assertEquals("Unexpected value from getInt()", 987654321, (int) field.getObject(Integer.class));
    }
    
    @Test
    public void getIntTooHigh() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations((Integer.MAX_VALUE + 1L) * 100);
        
        field.getInt();
    }
    
    @Test
    public void getIntTooLow() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations((Integer.MIN_VALUE - 1L) * 100);
        
        field.getInt();
    }
    
    @Test
    @Override
    public void setInteger() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setIntegerExpectations(1234560);
        
        field.setInteger(123456);
    }
    
    @Test
    public void getLongNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertEquals("Expected getLong() to return 0 for NULL value", 0, field.getLong());
    }

    @Test
    public void getObject_LongNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected getObject(Long.class) to return null for NUL value", field.getObject(Long.class));
    }
    
    @Test
    @Override
    public void getLongNonNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(Long.MAX_VALUE);
        
        assertEquals("Unexpected value from getLong()", Long.MAX_VALUE / 100, field.getLong());
    }

    @Test
    @Override
    public void getObject_Long() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(Long.MAX_VALUE);

        assertEquals("Unexpected value from getLong()", Long.MAX_VALUE / 100, (long) field.getObject(Long.class));
    }
    
    @Test
    @Override
    public void setLong() throws SQLException {
        fieldDescriptor = createShortFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setShortExpectations((short)3500);
        
        field.setLong(35);
    }
    
    @Test
    @Override
    public void getObjectNonNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-8);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(51300000000L);
        
        BigDecimal expectedValue = new BigDecimal("513.00000000");
        assertEquals("Unexpected value for long BigDecimal", expectedValue, field.getObject());
    }
    
    @Test
    @Override
    public void setObjectNonNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-3);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setIntegerExpectations(1234567);
        
        field.setObject(new BigDecimal("1234.567"));
    }
    
    // TODO Add tests for other object types
    
    @Test
    public void getShortNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertEquals("Expected getShort() to return 0 for NULL value", 0, field.getShort());
    }

    @Test
    public void getObject_ShortNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull("Expected getObject(Short.class) to return null for NUL value", field.getObject(Short.class));
    }
    
    @Test
    @Override
    public void getShortNonNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-4);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations(123456789);
        
        assertEquals("Unexpected value from getShort()", 12345, field.getShort());
    }

    @Test
    @Override
    public void getObject_Short() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-4);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations(123456789);

        assertEquals("Unexpected value from getShort()", 12345, (short) field.getObject(Short.class));
    }
    
    @Test
    public void getShortTooHigh() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations((Short.MAX_VALUE + 1) * 100);
        
        field.getShort();
    }
    
    @Test
    public void getShortTooLow() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnIntegerExpectations((Short.MIN_VALUE - 1) * 100);
        
        field.getShort();
    }
    
    @Test
    @Override
    public void setShort() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-3);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setLongExpectations(Short.MIN_VALUE * 1000L);
        
        field.setShort(Short.MIN_VALUE);
    }
    
    @Test
    public void getStringNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();
        
        assertNull(field.getString());
    }

    @Test
    public void getObject_StringNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnNullExpectations();

        assertNull(field.getObject(String.class));
    }
    
    @Test
    @Override
    public void getStringNonNull() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(456789123);
        
        assertEquals("Unexpected value from getString()", "4567891.23", field.getString());
    }

    @Test
    @Override
    public void getObject_String() throws SQLException {
        fieldDescriptor = createLongFieldDescriptor(-2);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        toReturnLongExpectations(456789123);

        assertEquals("Unexpected value from getString()", "4567891.23", field.getObject(String.class));
    }
    
    @Test
    @Override
    public void setStringNonNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setIntegerExpectations(789123);
        
        field.setString("78912.3456");
    }
    
    @Test
    public void setStringNull() throws SQLException {
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        setNullExpectations();
        
        field.setString(null);
    }
    
    @Test
    public void setStringNonNumber() throws SQLException {
        expectedException.expect(TypeConversionException.class);
        fieldDescriptor = createIntegerFieldDescriptor(-1);
        FBBigDecimalField field = new FBBigDecimalField(fieldDescriptor, fieldData, Types.NUMERIC);
        
        field.setString("NotANumber");
    }
    
    @SuppressWarnings("unused")
    @Test
    public void constructWithUnsupportedSqlType() throws SQLException {
        expectedException.expect(SQLException.class);
        rowDescriptorBuilder.setType(ISCConstants.SQL_VARYING);
        fieldDescriptor = rowDescriptorBuilder.toFieldDescriptor();
        new FBBigDecimalField(fieldDescriptor, fieldData, Types.VARCHAR);
    }

    private static FieldDescriptor createShortFieldDescriptor(int scale) {
        return new RowDescriptorBuilder(1, datatypeCoder)
                .setType(ISCConstants.SQL_SHORT)
                .setScale(scale)
                .toFieldDescriptor();
    }

    private static FieldDescriptor createIntegerFieldDescriptor(int scale) {
        return new RowDescriptorBuilder(1, datatypeCoder)
                .setType(ISCConstants.SQL_LONG)
                .setScale(scale)
                .toFieldDescriptor();
    }

    private static FieldDescriptor createLongFieldDescriptor(int scale) {
        return new RowDescriptorBuilder(1, datatypeCoder)
                .setType(ISCConstants.SQL_INT64)
                .setScale(scale)
                .toFieldDescriptor();
    }

    @Override
    protected BigDecimal getNonNullObject() {
        return BigDecimal.ONE;
    }
}
