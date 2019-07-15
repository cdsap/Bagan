import com.cdsap.kscript.LookupFilesDependencies
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import com.cdsap.kscript.entities.Element
import com.cdsap.kscript.entities.Type
import com.cdsap.kscript.entities.Action
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import java.io.*

abstract class TaskHeaderReplacer : DefaultTask() {
    @get:InputDirectory
    abstract val input: DirectoryProperty
    @get:OutputDirectory
    abstract val output: DirectoryProperty = Dire

    -rwxr-xr-x
    -rw-r--r--
    private val file = "/src/main/java/"
    //    @getOup
    private val lookup = LookupFilesDependencies.getFilesDependencies()


    @TaskAction
    fun showFile() {
        val file = File(project.projectDir.toString() + file)
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
        val output = "${project.buildDir}/kscript/"
        val dir = File(output)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val dirCreator = File("$output/creator")
        val dirInjector = File("$output/kscript/injector")
        val dirProperties = File("$output/kscript/properties")
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

}
