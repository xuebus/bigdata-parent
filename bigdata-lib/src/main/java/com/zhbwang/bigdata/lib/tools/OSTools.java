// ----------------------------------------------------------------------------
// Copyright 2007-2012, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Description:
//  General OS specific tools
// ----------------------------------------------------------------------------
// Change History:
//  2006/03/26  Martin D. Flynn
//     -Initial release
//  2006/06/30  Martin D. Flynn
//     -Repackaged
//  2008/06/20  Martin D. Flynn
//     -Added method 'getProcessID()'
//  2010/05/24  Martin D. Flynn
//     -Added "getMemoryUsage", "printMemoryUsage"
//  2011/08/21  Martin D. Flynn
//     -Added "getOSTypeName"
//  2012/04/20  Martin D. Flynn
//     -Updated "isSunJava"
// ----------------------------------------------------------------------------
package com.zhbwang.bigdata.lib.tools;

import java.io.File;
import java.lang.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings({ "rawtypes" })
public class OSTools {

    // ------------------------------------------------------------------------

    private static final Object LockObject = new Object();

    // ------------------------------------------------------------------------
    // OS and JVM specific tools
    // ------------------------------------------------------------------------

    // Note: these values may change from release to release
    // Reference: http://lopica.sourceforge.net/os.html
    private static final int OS_INITIALIZE = -1;
    public static final int OS_TYPE_MASK = 0x00FFFF00;
    public static final int OS_SUBTYPE_MASK = 0x000000FF;

    public static final int OS_UNKNOWN = 0x00000000;

    public static final int OS_LINUX = 0x00010100;
    public static final int OS_LINUX_FEDORA = 0x00000001; // not detected
    public static final int OS_LINUX_CENTOS = 0x00000002; // not detected
    public static final int OS_LINUX_UBUNTU = 0x00000003; // not detected
    public static final int OS_LINUX_DEBIAN = 0x00000004; // not detected

    public static final int OS_UNIX = 0x00010200;
    public static final int OS_UNIX_SOLARIS = 0x00000001;
    public static final int OS_UNIX_SUNOS = 0x00000002;
    public static final int OS_UNIX_AIX = 0x00000004;
    public static final int OS_UNIX_DIGITAL = 0x00000005;
    public static final int OS_UNIX_HPUX = 0x00000006;
    public static final int OS_UNIX_IRIX = 0x00000007;

    public static final int OS_BSD = 0x00010300;
    public static final int OS_BSD_FREEBSD = 0x00000001;

    public static final int OS_MACOS = 0x00010500;
    public static final int OS_MACOS_X = 0x00000001;

    public static final int OS_WINDOWS = 0x00000700;
    public static final int OS_WINDOWS_9X = 0x00000001; // 95/98/ME
    public static final int OS_WINDOWS_XP = 0x00000002;
    public static final int OS_WINDOWS_VISTA = 0x00000003;
    public static final int OS_WINDOWS_7 = 0x00000004;
    public static final int OS_WINDOWS_CE = 0x00000010;
    public static final int OS_WINDOWS_NT = 0x00000011;
    public static final int OS_WINDOWS_2000 = 0x00000012;
    public static final int OS_WINDOWS_2003 = 0x00000013;
    public static final int OS_WINDOWS_CYGWIN = 0x000000C0; // not detected

    private static int OSType = OS_INITIALIZE;

