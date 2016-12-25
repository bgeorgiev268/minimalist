package ch.tutteli.minimaltestsuite

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class SimpleFileSearcherTest {
    @Rule @JvmField val tempFolder: TemporaryFolder = TemporaryFolder()
    private val testee = SimpleFileSearcher()

    @Test fun searchFilesStartingWith_NotAFolder_IllegalArgumentException() {
        //arrange
        val fileName = "test.class"
        val file = tempFolder.newFile(fileName)
        //act & assert
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { testee.searchFilesStartingWith("Test").inFolder(file) }
            .withMessageContaining(fileName)
    }

    @Test fun searchFileStartingWith_NoFiles_EmptySet() {
        //arrange
        val folder = tempFolder.newFolder("test")
        //act
        val result = testee.searchFilesStartingWith("Test").inFolder(folder)
        //assert
        assertThat(result).isEmpty()
    }

    @Test fun searchFileStartingWith_SeveralFilesOneMatches_SetWithFile() {
        //arrange
        val folderName = "test"
        val folder = setUpFolderWithFiles(folderName, "NotStartingWithTest", 5)
        val testFile = tempFolder.newFile(folderName + "/TestA.class")
        //act
        val result = testee.searchFilesStartingWith("Test").inFolder(folder)
        //assert
        assertThat(result).contains(testFile)
    }

    @Test fun searchFileStartingWith_SeveralFilesTwoMatch_SetWithBothFiles() {
        //arrange
        val folderName = "test"
        val folder = setUpFolderWithFiles(folderName, "NotStartingWithTest", 5)
        val testFile1 = tempFolder.newFile(folderName + "/TestA.class")
        val testFile2 = tempFolder.newFile(folderName + "/TestB.class")
        //act
        val result = testee.searchFilesStartingWith("Test").inFolder(folder)
        //assert
        assertThat(result).contains(testFile1, testFile2)
    }

    private fun setUpFolderWithFiles(folderName: String, fileNamePrefix: String, quantity: Int): File {
        val folder = tempFolder.newFolder(folderName)
        for (i in 1..quantity) {
            tempFolder.newFile("$folderName/$fileNamePrefix$i.class")
        }
        return folder
    }
}

