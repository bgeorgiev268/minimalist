package ch.tutteli.minimaltestsuite

class JavaClassFileLocator_SimpleFileSearcherTest : JavaClassFileLocator_FileSearcherTest() {
    override fun createFileSearcher() = SimpleFileSearcher()
}

