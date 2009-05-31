/*
Copyright (C) 2002 Steve Jernigan <sjernigan@iname.com>.

This file is part of JavaNCSS2Ant and JavaNCSS
(http://sourceforge.net/projects/javancss2ant/ ,
http://www.kclee.com/clemens/java/javancss/).

JavaNCSS2Ant and JavaNCSS are free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

JavaNCSS2Ant and JavaNCSS are distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with JavaNCSS; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.  */

package javancss;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ccl.util.Util;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

/**
 * Ant task to report and check basic code metrics.
 *
 * <p>This task wraps the JavaNCSS library for determining code metrics. The
 * library determines several code metrics such as object counts, non-commented
 * source statements (NCSS), cyclomatic complexity numbers (CCN), and javadoc
 * statements. These counts are subtotaled per function, class, and package.
 * This task allows you to place minimum and maximum thresholds on each of these
 * (not all of these make sense but all are included for completeness).
 *
 * <p>The original version of this task was written by Steve Jernigan and made
 * available on SourceForge as the JavaNCSS2Ant project. It was subsequently
 * extended by Phillip Wells to enable access to the report generation feature
 * of the tool whereupon it was submitted for inclusion in Ant itself.
 *
 * <p>JavaNCSS was developed by Christoph Clemens Lee and is available
 * <a href="http://www.kclee.com/clemens/java/javancss/">here</a>.
 *
 * @author Phillip Wells
 * @author Steve Jernigan
 * @author Clemens Lee
 */
public class JavancssAntTask extends MatchingTask {
    /**
     * The command to be executed to run to tool.
     */
    private CommandlineJava commandline = new CommandlineJava();
    /**
     * Whether the build should halt if there is an error or a threshold is
     * exceeded.
     */
    private boolean abortOnFail = false;
    /**
     * The directory containing the source files to be scanned by the tool.
     */
    private File srcdir;
    /**
     * The classpath to be used.
     */
    private Path classpath;
    /**
     * The location of the output file.
     */
    private File outputfile;
    /**
     * The format of the output file. Allowable values are 'plain' or 'xml'.
     */
    private String format = "plain";
    /**
     * Indicates the failure of the JavaNCSS process.
     */
    private static final int FAILURE = 1;
    /**
     * Indicates the success of the JavaNCSS process.
     */
    private static final int SUCCESS = 0;
    /**
     * The maximum number of classes per package.
     */
    private int classPerPkgMax = Integer.MAX_VALUE;
    /**
     * The minimum number of classes per package.
     */
    private int classPerPkgMin = -1;
    /**
     * The maximum number of functions per package.
     */
    private int funcPerPkgMax = Integer.MAX_VALUE;
    /**
     * The minimum number of functions per package.
     */
    private int funcPerPkgMin = -1;
    /**
     * The maximum number of non-commenting source statements per package.
     */
    private int ncssPerPkgMax = Integer.MAX_VALUE;
    /**
     * The minimum number of non-commenting source statements per package.
     */
    private int ncssPerPkgMin = -1;
    /**
     * The maximum number of inner classes per class.
     */
    private int classPerClassMax = Integer.MAX_VALUE;
    /**
     * The minimum number of inner classes per class.
     */
    private int classPerClassMin = -1;
    /**
     * The maximum number of functions per class.
     */
    private int funcPerClassMax = Integer.MAX_VALUE;
    /**
     * The minimum number of functions per class.
     */
    private int funcPerClassMin = -1;
    /**
     * The maximum number of non-commenting source statements per class.
     */
    private int ncssPerClassMax = Integer.MAX_VALUE;
    /**
     * The minimum number of non-commenting source statements per class.
     */
    private int ncssPerClassMin = -1;
    /**
     * The maximum number of javadoc comments per class.
     */
    private int jvdcPerClassMax = Integer.MAX_VALUE;
    /**
     * The minimum number of javadoc comments per class.
     */
    private int jvdcPerClassMin = -1;
    /**
     * The maximum number of javadoc comments per function.
     */
    private int jvdcPerFuncMax = Integer.MAX_VALUE;
    /**
     * The minimum number of javadoc comments per function.
     */
    private int jvdcPerFuncMin = -1;
    /**
     * The maximum value of the Cyclomatic Complexity Number per function.
     */
    private int ccnPerFuncMax = Integer.MAX_VALUE;
    /**
     * The minimum value of the Cyclomatic Complexity Number per function.
     */
    private int ccnPerFuncMin = -1;
    /**
     * The maximum number of non-commenting source statements per function.
     */
    private int ncssPerFuncMax = Integer.MAX_VALUE;
    /**
     * The minimum number of non-commenting source statements per function.
     */
    private int ncssPerFuncMin = -1;
    /**
     * Whether package metrics should be generated.
     */
    private boolean packageMetrics = true;
    /**
     * Whether class metrics should be generated.
     */
    private boolean classMetrics = true;
    /**
     * Whether function metrics should be generated.
     */
    private boolean functionMetrics = true;
    /**
     * Whether to generate a report.
     */
    private boolean generateReport = false;
    /**
     * The JavaNCSS object containing details of the code whose metrics are
     * to be checked.
     */
    private Javancss javancss;

