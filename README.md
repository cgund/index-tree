# index-tree

A bunch of <a href=http://www.textfiles.com>.txt files</a> are bundled in the .jar file as a resources directory.  The program gets the names of those files.  It then creates an input stream for each one when the user clicks on the hyperlink representing that resource.  It reads the stream, adding each word and the line it occurs on to a binary search tree, which is built as the stream is being read.  The program then traverses the entire tree, building a string to represent each node in it and then returning that tree where it is displayed in a UI.

A different code path can show the user the full text of the resource.
