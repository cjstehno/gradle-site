/*
 * Copyright (C) 2019 Christopher J. Stehno <chris@stehno.com>
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

import groovyx.net.http.HttpBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * The 'verifySite' task is used to verify that a running web server contains the desired pages. This verification only ensures that the specified
 * pages exist on the site, not that the content matches.
 *
 * The base URL is determined by the site.siteUrl configuration, though a "-PsiteUrl" parameter may be specified to override it.
 *
 * By default, the index.html page is tested; however, additional tested paths may be provided in the configuration.
 */
class VerifySiteTask extends DefaultTask {

    static final String NAME = 'verifySite'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void verifySite() {
        SiteExtension siteExtension = project.extensions.findByType(SiteExtension)

        def siteUrl = project.hasProperty('siteUrl') ? project.property('siteUrl') : siteExtension.siteUrl

        logger.lifecycle "Verifying that site exists at ${siteUrl}..."

        HttpBuilder http = HttpBuilder.configure {
            request.uri = siteUrl
        }

        // we'll just hit the important ones
        boolean exists = (['/index.html'] + siteExtension.testedPaths).every { page ->
            boolean ok = http.head(Boolean) {
                request.uri.path = page
                response.success { fs, obj -> true }
            }

            logger.info " - Checking: ${siteUrl}${page}: $ok"
            ok
        }

        assert exists, 'Some or all of the documentation site pages are not published. Run with --info for more details.'
    }
}