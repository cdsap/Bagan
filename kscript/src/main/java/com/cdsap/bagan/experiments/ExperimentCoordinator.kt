package com.cdsap.bagan.experiments


import com.cdsap.bagan.experiments.Versions.CONF_FILE
import java.nio.file.Files
import java.nio.file.Paths
import com.cdsap.bagan.experiments.Versions.CURRENT_VERSION
import com.cdsap.bagan.experiments.Versions.PATH
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.ThreadLocalRandom


fun main() {
    val moshiProvider = MoshiProvider()
    val commandExecutor = CommandExecutor()
    val monitor = MonitorReporting(moshiProvider, commandExecutor)

    ExperimentCoordinator(moshiProvider, commandExecutor, monitor).init()
}


class ExperimentCoordinator(
    val moshiProvider: MoshiProvider,
    private val commandExecutor: CommandExecutor,
    private val monitorReporting: MonitorReporting
) {

    fun init() {

        checkFile()
        val bagan = getBagan()
        val experimentsPermuted = ExperimentGenerator(bagan).getExperiments()
        var count = 0
        val experiment = registerExperiment()
        monitorReporting.insertExperiment(experiment)

        experimentsPermuted.forEach {

            val nameExperiment = "experiment$count".toLowerCase()
            val nameConfigMap = "configmap$nameExperiment"
            val namePod = "$nameExperiment/templates/pod$nameExperiment.yaml"
            monitorReporting.insertPod(
                values = it.replace("\n     ", "\n"),
                iterations = bagan.iterations,
                configMap = nameConfigMap,
                experiment = experiment,
                pod = nameExperiment

            )


            createFolder(nameExperiment)
            createChartFile("$nameExperiment/Chart.yaml", nameExperiment)
            createFileValues(
                "$nameExperiment/values.yaml",
                bagan.repository,
                bagan.gradleCommand,
                nameConfigMap,
                bagan.iterations,
                nameExperiment
            )

            createTemplateFolder(nameExperiment)
            createConfigMaps(
                nameExperiment, nameConfigMap,
                it
            )
            createPods(
                namePod,
                nameExperiment,
                experiment
            )
            commandExecutor.execute("helm install -n $nameExperiment -f $nameExperiment/values.yaml $nameExperiment/")
            count++
        }
    }

    fun getBagan(): Bagan {

        val jsonAdapter = moshiProvider.adapter(BaganJson::class.java)

        val baganJson: BaganJson =
            jsonAdapter.fromJson(
                String(Files.readAllBytes(Paths.get("${PATH}bagan_conf.json")), StandardCharsets.US_ASCII)
            ) ?: throw Exception("Error parsing json file")

        return baganJson.bagan
    }

    fun createChartFile(path: String, id: String) {
        println("creating chart file ")
        val file = File(path)
        file.writeText(
            "apiVersion: v1\n" +
                    "appVersion: \"1.0\"\n" +
                    "description: Chart, used for Bagan resource\n" +
                    "name: $id\n" +
                    "version: $CURRENT_VERSION"
        )

    }

    fun createFolder(path: String) {
        println("creating folder")
        Files.createDirectory(Paths.get(path))
    }

    fun createFileValues(
        path: String,
        nameRepo: String,
        gradleCommand: String,
        s2: String,
        iterations: Int,
        nameExperiment: String
    ) {
        val file = File(path)
        println("creating fileValues")
        file.writeText(
            "repository: $nameRepo\n" +
                    "configMaps : $s2\n" +
                    "name : $nameExperiment\n" +
                    "image: cdsap/bagan-pod-injector\n" +
                    "command: $gradleCommand\n" +
                    "iterations: $iterations"
        )
    }

    fun createTemplateFolder(path: String) {
        println("creating template folder")
        Files.createDirectory(Paths.get("$path/templates"))

    }

    fun createConfigMaps(
        nameExperiment: String,
        s1: String,
        propertyName: String

    ) {
        println("creating configMpas")
        val file = File("$nameExperiment/templates/$s1.yaml")
        file.writeText(
            "apiVersion: v1\n" +
                    "kind: ConfigMap\n" +
                    "metadata:\n" +
                    "  name: $s1\n" +
                    "  labels: \n" +
                    "    type: experiment\n" +
                    "    experiment_id: $nameExperiment\n" +
                    "data:\n" +
                    "  id: $nameExperiment\n" +
                    "  experiments: |\n" +
                    "     $propertyName"
        )
    }

    fun createPods(path: String, nameExperiment: String, experiment: String) {
        val file = File(path)
        file.writeText(
            "apiVersion: v1\n" +
                    "kind: Pod\n" +
                    "metadata:\n" +
                    "  name: $nameExperiment\n" +
                    "  labels: \n" +
                    "    app: experiment\n" +
                    "    type: experiment\n" +
                    "    experimentid: $experiment\n" +
                    "  annotations:\n" +
                    "    seccomp.security.alpha.kubernetes.io/pod: 'docker/default'\n" +
                    "spec:\n" +
                    "  initContainers:\n" +
                    "    - name: git-clone\n" +
                    "      image: alpine/git\n" +
                    "      args:\n" +
                    "        - clone\n" +
                    "        - --single-branch\n" +
                    "        - --\n" +
                    "        - {{ .Values.repository }}\n" +
                    "        - /repo\n" +
                    "      securityContext:\n" +
                    "        runAsUser: 1\n" +
                    "        allowPrivilegeEscalation: false\n" +
                    "        readOnlyRootFilesystem: true\n" +
                    "      volumeMounts:\n" +
                    "        - name: git-repo\n" +
                    "          mountPath: /repo\n" +
                    "  containers:\n" +
                    "    - name:  agent\n" +
                    "      image: {{ .Values.image }}\n" +
                    "      args: ['sleep', '100000']\n" +
                    "      envFrom:\n" +
                    "        - configMapRef:\n" +
                    "            name: {{ .Values.configMaps }}\n" +
                    "      volumeMounts:\n" +
                    "        - name: git-repo\n" +
                    "          mountPath: /repo\n" +
                    "      lifecycle:\n" +
                    "            postStart:\n" +
                    "              exec:\n" +
                    "                command:\n" +
                    "                - bash\n" +
                    "                - -c\n" +
                    "                - |\n" +
                    "                  mv *.kt /repo \n" +
                    "                  cd /usr/share/sdkman/bin \n" +
                    "                  source sdkman-init.sh \n" +
                    "                  cd /repo\n" +
                    "                  kscript TalaiotInjector.kt \n" +
                    "                  kscript RewriteProperties.kt \n" +
                    "                  pwd >  /usr/share/message\n" +
                    "                  for i in `seq 1 {{ .Values.iterations }}`; do {{ .Values.command }}; done\n" +
                    "  volumes:\n" +
                    "    - name: git-repo\n" +
                    "      emptyDir: {}\n"
        )
    }

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')


    private fun registerExperiment(): String {
        return getStringExperiment()
    }

    private fun getStringExperiment() = (1..20)
        .map { i -> ThreadLocalRandom.current().nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")


    private fun checkFile() {
        if (!File(CONF_FILE).exists()) {
            throw Exception("Error: file not found.")
        }
    }
}
