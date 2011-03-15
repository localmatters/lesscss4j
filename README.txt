What is LessCSS4j?
------------------

LessCSS4j is an implementation of the LESS language defined by the Ruby
Gem (http://lesscss.org).  It is designed to be a library for use in your
own applications or as a stand alone command line application.  In both
cases, the goal is to "compile" a LESS file into valid CSS.


LessCSS4j's "jlessc.sh" command is nearly 80% faster than the Ruby "lessc"
command.


Licensing
---------

LessCSS4j is licensed under ?????

See LICENSE.txt for more information


Getting Started
---------------

From the command line:

$ jlessc.sh myfile.less

This will produce the file "myfile.css" in the same directory as
"myfile.less".

In Java:

StyleSheetResource resource = new FileStyleSheetResource(filename);
LessCssCompiler compiler = new DefaultLessCssCompilerFactory().create();
compiler.compile(resource, System.out, null);
