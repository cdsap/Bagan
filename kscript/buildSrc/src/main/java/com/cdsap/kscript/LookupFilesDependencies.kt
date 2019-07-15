package com.cdsap.kscript

import com.cdsap.kscript.entities.Element
import com.cdsap.kscript.entities.Type
import com.cdsap.kscript.entities.Action

object LookupFilesDependencies{
    fun getFilesDependencies() : List<Element> =  listOf(
        Element(
            "BaganFileGenerator.kt",
            "@file:Include(\"Bagan.kt\")\n" +
                    "@file:Include(\"Chart.kt\")\n" +
                    "@file:Include(\"ConfigMap.kt\")\n" +
                    "@file:Include(\"Pod.kt\")\n" +
                    "@file:Include(\"Values.kt\")\n"
            ,
            Type.CREATOR,
            Action.REPLACE,
            "kscript/creator"
        ),
        Element(
            "BaganGenerator.kt",
            "@file:Include(\"MonitorReporting.kt\")\n" +
                    "@file:Include(\"GradleExperimentsProperties.kt\")\n" +
                    "@file:Include(\"Bagan.kt\")\n" +
                    "@file:Include(\"Logger.kt\")\n" +
                    "@file:Include(\"ExperimentProvider.kt\")\n" +
                    "@file:Include(\"BaganConfFileProvider.kt\")\n" +
                    "@file:Include(\"BaganFileGenerator.kt\")\n" +
                    "@file:Include(\"CommandExecutor.kt\")\n" +
                    "@file:Include(\"MoshiProvider.kt\")\n" +
                    "@file:Include(\"BaganJson.kt\")\n" +
                    "@file:Include(\"Property.kt\")\n" +
                    "@file:Include(\"Versions.kt\")",
            Type.CREATOR,
            Action.REPLACE, "kscript/creator"
        ),
        Element(
            "Bagan.kt",
            "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n",
            Type.CREATOR,
            Action.REPLACE,
            "kscript/creator"
        ),
        Element("Chart.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("Bootstraping.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("Values.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("Pod.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("CommandExecutor.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element(
            "ExperimentProvider.kt",
            "@file:Include(\"BaganConfFileProvider.kt\")\n",

            Type.CREATOR, Action.MOVE, "kscript/creator"
        ),
        Element("ConfigMap.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element(
            "MoshiProvider",
            "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n",
            Type.CREATOR,
            Action.REPLACE,
            "kscript/creator"
        ),
        Element("BaganConfFileProviderImpl.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("BaganConfFileProvider.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("BaganJson.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("Versions.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("GradleExperimentsProperties.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("Property.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("MoshiProvider.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("MonitorReporting.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("Logger.kt", "", Type.CREATOR, Action.MOVE, "kscript/creator"),
        Element("TalaiotInjector.kt", "", Type.INJECTOR, Action.MOVE, "kscript/injector"),
        Element("RewriteProperties.kt", "", Type.PROPERTIES, Action.MOVE, "kscript/properties")
    )
}