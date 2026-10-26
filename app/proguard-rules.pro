# Keep the app's own code whole. Only unused library code gets removed.
-keep class app.mybooks.** { *; }
-keepattributes *Annotation*, InnerClasses, Signature
-dontobfuscate