    /**
     * Creates a new instance of the task.
     */
    public JavancssAntTask() {
        commandline.setClassname("javancss.Main");
    }

    /**
     * Sets the format of the output file.
     * @param format the format of the output file. Allowable values are 'plain'
     * or 'xml'.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Whether package metrics should be generated.
     * @param packageMetrics true if they should; false otherwise.
     */
    public void setPackageMetrics(boolean packageMetrics) {
        this.packageMetrics = packageMetrics;
    }

    /**
     * Whether class/interface metrics should be generated.
     * @param classMetrics true if they should; false otherwise.
     */
    public void setClassMetrics(boolean classMetrics) {
        this.classMetrics = classMetrics;
    }

    /**
     * Whether function metrics should be generated.
     * @param functionMetrics true if they should; false otherwise.
     */
    public void setFunctionMetrics(boolean functionMetrics) {
        this.functionMetrics = functionMetrics;
    }

    /**
     * Whether a report should be generated. Default is false.
     * @param generateReport true if they should; false otherwise.
     */
    public void setGenerateReport(boolean generateReport) {
        this.generateReport = generateReport;
    }

    /**
     * Sets the directory to be scanned by the tool. This should be the
     * directory containing the source files whose metrics are to be
     * analysed.
     * @param srcdir the directory to be scanned by the tool.
     */
    public void setSrcdir(File srcdir) {
        this.srcdir = srcdir;
    }

    /**
     * Sets the location of the output file.
     * @param outputfile the location of the output file.
     */
    public void setOutputfile(File outputfile) {
        this.outputfile = outputfile;
    }

    /**
     * Set the classpath to be used.
     * @param classpath the classpath to be used.
     */
    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    /**
     * Sets whether the build should halt if there is an error or a threshold is
     * exceeded.
     * @param abortOnFail true if it should; false otherwise.
     */
    public void setAbortOnFail(boolean abortOnFail) {
        this.abortOnFail = abortOnFail;
    }

    /**
     * Executes this task.
     * @throws BuildException if an error occurs.
     */
    public void execute() throws BuildException {
        if (srcdir == null) {
            throw new BuildException("srcdir attribute must be set!");
        }
        if (!srcdir.exists()) {
            throw new BuildException("srcdir does not exist!");
        }
        if (!srcdir.isDirectory()) {
            throw new BuildException("srcdir is not a directory!");
        }

        List fileList = findFilesToAnalyse();

        // First check thresholds
        if (thresholdsExceeded(fileList) && abortOnFail) {
            throw new BuildException("Metric threshold value(s) surpassed");
        }

        // Then generate report
        int exitValue = generateReport(fileList);
        if (exitValue == FAILURE) {
            if (abortOnFail) {
                throw new BuildException("JavaNcss failed", location);
            } else {
                log("JavaNcss failed", Project.MSG_ERR);
            }
        }
    }

    /**
     * Generates a report on the specified files.
     * @param fileList the files to be analyzed.
     * @return {@link #SUCCESS} if there were no errors; otherwise {@link #FAILURE}.
     * @throws BuildException if an error occurs whilst generating the report.
     */
    private int generateReport(List fileList) {
        // If an output file has not been specified no report should be
        // generated
        if (!generateReport) {
            return SUCCESS;
        }

        // result is in this.javancssArguments
        log("Generating report");
        if (outputfile != null) {
            log("Report to be stored in " + outputfile.getPath(), Project.MSG_VERBOSE);
        } else {
            log("Report to be sent to standard output", Project.MSG_VERBOSE);
        }
        String[] javancssArguments = getCommandLineArguments(fileList);
        log("Executing: javancss " + Util.objectsToVector(javancssArguments)
            , Project.MSG_VERBOSE);

        try {
            Javancss javancss = new Javancss(javancssArguments);

            if (javancss.getLastError() == null) {
                return SUCCESS;
            }
        }
        catch (IOException ioe)
        {
            log("IO exception while executing JavaNCSS: " + ioe.getMessage(), Project.MSG_ERR);
        }

        return FAILURE;
    }

