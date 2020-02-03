package com.cdsap.bagan.experiments

class IncrementalChange(
    private val updates: List<Update>,
    private val coordinator: ChangeProvider,
    private val scriptGenerator: ScriptGenerator,
    private val incrementalChangeInfo: IncrementalChangeInfo
) {
    fun execute() {
        updates.forEach {
            initHeader()
            val change = coordinator.change(TypeChange.ADD_METHOD)
            val changeUpdate = "sed '\$s/}/$change }/' ${it.target} > temp.kt && mv temp.kt ${it.target} && rm temp.kt"
            addingChangedFile(changeUpdate, it.module)
        }
    }

    private fun initHeader() {
        scriptGenerator.addCommand(
            """
               for i in `seq 1 ${incrementalChangeInfo.initialTaskIterations}`; do ${incrementalChangeInfo.initialTask} -PextraLabel=bootstraping; done;
            """.trimIndent()
        )
    }

    private fun addingChangedFile(command: String, module: String) {
        val moduleFormatted = module.replace(":", "")
        scriptGenerator.addCommand(
            """
              $command
              for i in `seq 1 ${incrementalChangeInfo.taskExperimentationIterations}`; do ${incrementalChangeInfo.taskExperimentation} -PextraLabel=$moduleFormatted ; done;
            """.trimIndent()
        )
    }
}
