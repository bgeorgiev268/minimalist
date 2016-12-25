package ch.tutteli.minimaltestsuite

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.regex.Pattern

class SimpleFileSearcherTest {
    @Rule @JvmField val tempFolder: TemporaryFolder = TemporaryFolder()
    private val testee = SimpleFileSearcher()

    @Test fun inFolder_NotAFolder_IllegalArgumentException() {
        //arrange
        val fileName = "test.class"
        val file = tempFolder.newFile(fileName)
        //act & assert
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { testee.searchFilesStartingWith("Test").inFolder(file) }
            .withMessageContaining(fileName)
    }

    @Test fun inFolder_NoFiles_EmptySet() {
        //arrange
        val folder = tempFolder.newFolder("test")
        //act
        val result = testee.searchFilesStartingWith("Test").inFolder(folder)
        //assert
        assertThat(result).isEmpty()
    }

    @Test fun searchFilesStartingWith_SeveralFilesOneMatches_SetWithFile() {
        //arrange
        val folder = setUpFolderWithFiles("test", "NotStartingWithTest", 5)
        val testFile = tempFolder.newFileInFolder("TestA.class", folder)
        //act
        val result = testee.searchFilesStartingWith("Test").inFolder(folder)
        //assert
        assertThat(result).containsOnly(testFile)
    }

    @Test fun searchFilesStartingWith_SeveralFilesAndTwoMatch_SetWithBothFiles() {
        //arrange
        val folder = setUpFolderWithFiles("test", "NotStartingWithTest", 5)
        val testFile1 = tempFolder.newFileInFolder("TestA.class", folder)
        val testFile2 = tempFolder.newFileInFolder("TestB.class", folder)
        //act
        val result = testee.searchFilesStartingWith("Test").inFolder(folder)
        //assert
        assertThat(result).containsOnly(testFile1, testFile2)
    }

    @Test fun searchFiles_SeveralFilesAndTwoMatch_SetWithBothFiles() {
        //arrange
        val folder = setUpFolderWithFiles("test", "Test", 5)
        val testFile1 = tempFolder.newFileInFolder("TestA.class", folder)
        val testFile2 = tempFolder.newFileInFolder("TestA$1.class", folder)
        //act
        val result = testee
            .searchFiles(Pattern.compile("^TestA(?:\\$[^\\\\.]+)?\\.class$"))
            .inFolder(folder)
        //assert
        assertThat(result).containsOnly(testFile1, testFile2)
    }

    private fun setUpFolderWithFiles(folderName: String, fileNamePrefix: String, quantity: Int): File {
        val folder = tempFolder.newFolder(folderName)
        for (i in 1..quantity) {
            tempFolder.newFile("$folderName/$fileNamePrefix$i.class")
        }
        return folder
    }
}

