package ch.tutteli.minimalist

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

abstract class JavaClassFileLocator_FileSearcherTest {
    @Rule @JvmField val tempFolder = TemporaryFolder()

    abstract fun createFileSearcher(): FileSearcher

    @Test fun locate_ClassWithoutPackage_ContainsClassFile() {
        //arrange
        val (testClassFile, classesDir) = createTestClassFileInClassesDir()
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        val classFiles = testee.locate("src/Test.java")
        //assert
        assertThat(classFiles).containsOnly(testClassFile)
    }

    @Test fun locate_SrcInSubFolderAndClassWithoutPackage_ContainsClassFile() {
        //arrange
        val (testClassFile, classesDir) = createTestClassFileInClassesDir()
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src/main/java", classesDir)
        //act
        val classFiles = testee.locate("src/main/java/Test.java")
        //assert
        assertThat(classFiles).containsOnly(testClassFile)
    }

    @Test fun locate_ClassWithPackage_ContainsClassFile() {
        //arrange
        val tutteli = tempFolder.newFolder("build", "classes", "ch", "tutteli")
        val testFile = tempFolder.newFileInFolder("TestA.class", tutteli)
        val classesDir = tempFolder.getFolder("build/classes")
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        val classFiles = testee.locate("src/ch/tutteli/TestA.java")
        //assert
        assertThat(classFiles).containsOnly(testFile)
    }

    @Test fun locate_SrcInSubFolderAndClassWithPackage_ContainsClassFile() {
        //arrange
        val tutteli = tempFolder.newFolder("build", "classes", "ch", "tutteli")
        val testFile = tempFolder.newFileInFolder("TestA.class", tutteli)
        val classesDir = tempFolder.getFolder("build/classes")
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src/main/java", classesDir)
        //act
        val classFiles = testee.locate("src/main/java/ch/tutteli/TestA.java")
        //assert
        assertThat(classFiles).containsOnly(testFile)
    }

    @Test fun locate_ClassWithInnerClassAndWithoutPackage_ContainsOnlyBoth() {
        //arrange
        val (testClassFile, classesDir) = createTestClassFileInClassesDir()
        val testInnerClassFile = tempFolder.newFileInFolder("Test\$Companion.class", classesDir)
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        val classFiles = testee.locate("src/Test.java")
        //assert
        assertThat(classFiles).containsOnly(testClassFile, testInnerClassFile)
    }

    @Test fun locate_ClassWithTwoAnonymousClassesAndWithoutPackage_ContainsAllThree() {
        //arrange
        val (testClassFile, classesDir) = createTestClassFileInClassesDir()
        val testAnonymousClassFile1 = tempFolder.newFileInFolder("Test\$1.class", classesDir)
        val testAnonymousClassFile2 = tempFolder.newFileInFolder("Test\$2.class", classesDir)
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        val classFiles = testee.locate("src/Test.java")
        //assert
        assertThat(classFiles).containsOnly(testClassFile, testAnonymousClassFile1, testAnonymousClassFile2)
    }

    @Test fun locate_ClassWithPackageAndSeveralOtherClassesWithSamePrefix_ContainsOnlyClassFile() {
        //arrange
        val tutteli = tempFolder.newFolder("build", "classes", "ch", "tutteli")
        val testFile = tempFolder.newFileInFolder("TestA.class", tutteli)
        val classesDir = tempFolder.getFolder("build/classes")
        tempFolder.newFileInFolder("TestB.class", tutteli)
        tempFolder.newFileInFolder("TestC.class", tutteli)
        val fileSearcher = createFileSearcher()
        val testee = JavaClassFileLocator(fileSearcher, tempFolder.root, "src", classesDir)
        //act
        val classFiles = testee.locate("src/ch/tutteli/TestA.java")
        //assert
        assertThat(classFiles).containsOnly(testFile)
    }

    private fun createTestClassFileInClassesDir(): Pair<File, File> {
        val classesDir = tempFolder.newFolder("build", "classes")
        val testClassFile = tempFolder.newFileInFolder("Test.class", classesDir)
        return Pair(testClassFile, classesDir)
    }
}
