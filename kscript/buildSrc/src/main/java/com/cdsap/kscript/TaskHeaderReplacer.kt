import org.gradle.api.DefaultTask
import org.gradle.api.Project

import java.io.*

abstract class TaskHeaderReplacer : DefaultTask() {
    private val file = "/src/main/java/"
    private val lookup = getFiles()

    fun showFile(project: Project) {
        val file = File(project.projectDir.toString() + file)

        println(file)
        createOutput()
        if (file.isDirectory) {
            file.walkTopDown().iterator().forEach {
                if (it.isFile) {
                    println(it.path)
                    updateHeadersFile(it.path)

                }
            }
        }
    }

    private fun createOutput() {
        val dir = File("${project.rootDir}/kscript/")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val dirCreator = File("${project.rootDir}/kscript/creator")
        val dirInjector = File("${project.rootDir}/kscript/injector")
        val dirProperties = File("${project.rootDir}/kscript/properties")
        if (!dirCreator.exists()) {
            dirCreator.mkdir()
        }
        if (!dirInjector.exists()) {
            dirInjector.mkdir()
        }
        if (!dirProperties.exists()) {
            dirProperties.mkdir()
        }
    }

    private fun updateHeadersFile(path: String) {
        val file = File(path)
        val nameFile = file.name

        lookup.filter {
            it.nameFile == nameFile
        }.map {
            println(it.nameFile)
            println(nameFile)
            when (it.action) {
                Action.MOVE -> {
                    copyFile(file.path, "${it.dir}/${file.name}")
                }
                Action.REPLACE -> {
                    if (it.type == Type.CREATOR) {
                        val fis = FileInputStream(file)
                        val br = BufferedReader(InputStreamReader(fis))
                        var result = ""
                        var line = br.readLine()
                        while (line != null) {
                            result += line + "\n"
                            line = br.readLine()
                        }
                        val v = it.dependencies
                        result = "$v\n\n$result"

                        File("${it.dir}/${file.name}").createNewFile()
                        val file = File("${it.dir}/${file.name}")
                        val fos = FileOutputStream(file)

                        fos.write(result.toByteArray())
                        fos.flush()
                    }
                }

            }

        }
    }

    data class Element(
        val nameFile: String,
        val dependencies: String,
        val type: Type,
        val action: Action,
        val dir: String
    )

    enum class Type {
        CREATOR,
        INJECTOR,
        PROPERTIES
    }

    enum class Action {
        REPLACE,
        MOVE
    }

    private fun copyFile(input: String, output: String) {
        val _in = FileInputStream(input)
        println(output)
        val out = FileOutputStream(output)

        val buf = ByteArray(1024)
        var len: Int
        len = _in.read(buf)
        while (len > 0) {

            if (len != -1) {

                out.write(buf, 0, len)
                len = _in.read(buf)
            }
        }
        _in.close()
        out.close()
    }

    private fun getFiles(): List<Element> =
        listOf(
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
