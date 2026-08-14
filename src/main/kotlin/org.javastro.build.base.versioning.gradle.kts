// The version of the product itself is read from the 'version.txt' file in the projectdir of a product.
val versionFile = File(projectDir, "version.txt")
val rootFile = File(rootDir, "version.txt")
if(version == "unspecified") {
    if (versionFile.exists()) {
        version = versionFile.readText().trim()
    } else {
        if (rootFile.exists()) {
            logger.warn("version.txt not found in projectDir, using version from rootDir")
            version = rootFile.readText().trim()
        }
    }
}
