package com.akchimwf.loftcoin.util;

import androidx.annotation.NonNull;

import java.util.Locale;

import timber.log.Timber;

/*A Tree for debug builds. Automatically infers the tag from the calling class.*/
/*extending DebugTree for our needs*/
public class DebugTree extends Timber.DebugTree {

    /*Timber use this method in every logging -> override it*/
    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        /*Provides programmatic access to the stack trace information printed by printStackTrace().
        Returns an array of stack trace elements, each representing one stack frame.*/
        /*get Stack trace*/
        final StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();

        /*to check all StackTraceElements when Timber calling log()*/
        /*  for (StackTraceElement ste : stackTrace) {
         super.log(priority, tag, ste.toString(), t);
        }*/

        /*[5] as we need 5th element in Stack trace, could check it a for() above*/
        final StackTraceElement ste = stackTrace[5];
        /*logging only one line with everything, not all Stack trace*/
        super.log(priority, tag, String.format(Locale.US,
                "[%s] %s.%s(%s:%d): %s",
                Thread.currentThread().getName(),
                ste.getClassName(),
                ste.getMethodName(),
                ste.getFileName(),
                ste.getLineNumber(),
                message
        ), t);
    }
}
