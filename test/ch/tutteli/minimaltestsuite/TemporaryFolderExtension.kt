package ch.tutteli.minimaltestsuite

import org.junit.rules.TemporaryFolder
import java.io.File

fun TemporaryFolder.newFileInFolder(fileName: String, folder: File): File {
    if (!folder.isDirectory) throw AssertionError("$folder has to be directory")
    return newFile(folder.absolutePath.substringAfter(root.absolutePath) + '/' + fileName)
}

fun TemporaryFolder.getFolder(folderPath: String): File {
    val folder = File(root, folderPath)
    if(!folder.exists()) throw AssertionError("$folderPath does not exist under $root")
    return folder
}
