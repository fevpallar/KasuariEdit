# acak classes, methods, fields, method arguments, dan local variables
-keep,allowobfuscation class * {
    *;
}

# acak2 yang lainnya
-repackageclasses ''
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-optimizations !code/simplification/arithmetic
