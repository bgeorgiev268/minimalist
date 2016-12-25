package ch.tutteli.minimaltestsuite

import java.io.File

interface FileSearcher {
    fun searchFilesStartingWith(suffix: String): Builder

    interface Builder {
        fun inFolder(directory: File): Set<File>
    }
}
