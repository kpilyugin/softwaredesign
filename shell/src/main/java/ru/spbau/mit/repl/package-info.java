/*
* This package contains a read-eval-print loop shell prototype with basic commands:
* echo, cat, wc, pwd, grep, exit and external process.
*
* It contains the central class Shell, which builds a map of commands, each of them implement
* Command interface and can be executed with given arguments, input and output streams.
* Shell manages environmental variables and their substitution via ShellVariables class and uses
* Parser util for choosing right command and parsing its arguments.
 */
package ru.spbau.mit.repl;