    /**
     *** Returns the known OS type as an integer bitmask
     *** 
     * @return The OS type
     **/
    public static int getOSType() {
        if (OSType == OS_INITIALIZE) {
            String osName = System.getProperty("os.name")
                                  .toLowerCase();
            // LogUtil.info("OS: " + osName);
            if (osName.startsWith("windows")) {
                OSType = OS_WINDOWS;
                if (osName.startsWith("windows xp")) {
                    OSType |= OS_WINDOWS_XP;
                } else if (osName.startsWith("windows 9")
                           || osName.startsWith("windows m")) {
                    OSType |= OS_WINDOWS_9X;
                } else if (osName.startsWith("windows 7")) {
                    OSType |= OS_WINDOWS_7;
                } else if (osName.startsWith("windows vista")) {
                    OSType |= OS_WINDOWS_VISTA;
                } else if (osName.startsWith("windows nt")) {
                    OSType |= OS_WINDOWS_NT;
                } else if (osName.startsWith("windows 2000")) {
                    OSType |= OS_WINDOWS_2000;
                } else if (osName.startsWith("windows 2003")) {
                    OSType |= OS_WINDOWS_2003;
                } else if (osName.startsWith("windows ce")) {
                    OSType |= OS_WINDOWS_CE;
                }
            } else if (osName.startsWith("mac")) {
                // "Max OS X"
                OSType = OS_MACOS;
                if (osName.startsWith("mac os x")) {
                    OSType |= OS_MACOS_X;
                }
            } else if (osName.startsWith("linux")) {
                // "Linux"
                OSType = OS_LINUX;
            } else if (osName.startsWith("solaris")) {
                // "Solaris"
                OSType = OS_UNIX | OS_UNIX_SOLARIS;
            } else if (osName.startsWith("sunos")) {
                // "Solaris"
                OSType = OS_UNIX | OS_UNIX_SUNOS;
            } else if (osName.startsWith("hp ux") || osName.startsWith("hp-ux")) {
                // "HP UX"
                OSType = OS_UNIX | OS_UNIX_HPUX;
            } else if (osName.startsWith("digital unix")) {
                // "Digital Unix"
                OSType = OS_UNIX | OS_UNIX_DIGITAL;
            } else if (osName.startsWith("aix")) {
                // "AIX"
                OSType = OS_UNIX | OS_UNIX_AIX;
            } else if (osName.startsWith("irix")) {
                // "Irix"
                OSType = OS_UNIX | OS_UNIX_IRIX;
            } else if (osName.startsWith("freebsd")) {
                // "FreeBSD"
                OSType = OS_BSD | OS_BSD_FREEBSD;
            } else if (osName.indexOf("unix") >= 0) {
                // "*Unix*"
                OSType = OS_UNIX;
            } else if (osName.indexOf("linux") >= 0) {
                // "*Linux*"
                OSType = OS_LINUX;
            } else if (File.separatorChar == '/') {
                // "Linux"
                OSType = OS_LINUX;
            } else {
                OSType = OS_UNKNOWN;
            }
        }
        return OSType;
    }

