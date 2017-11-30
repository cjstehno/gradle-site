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

import groovyx.net.http.HttpBuilder.configure
import groovyx.net.http.HttpObjectConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.util.function.Consumer

/**
 * The 'verifySite' task is used to verify that a running web server contains the desired pages. This verification only ensures that the specified
 * pages exist on the site, not that the content matches.
 *
 * The base URL is determined by the site.siteUrl configuration, though a "-PsiteUrl" parameter may be specified to override it.
 *
 * By default, the index.html page is tested; however, additional tested paths may be provided in the configuration.
 */
open class VerifySiteTask : DefaultTask() {

    companion object {
        const val NAME = "verifySite"
    }

    @TaskAction
    fun verifySite() {
        val extension = project.extensions.findByType(SiteExtension::class.java)

        val siteUrl = when {
            project.hasProperty("siteUrl") -> project.property("siteUrl").toString()
            else -> extension!!.siteUrl
        }

        logger.lifecycle("Verifying that site exists at $siteUrl...")

        val http = configure(Consumer<HttpObjectConfig> { c ->
            c.request.setUri(siteUrl)
        })

        val paths = mutableListOf("index.html")
        paths.addAll(extension!!.testedPaths)

        // Not the most efficient way to do this but should be ok for now
        val exists = paths.all { page ->
            val ok = http.head(Boolean::class.java, { c ->
                c.request.uri.setPath(page)
                c.response.success { _, _ -> true }
            })

            logger.info(" - Checking: $siteUrl$page: $ok")
            ok
        }

        assert(exists) { "Some or all of the documentation site pages are not published. Run with --info for more details." }
    }
}