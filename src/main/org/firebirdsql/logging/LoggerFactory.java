/*
 * $Id$
 *
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
package org.firebirdsql.logging;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Factory for Logger instances
 * 
 * @author <a href="mailto:brodsom@users.sourceforge.net">Blas Rodriguez Somoza</a>
 * @author <a href="mailto:mrotteveel@users.sourceforge.net">Mark Rotteveel</a>
 * @version 1.0
 */
public final class LoggerFactory {

    /**
     * NullLogger to use for all getLogger requests if no logging is configured
     */
    private static final Logger NULL_LOGGER = new NullLogger();

    private static final LoggerCreator loggerCreator;

    public static final String FORCE_CONSOLE_LOGGER_PROP = "org.firebirdsql.jdbc.forceConsoleLogger";
    public static final String DISABLE_LOGGING_PROP = "org.firebirdsql.jdbc.disableLogging";
    public static final String LOGGER_IMPLEMENTATION_PROP = "org.firebirdsql.jdbc.loggerImplementation";

    static {
        LoggerCreator tempLoggerCreator = JulLoggerCreator.INSTANCE;
        try {
            String sForceConsoleLogger = getSystemPropertyPrivileged(FORCE_CONSOLE_LOGGER_PROP);
            String sDisableLogging = getSystemPropertyPrivileged(DISABLE_LOGGING_PROP);
            String sLoggerImplementation = getSystemPropertyPrivileged(LOGGER_IMPLEMENTATION_PROP);
            boolean bForceConsoleLogger = "true".equalsIgnoreCase(sForceConsoleLogger);
            boolean bDisableLogging = "true".equalsIgnoreCase(sDisableLogging);

            tempLoggerCreator = getLoggerCreator(sLoggerImplementation, bForceConsoleLogger, bDisableLogging);
        } catch (Exception e) {
            // JulLoggerCreator will be used
            e.printStackTrace();
        } finally {
            loggerCreator = tempLoggerCreator;
        }
    }

    private LoggerFactory() {
        // Do not instantiate
    }

    public static Logger getLogger(String name) {
        return loggerCreator.createLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    private static String getSystemPropertyPrivileged(final String propertyName) {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty(propertyName);
            }
        });
    }

    private static LoggerCreator getLoggerCreator(String loggerImplementationClassName, boolean forceConsoleLogger, boolean disableLogging) {
        if (disableLogging) {
            return new NullLoggerCreator();
        }

        if (forceConsoleLogger || ConsoleLogger.class.getName().equals(loggerImplementationClassName)) {
            return new ConsoleLoggerCreator();
        }

        if (loggerImplementationClassName == null || JulLogger.class.getName().equals(loggerImplementationClassName)) {
            return JulLoggerCreator.INSTANCE;
        }

        try {
            return new ReflectionLoggerCreator(loggerImplementationClassName);
        } catch (Exception e) {
            e.printStackTrace();
            return new JulLoggerCreator();
        }
    }

    private interface LoggerCreator {
        Logger createLogger(String name);
    }

    private static class JulLoggerCreator implements LoggerCreator {

        private static JulLoggerCreator INSTANCE = new JulLoggerCreator();

        @Override
        public Logger createLogger(String name) {
            return new JulLogger(name);
        }
    }

    private static class NullLoggerCreator implements LoggerCreator {
        @Override
        public Logger createLogger(String name) {
            return NULL_LOGGER;
        }
    }

    private static class ConsoleLoggerCreator implements LoggerCreator {
        @Override
        public Logger createLogger(String name) {
            return new ConsoleLogger(name);
        }
    }

    private static class ReflectionLoggerCreator implements LoggerCreator {

        private final Class<? extends Logger> loggerClass;
        private final Constructor<? extends Logger> loggerConstructor;

        ReflectionLoggerCreator(String loggerImplementationClassName) throws ClassNotFoundException, NoSuchMethodException {
            Class<?> loggerClassCandidate = Class.forName(loggerImplementationClassName);
            if (!Logger.class.isAssignableFrom(loggerClassCandidate)) {
                throw new IllegalArgumentException(loggerImplementationClassName + " does not implement org.firebirdsql.logging.Logger");
            }
            loggerClass = (Class<? extends Logger>) loggerClassCandidate;
            loggerConstructor = loggerClass.getConstructor(String.class);
        }

        @Override
        public Logger createLogger(String name) {
            try {
                return loggerConstructor.newInstance(name);
            } catch (Exception e) {
                e.printStackTrace();
                return NULL_LOGGER;
            }
        }
    }
}
