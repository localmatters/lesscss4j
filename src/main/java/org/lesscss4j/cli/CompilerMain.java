/**
 * File: CompilerMain.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 29, 2010
 * Creation Time: 10:15:36 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.lesscss4j.compile.LessCssCompilerImpl;
import org.lesscss4j.output.PrettyPrintOptions;
import org.lesscss4j.output.StyleSheetWriterImpl;

public class CompilerMain {
    public boolean _prettyPrint = false;
    public PrettyPrintOptions _prettyPrintOptions;

    public PrettyPrintOptions getPrettyPrintOptions() {
        return _prettyPrintOptions;
    }

    public void setPrettyPrintOptions(PrettyPrintOptions prettyPrintOptions) {
        _prettyPrintOptions = prettyPrintOptions;
    }

    public boolean isPrettyPrint() {
        return _prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        _prettyPrint = prettyPrint;
    }

    protected void usage() {
        System.out.println("Usage: jlessc [options] [input] [output]");
        System.out.println("  -h,  --help               Display this help message");
        System.out.println("  -v,  --version            Display the version");
        System.out.println("  -f,  --format             Format (pretty print) the CSS file");
        System.out.println("  -nf, --no-format          Don't format (pretty print) the CSS file");
        System.out.println("  -i,  --indent <value>     Indent VALUE spaces");
        System.out.println("  -l,  --line-break         Place a blank line between CSS rule sets");
        System.out.println("  -nl, --no-line-break      Don't place a blank line between CSS rule sets");
        System.out.println("  -s,  --single-line        Place single declarations rulesets on one line");
        System.out.println("  -ns, --no-single-line     Don't place single declaration rulesets on one line");
        System.out.println("  -b,  --brace-newline      Place opening braces on their own line");
        System.out.println("  -nb  --no-brace-newline   Don't place opening braces on their own line");
    }

    protected void version() {
        System.out.println("jlessc <todo:VERSION>");
    }

    protected boolean isOption(String arg, String... options) {
        for (String option : options) {
            if (option.equals(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the argument is an "anti" option, meaning that is disables a regular option.  If the regular options
     * are --something or -s, the "anti" options are --no-something or -ns.
     * @param arg
     * @return
     */
    protected boolean isAntiOption(String arg) {
        return arg.startsWith("--no") || (arg.startsWith("-n") && arg.length() >= 3);
    }


    public int run(String[] args) {

        String inputFilename = null;
        String outputFilename = null;

        boolean argsFinished = false;

        PrettyPrintOptions formatOptions = new PrettyPrintOptions();

        for (int idx = 0; idx < args.length; idx++) {
            String arg = args[idx];
            if (arg.equals("--")) {
                argsFinished = true;
            }
            if (!argsFinished && isOption(arg, "--help", "-h")) {
                usage();
                return 1;
            }
            else if (!argsFinished && isOption(arg, "--version", "-v")) {
                version();
            }
            else if (!argsFinished && isOption(arg, "--format", "-f", "--no-format", "-nf")) {
                setPrettyPrint(!isAntiOption(arg));
            }
            else if (!argsFinished && isOption(arg, "--indent", "-i")) {
                String indent = args[++idx];
                try {
                    formatOptions.setIndentSize(Integer.parseInt(indent));
                }
                catch (NumberFormatException ex) {
                    System.err.println("Invalid indent value: " + indent);
                    return -1;
                }
            }
            else if (!argsFinished && isOption(arg, "--line-break", "-l", "--no-line-break", "-nl")) {
                formatOptions.setLineBetweenRuleSets(!isAntiOption(arg));
            }
            else if (!argsFinished && isOption(arg, "--single-line", "-s", "--no-single-line", "-ns")) {
                formatOptions.setSingleDeclarationOnOneLine(!isAntiOption(arg));
            }
            else if (!argsFinished && isOption(arg, "--brace-newline", "-b", "--no-brace-newline", "-nb")) {
                formatOptions.setOpeningBraceOnNewLine(!isAntiOption(arg));
            }
            else if (!argsFinished &&
                     (arg.startsWith("--") || (arg.startsWith("-") && arg.length() > 1))) {
                System.err.println("Unknown option: " + arg);
            }
            else{
                if (inputFilename == null) {
                    inputFilename = arg;
                }
                else if (outputFilename == null) {
                    outputFilename = arg;
                }
                else {
                    // error
                }
            }
        }

        setPrettyPrintOptions(formatOptions);

        try {
            compile(inputFilename, outputFilename);
        }
        catch (IOException io) {
            System.err.println(io.toString());
        }

        return 0;
    }

    public void compile(String inputFilename, String outputFilename) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {

            // Generate an output filename from the input filename
            if (outputFilename == null && inputFilename != null) {
                outputFilename = generateOutputFilename(inputFilename);
            }

            input = createInputStream(inputFilename);
            output = createOutputStream(outputFilename);

            LessCssCompilerImpl compiler = new LessCssCompilerImpl();

            ((StyleSheetWriterImpl) compiler.getStyleSheetWriter()).setPrettyPrintEnabled(isPrettyPrint());
            if (isPrettyPrint() && getPrettyPrintOptions() != null) {
                ((StyleSheetWriterImpl) compiler.getStyleSheetWriter()).setPrettyPrintOptions(getPrettyPrintOptions());
            }

            compiler.compile(input, output);
        }
        catch (IOException ex) {
            // delete the bogus output file if we're not writing to stdout
            if (outputFilename != null) {
                new File(outputFilename).delete();
            }
        }
        finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    private OutputStream createOutputStream(String outputFilename) throws IOException {
        if (outputFilename == null || "-".equals(outputFilename)) {
            return System.out;
        }
        else {
            return new FileOutputStream(outputFilename);
        }
    }

    private InputStream createInputStream(String inputFilename) throws FileNotFoundException {
        if (inputFilename == null || inputFilename.equals("-")) {
            return System.in;
        }
        else {
            return new FileInputStream(inputFilename);
        }
    }

    private String generateOutputFilename(String inputFilename) {
        String outputFilename;
        int extIdx = inputFilename.lastIndexOf('.');
        if (extIdx >= 0 && extIdx == inputFilename.length() - 1 &&
            inputFilename.regionMatches(true, extIdx + 1, "less", 0, 4)) {
            outputFilename = inputFilename.substring(0, extIdx) + "css";
        }
        else {
            outputFilename = inputFilename + ".css";
        }
        return outputFilename;
    }

    public static void main(String[] args) {
        int returnValue = new CompilerMain().run(args);
        if (returnValue != 0) {
            System.exit(returnValue);
        }
    }

}
