//DEPS com.squareup.moshi:moshi-adapters:1.8.0
//DEPS com.squareup.moshi:moshi-kotlin:1.8.0
@file:Include("TypePanels.kt")
@file:Include("Logger.kt")
@file:Include("CommandExecutor.kt")
@file:Include("EntitiesGrafana.kt")


package com.cdsap.bagan.experiments

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File


class DashboardProvider(
    private val bagan: Bagan,
    private val path: String,
    private val logger: Logger,
    private val commandExecutor: CommandExecutor
) {
    val TAG = "DashboardProvider"
    private val moshi: Moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(Panel::class.java, "type")
                .withSubtype(Graph::class.java, PanelType.graph.name)
                .withSubtype(Table::class.java, PanelType.table.name)
                .withSubtype(Markdown::class.java, PanelType.text.name)
                .withSubtype(Winner::class.java, PanelType.singlestat.name)
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    fun generate(experiments: List<Experiment>) {
        logger.log(TAG, "Starting DashboardProvider")
        generateDashBoard(experiments, getCommands(bagan.gradleCommand))
        executeUpgradeHelm()
    }

    private fun generateDashBoard(
        experiments: List<Experiment>,
        commands: List<String>
    ) {
        val adapter = moshi.adapter<Any>(Dashboard::class.java)
        val dashboard = Dashboard(panels = calculatePanels(experiments, commands))
        val dashBoardString = adapter.toJson(dashboard)
        val file = File("$path/grafana/dashboards/dashboard.json")
        file.writeText(dashBoardString.toString())
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
        commands.forEach {

            panels.add(
                generateWinner(
                    id = counter++,
                    title = "Experiment Winner",
                    gridPos = Gridpos(w = 5, h = 7, x = 19, y = 7),
                    valueName = "Name"
                )
            )

            panels.add(
                generateGraphs
                    (title = it, gridPos = Gridpos(w = 19, h = 7, x = 0, y = 0), id = counter++)
            )

            panels.add(generateTableMinBuildTimes(title = "Min build times $it", id = counter++))

            panels.add(
                generateTablePercentiles(
                    title = "Percentile(80)",
                    id = counter++
                )
            )

        }
        val array = arrayOfNulls<Panel>(panels.size)
        panels.toArray(array)
        return array
    }


    private fun generateLegend(experiments: List<Experiment>, id: Int) = Markdown(
        content = getContentLegend(experiments),
        id = id,
        title = "Experiments",
        gridPos = Gridpos(7, 5, 19, 0)
    )

    private fun generateWinner(
        id: Int,
        title: String,
        gridPos: Gridpos,
        valueName: String
    ) = Winner(
        id = id,
        title = title,
        gridPos = gridPos,
        valueName = valueName,
        targets = targetIndicator()
    )

    private fun generateGraphs(
        title: String,
        id: Int,
        gridPos: Gridpos
    ) = Graph(
        title = title,
        id = id,
        gridPos = gridPos,
        targets = targetGraph()
    )

    private fun generateTableMinBuildTimes(title: String, id: Int) = Table(
        id = id,
        gridPos = Gridpos(x = 10, y = 7, w = 9, h = 7),
        title = title,
        targets = targetTableMinBuildTimes()
    )

    private fun generateTablePercentiles(title: String, id: Int) = Table(
        id = id,
        gridPos = Gridpos(x = 0, y = 7, w = 10, h = 7),
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
        rawQuery = true,
        groupBy = emptyArray(),
        select = emptyArray(),
        tags = emptyArray()
    )
)

fun targetTablePercentiles() = arrayOf(
    Target(
        measurement = "tracking",
        orderByTime = "ASC",
        policy = "default",
        resultFormat = "table",
        query = "SELECT percentile(\"duration\", 80) FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP by \"experiment\"",
        rawQuery = true,
        groupBy = emptyArray(),
        select = emptyArray(),
        tags = emptyArray()
    )
)

fun targetGraph() = arrayOf(
    Target(
        measurement = "tracking",
        orderByTime = "ASC",
        policy = "default",
        resultFormat = "time_series",
        query = "SELECT percentile(\"duration\", 99) FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP BY time(\$interval), \"experiment\"",
        rawQuery = true,
        groupBy = emptyArray(),
        select = emptyArray(),
        tags = emptyArray()
    )
)


fun targetIndicator() = arrayOf(
    Target(
        query = "select \"experiment\" from (SELECT min(\"per\"), \"experiment\" FROM (SELECT percentile(\"duration\",80) as \"per\" FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP BY  \"experiment\") )",
        rawQuery = true,
        resultFormat = "time_series",
        groupBy = emptyArray(),
        select = emptyArray(),
        tags = emptyArray()
    )
)

fun getContentLegend(experiments: List<Experiment>): String {
    var content = ""

    experiments.forEach {
        val contentExperiment = "### ${it.name} ###\n\n${it.values}\n\n\n"
        content += contentExperiment
    }
    return content
}



