package ch.tutteli.minimalist

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.regex.Pattern

class JavaClassFileLocatorTest {
    @Rule @JvmField val tempFolder = TemporaryFolder()

    @Test fun constructor_NonExistingWorkDir_IllegalArgumentException() {
        //arrange
        val workDir = File("nonExisting")
        val classesDir = tempFolder.newFolder("build", "classes")
        //act & assert
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                JavaClassFileLocator(mock<FileSearcher>(), workDir, "src", classesDir)
            }
    }

    @Test fun constructor_WorkDirIsAFile_IllegalArgumentException() {
        //arrange
        val workDir = tempFolder.newFile("test")
        val classesDir = tempFolder.newFolder("build", "classes")
        //act & assert
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                JavaClassFileLocator(mock<FileSearcher>(), workDir, "src", classesDir)
            }
    }

    @Test fun constructor_NonExistingOutputDir_IllegalArgumentException() {
        //arrange
        val classesDir = File("nonExisting")
        //act & assert
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                JavaClassFileLocator(mock<FileSearcher>(), tempFolder.root, "src", classesDir)
            }
    }

    @Test fun constructor_OutputDirIsAFile_IllegalArgumentException() {
        //arrange
        val classesDir = tempFolder.newFile("test")
        //act & assert
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                JavaClassFileLocator(mock<FileSearcher>(), tempFolder.root, "src", classesDir)
            }
    }

    @Test fun locate_ClassWithoutPackage_DelegatesToFileSearcher() {
        //arrange
        val classesDir = tempFolder.newFolder("build", "classes")
        val (fileSearcher, folderBuilder) = mockFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        testee.locate("src/Test.java")
        //assert
        verify(fileSearcher).searchFiles(any<Pattern>())
        verify(folderBuilder).inFolder(classesDir)
    }

    @Test fun locate_ClassWithPackageAndFolderExists_DelegatesToFileSearcher() {
        //arrange
        val tutteli = tempFolder.newFolder("build", "classes", "ch", "tutteli")
        val classesDir = tempFolder.getFolder("build/classes")
        val (fileSearcher, folderBuilder) = mockFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        testee.locate("src/ch/tutteli/TestA.java")
        //assert
        verify(fileSearcher).searchFiles(any<Pattern>())
        verify(folderBuilder).inFolder(tutteli)
    }

    @Test fun locate_ClassWithPackageAndFolderDoesNotExist_DoesNotDelegateToFileSearcher() {
        //arrange
        val classesDir = tempFolder.newFolder("build", "classes")
        val fileSearcher = mock<FileSearcher>()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        testee.locate("src/ch/tutteli/TestA.java")
        //assert
        verifyZeroInteractions(fileSearcher)
    }

    @Test fun locate_SrcWithSlashInTheEndAndClassWithoutPackage_DelegatesToFileSearcher() {
        //arrange
        val build = tempFolder.newFolder("build", "classes")
        val (fileSearcher, folderBuilder) = mockFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src/", build)
        //act
        testee.locate("src/Test.java")
        //assert
        verify(fileSearcher).searchFiles(any<Pattern>())
        verify(folderBuilder).inFolder(build)
    }

    @Test fun locate_SrcInSubFolderClassWithoutPackage_DelegatesToFileSearcher() {
        //arrange
        val build = tempFolder.newFolder("build", "classes")
        val (fileSearcher, folderBuilder) = mockFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src/java/main", build)
        //act
        testee.locate("src/java/main/Test.java")
        //assert
        verify(fileSearcher).searchFiles(any<Pattern>())
        verify(folderBuilder).inFolder(build)
    }

    @Test fun locate_SrcInSubFolderClassWithPackage_DelegatesToFileSearcher() {
        //arrange
        val tutteli = tempFolder.newFolder("build", "classes", "ch", "tutteli")
        val classesDir = tempFolder.getFolder("build/classes")
        val (fileSearcher, folderBuilder) = mockFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src/main/java", classesDir)
        //act
        testee.locate("src/main/java/ch/tutteli/TestA.java")
        //assert
        verify(fileSearcher).searchFiles(any<Pattern>())
        verify(folderBuilder).inFolder(tutteli)
    }

    @Test fun supportedFilePattern_NonJava_False(){
        //arrange
        val testee = JavaClassFileLocator(mock<FileSearcher>(), tempFolder.root, "src/main/java", tempFolder.newFolder())
        //act
        val result = testee.supportedFilePattern().matcher("Test.kt").matches()
        //assert
        assertFalse(result)
    }

    @Test fun supportedFilePattern_Java_True(){
        //arrange
        val testee = JavaClassFileLocator(mock<FileSearcher>(), tempFolder.root, "src/main/java", tempFolder.newFolder())
        //act
        val result = testee.supportedFilePattern().matcher("Test.java").matches()
        //assert
        assertTrue(result)
    }

    @Test fun supportedFilePattern_DoubleJava_False(){
        //arrange
        val testee = JavaClassFileLocator(mock<FileSearcher>(), tempFolder.root, "src/main/java", tempFolder.newFolder())
        //act
        val result = testee.supportedFilePattern().matcher("Test.java.java").matches()
        //assert
        assertFalse(result)
    }

    private fun mockFileSearcher(): Pair<FileSearcher, FileSearcher.InFolderBuilder> {
        val folderBuilder = mock<FileSearcher.InFolderBuilder>()
        val fileSearcher = mock<FileSearcher> {
            on { searchFiles(any<Pattern>()) } doReturn folderBuilder
        }
        return Pair(fileSearcher, folderBuilder)
    }
}
