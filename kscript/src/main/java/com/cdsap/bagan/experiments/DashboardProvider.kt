package com.cdsap.bagan.experiments

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
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
        //val moshiProvider = MoshiProvider()
        val moshi = Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Panel::class.java, "type")
                    .withSubtype(Graph::class.java, PanelType.graph.name)
                    .withSubtype(Markdown::class.java, PanelType.text.name)
                    .withSubtype(Indicator::class.java, PanelType.singlestat.name)
            )
            .add(KotlinJsonAdapterFactory())
            .build()

        val adapter = moshi.adapter<Any>(Dashboard::class.java)
        val dashboard = Dashboard(panels = calculatePannels())
        val dashBoardString = adapter.toJson(dashboard)
        println(dashBoardString.toString())
    }

    fun calculatePannels(): Array<Panel?> {
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
            generateGraphs(title = it, id = counter++, gridPos = Gridpos(h = heightGraph, w = widthGraph, x = x, y = y))
            y = y + 8
            panels.add(
                generateWinner(
                    id = counter++,
                    title = "Experiment Winner $it",
                    gridPos = Gridpos(w = 10, h = 12, x = 0, y = y),
                    maxValue = 200,
                    minValue = 200,
                    valueName = "Name"
                )
            )

            panels.add(generateGraphs(title = it, gridPos = Gridpos(w = 14, h = 12, x = 10, y = y), id = counter++))
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

        return Markdown(content = content, id = 0, title = "Experiments", gridPos = Gridpos(8, 24, 0, 0))

    }
//
//    private fun calculateY(): Int {
//
//    }
//
//    private fun calculateX(): Int {
//
//    }
//
//    private fun calculateW(): Int {
//
//    }
//
//    private fun calculateH(): Int {
//
//    }

    private fun generateWinner(
        id: Int,
        title: String,
        gridPos: Gridpos,
        valueName: String,
        maxValue: Int,
        minValue: Int
    ): Panel {
        return Indicator(
            id = id,
            title = title,
            gridPos = gridPos,
            valueName = valueName,
            gauge = Gauge(
                maxValue = maxValue,
                minValue = minValue
            )
        )
    }

    private fun generateGraphs(
        title: String,
        id: Int,
        gridPos: Gridpos
    ): Panel {

        return Graph(
            title = title,
            id = id,
            gridPos = gridPos
        )
    }

    fun minIndicators() {
        // winner

    }

}


data class Markdown(
    val content: String,
    val id: Int,
    val mode: String = "markdown",
    val timeFrom: String? = null,
    val timeShift: String? = null,
    val title: String,
    val links: Array<String>? = emptyArray(),
    val type: String = "Text",
    val options: Any? = null,
    val gridPos: Gridpos
) : Panel(PanelType.text)


data class Gridpos(
    val h: Int,
    val w: Int,
    val x: Int,
    val y: Int
)


data class Indicator(
    val cacheTimeout: Boolean = false,
    val colorBackground: Boolean = false,
    val colorValue: Boolean = false,
    val format: String = "none",
    val id: Int,
    val interval: String? = null,
    val mappingType: Int = 1,
    val maxDataPoints: Int = 100,
    val nullPointMode: String = "connected",
    val nullText: String? = null,
    val postfix: String = "",
    val postfixFontSize: String = "50%",
    val prefix: String = "",
    val prefixFontSize: String = "50%",
    val tableColumn: String = "",
    val thresholds: String = "",
    val timeFrom: String? = null,
    val timeShift: String? = null,
    val title: String,
    val type: String = "singlestat",
    val valueFontSize: String = "80%",
    val valueName: String,
    val gridPos: Gridpos,
    val colors: Array<String> = arrayOf(
        "#299c46", "rgba(237, 129, 40, 0.89)", "#d44a3a"
    ),
    val gauge: Gauge,
    val links: Array<String>? = emptyArray(),
    val mappingTypes: Array<MappingType> = arrayOf(
        MappingType(name = "value to text", value = 1),
        MappingType(name = "range to text", value = 2)
    ),
    val options: Any? = null,
    val rangeMaps: Array<RangeMap> = arrayOf(RangeMap()),
    val sparkLine: SparkLine = SparkLine()
) : Panel(PanelType.singlestat)


data class Target(
    val name: String,
    val orderByTime: String = "ASC",
    val policy: String = "default",
    val refId: String = "A",
    val resultFormat: String = "time_series",
    val tags: Array<Any> = emptyArray()
)

