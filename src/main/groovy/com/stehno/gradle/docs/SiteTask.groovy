/*
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
package com.stehno.gradle.docs

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Gradle task used to generate the project web site based on configured content and available reports.
 */
class SiteTask extends DefaultTask {

    // TODO: how to configure and expose UP-TO-DATE-ness for this task?

    static final String NAME = 'site'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void site() {
        logger.info 'Building documentation web site...'

        // TODO: these should be configurable
        String siteSource = 'src/site'
        String siteBuild = 'build/site'

        if (project.file(siteSource).exists()) {
            // TODO: allow config of additional replacement vars
            // copy the templates
            project.copy {
                from siteSource
                into siteBuild
                include '**/*.html'
                expand([project_version: project.version, year: Calendar.instance.get(Calendar.YEAR)])
            }

            // copy the assets (no variable replacement)
            project.copy {
                from siteSource
                into siteBuild
                include '**/css/**'
                include '**/js/**'
                include '**/img/**'
            }

            // copy the build reports
            project.copy {
                from 'build/reports'
                include '**/**'
                into siteBuild
            }
        }
    }
}