    /**
     * Checks to see if the metrics of the specified files have exceeded any of
     * the thresholds.
     * @param fileList the files to be analysed.
     * @return true if any of the thresholds have been exceeded; false otherwise.
     */
    private boolean thresholdsExceeded(List fileList) {
        return packageThresholdsExceeded(fileList) ||
                classThresholdsExceeded(fileList) ||
                functionThresholdsExceeded(fileList);
    }


    /**
     * Builds a list of all files to be analysed. We need to do this when
     * testing thresholds as the Javancss object does not have a constructor
     * that lets us make use of the -recursive option
     */
    private List findFilesToAnalyse() {
        DirectoryScanner ds = super.getDirectoryScanner(srcdir);
        String files[] = ds.getIncludedFiles();
        if (files.length == 0) {
            log("No files in specified directory " + srcdir, 3);
        }
        return copyFiles(files);
    }

    /**
     * Converts the specified array of filenames into a vector of paths.
     * @param filesArray an array of filenames.
     * @return a vector of paths. The path is constructed by prepending this
     * task's source directory to each filename.
     */
    private List copyFiles(String[] filesArray) {
        List returnVector = new ArrayList(filesArray.length);
        for (int i = 0; i < filesArray.length; i++) {
            returnVector.add(new File(srcdir, filesArray[i]));
        }
        return returnVector;
    }

    /**
     * Maybe creates a nested classpath element.
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(project);
        }
        return classpath.createPath();
    }

    /**
     * Gets the command line arguments to be sent to JavaNCSS.
     * @param fileList a list of all source files to be analysed.
     * @return the command line arguments to be sent to JavaNCSS.
     */
    private String[] getCommandLineArguments(List fileList) {
        List arguments = new ArrayList();

        // Set metrics to be generated
        if (packageMetrics) {
            arguments.add("-package");
        }
        if (classMetrics) {
            arguments.add("-object");
        }
        if (functionMetrics) {
            arguments.add("-function");
        }

        // Set format of report
        if (format.equals("xml")) {
            arguments.add("-xml");
        }

        // Set location of report
        if (outputfile != null) {
            arguments.add("-out");
            arguments.add(outputfile.getPath());
        }

        // Set source code to be processed
        arguments.add("@" + createSourceListFile(fileList).getPath());

        String[] javancssArguments = new String[arguments.size()];
        for (int argument = 0; argument < arguments.size(); argument++) {
            javancssArguments[argument] = (String) arguments.get(argument);
        }
        return javancssArguments;
    }

