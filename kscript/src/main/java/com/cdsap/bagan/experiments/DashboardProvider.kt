package com.cdsap.bagan.experiments

import com.cdsap.bagan.dashboard.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun main() {
    val dashboardProvider = DashboardProvider(
        listOf(
            Experiment("experiment0", "org.gradle.caching=4g"),
            Experiment("experiment1", "org.gradke.caching=6g"),
            Experiment("experiment2", "org.gradle.caching=8g")
        ), listOf("assembleDebug")
    )
    dashboardProvider.generate()
}

class DashboardProvider(
    val experiments: List<Experiment>,
    val commands: List<String>
) {

    fun generate() {
        val moshi = Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Panel::class.java, "type")
                    .withSubtype(Graph::class.java, PanelType.graph.name)
                    .withSubtype(Table::class.java, PanelType.table.name)
                    .withSubtype(Markdown::class.java, PanelType.text.name)
                    .withSubtype(Winner::class.java, PanelType.singlestat.name)
            )
            .add(KotlinJsonAdapterFactory())
            .build()

        val adapter = moshi.adapter<Any>(Dashboard::class.java)
        val dashboard = Dashboard(panels = calculatePannels())
        val dashBoardString = adapter.toJson(dashboard)
        println(dashBoardString.toString())
    }

    private fun calculatePannels(): Array<Panel?> {
        val panels = ArrayList<Panel>()
        // 1- Build Legend
        panels.add(buildLegend())


        // Experiment 0  --> Gradle property1=A
        //                          property2=B
        // 2- Build Main DashBoard by command
        var counter = 0
        val heightGraph = 8
        val widthGraph = 24
        val x = 0
        var y = 0
        commands.forEach {

            y += 8
            panels.add(
                generateWinner(
                    id = 102,
                    title = "Experiment Winner $it",
                    gridPos = Gridpos(w = 7, h = 8, x = 12, y = 0),
                    maxValue = 200,
                    minValue = 200,
                    valueName = "Name"
                )
            )

            panels.add(generateGraphs(title = it, gridPos = Gridpos(w = 19, h = 8, x = 0, y = 8), id = 200))
            panels.add(generateTable())
            y += 14
            counter++
        }
        val array = arrayOfNulls<Panel>(panels.size)
        panels.toArray(array)

        return array
    }

    private fun buildLegend(): Panel {
        var content = ""
        experiments.forEach {
            content += it.name + it.values + "\n"
        }
        return Markdown(content = content, id = 99, title = "Experiments", gridPos = Gridpos(16, 5, 19, 0))
    }


    private fun generateWinner(
        id: Int,
        title: String,
        gridPos: Gridpos,
        valueName: String,
        maxValue: Int,
        minValue: Int
    ) = Winner(
        id = id,
        title = title,
        gridPos = gridPos,
        valueName = valueName,
        gauge = Gauge(
            maxValue = maxValue,
            minValue = minValue
        )
    )

    private fun generateGraphs(
        title: String,
        id: Int,
        gridPos: Gridpos
    ) = Graph(
        title = title,
        id = id,
        gridPos = gridPos
    )

    private fun generateTable() = Table(
        id = 100,
        gridPos = Gridpos(x = 0, y = 8, w = 12, h = 10),
        title = "xxxxx",
        targets = arrayOf(Target2(tags = arrayOf(Tags(key = "task", operator = "=", value = ":app:assembleDebug"))))
    )

}







