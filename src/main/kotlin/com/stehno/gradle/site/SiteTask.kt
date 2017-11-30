/**
 * Copyright (C) 2017 Christopher J. Stehno <chris@stehno.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.gradle.site

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction
import java.util.*

/**
 * Gradle task used to generate the project web site based on configured content and available reports.
 */
open class SiteTask : DefaultTask() {

    // TODO: how to configure and expose UP-TO-DATE-ness for this task?

    companion object {
        const val NAME = "site"
    }

    @TaskAction
    fun site() {
        logger.info("Building documentation web site...")

        val siteExtension = project.extensions.findByType(SiteExtension::class.java)

        // TODO: add asset into and replace support "into" , replace, external

        if (project.file(siteExtension!!.srcDir).exists()) {
            val vars = mutableMapOf<String, Any>(
                Pair("project_version", project.version),
                Pair("year", Calendar.getInstance().get(Calendar.YEAR))
            )
            vars.putAll(siteExtension.variables)

            // copy the templates
            val templateSpec = project.copySpec().from(siteExtension.srcDir).into(siteExtension.buildDir).include("**/*.html").expand(vars)
            project.copy()

            val copy =

            // copy the assets (no variable replacement)
            project.copySpec().from(siteExtension.srcDir).into(siteExtension.buildDir).include("**/css/**", "**/js/**", "**/img/**")

            // copy any added assets
            siteExtension.assetDirs.forEach { asset ->
                val fromDir = when {
                    asset.external -> asset.dir
                    else -> siteExtension.srcDir
                }

                val intoDir = when {
                    !asset.into.isNullOrBlank() -> "/${asset.into}"
                    else -> ""
                }

                val including = when {
                    asset.external -> "**/**"
                    else -> asset.dir
                }

                val spec = project.copySpec().from(fromDir).into("${siteExtension.buildDir}$intoDir").include(including)
                if (asset.replace) {
                    spec.expand(vars)
                }
            }

            // copy the build reports
            project.copySpec().from("build/reports").into("${siteExtension.buildDir}/reports").include("**/**")

            // copy the docs
            project.copySpec().from("build/docs").into("${siteExtension.buildDir}/docs").include("**/**")

            // copy the asciidocs
            project.copySpec().from("build/asciidoc").into("${siteExtension.buildDir}/asciidoc").include("**/**")
        }
    }
}
