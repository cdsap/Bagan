package com.cdsap.bagan.generator

import com.cdsap.bagan.utils.TestBagan
import com.cdsap.bagan.utils.TestExperiments
import com.cdsap.bagan.utils.TestLogger
import com.cdsap.bagan.utils.TestFolder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kotlintest.inspectors.*
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.string.shouldNotContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import java.io.File


class DashboardProviderTest : BehaviorSpec({
    given("DashBoardProvider Instance") {
        val testLogger = TestLogger()
        val moshiProvider = MoshiProvider()
        File("tmp").mkdir()
        File("tmp/grafana").mkdir()
        File("tmp/grafana/dashboards/").mkdir()

        `when`("Dashboard is generated") {
            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBagan(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            dashboardProvider.generate(TestExperiments.getExperiments())
            val dashboard = getDashboard(moshiProvider)

            then("Properties and Panels are generated") {
                if (dashboard != null) {
                    assert(dashboard.title == "Bagan")
                    assert(dashboard.uid == "IS3q0sSWz")
                    assert(dashboard.editable)
                    assert(dashboard.panels.size == 5)
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("text")
                        it?.mode.shouldContain("markdown")
                        it?.title.shouldContain("Experiments")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("graph")
                        it?.title.shouldContain("assemble")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("singlestat")
                        it?.title.shouldContain("Experiment Winner")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Percentile(80)")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Min build times assemble")
                    }


                } else {
                    assert(false)
                }

            }
            then("Position on the Grid are generated correctly") {
                if (dashboard != null) {
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("text")
                        it?.gridPos?.w.shouldBe(5)
                        it?.gridPos?.h.shouldBe(7)
                        it?.gridPos?.x.shouldBe(19)
                        it?.gridPos?.y.shouldBe(0)
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("graph")
                        it?.gridPos?.w.shouldBe(19)
                        it?.gridPos?.h.shouldBe(7)
                        it?.gridPos?.x.shouldBe(0)
                        it?.gridPos?.y.shouldBe(0)
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("singlestat")
                        it?.gridPos?.w.shouldBe(5)
                        it?.gridPos?.h.shouldBe(7)
                        it?.gridPos?.x.shouldBe(19)
                        it?.gridPos?.y.shouldBe(7)
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Percentile(80)")
                        it?.gridPos?.w.shouldBe(10)
                        it?.gridPos?.h.shouldBe(7)
                        it?.gridPos?.x.shouldBe(0)
                        it?.gridPos?.y.shouldBe(7)
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Min build times assemble")
                        it?.gridPos?.w.shouldBe(9)
                        it?.gridPos?.h.shouldBe(7)
                        it?.gridPos?.x.shouldBe(10)
                        it?.gridPos?.y.shouldBe(7)
                    }
                } else {
                    assert(false)
                }
            }
            then("queries are generated") {
                if (dashboard != null) {
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("graph")
                        it?.targets?.get(0)
                            ?.query.shouldBe("SELECT percentile(\"duration\", 99) FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP BY time(\$interval), \"experiment\"")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("singlestat")
                        it?.targets?.get(0)
                            ?.query.shouldBe("select \"experiment\" from (SELECT min(\"per\"), \"experiment\" FROM (SELECT percentile(\"duration\",80) as \"per\" FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP BY  \"experiment\") )")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Percentile(80)")
                        it?.targets?.get(0)
                            ?.query.shouldBe("SELECT percentile(\"duration\", 80) FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP by \"experiment\"")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Min build times assemble")
                        it?.targets?.get(0)
                            ?.query.shouldBe("SELECT min(\"duration\") FROM \"tracking\".\"rpTalaiot\".\"build\" WHERE \$timeFilter GROUP by \"experiment\"")
                    }
                } else {
                    assert(false)
                }
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Command input in Bagan conf is multiple") {
            File("tmp").mkdir()
            File("tmp/grafana").mkdir()
            File("tmp/grafana/dashboards/").mkdir()

            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBaganMultipleCommands(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            dashboardProvider.generate(TestExperiments.getExperiments())
            val dashboard = getDashboard(moshiProvider)

            then("only the first command is processed") {
                if (dashboard != null) {

                    assert(dashboard.panels.size == 5)
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("text")
                        it?.mode.shouldContain("markdown")
                        it?.title.shouldContain("Experiments")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("graph")
                        it?.title.shouldContain("assemble")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("singlestat")
                        it?.title.shouldContain("Experiment Winner")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Percentile(80)")
                    }
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("table")
                        it?.title.shouldContain("Min build times assemble")
                    }


                } else {
                    assert(false)
                }

            }
            TestFolder.recursiveDelete(File("tmp"))

        }
        `when`("Command input in Bagan conf is empty") {
            File("tmp").mkdir()
            File("tmp/grafana").mkdir()
            File("tmp/grafana/dashboards/").mkdir()

            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBaganWithOnlyCleanCommand(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            dashboardProvider.generate(TestExperiments.getExperiments())
            val dashboard = getDashboard(moshiProvider)

            then("error message is processed and not Points panels created") {
                if (dashboard != null) {

                    assert(testLogger.containsLog("no commands found"))
                    assert(dashboard.title == "Bagan")
                    assert(dashboard.uid == "IS3q0sSWz")
                    assert(dashboard.editable)
                    assert(dashboard.panels.size == 1)

                } else {
                    assert(false)
                }

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Bagan configuration contains only experiments of properties") {
            File("tmp").mkdir()
            File("tmp/grafana").mkdir()
            File("tmp/grafana/dashboards/").mkdir()

            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBaganWithOnlyCleanCommand(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            val experiments = listOf(
                Experiment("property=a", "", ""),
                Experiment("property=b", "", "")
            )
            var counter = 1
            experiments.forEach {
                it.name = "experiment" + counter++
            }
            dashboardProvider.generate(experiments)
            val dashboard = getDashboard(moshiProvider)

            then("Panel of legend contains only Gradle Properties info") {
                if (dashboard != null) {

                    assert(testLogger.containsLog("no commands found"))
                    assert(dashboard.title == "Bagan")
                    assert(dashboard.uid == "IS3q0sSWz")
                    assert(dashboard.editable)
                    assert(dashboard.panels.size == 1)
                    dashboard.panels.forExactly(1) {
                        it?.type.shouldContain("text")
                        it?.mode.shouldContain("markdown")
                        it?.title.shouldContain("Experiments")
                        val content = "### experiment1:\n" +
                                "#### Gradle Properties:\n" +
                                "property=a\n" +
                                "### experiment2:\n" +
                                "#### Gradle Properties:\n" +
                                "property=b\n"
                        it?.content.shouldContain(content)
                        it?.content.shouldNotContain("#### Branch:\n")
                        it?.content.shouldNotContain("#### Gradle Wrapper:\n")
                    }


                } else {
                    assert(false)
                }

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Bagan configuration contains only experiments of branches") {
            File("tmp").mkdir()
            File("tmp/grafana").mkdir()
            File("tmp/grafana/dashboards/").mkdir()

            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBaganWithOnlyCleanCommand(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            val experiments = listOf(
                Experiment("", "develop", ""),
                Experiment("", "master", "")
            )
            var counter = 1
            experiments.forEach {
                it.name = "experiment" + counter++
            }

            dashboardProvider.generate(experiments)
            val dashboard = getDashboard(moshiProvider)

            then("Panel of legend contains only Gradle Properties info") {
                assert(dashboard?.panels?.size == 1)
                dashboard?.panels?.forExactly(1) {
                    it?.type.shouldContain("text")
                    it?.mode.shouldContain("markdown")
                    it?.title.shouldContain("Experiments")
                    val content = "### experiment1:\n" +
                            "#### Branch:\n" +
                            "develop\n" +
                            "### experiment2:\n" +
                            "#### Branch:\n" +
                            "master\n"
                    it?.content.shouldContain(content)
                    it?.content.shouldNotContain("#### Gradle Properties:\n")
                    it?.content.shouldNotContain("#### Gradle Wrapper:\n")
                }
            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Bagan configuration contains only experiments of Gradle Wrapper Versions") {
            File("tmp").mkdir()
            File("tmp/grafana").mkdir()
            File("tmp/grafana/dashboards/").mkdir()

            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBaganWithOnlyCleanCommand(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            val experiments = listOf(
                Experiment("", "", "4.5"),
                Experiment("", "", "5.1")
            )
            var counter = 1
            experiments.forEach {
                it.name = "experiment" + counter++
            }
            dashboardProvider.generate(experiments)
            val dashboard = getDashboard(moshiProvider)
            then("Panel of legend contains only Gradle Wrapper version info") {

                assert(dashboard?.panels?.size == 1)
                dashboard?.panels?.forExactly(1) {
                    it?.type.shouldContain("text")
                    it?.mode.shouldContain("markdown")
                    it?.title.shouldContain("Experiments")
                    val content = "### experiment1:\n" +
                            "#### Gradle Wrapper:\n" +
                            "4.5\n" +
                            "### experiment2:\n" +
                            "#### Gradle Wrapper:\n" +
                            "5.1\n"
                    it?.content.shouldContain(content)
                    it?.content.shouldNotContain("#### Branch:\n")
                    it?.content.shouldNotContain("#### Gradle Properties:\n")
                }

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
        `when`("Bagan configuration contains all type of experiments") {
            File("tmp").mkdir()
            File("tmp/grafana").mkdir()
            File("tmp/grafana/dashboards/").mkdir()
            val dashboardProvider = DashboardProvider(
                bagan = TestBagan.getBaganWithOnlyCleanCommand(),
                moshiProvider = moshiProvider,
                commandExecutor = CommandExecutor(testLogger, true),
                logger = testLogger,
                path = "tmp"
            )
            dashboardProvider.generate(TestExperiments.getExperiments())
            val dashboard = getDashboard(moshiProvider)

            then("Panel of legend contains only Gradle Wrapper version info") {
                assert(dashboard?.panels?.size == 1)
                dashboard?.panels?.forExactly(1) {
                    it?.type.shouldContain("text")
                    it?.mode.shouldContain("markdown")
                    it?.title.shouldContain("Experiments")
                    val content = "### experiment1:\n" +
                            "#### Gradle Properties:\n" +
                            "property1\n" +
                            "#### Branch:\n" +
                            "master\n" +
                            "#### Gradle Wrapper:\n" +
                            "4.5\n" +
                            "### experiment2:\n" +
                            "#### Gradle Properties:\n" +
                            "property2\n" +
                            "#### Branch:\n" +
                            "develop\n" +
                            "#### Gradle Wrapper:\n" +
                            "5.1\n"

                    it?.content.shouldContain(content)
                }

            }
            TestFolder.recursiveDelete(File("tmp"))
        }
    }
})

fun getDashboard(moshiProvider: MoshiProvider): Dashboard? {
    val file = File("tmp/grafana/dashboards/dashboard.json")
    return moshiProvider.adapter(Dashboard::class.java).fromJson(file.readText())

}



