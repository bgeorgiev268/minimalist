package ch.tutteli.minimalist

class JavaClassFileLocator_SimpleFileSearcherTest : JavaClassFileLocator_FileSearcherTest() {
    override fun createFileSearcher() = SimpleFileSearcher()
}

