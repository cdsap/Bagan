package com.cdsap.kscript

import com.cdsap.kscript.entities.Element
import com.cdsap.kscript.entities.Type
import com.cdsap.kscript.entities.Action

object LookupFilesDependencies {
    fun getFilesDependencies(): List<Element> = listOf(
        Element(
            "BaganFileGenerator.kt",
            "@file:Include(\"Bagan.kt\")\n" +
                    "@file:Include(\"K8Template.kt\")\n"
            ,
            Type.GENERATOR,
            Action.REPLACE,
            "baganGenerator/generator"
        ),
        Element(
            "BaganGenerator.kt",
            "@file:Include(\"Bagan.kt\")\n" +
                    "@file:Include(\"Logger.kt\")\n" +
                    "@file:Include(\"ExperimentProvider.kt\")\n" +
                    "@file:Include(\"BaganConfFileProvider.kt\")\n" +
                    "@file:Include(\"BaganFileGenerator.kt\")\n" +
                    "@file:Include(\"DashboardProvider.kt\")\n" +
                    "@file:Include(\"CommandExecutor.kt\")\n" +
                    "@file:Include(\"MoshiProvider.kt\")\n" +
                    "@file:Include(\"Versions.kt\")",
            Type.GENERATOR,
            Action.REPLACE, "baganGenerator/generator"
        ),
        Element(
            "Bagan.kt",
            "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n",
            Type.GENERATOR,
            Action.REPLACE,
            "baganGenerator/generator"
        ),
        Element("Bootstraping.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element(
            "DashboardProvider.kt",
            "//DEPS com.squareup.moshi:moshi-adapters:1.8.0\n" +
                    "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n" +
                    "@file:Include(\"Panel.kt\")\n" +
                    "@file:Include(\"Logger.kt\")\n" +
                    "@file:Include(\"CommandExecutor.kt\")\n" +
                    "@file:Include(\"EntitiesGrafana.kt\")\n",
            Type.GENERATOR,
            Action.REPLACE,
            "baganGenerator/generator"
        ),
        Element("EntitiesGrafana.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element(
            "Panel.kt",
            "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n",
            Type.GENERATOR,
            Action.MOVE,
            "baganGenerator/generator"
        ),
        Element("K8Template.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element("CommandExecutor.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element(
            "ExperimentProvider.kt",
            "@file:Include(\"BaganConfFileProvider.kt\")\n",

            Type.GENERATOR, Action.MOVE, "baganGenerator/generator"
        ),
        Element(
            "MoshiProvider",
            "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n",
            Type.GENERATOR,
            Action.REPLACE,
            "baganGenerator/generator"
        ),
        Element("BaganConfFileProviderImpl.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element("BaganConfFileProvider.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element("Versions.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element("MoshiProvider.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element("Logger.kt", "", Type.GENERATOR, Action.MOVE, "baganGenerator/generator"),
        Element("LoggerPod.kt", "", Type.EXPERIMENTS, Action.MOVE, "baganGenerator/experiments"),
        Element(
            "TalaiotInjector.kt",
            "@file:Include(\"LoggerPod.kt\")\n",
            Type.EXPERIMENTS,
            Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element("ExperimentPod.kt", "", Type.EXPERIMENTS, Action.MOVE, "baganGenerator/experiments"),
        Element(
            "RewriteProperties.kt",
            "@file:Include(\"ExperimentPod.kt\")" +
                    "@file:Include(\"LoggerPod.kt\")\n",
            Type.EXPERIMENTS,
            Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element(
            "GradleWrapperVersion.kt",
            "@file:Include(\"ExperimentPod.kt\")\n" +
                    "@file:Include(\"LoggerPod.kt\")\n",
            Type.EXPERIMENTS,
            Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element(
            "ExperimentController.kt",
            "@file:Include(\"TalaiotInjector.kt\")\n" +
                    "@file:Include(\"RewriteProperties.kt\")\n" +
                    "@file:Include(\"LoggerPod.kt\")\n" +
                    "@file:Include(\"GradleWrapperVersion.kt\")\n",
            Type.EXPERIMENTS,
            Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element(
            "ExecutionGenerator.kt",
            "@file:Include(\"ChangesCoordinator.kt\")\n" +
                    "@file:Include(\"ScriptGenerator.kt\")\n" +
                    "@file:Include(\"ComposedExperiments.kt\")\n" +
                    "@file:Include(\"ChangeUpdate.kt\")\n" +
                    "@file:Include(\"MetadataI.kt\")\n",
            Type.EXPERIMENTS, Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element(
            "MetadataI.kt",
            "",
            Type.EXPERIMENTS,
            Action.MOVE,
            "baganGenerator/experiments"
        ),
        Element(
            "ChangesCoordinator.kt",
            "@file:Include(\"Change.kt\")\n" +
                    "@file:Include(\"ChangeUpdate.kt\")\n",
            Type.EXPERIMENTS,
            Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element(
            "ScriptGenerator.kt",
            "",
            Type.EXPERIMENTS,
            Action.MOVE,
            "baganGenerator/experiments"
        ),
        Element(
            "ComposedExperiments.kt",
            "@file:Include(\"Update.kt\")\n",
            Type.EXPERIMENTS,
            Action.REPLACE,
            "baganGenerator/experiments"
        ),
        Element(
            "Update.kt",
            "",
            Type.EXPERIMENTS,
            Action.MOVE,
            "baganGenerator/experiments"
        ),
        Element(
            "Change.kt",
            "",
            Type.EXPERIMENTS,
            Action.MOVE,
            "baganGenerator/experiments"
        ),
        Element(
            "ChangeUpdate.kt",
            "",
            Type.EXPERIMENTS,
            Action.MOVE,
            "baganGenerator/experiments"
        )
    )
}