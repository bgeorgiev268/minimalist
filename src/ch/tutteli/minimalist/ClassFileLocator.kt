package ch.tutteli.minimalist

import java.io.File
import java.util.regex.Pattern

interface ClassFileLocator {
    fun locate(changedFile: String) : Set<File>
    fun supportedFilePattern() : Pattern
}
