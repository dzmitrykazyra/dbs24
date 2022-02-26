/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.nullsafe.NullSafe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.dbs24.application.core.service.funcs.StringFuncs.long2StringFormatter;

public final class SysEnvFuncs {

    //==========================================================================
    public static String getCPLoaders() {

        final ClassLoader cl = ClassLoader.getSystemClassLoader();

        return Stream.of((cl.getDefinedPackages()))
                .map(url -> url.getName())
                .sorted()
                .reduce("URLClassLoader \n ",
                        (x, y) -> x.concat(" ").concat(format("%s\n", y)));
    }

    //==========================================================================
    public static String getSysEnvFuns() {

        return System.getenv()
                .keySet()
                .stream()
                .sorted()
                .reduce("System env \n ",
                        (x, y) -> x.concat(" ").concat(format("%40s = '%s'\n",
                                y, NullSafe.getStringObjValue(System.getenv(y)))));
    }
    //==========================================================================

    public static String getSysProperties() {

        return System.getProperties()
                .keySet()
                .stream()
                .map(x -> (String) x)
                .sorted()
                .reduce("sysProperties \n ", (x, y) -> x.concat(" ").concat(format("%40s = '%s'\n",
                        y, NullSafe.getStringObjValue(System.getProperty(y)))));
    }

    //==========================================================================
    public static String getMemoryStatistics() {

        // get Runtime instance
        final Runtime instance = Runtime.getRuntime();

        return format(
                "Heap utilization statistics [PID=%d]"
                        + ", Total Memory: %s bytes"
                        + ", Free Memory: %s bytes"
                        + ", Used Memory: %s bytes"
                        + ", Max Memory: %s bytes"
                        + ", CodeCache size: %s"
                        + ", Processors/Threads: %d",
                ProcessHandle.current().pid(),
                long2StringFormatter(instance.totalMemory()),
                long2StringFormatter(instance.freeMemory()),
                long2StringFormatter((instance.totalMemory() - instance.freeMemory())),
                long2StringFormatter(instance.maxMemory()),
                getCodeCacheStatistics(),
                instance.availableProcessors());
    }

    //==========================================================================
    public static String getCodeCacheStatistics() {

        return "not implemented";
    }

    public static String runTimeExec(String cmd) {
        String s;
        final StringBuilder sb = new StringBuilder(1024);
        final Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) sb.append(format("%s: %s\n", cmd, s));
            p.waitFor();
            sb.append(format("%s: exit: %s", cmd, p.exitValue()));
            p.destroy();
        } catch (Exception e) {
        }

        return sb.toString();
    }

    public static String runTimeExec(String[] cmds) {

        final StringBuilder sb = new StringBuilder(1024);
        final Runtime rt = Runtime.getRuntime();
        try {
            final Process proc = rt.exec(cmds);
            final BufferedReader is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;

            while ((line = is.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
        }

        return sb.toString();
    }

    public static String runTimeExecSh(String cmd) {

        final String[] multiCmd = {"/bin/sh", "-c", cmd};

        return runTimeExec(multiCmd);
    }
}
