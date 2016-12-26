package ch.tutteli.minimalist

import java.io.File
import java.util.*
import java.util.regex.Pattern

class SimpleFileSearcher : FileSearcher {
    override fun searchFiles(pattern: Pattern): FileSearcher.InFolderBuilder {
        return InFolderBuilder(pattern)
    }

    override fun searchFilesStartingWith(suffix: String): InFolderBuilder {
        return InFolderBuilder(Pattern.compile("^\\Q$suffix\\E.*"))
    }

    class InFolderBuilder(private val pattern: Pattern) : FileSearcher.InFolderBuilder {

        override fun inFolder(directory: File): Set<File> {
            if (!directory.isDirectory) {
                throw IllegalArgumentException("$directory must be a directory")
            }
            val files = HashSet<File>()
            directory.listFiles().forEach {
                if (pattern.matcher(it.name).matches()) {
                    files.add(it)
                }
            }
            return files
        }
    }

}

