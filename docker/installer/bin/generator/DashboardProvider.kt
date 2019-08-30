//DEPS com.squareup.moshi:moshi-adapters:1.8.0
//DEPS com.squareup.moshi:moshi-kotlin:1.8.0
@file:Include("Panel.kt")
@file:Include("Logger.kt")
@file:Include("CommandExecutor.kt")
@file:Include("EntitiesGrafana.kt")


package com.cdsap.bagan.generator


import java.io.File


class DashboardProvider(
    private val bagan: Bagan,
    private val moshiProvider: MoshiProvider,
    private val path: String,
    private val logger: Logger,
    private val commandExecutor: CommandExecutor
) {
    private val TAG = "DashboardProvider"

    fun generate(experiments: List<Experiment>) {
        logger.log(TAG, "Starting DashboardProvider")
        generateDashBoard(experiments, getCommands(bagan.gradleCommand))
        executeUpgradeHelm()
    }

    private fun generateDashBoard(experiments: List<Experiment>, commands: List<String>) {
        val dashboard = Dashboard(panels = calculatePanels(experiments, commands))
        val adapter = moshiProvider.adapter(Dashboard::class.java)
        val dashBoardString = adapter.toJson(dashboard)
        generateJson(dashBoardString)
    }

    private fun generateJson(dashBoardString: String) {
        val file = File("$path/grafana/dashboards/dashboard.json")
        file.writeText(dashBoardString)
    }

    private fun executeUpgradeHelm() {
        commandExecutor.execute("helm upgrade bagan-grafana $path/grafana")
    }


    private fun getCommands(command: String): List<String> {
        val values = command.split(" ")
        return values.filter {
            it != "./gradlew"
                    && it != "clean"
        }.toList()
    }

    private fun calculatePanels(
        experiments: List<Experiment>,
        commands: List<String>
    ): Array<Panel?> {
        val panels = ArrayList<Panel>()
        var counter = 0
        panels.add(generateLegend(experiments, counter++))
        if (commands.isEmpty()) {
            logger.log(TAG, "no commands found")

        } else {
            // we are considering only one command.
            // in future releases we need to provide
            // support for more than command type:
            // ./gradlew assembleTest
            // (clean is processed)
            if (commands.size > 1) {
                logger.log(TAG, "more than one command found. Only is going to be processed ${commands[0]}")
            }
            val command = commands[0]
            panels.add(generateWinner(id = counter++, title = "Experiment Winner"))
            panels.add(generateGraphs(title = command, id = counter++))
            panels.add(generateTableMinBuildTimes(title = "Min build times $command", id = counter++))
            panels.add(generateTablePercentiles(title = "Percentile(80)", id = counter++))
        }

        val array = arrayOfNulls<Panel>(panels.size)
        panels.toArray(array)
        return array
    }


    private fun generateLegend(experiments: List<Experiment>, id: Int) =
        Panel(
            content = getContentLegend(experiments),
            type = "text",
            mode = "markdown",
            id = id,
            title = "Experiments",
            gridPos = Gridpos(7, 5, 19, 0)
        )

    private fun generateWinner(
        id: Int,
        title: String
    ) = Panel(
        id = id,
        type = "singlestat",
        title = title,
        gridPos = Gridpos(w = 5, h = 7, x = 19, y = 7),
        valueName = "Name",
        targets = targetIndicator()
    )

    private fun generateGraphs(
        title: String,
        id: Int
    ) = Panel(
        title = title,
        id = id,
        type = "graph",
        gridPos = Gridpos(w = 19, h = 7, x = 0, y = 0),
        targets = targetGraph()
    )

    private fun generateTableMinBuildTimes(title: String, id: Int) = Panel(
        id = id,
        type = "table",
        gridPos = Gridpos(x = 10, y = 7, w = 9, h = 7),
        title = title,
        style = arrayOf(Style(), Style2()),
        styles = arrayOf(Style(), Style2()),
        sort = Sort(2, false),
        targets = targetTableMinBuildTimes()
    )

    private fun generateTablePercentiles(title: String, id: Int) = Panel(
        id = id,
        type = "table",
        gridPos = Gridpos(x = 0, y = 7, w = 10, h = 7),
        style = arrayOf(Style(), Style2()),
        styles = arrayOf(Style(), Style2()),
        sort = Sort(2, false),
        title = title,
        targets = targetTablePercentiles()
    )
}

fun targetTableMinBuildTimes() = arrayOf(
    Target(
        measurement = "tracking",
        orderByTime = "ASC",
        policy = "default",
        resultFormat = "table",
        query = "SELECT min(\"duration\") FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP by \"experiment\"",
        rawQuery = true
    )
)

fun targetTablePercentiles() = arrayOf(
    Target(
        measurement = "tracking",
        orderByTime = "ASC",
        policy = "default",
        resultFormat = "table",
        query = "SELECT percentile(\"duration\", 80) FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP by \"experiment\"",
        rawQuery = true
    )
)

fun targetGraph() = arrayOf(
    Target(
        measurement = "tracking",
        orderByTime = "ASC",
        policy = "default",
        resultFormat = "time_series",
        query = "SELECT percentile(\"duration\", 99) FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP BY time(\$interval), \"experiment\"",
        rawQuery = true
    )
)


fun targetIndicator() = arrayOf(
    Target(
        query = "select \"experiment\" from (SELECT min(\"per\"), \"experiment\" FROM (SELECT percentile(\"duration\",80) as \"per\" FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP BY  \"experiment\") )",
        rawQuery = true,
        resultFormat = "time_series"
    )
)

fun getContentLegend(experiments: List<Experiment>): String {
    var content = ""

    experiments.forEach {
        var contentExperiment = "### ${it.name}:\n"
        if (it.properties.isNotBlank()) {
            contentExperiment += "#### Gradle Properties:\n"
            contentExperiment += "${it.properties}\n"
        }
        if (it.branch.isNotBlank()) {
            contentExperiment += "#### Branch:\n"
            contentExperiment += "${it.branch}\n"
        }
        if (it.gradleWrapperVersion.isNotBlank()) {
            contentExperiment += "#### Gradle Wrapper:\n"
            contentExperiment += "${it.gradleWrapperVersion}\n"
        }
        content += contentExperiment
    }
    return content
}



