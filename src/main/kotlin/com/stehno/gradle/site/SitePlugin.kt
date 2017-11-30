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

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin used to add documentation site creation and management tasks.
 */
open class SitePlugin : Plugin<Project> {

    companion object {
        private const val WEB_PREVIEW_PLUGIN_ID = "com.stehno.gradle.webpreview"
    }

    override fun apply(project: Project) {
        project.extensions.create("site", SiteExtension::class.java)

        applyWebPreviewConfig(project)

        // TODO: depends on: build, jacocoTestReport (others?)

        project.task(taskDetails<SiteTask>("Generates the project web site."), SiteTask.NAME)
        project.task(taskDetails<CheckVersionTask>("Verify that the project version is reflected in the documentation."), CheckVersionTask.NAME)
        project.task(taskDetails<UpdateVersionTask>("Updates the version in documentation based on the project version"), UpdateVersionTask.NAME)
        project.task(taskDetails<VerifySiteTask>("Verifies that the documentation site exists at the expected address."), VerifySiteTask.NAME)
    }

    private inline fun <reified T : DefaultTask> taskDetails(description: String): Map<String, *> {
        return mapOf(
            Pair("type", T::class.java),
            Pair("group", "Documentation"),
            Pair("description", description)
        )
    }

    private fun applyWebPreviewConfig(project: Project) {
        project.afterEvaluate {
            project.plugins.withId(WEB_PREVIEW_PLUGIN_ID) {
                val siteExtension = project.extensions.getByType(SiteExtension::class.java)

                val webPreviewExt = project.extensions.findByName("webPreview")
// FIXME: how in kotlin?               webPreviewExt.resourceDir = project.file(siteExtension.buildDir)

                project.logger.info("Applied default configuration for Web Preview plugin.")
            }
        }
    }
}
