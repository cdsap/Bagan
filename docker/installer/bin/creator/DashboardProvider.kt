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
    private val commandExecutor: CommandExecutor,
    private val path: String
) {
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

    fun generate(
        experiments: List<Experiment>,
        commands: List<String>
    ) {
        generateDashBoard(experiments, commands)
        executeUpgradeHelm()
    }

    private fun generateDashBoard(
        experiments: List<Experiment>,
        commands: List<String>
    ) {
        val adapter = moshi.adapter<Any>(Dashboard::class.java)
        val dashboard = Dashboard(panels = calculatePannels(experiments, commands))
        val dashBoardString = adapter.toJson(dashboard)
        val file = File("$path/grafana/dashboards/dashboard.json")
        file.writeText(dashBoardString.toString())

    }

    private fun executeUpgradeHelm() {
          commandExecutor.execute("helm upgrade bagan-grafana $path/grafana")
    }

    private fun calculatePannels(
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
                    title = "Experiment Winner $it",
                    gridPos = Gridpos(w = 7, h = 8, x = 12, y = 0),
                    valueName = "Name",
                    command = it
                )
            )
            panels.add(
                generateGraphs
                    (title = it, gridPos = Gridpos(w = 19, h = 8, x = 0, y = 8), id = counter++, command = it)
            )
            panels.add(generateTable(title = "Min build times $it", command = it, id = counter))

        }
        val array = arrayOfNulls<Panel>(panels.size)
        panels.toArray(array)
        return array
    }

    private fun generateLegend(experiments: List<Experiment>, id: Int) = Markdown(
        content = getContentLegend(experiments),
        id = id,
        title = "Experiments",
        gridPos = Gridpos(16, 5, 19, 0)
    )

    private fun generateWinner(
        id: Int,
        title: String,
        gridPos: Gridpos,
        valueName: String,
        command: String
    ) = Winner(
        id = id,
        title = title,
        gridPos = gridPos,
        valueName = valueName,
        targets = targetIndicator(command)
    )

    private fun generateGraphs(
        title: String,
        id: Int,
        gridPos: Gridpos,
        command: String
    ) = Graph(
        title = title,
        id = id,
        gridPos = gridPos,
        targets = targetGraph(command)
    )

    private fun generateTable(command: String, title: String, id: Int) = Table(
        id = id,
        gridPos = Gridpos(x = 0, y = 0, w = 19, h = 8),
        title = title,
        targets = targetTable(command)
    )
}


fun groupByExperiment() = query("tag", "experiment")
fun queryValue() = query("field", "value")
fun queryInterval() = query("time", "\$interval")
fun tagCommand(command: String) = Tags(key = "task", operator = "=", value = command)

fun query(type: String, value: String) = Query(type = type, params = arrayOf(value))

fun targetTable(command: String) = arrayOf(
    Target(
        groupBy = arrayOf(groupByExperiment()),
        select = arrayOf(arrayOf(queryValue())),
        tags = arrayOf(tagCommand(command))
    )
)

fun targetGraph(command: String) = arrayOf(
    Target(
        resultFormat = "time_series",
        groupBy = arrayOf(queryInterval(), groupByExperiment()),
        select = arrayOf(
            arrayOf(
                queryValue(),
                query("percentile", "99")
            )
        ),
        tags = arrayOf(tagCommand(command))
    )
)

fun targetIndicator(command: String) = arrayOf(
    Target(
        query = "select experiment from (select min(\"mean\"), experiment from ( select mean(value) from \"tracking\" WHERE \"task\" = '$command' GROUP BY  time(\$interval), experiment) GROUP BY  time(\$interval))",
        rawQuery = true,
        resultFormat = "time_series",
        groupBy = arrayOf(queryInterval()),
        select = arrayOf(
            arrayOf(
                queryValue(),
                query("mean", "value")
            )
        ),
        tags = arrayOf(tagCommand(command))
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



