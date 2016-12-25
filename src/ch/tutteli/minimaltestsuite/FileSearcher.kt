package ch.tutteli.minimaltestsuite

import java.io.File
import java.util.regex.Pattern

interface FileSearcher {
    fun searchFilesStartingWith(suffix: String): InFolderBuilder

    fun searchFiles(pattern: Pattern): InFolderBuilder

    interface InFolderBuilder {
        fun inFolder(directory: File): Set<File>
    }
}
