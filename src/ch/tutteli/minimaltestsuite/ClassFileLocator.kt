package ch.tutteli.minimaltestsuite

import java.io.File
import java.util.regex.Pattern

interface ClassFileLocator {
    fun locate(changedFile: String) : Set<File>
    fun supportedFilePattern() : Pattern
}