data class SparkLine(
    val fillColor: String = "rgba(31,118,189,0.18)",
    val full: Boolean = false,
    val lineColor: String = "rgba(31,120,193)",
    val show: Boolean = false
)

data class RangeMap(
    val from: String = "null",
    val text: String = "N/A",
    val to: String = "null"
)

data class MappingType(
    val name: String,
    val value: Int
)

data class Gauge(
    val maxValue: Int,
    val minValue: Int,
    val show: Boolean = true,
    val thresholdLabels: Boolean = false,
    val thresholdMarkers: Boolean = true
)


data class Graph(
    val aliasColors: Array<Any> = emptyArray(),
    var bars: Boolean = false,
    val dashLength: Int = 10,
    val dashes: Boolean = false,
    val dataSource: String = "InfluxDb",
    val fill: Int = 1,
    val id: Int,
    val lines: Boolean = true,
    val lineWidth: Int = 1,
    val nullPointMode: String = "null",
    val percentage: Boolean = false,
    val pointradius: Int = 2,
    val points: Boolean = false,
    val renderer: String = "flot",
    val spaceLength: Int = 10,
    val steppedLine: Boolean = false,
    val stack: Boolean = false,
    val timeFrom: Any? = null,
    val timeShift: Any? = null,
    val title: String,
    val type: String = "graph",
    val gridPos: Gridpos,
    val legend: Legend = Legend(),
    val links: Array<String>? = emptyArray(),
    val options: Options = Options(),
    val seriesOverrides: Array<String> = emptyArray(),
    val thresholds: Array<String> = emptyArray(),
    val timeRegions: Array<String> = emptyArray(),
    val tooltip: Tooltip = Tooltip(),
    val xaxis: Xaxis = Xaxis(),
    val yaxes: Array<Yaxes> = arrayOf(Yaxes()),
    val yaxis: Yaxis = Yaxis()
) : Panel(PanelType.graph)

data class Xaxis(
    val buckets: Any? = null,
    val mode: String = "time",
    val name: Any? = null,
    val show: Boolean = true,
    val values: Array<String> = emptyArray()
)

data class Yaxes(
    val format: String = "short",
    val label: Any? = null,
    val logBase: Int = 1,
    val max: Any? = null,
    val min: Any? = null,
    val show: Boolean = true
)

data class Yaxis(
    val align: Boolean = false,
    val alignLevel: Any? = null
)

data class Tooltip(
    val shared: Boolean = true,
    val sort: Int = 0,
    val value_type: String = "individual"
)

data class Legend(
    val avg: Boolean = false,
    val current: Boolean = false,
    val max: Boolean = false,
    val min: Boolean = false,
    val show: Boolean = true,
    val total: Boolean = false,
    val values: Boolean = false
)

class Options

data class Dashboard(
    val editable: Boolean = false,
    val gnetld: Any? = null,
    val graphTooltip: Int = 0,
    val id: Int = 1,
    val schemaVersion: Int = 18,
    val style: String = "dark",
    val timezone: String = "",
    val title: String = "Bagan",
    val uid: String = "IS3q0sSWz",
    val version: Int = 5,
    val links: Array<String> = emptyArray(),
    val tags: Array<String> = emptyArray(),
    val annotations: Array<ListAnnotations> = arrayOf(ListAnnotations()),
    val templating: Array<ListAnnotations> = emptyArray(),
    val time: Time = Time(),
    val timePicker: TimePicker = TimePicker(),
    val panels: Array<Panel?>
)


sealed class Panel(@Json(name = "type") val panelType: PanelType)


data class TimePicker(
    val refresh_intervals: Array<String> =
        arrayOf("5s", "10s", "30s", "1m", "5m", "15m", "30m", "1h", "2h", "1d"),
    val time_options: Array<String> =
        arrayOf("5m", "15m", "1h", "6h", "12h", "24h")
)

data class Time(
    val from: String = "now-1h",
    val to: String = "now"
)

data class ListAnnotations(val list: Array<Annotation> = arrayOf(Annotation()))

data class Annotation(
    val buildln: Int = 1,
    val datasource: String = "-- Grafana --",
    val enable: Boolean = true,
    val hide: Boolean = true,
    val iconColor: String = "rgba(0, 211,255,1)",
    val name: String = "Annotations & Alert",
    val type: String = "dashboard"

)


enum class PanelType {
    graph,
    text,
    singlestat
}
