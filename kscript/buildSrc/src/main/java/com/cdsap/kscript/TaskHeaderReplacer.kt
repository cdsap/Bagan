import com.cdsap.kscript.LookupFilesDependencies
import org.gradle.api.DefaultTask
import com.cdsap.kscript.entities.Type
import com.cdsap.kscript.entities.Action
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.*

abstract class TaskHeaderReplacer : DefaultTask() {
    @get:InputDirectory
    abstract val input: DirectoryProperty
    @get:OutputDirectory
    abstract val output: DirectoryProperty
    private val lookup = LookupFilesDependencies.getFilesDependencies()

    @TaskAction
    fun replacer() {
        createOutputFolder()
        input.get().asFileTree.forEach {
            if (it.isFile) {
                updateHeadersFile(it.path)
            }
        }
    }

    private fun createOutputFolder() {
        val dir = output.get().asFile.path
        val kscriptOutput = "$dir/kscript"
        val dirKscript = File(kscriptOutput)
        val dirCreator = File("$kscriptOutput/creator")
        val dirInjector = File("$kscriptOutput/injector")
        val dirProperties = File("$kscriptOutput/properties")
        if (!dirKscript.exists()) {
            dirKscript.mkdir()
        }
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

        val dir = output.get().asFile.path

        lookup.filter {
            it.nameFile == nameFile
        }.map {

            when (it.action) {
                Action.MOVE -> {
                    copyFile(file.path, "$dir/${it.dir}/${file.name}")
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

                        File("$dir/${it.dir}/${file.name}").createNewFile()
                        val fos = FileOutputStream(File("$dir/${it.dir}/${file.name}"))

                        fos.write(result.toByteArray())
                        fos.flush()
                    }
                }
            }
        }
    }

    private fun copyFile(input: String, output: String) {
        val _in = FileInputStream(input)
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