    /**
     * Creates a temporary file containing a list of all source files to be
     * analysed.
     * @param fileList the source files to be analysed.
     * @return a file containing a list of all specified source files.
     */
    private File createSourceListFile(List fileList) {
        File srcListFile;
        try {
            srcListFile = File.createTempFile("srcList", null);
            srcListFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(srcListFile);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < fileList.size(); i++) {
                log(fileList.get(i).toString(), 3);
                pw.println(fileList.get(i).toString());
            }
            pw.close();
            fos.close();
        } catch (IOException e) {
            throw new BuildException(e, location);
        }
        return srcListFile;
    }

    /**
     * Gets the JavaNCSS object containing details of the code whose metrics are
     * to be checked.
     * @param fileList the files to be analysed.
     * @return the JavaNCSS object containing details of the code whose metrics
     * are to be checked.
     */
    private Javancss getJavaNcss(List fileList)
    {
        if (javancss == null)
        {
            log("Checking metrics on " + fileList.size() + " files");
            javancss = new Javancss(fileList);
        }
        return javancss;
    }

    /**
     * Checks package thresholds for all packages.
     * @param fileList the files to be analysed.
     * @return true if a threshold has been exceeded; false otherwise.
     */
    private boolean packageThresholdsExceeded(List fileList) {
        boolean failed = false;
        if (!((classPerPkgMax == Integer.MAX_VALUE) &&
                (classPerPkgMin == -1) &&
                (funcPerPkgMax == Integer.MAX_VALUE) &&
                (funcPerPkgMin == -1) &&
                (ncssPerPkgMax == Integer.MAX_VALUE) &&
                (ncssPerPkgMin == -1))) {
            List pkgMetrics = getJavaNcss(fileList).getPackageMetrics();
            for (int i = 0; i < pkgMetrics.size(); i++) {
                PackageMetric pkgMetric = (PackageMetric) pkgMetrics.get(i);
                failed = packageThresholdExceeded(pkgMetric) || failed;
            }
        }
        return failed;
    }

    /**
     * Checks thresholds for the specified package.
     * @param packageMetrics the metrics of the package under test.
     * @return true if a threshold has been exceeded; false otherwise.
     */
    private boolean packageThresholdExceeded(PackageMetric packageMetrics) {
        boolean failed = false;
        String errorMsg = "";
        if (classPerPkgMax < packageMetrics.classes) {
            failed = true;
            errorMsg = packageMetrics.classes + " classes exceeds maximum per package";
        } else if (classPerPkgMin > packageMetrics.classes) {
            failed = true;
            errorMsg = packageMetrics.classes + " classes does not meet minimum per package";
        }
        if (funcPerPkgMax < packageMetrics.functions) {
            failed = true;
            errorMsg = packageMetrics.functions + " functions exceeds maximum pre package";
        } else if (funcPerPkgMin > packageMetrics.functions) {
            failed = true;
            errorMsg = packageMetrics.functions + " functions does not meet minimum per package";
        }
        if (ncssPerPkgMax < packageMetrics.ncss) {
            failed = true;
            errorMsg = packageMetrics.ncss + " NCSS exceeds maximum per package";
        } else if (ncssPerPkgMin > packageMetrics.ncss) {
            failed = true;
            errorMsg = packageMetrics.ncss + " NCSS does not meet minimum per package";
        }

        if (failed) {
            log(packageMetrics.name + " - " + errorMsg, Project.MSG_INFO);
        }
        return failed;
    }

    /**
     * Checks thresholds for all classes and interfaces.
     * @param fileList the files to be analysed.
     * @return true if a threshold has been exceeded; false otherwise.
     */
    private boolean classThresholdsExceeded(List fileList) {
        boolean failed = false;
        if (!((classPerClassMax == Integer.MAX_VALUE) &&
                (classPerClassMin == -1) &&
                (funcPerClassMax == Integer.MAX_VALUE) &&
                (funcPerClassMin == -1) &&
                (jvdcPerClassMax == Integer.MAX_VALUE) &&
                (jvdcPerClassMin == -1) &&
                (ncssPerClassMax == Integer.MAX_VALUE) &&
                (ncssPerClassMin == -1))) {
            List objMetrics = getJavaNcss(fileList).getObjectMetrics();
            for (int i = 0; i < objMetrics.size(); i++) {
                ObjectMetric objMetric = (ObjectMetric) objMetrics.get(i);
                failed = classThresholdExceeded(objMetric) || failed;
            }
        }
        return failed;
    }

    /**
     * Checks thresholds for the specified class or interface.
     * @param classMetrics the metrics of the class or interface under test.
     * @return true if a threshold has been exceeded; false otherwise.
     */
    private boolean classThresholdExceeded(ObjectMetric classMetrics) {
        boolean failed = false;
        String errorMsg = "";

        int classPerClass = classMetrics.classes;
        int funcPerClass = classMetrics.functions;
        int ncssPerClass = classMetrics.ncss;
        int jvdcPerClass = classMetrics.javadocs;

        if (classPerClassMax < classPerClass) {
            failed = true;
            errorMsg = classPerClass + " inner classes exceeds maximum per class";
        } else if (classPerClassMin > classPerClass) {
            failed = true;
            errorMsg = classPerClass + " inner classes does not meet minimum per class";
        }
        if (funcPerClassMax < funcPerClass) {
            failed = true;
            errorMsg = funcPerClass + " functions exceeds maximum pre class";
        } else if (funcPerClassMin > funcPerClass) {
            failed = true;
            errorMsg = funcPerClass + " functions does not meet minimum per class";
        }
        if (ncssPerClassMax < ncssPerClass) {
            failed = true;
            errorMsg = ncssPerClass + " NCSS exceeds maximum per class";
        } else if (ncssPerClassMin > ncssPerClass) {
            failed = true;
            errorMsg = ncssPerClass + " NCSS does not meet minimum per class";
        }
        if (jvdcPerClassMax < jvdcPerClass) {
            failed = true;
            errorMsg = jvdcPerClass + " javadoc statements exceeds maximum per class";
        } else if (jvdcPerClassMin > jvdcPerClass) {
            failed = true;
            errorMsg = jvdcPerClass + " javadoc statements does not meet minimum per class";
        }

        if (failed) {
            log(classMetrics.name + " - " + errorMsg, Project.MSG_INFO);
        }
        return failed;
    }

    /**
     * Checks thresholds for all functions.
     * @param fileList the files to be analysed.
     * @return true if a threshold has been exceeded; false otherwise.
     */
    private boolean functionThresholdsExceeded(List fileList) {
        boolean failed = false;
        //check thresholds
        if (!((jvdcPerFuncMax == Integer.MAX_VALUE) &&
                (jvdcPerFuncMin == -1) &&
                (ccnPerFuncMax == Integer.MAX_VALUE) &&
                (ccnPerFuncMin == -1) &&
                (ncssPerFuncMax == Integer.MAX_VALUE) &&
                (ncssPerFuncMin == -1))) {
            //call getFunctionMetrics
            List funcMetrics = getJavaNcss(fileList).getFunctionMetrics();
            for (int i = 0; i < funcMetrics.size(); i++) {
                FunctionMetric funcMetric = (FunctionMetric) funcMetrics.get(i);
                failed = functionThresholdExceeded(funcMetric) || failed;
            }
        }
        return failed;
    }

    /**
     * Checks thresholds for the specified function.
     * @param functionMetrics the metrics of the function under test.
     * @return true if a threshold has been exceeded; false otherwise.
     */
    private boolean functionThresholdExceeded(FunctionMetric functionMetrics) {
        boolean failed = false;
        String errorMsg = "";

        int ccnPerFunc = functionMetrics.ccn;
        int ncssPerFunc = functionMetrics.ncss;
        int jvdcPerFunc = functionMetrics.javadocs;

        if (ccnPerFuncMax < ccnPerFunc) {
            failed = true;
            errorMsg = ccnPerFunc + " CCN exceeds maximum per function";
        } else if (ccnPerFuncMin > ccnPerFunc) {
            failed = true;
            errorMsg = ccnPerFunc + " CCN does not meet minimum per function";
        }
        if (ncssPerFuncMax < ncssPerFunc) {
            failed = true;
            errorMsg = ncssPerFunc + " NCSS exceeds maximum per function";
        } else if (ncssPerFuncMin > ncssPerFunc) {
            failed = true;
            errorMsg = ncssPerFunc + " NCSS does not meet minimum per function";
        }
        if (jvdcPerFuncMax < jvdcPerFunc) {
            failed = true;
            errorMsg = jvdcPerFunc + " javadoc statements exceeds maximum per function";
        } else if (jvdcPerFuncMin > jvdcPerFunc) {
            failed = true;
            errorMsg = jvdcPerFunc + " javadoc statements does not meet minimum per function";
        }

        if (failed) {
            log(functionMetrics.name + " - " + errorMsg, Project.MSG_INFO);
        }
        return failed;
    }

    /**
     * Sets the maximum number of classes per package.
     * @param classPerPkgMax the maximum number of classes per package.
     */
    public void setClassPerPkgMax(int classPerPkgMax) {
        this.classPerPkgMax = classPerPkgMax;
    }

    /**
     * Sets the minimum number of classes per package.
     * @param classPerPkgMin the minimum number of classes per package.
     */
    public void setClassPerPkgMin(int classPerPkgMin) {
        this.classPerPkgMin = classPerPkgMin;
    }

    /**
     * Sets the maximum number of functions per package.
     * @param funcPerPkgMax the maximum number of functions per package.
     */
    public void setFuncPerPkgMax(int funcPerPkgMax) {
        this.funcPerPkgMax = funcPerPkgMax;
    }

    /**
     * Sets the minimum number of functions per package.
     * @param funcPerPkgMin the minimum number of functions per package.
     */
    public void setFuncPerPkgMin(int funcPerPkgMin) {
        this.funcPerPkgMin = funcPerPkgMin;
    }

    /**
     * Sets the maximum number of non-commenting source statements per package.
     * @param ncssPerPkgMax the maximum number of non-commenting source
     * statements per package.
     */
    public void setNcssPerPkgMax(int ncssPerPkgMax) {
        this.ncssPerPkgMax = ncssPerPkgMax;
    }

    /**
     * Sets the minimum number of non-commenting source statements per package.
     * @param ncssPerPkgMin the minimum number of non-commenting source
     * statements per package.
     */
    public void setNcssPerPkgMin(int ncssPerPkgMin) {
        this.ncssPerPkgMin = ncssPerPkgMin;
    }

    /**
     * Sets the maximum number of inner classes per class.
     * @param classPerClassMax the maximum number of inner classes per class.
     */
    public void setClassPerClassMax(int classPerClassMax) {
        this.classPerClassMax = classPerClassMax;
    }

    /**
     * Sets the minimum number of inner classes per class.
     * @param classPerClassMin the minimum number of inner classes per class.
     */
    public void setClassPerClassMin(int classPerClassMin) {
        this.classPerClassMin = classPerClassMin;
    }

    /**
     * Sets the maximum number of functions per class.
     * @param funcPerClassMax the maximum number of functions per class.
     */
    public void setFuncPerClassMax(int funcPerClassMax) {
        this.funcPerClassMax = funcPerClassMax;
    }

    /**
     * Sets the minimum number of functions per class.
     * @param funcPerClassMin the minimum number of functions per class.
     */
    public void setFuncPerClassMin(int funcPerClassMin) {
        this.funcPerClassMin = funcPerClassMin;
    }

    /**
     * Sets the maximum number of non-commenting source statements per class.
     * @param ncssPerClassMax the maximum number of non-commenting source
     * statements per class.
     */
    public void setNcssPerClassMax(int ncssPerClassMax) {
        this.ncssPerClassMax = ncssPerClassMax;
    }

    /**
     * Sets the minimum number of non-commenting source statements per class.
     * @param ncssPerClassMin the minimum number of non-commenting source
     * statements per class.
     */
    public void setNcssPerClassMin(int ncssPerClassMin) {
        this.ncssPerClassMin = ncssPerClassMin;
    }

    /**
     * Sets the maximum number of javadoc comments per class.
     * @param jvdcPerClassMax the maximum number of javadoc comments per class.
     */
    public void setJvdcPerClassMax(int jvdcPerClassMax) {
        this.jvdcPerClassMax = jvdcPerClassMax;
    }

    /**
     * Sets the minimum number of javadoc comments per class.
     * @param jvdcPerClassMin the minimum number of javadoc comments per class.
     */
    public void setJvdcPerClassMin(int jvdcPerClassMin) {
        this.jvdcPerClassMin = jvdcPerClassMin;
    }

    /**
     * Sets the maximum value of the Cyclomatic Complexity Number per function.
     * @param ccnPerFuncMax the maximum value of the Cyclomatic Complexity Number
     * per function.
     */
    public void setCcnPerFuncMax(int ccnPerFuncMax) {
        this.ccnPerFuncMax = ccnPerFuncMax;
    }

    /**
     * Sets the minimum value of the Cyclomatic Complexity Number per function.
     * @param ccnPerFuncMin the minimum value of the Cyclomatic Complexity Number
     * per function.
     */
    public void setCcnPerFuncMin(int ccnPerFuncMin) {
        this.ccnPerFuncMin = ccnPerFuncMin;
    }

    /**
     * Sets the maximum number of non-commenting source statements per function.
     * @param ncssPerFuncMax the maximum number of non-commenting source
     * statements per function.
     */
    public void setNcssPerFuncMax(int ncssPerFuncMax) {
        this.ncssPerFuncMax = ncssPerFuncMax;
    }

    /**
     * Sets the minimum number of non-commenting source statements per function.
     * @param ncssPerFuncMin the minimum number of non-commenting source
     * statements per function.
     */
    public void setNcssPerFuncMin(int ncssPerFuncMin) {
        this.ncssPerFuncMin = ncssPerFuncMin;
    }

    /**
     * Sets the maximum number of javadoc comments per function.
     * @param jvdcPerFuncMax the maximum number of javadoc comments per function.
     */
    public void setJvdcPerFuncMax(int jvdcPerFuncMax) {
        this.jvdcPerFuncMax = jvdcPerFuncMax;
    }

    /**
     * Sets the minimum number of javadoc comments per function.
     * @param jvdcPerFuncMin the minimum number of javadoc comments per function.
     */
    public void setJvdcPerFuncMin(int jvdcPerFuncMin) {
        this.jvdcPerFuncMin = jvdcPerFuncMin;
    }
}
