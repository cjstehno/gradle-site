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

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin used to add documentation site creation and management tasks.
 */
class ProjectDocsPlugin implements Plugin<Project> {

    private static final String WEB_PREVIEW_PLUGIN_ID = 'com.stehno.gradle.webpreview'

    @Override
    void apply(final Project project) {
        project.extensions.create('site', SiteExtension) as SiteExtension

        applyWebPreviewConfig project

        // TODO: depends on: build, jacocoTestReport (others?)
        project.task SiteTask.NAME, type: SiteTask, group: 'Documentation', description: 'Generates the project web site.'

        project.task CheckVersionTask.NAME, type: CheckVersionTask, group: 'Documentation', description: 'Verify that the project version is reflected in the documentation.'
        project.task UpdateVersionTask.NAME, type: UpdateVersionTask, group: 'Documentation', description: 'Updates the version in documentation based on the project version'
        project.task VerifySiteTask.NAME, type: VerifySiteTask, group: 'Documentation', description: 'Verifies that the documentation site exists at the expected address.'
    }

    private static void applyWebPreviewConfig(final Project project) {
        project.afterEvaluate {
            project.plugins.withId(WEB_PREVIEW_PLUGIN_ID) {
                SiteExtension siteExtension = project.extensions.getByType(SiteExtension)

                Object webPreviewExt = project.extensions.findByName('webPreview')
                webPreviewExt.resourceDir = project.file(siteExtension.buildDir)

                project.logger.info 'Applied default configuration for Web Preview plugin.'
            }
        }
    }
}
