package ch.tutteli.minimaltestsuite

import java.io.File
import java.util.*
import java.util.regex.Pattern

class SimpleFileSearcher : FileSearcher {

    override fun searchFilesStartingWith(suffix: String): Builder {
        return Builder(Pattern.compile("^\\Q$suffix\\E.*"))
    }

    class Builder(private val pattern: Pattern) : FileSearcher.Builder {

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

