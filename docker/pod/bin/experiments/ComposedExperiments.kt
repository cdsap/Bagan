@file:Include("Update.kt")


package com.cdsap.bagan.experiments

class ComposedExperiments(
    private val updates: List<Update>,
    private val coordinator: ChangesCoordinator,
    private val scriptGenerator: ScriptGenerator,
    private val metadata: MetadataI
) {
    fun execute() {
        updates.forEach {
            initHeader()
            val changeUpdate = ChangeUpdate(
                it.target,
                coordinator.change(Change.ADD_METHOD)
            ).changeCode()
            val x = it.module
            addingChangedFile(changeUpdate,x)
        }
    }

    private fun initHeader() {
        scriptGenerator.addCommand(
            """
for i in `seq 1 ${metadata.initialTaskIterations}`; do ${metadata.initialTask} -Ptinder.trackinglabel=bootstraping; done;
        """.trimIndent()
        )
    }

    private fun addingChangedFile(command: String, module: String) {
        println(module.replace(":",""))
        println(metadata.taskExperimentation)
        scriptGenerator.addCommand(
            """
$command
for i in `seq 1 ${metadata.taskExperimentationIterations}`; do ${metadata.taskExperimentation} -Ptinder.trackinglabel=${module.replace(":","")}; done;
            """.trimIndent()
        )
    }
}
