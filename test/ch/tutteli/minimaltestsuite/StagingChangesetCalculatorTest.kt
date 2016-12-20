package ch.tutteli.minimaltestsuite

import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

class StagingChangesetCalculatorTest {
    @Rule @JvmField val gitRule = GitRule()

    @Test fun calculate_NewFileButNotInStaging_EmptySet() {
        //arrange
        gitRule.newFileInWorkDir("test.java").filePath
        val testee = StagingChangesetCalculator(gitRule.git)
        //act
        val changeset = testee.calculate()
        //assert
        Assertions.assertThat(changeset).isEmpty()
    }

    @Test fun calculate_NewFileAddedToStaging_TheFile() {
        //arrange
        val fileName = gitRule
            .newFileInWorkDir("test.java")
            .addToGit()
            .filePath
        val testee = StagingChangesetCalculator(gitRule.git)
        //act
        val changeset = testee.calculate()
        //assert
        Assertions.assertThat(changeset).contains(fileName)
    }

    @Test fun calculate_NewFileInSubfolderAddedToStaging_TheFileInclPath() {
        //arrange
        val filePath = gitRule
            .newFileInWorkDir("test.java", "subFolder")
            .addToGit()
            .filePath
        val testee = StagingChangesetCalculator(gitRule.git)
        //act
        val changeset = testee.calculate()
        //assert
        Assertions.assertThat(changeset).contains(filePath)
    }


    @Test fun calculate_ChangedFileButNotInStaging_EmptySet() {
        //arrange
        val filePath = gitRule
            .newFileInWorkDir("test.java")
            .addToGit()
            .commit()
            .changeContent("test")
            .addToGit()
            .filePath
        val testee = StagingChangesetCalculator(gitRule.git)
        //act
        val changeset = testee.calculate()
        //assert
        Assertions.assertThat(changeset).contains(filePath)
    }

    @Test fun calculate_ChangedFileAndInStaging_TheFile() {
        //arrange
        val filePath = gitRule
            .newFileInWorkDir("test.java")
            .addToGit()
            .commit()
            .changeContent("test")
            .addToGit()
            .filePath
        val testee = StagingChangesetCalculator(gitRule.git)
        //act
        val changeset = testee.calculate()
        //assert
        Assertions.assertThat(changeset).contains(filePath)
    }

    @Test fun calculate_ChangedFileInSubfolderAndInStaging_TheFileInclPath() {
        //arrange
        val filePath = gitRule
            .newFileInWorkDir("test.java", "subfolder")
            .addToGit()
            .commit()
            .changeContent("test")
            .addToGit()
            .filePath
        val testee = StagingChangesetCalculator(gitRule.git)
        //act
        val changeset = testee.calculate()
        //assert
        Assertions.assertThat(changeset).contains(filePath)
    }

}
