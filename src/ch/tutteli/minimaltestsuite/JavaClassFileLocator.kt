package ch.tutteli.minimaltestsuite

import java.io.File
import java.util.regex.Pattern

class JavaClassFileLocator private constructor(
    private val fileSearcher: FileSearcher,
    private val workDirectoryPath: String,
    private val srcPath: String,
    private val classesDirectoryPath: String) : ClassFileLocator {

    constructor(fileSearcher: FileSearcher, workDirectory: File, srcPath: String, classesDirectory: File) : this(
        fileSearcher,
        checkFolderAndConvertToPath(workDirectory),
        if (srcPath.endsWith('/')) srcPath else srcPath + '/',
        checkFolderAndConvertToPath(classesDirectory)
    )

    companion object {
        private fun checkFolderAndConvertToPath(folder: File): String {
            if (!folder.exists()) throw IllegalArgumentException("$folder does not exist")
            if (!folder.isDirectory) throw IllegalArgumentException("$folder is not a directory")
            return folder.absolutePath + '/'
        }
    }

    override fun supportedFilePattern(): Pattern = Pattern.compile("[^\\\\.]+\\.java")

    override fun locate(changedFile: String): Set<File> {
        val srcAbsolutePath = workDirectoryPath + srcPath
        val fileAbsolutePath = workDirectoryPath + changedFile
        val fileInclPackage = fileAbsolutePath.substringAfter(srcAbsolutePath)
        val packagePath = fileInclPackage.substringBeforeLast('/', "")
        val className = fileInclPackage.substringAfterLast('/').substringBefore(".java")
        val packageInOutput = File(classesDirectoryPath + packagePath)
        if (packageInOutput.exists()) {
            return fileSearcher
                .searchFiles(createClassFilePattern(className))
                .inFolder(packageInOutput)
        }
        return emptySet()
    }

    private fun createClassFilePattern(className: String): Pattern {
        return Pattern.compile("^" + Pattern.quote(className) + "(?:\\$[^\\.]+)?\\.class$")
    }
}
