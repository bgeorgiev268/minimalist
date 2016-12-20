package ch.tutteli.minimaltestsuite

import org.eclipse.jgit.api.Git
import org.junit.rules.TemporaryFolder

private const val WORK_DIR = "workDir"

class GitRule : TemporaryFolder() {
    lateinit var git: Git
        private set

    override fun before() {
        super.before()
        val workFolder = newFolder(WORK_DIR)
        git = Git.init().setDirectory(workFolder).call()
    }


    fun newFileInWorkDir(fileName: String, vararg subFolders: String): Builder {
        var prefix = ""
        if (subFolders.isNotEmpty()) {
            newFolder(WORK_DIR, *subFolders)
            prefix = subFolders.joinToString(separator = "/", postfix = "/")
        }
        val filePath = prefix + fileName
        return Builder(filePath)
    }

    inner class Builder(val filePath: String) {
        var file = newFile(WORK_DIR + "/" + filePath)!!
            private set

        fun addToGit(): Builder {
            git.add().addFilepattern(filePath).call()
            return this
        }

        fun changeContent(content: String): Builder {
            file.writeText(content)
            return this
        }

        fun commit(message: String = "test"): Builder {
            git.commit().setMessage(message).call()
            return this
        }
    }

}
