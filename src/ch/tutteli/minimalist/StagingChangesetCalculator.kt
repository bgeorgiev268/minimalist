package ch.tutteli.minimalist

import org.eclipse.jgit.api.Git

class StagingChangesetCalculator(private val git: Git) : ChangesetCalculator {
    override fun calculate(): Set<String> {
        val status = git.status().call()
        return status.added + status.changed
    }
}
