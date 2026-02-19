# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep application entry points


# Remove broad component keeps so class names can be obfuscated.

# Flatten the package hierarchy
-repackageclasses ''

# Allow R8 to modify access levels for more optimization
-allowaccessmodification

# Reuse method names as much as possible
-overloadaggressively

# Allow unrelated class members to have the same obfuscated name
-useuniqueclassmembernames

# (Optional) Provide a dictionary for renaming classes and members
#-obfuscationdictionary dictionary.txt
#-classobfuscationdictionary dictionary.txt