    /**
     *** Returns the String representation of the specified OS type
     *** 
     * @param type
     *            The OS type
     *** @return The OS type name
     **/
    public static String getOSTypeName(int type,
                                       boolean inclSubtype) {
        switch (type & OS_TYPE_MASK) {
        case OS_LINUX:
            if (inclSubtype) {
                switch (type & OS_SUBTYPE_MASK) {
                case OS_LINUX_FEDORA:
                    return "LINUX_FEDORA";
                case OS_LINUX_CENTOS:
                    return "LINUX_CENTOS";
                case OS_LINUX_UBUNTU:
                    return "LINUX_CENTOS";
                case OS_LINUX_DEBIAN:
                    return "LINUX_DEBIAN";
                default:
                    return "LINUX";
                }
            } else {
                return "LINUX";
            }
        case OS_UNIX:
            if (inclSubtype) {
                switch (type & OS_SUBTYPE_MASK) {
                case OS_UNIX_SOLARIS:
                    return "UNIX_SOLARIS";
                case OS_UNIX_SUNOS:
                    return "UNIX_SUNOS";
                case OS_UNIX_AIX:
                    return "UNIX_AIX";
                case OS_UNIX_DIGITAL:
                    return "UNIX_DIGITAL";
                case OS_UNIX_HPUX:
                    return "UNIX_HPUX";
                case OS_UNIX_IRIX:
                    return "UNIX_IRIX";
                default:
                    return "UNIX";
                }
            } else {
                return "UNIX";
            }
        case OS_BSD:
            if (inclSubtype) {
                switch (type & OS_SUBTYPE_MASK) {
                case OS_BSD_FREEBSD:
                    return "BSD_FREEBSD";
                default:
                    return "BSD";
                }
            } else {
                return "BSD";
            }
        case OS_MACOS:
            if (inclSubtype) {
                switch (type & OS_SUBTYPE_MASK) {
                case OS_MACOS_X:
                    return "MACOS_X";
                default:
                    return "MACOS";
                }
            } else {
                return "MACOS";
            }
        case OS_WINDOWS:
            if (inclSubtype) {
                switch (type & OS_SUBTYPE_MASK) {
                case OS_WINDOWS_9X:
                    return "WINDOWS_9X";
                case OS_WINDOWS_XP:
                    return "WINDOWS_XP";
                case OS_WINDOWS_VISTA:
                    return "WINDOWS_VISTA";
                case OS_WINDOWS_7:
                    return "WINDOWS_7";
                case OS_WINDOWS_2000:
                    return "WINDOWS_2000";
                case OS_WINDOWS_NT:
                    return "WINDOWS_NT";
                default:
                    return "WINDOWS";
                }
            } else {
                return "WINDOWS";
            }
        default:
            return "UNKNOWN";
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     *** Returns true if the OS is the specified type
     *** 
     * @return True if the OS is the specified type
     **/
    public static boolean isOSType(int type) {
        int osType = getOSType();
        return ((osType & OS_TYPE_MASK) == type);
    }

    /**
     *** Returns true if the OS is the specified type
     *** 
     * @return True if the OS is the specified type
     **/
    public static boolean isOSType(int type,
                                   int subType) {
        int osType = getOSType();
        if ((osType & OS_TYPE_MASK) != type) {
            // type mismatch
            return false;
        } else if (subType <= 0) {
            // subtype not specified
            return true;
        } else {
            // test subtype
            return ((osType & OS_SUBTYPE_MASK & subType) != 0);
        }
    }

    /**
     *** Returns true if the OS is unknown
     *** 
     * @return True if the OS is unknown
     **/
    public static boolean isUnknown() {
        return (getOSType() == OS_UNKNOWN);
    }

    // ------------------------------------------------------------------------

    /**
     *** Returns true if the OS is a flavor of Windows
     *** 
     * @return True if the OS is a flavor of Windows
     **/
    public static boolean isWindows() {
        return isOSType(OS_WINDOWS);
    }

    /**
     *** Returns true if the OS is Windows XP
     *** 
     * @return True if the OS is Windows XP
     **/
    public static boolean isWindowsXP() {
        return isOSType(OS_WINDOWS, OS_WINDOWS_XP);
    }

    /**
     *** Returns true if the OS is Windows 95/98
     *** 
     * @return True if the OS is Windows 95/98
     **/
    public static boolean isWindows9X() {
        return isOSType(OS_WINDOWS, OS_WINDOWS_9X);
    }

    // ------------------------------------------------------------------------

    /**
     *** Returns true if the OS is Unix/Linux
     *** 
     * @return True if the OS is Unix/Linux
     **/
    public static boolean isLinux() {
        return isOSType(OS_LINUX) || isOSType(OS_UNIX);
    }

    /**
     *** Returns true if the OS is Apple Mac OS
     *** 
     * @return True if the OS is Apple Mac OS
     **/
    public static boolean isMacOS() {
        return isOSType(OS_MACOS);
    }

    /**
     *** Returns true if the OS is Apple Mac OS X
     *** 
     * @return True if the OS is Apple Mac OS X
     **/
    public static boolean isMacOSX() {
        return isOSType(OS_MACOS, OS_MACOS_X);
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     *** Gets the current host name
     *** 
     * @return The current hostname
     **/
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost()
                              .getHostName();
        } catch (UnknownHostException uhe) {
            // LogUtil.error("Error", uhe);
            return "";
        }
    }

    /**
     *** Gets the current host IP address
     *** 
     * @return The current IP address
     **/
    // public static String getHostIP() {
    // try {
    // String ip =
    // StringTools.trim(InetAddress.getByName(InetAddress.getLocalHost()
    // .getHostName()));
    // int h = ip.indexOf("/");
    // return (h >= 0) ? ip.substring(h + 1) : ip;
    // } catch (UnknownHostException uhe) {
    // // LogUtil.error("Error", uhe);
    // return "";
    // }
    // }
    public static String getHostIP() {
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            String ip = netAddress.getHostAddress();
            return ip;
        } catch (UnknownHostException uhe) {
            // LogUtil.error("Error", uhe);
            return "";
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     *** Get the current memory usage (in number of bytes)
     *** 
     * @param L
     *            The long array where the memory values will be placed. If
     *            'null', is specified, or if the array has fewer than 3
     *            elements, a new long array will be returned. then the array
     *            must be
     *** @return The current memory usage as an array of 3 long values indicating
     *         { MaxMemory, TotalMemory, FreeMemory } (in that order).
     **/
    public static long[] getMemoryUsage(long L[]) {
        long mem[] = ((L != null) && (L.length >= 3)) ? L : new long[3];
        Runtime rt = Runtime.getRuntime();
        synchronized (OSTools.LockObject) {
            mem[0] = rt.maxMemory();
            mem[1] = rt.totalMemory();
            mem[2] = rt.freeMemory();
        }
        return mem;
    }

    /**
     *** Get the current memory usage String
     **/
    public static String getMemoryUsageStringMb() {
        // "Max=4.00, Total=4.00, Free=2.00, Used=2.00"
        double divisor = 1024.0 * 1024.0; // megabytes
        long mem[] = OSTools.getMemoryUsage(null);
        double maxK = (double) mem[0] / divisor;
        double totK = (double) mem[1] / divisor;
        double freK = (double) mem[2] / divisor;
        double useK = totK - freK;
        StringBuffer sb = new StringBuffer();
        // sb.append("[Mb] ");
        sb.append("Max=")
          .append(StringTools.format(maxK, "0.0"))
          .append(", ");
        sb.append("Total=")
          .append(StringTools.format(totK, "0.0"))
          .append(", ");
        sb.append("Free=")
          .append(StringTools.format(freK, "0.0"))
          .append(", ");
        sb.append("Used=")
          .append(StringTools.format(useK, "0.0"));
        return sb.toString();
    }

    /**
     *** Prints the current memory usage to the log file
     **/
    public static void printMemoryUsage() {
        long mem[] = OSTools.getMemoryUsage(null);
        long maxK = mem[0] / 1024L;
        long totK = mem[1] / 1024L;
        long freK = mem[2] / 1024L;
        LogUtil.info("Memory-K: max=%d, total=%d, free=%d, used=%d",
                     maxK,
                     totK,
                     freK,
                     (totK - freK));
        // OSTools.printMemoryUsageMXBean();
    }

    /**
     *** Prints the current memory usage to the log file
     **/
    public static void printMemoryUsageMXBean() {

        /* Heap/Non-Heap */
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memory.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memory.getNonHeapMemoryUsage();
        LogUtil.info("Heap Memory Usage    : " + formatMemoryUsage(heapUsage));
        LogUtil.info("Non-Heap Memory Usage: "
                     + formatMemoryUsage(nonHeapUsage));

        /* Pools */
        java.util.List<MemoryPoolMXBean> memPool = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mp : memPool) {
            String name = mp.getName();
            MemoryType type = mp.getType();
            MemoryUsage estUsage = mp.getUsage();
            MemoryUsage peakUsage = mp.getPeakUsage();
            MemoryUsage collUsage = mp.getCollectionUsage();
            LogUtil.info("Pool Usage: " + name + " [" + type + "]");
            LogUtil.info("  Estimate  : " + formatMemoryUsage(estUsage));
            LogUtil.info("  Peak      : " + formatMemoryUsage(peakUsage));
            LogUtil.info("  Collection: " + formatMemoryUsage(collUsage));
        }

    }

    /**
     *** Formats a MemoryUsage instance
     **/
    private static String formatMemoryUsage(MemoryUsage u) {
        if (u != null) {
            long comm = u.getCommitted() / 1024L;
            long init = u.getInit() / 1024L;
            long max = u.getMax() / 1024L;
            long used = u.getUsed() / 1024L;
            StringBuffer sb = new StringBuffer();
            sb.append("[K]");
            sb.append(" Committed=")
              .append(comm);
            sb.append(" Init=")
              .append(init);
            sb.append(" Max=")
              .append(max);
            sb.append(" Used=")
              .append(used);
            return sb.toString();
        } else {
            return "";
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @SuppressWarnings("restriction")
    private static Class _getCallerClass(int frame) throws Throwable {
        return sun.reflect.Reflection.getCallerClass(frame + 1); // <== ignore
                                                                 // any warnings
    }

    /**
     *** Gets the class of the caller at the specified frame index
     *** 
     * @param frame
     *            The frame index
     *** @return The calling class
     **/
    public static Class getCallerClass(int frame) {
        try {
            // sun.reflect.Reflection.getCallerClass(0) ==
            // sun.reflect.Reflection
            // sun.reflect.Reflection.getCallerClass(1) == OSTools
            Class clz = OSTools._getCallerClass(frame + 1);
            // Print._println("" + (frame + 1) + "] class " +
            // StringTools.className(clz));
            return clz;
        } catch (Throwable th) { // ClassNotFoundException
            // This can occur when the code has been compiled with the Sun
            // Microsystems version
            // of Java, but is executed with the GNU version of Java (or other
            // non-Sun version).
            LogUtil.error("Sun Microsystems version of Java is not in use", th);
            return null;
        }
    }

    /**
     *** Returns true if 'sun.reflect.Reflection' is present in the runtime
     * libraries.<br>
     *** (will return true when running with the Sun Microsystems version of Java)
     *** 
     * @return True if 'getCallerClass' is available.
     **/
    public static boolean hasGetCallerClass() {
        try {
            OSTools._getCallerClass(0);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    /**
     *** Prints the class of the caller (debug purposes only)
     **/
    public static void printCallerClasses() {
        try {
            for (int i = 0;; i++) {
                Class clz = OSTools._getCallerClass(i);
                LogUtil.info("" + i + "] class " + StringTools.className(clz));
                if (clz == null) {
                    break;
                }
            }
        } catch (Throwable th) { // ClassNotFoundException
            // This can occur when the code has been compiled with the Sun
            // Microsystems version
            // of Java, but is executed with the GNU version of Java.
            LogUtil.error("Sun Microsystems version of Java is not in use", th);
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     *** Returns true if the specified object is an instance of (or equal to) the
     * specified class name.
     **/
    public static boolean instanceOf(Object obj,
                                     String className) {
        if ((obj == null) || StringTools.isBlank(className)) {
            return false;
        } else {
            return StringTools.className(obj)
                              .equals(className);
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     *** Sleeps for the specified number of milliseconds
     *** 
     * @param ms
     *            Number of milliseconds to sleep
     *** @return True if sleep was performed without interruption, false otherwise
     **/
    public static boolean sleepMS(long ms) {
        if (ms < 0L) {
            return false;
        } else if (ms == 0L) {
            return true;
        } else {
            try {
                Thread.sleep(ms);
                return true;
            } catch (Throwable th) {
                return false;
            }
        }
    }

    /**
     *** Sleeps for the specified number of seconds
     *** 
     * @param sec
     *            Number of milliseconds to sleep
     *** @return True if sleep was performed without interruption, false otherwise
     **/
    public static boolean sleepSec(long sec) {
        if (sec < 0L) {
            return false;
        } else if (sec == 0L) {
            return true;
        } else {
            try {
                Thread.sleep(sec * 1000L);
                return true;
            } catch (Throwable th) {
                return false;
            }
        }
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     *** Main entry point for testing/debugging
     *** 
     * @param argv
     *            Comand-line arguments
     **/
    // public static void main(String argv[]) {
    // RTProperties props = RTProperties.parseCommandLineArgs(argv);
    //
    // LogUtil.info("");
    // LogUtil.info("Host ...");
    // LogUtil.info("Host Name   : " + getHostName());
    // LogUtil.info("Host IP     : " + getHostIP());
    //
    // LogUtil.info("");
    // LogUtil.info("OS Type ...");
    // LogUtil.info("Is Windows  : " + isWindows());
    // LogUtil.info("Is Windows9X: " + isWindows9X());
    // LogUtil.info("Is WindowsXP: " + isWindowsXP());
    // LogUtil.info("Is Linux    : " + isLinux());
    // LogUtil.info("Is MacOS    : " + isMacOS());
    // LogUtil.info("Is MacOSX   : " + isMacOSX());
    //
    // LogUtil.info("");
    // LogUtil.info("Memory ...");
    // Runtime rt = Runtime.getRuntime();
    // LogUtil.info("Total Mem   : " + rt.totalMemory() / (1024.0 * 1024.0)
    // + " Mb");
    // LogUtil.info("Max Mem     : " + rt.maxMemory() / (1024.0 * 1024.0)
    // + " Mb");
    // LogUtil.info("Free Mem    : " + rt.freeMemory() / (1024.0 * 1024.0)
    // + " Mb");
    //
    // }

}
