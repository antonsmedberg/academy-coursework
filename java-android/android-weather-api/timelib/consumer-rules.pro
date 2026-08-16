# Se till att timelib:s API finns kvar i appens R8-pass
-keep class com.example.timelib.** { *; }
-keep enum  com.example.timelib.** { *; }
-dontwarn   com.example.timelib.**