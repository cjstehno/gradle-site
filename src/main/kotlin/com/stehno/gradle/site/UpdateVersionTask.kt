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

import org.apache.tools.ant.taskdefs.Replace
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * The "updateVersion" task is used to update the project version string in configured "versioned files", the README.md by default. The "from" build
 * property is used to specify the old version, while the version configured in the build.gradle file is used as the new version. The old version
 * string will be replaced with the new version string in the configured files.
 */
open class UpdateVersionTask : DefaultTask() {

    companion object {
        const val NAME = "updateVersion"
    }

    @TaskAction
    fun updateVersion() {
        val extension = project.extensions.findByType(SiteExtension::class.java)

        val newVersion = project.version.toString()
        val oldVersion = project.property("from").toString()

        val versionedFiles = mutableListOf("README.md")
        versionedFiles.addAll(extension!!.versionedFiles)

        if (project.file("src/site/index.html").exists()) {
            versionedFiles.add("src/site/index.html")
        }

        logger.lifecycle("Updating documentation versions from $oldVersion to $newVersion...")

        versionedFiles.filter { f ->
            project.file(f).exists()
        }.forEach { f ->
            val replacer = Replace()
            replacer.setFile(project.file(f))
            replacer.setToken(oldVersion)
            replacer.setValue(newVersion)
            replacer.execute()
        }
    }
}
