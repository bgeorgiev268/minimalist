package ch.tutteli.minimaltestsuite

import org.eclipse.jgit.api.Git

class StagingChangesetCalculator(val git: Git) : ChangesetCalculator {
    override fun calculate(): Set<String> {
        val status = git.status().call()
        return status.added + status.changed
    }
}
