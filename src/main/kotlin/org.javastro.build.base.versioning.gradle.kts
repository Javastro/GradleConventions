// The version of the product itself is read from the 'version.txt' file in the root of a product.
val versionFile = File(rootDir, "version.txt")
if (versionFile.exists()) {
    version = versionFile.readText().trim()
}