import org.gradle.api.DefaultTask
import org.gradle.api.Project

import java.io.*

abstract class TaskHeaderReplacer : DefaultTask() {
    private val file = "/src/main/java/"
    private val lookup =

        listOf(
            Element("ExperimentGenerator.kt", "@file:Include(\"Bagan.kt\")\n", "creator", "replace"),
            Element(
                "ExperimentCoordinator.kt",
                "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n" +
                        "@file:Include(\"ExperimentGenerator.kt\")\n" +
                        "@file:Include(\"MonitorReporting.kt\")\n" +
                        "@file:Include(\"GradleExperimentsProperties.kt\")\n" +
                        "@file:Include(\"Bagan.kt\")\n" +
                        "@file:Include(\"BaganJson.kt\")\n" +
                        "@file:Include(\"Property.kt\")\n" +
                        "@file:Include(\"Versions.kt\")",
                "creator",
                "replace"
            ),
            Element("Bagan.kt", "//DEPS com.squareup.moshi:moshi-kotlin:1.8.0\n", "creator", "replace"),
            Element("BaganJson.kt", "", "creator", "move"),
            Element("Versions.kt", "", "creator", "move"),
            Element("GradleExperimentsProperties.kt", "", "creator", "move"),
            Element("Property.kt", "", "creator", "move"),
            Element("MonitorReporting.kt", "", "creator", "move"),
            Element("TalaiotInjector.kt", "", "injector", "move"),
            Element("RewriteProperties.kt", "", "properties", "move")
        )

    fun showFile(project: Project) {
        val file = File(project.projectDir.toString() + file)
        println(file)
        createOutput()
        println("input file: " + file.isDirectory)
        if (file.isDirectory) {
            file.walkTopDown().iterator().forEach {
                if (it.isFile) {
                    it.name
                    updateHeadersFile(it.path)
                    println(it.path)

                }
            }
        }

    }

    fun createOutput(){
        val dir = File("${project.rootDir}/kscript/")
        println(" exists ---->1" + dir.exists())
        if (!dir.exists()) {
            println(" exists ---->2222")
            val a = dir.mkdir()
            println(" exists ---->$a")
        }

    }

    fun updateHeadersFile(path: String) {
        val mFile = File(path)
        val nameFile = mFile.name
        println("  ----> $nameFile")

        val element = lookup.filter {
            it.nameFile == nameFile
        }.map {

            val action = it.action
            val replace = it.dependencies
            when (it.type) {
                "creator" -> {
                    val dir = File("${project.rootDir}/kscript/creator")
                    println(" exists ---->1" + dir.exists())
                    if (!dir.exists()) {
                        println(" exists ---->2222")
                        val a = dir.mkdir()
                        println(" exists ---->$a")
                    }

                    when (action) {
                        "replace" -> {
                            val fis = FileInputStream(mFile)
                            val br = BufferedReader(InputStreamReader(fis))
                            var result = ""
                            var line = br.readLine()
                            while (line != null) {
                                result += line + "\n"
                                line = br.readLine()
                            }
                            val v = replace
                            result = "$v\n\n$result"

                            println(" exists ---->2 --> \"$dir/${mFile.name}")
                            File("$dir/${mFile.name}").createNewFile()
                            val file = File("$dir/${mFile.name}")
                            val fos = FileOutputStream(file)
                            println(" exists ---->3")

                            fos.write(result.toByteArray())
                            fos.flush()

                        }
                        "move" -> {
                            copyFile(mFile.path, "$dir/${mFile.name}")

                        }
                    }


                }
                "injector" -> {
                    val dir = File("${project.rootDir}/kscript/injector")
                    println(" exists ---->1" + dir.exists())
                    if (!dir.exists()) {
                        println(" exists ---->2222")
                        val a = dir.mkdir()
                        println(" exists ---->$a")
                    }

                    when (action) {
                        "replace" -> {

                        }
                        "move" -> {

                            copyFile(mFile.path, "$dir/${mFile.name}")
                        }
                    }

                }
                "properties" -> {
                    val dir = File("${project.rootDir}/kscript/properties")
                    println(" exists ---->1" + dir.exists())
                    if (!dir.exists()) {
                        println(" exists ---->2222")
                        val a = dir.mkdir()
                        println(" exists ---->$a")
                    }

                    when (action) {
                        "replace" -> {

                        }
                        "move" -> {
                            copyFile(mFile.path, "$dir/${mFile.name}")
                        }
                    }

                }


            }
        }
    }


    data class Element(
        val nameFile: String,
        val dependencies: String,
        val type: String,
        val action: String
    )

    enum class TYPEX {
        CREATOR,
        INJECTOR,
        PROPERTIES
    }

    fun copyFile(ina: String, ino: String) {
        val _in = FileInputStream(ina)
        val out = FileOutputStream(ino)

        // Copy the bits from instream to outstream
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
}
